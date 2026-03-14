import serial
import requests
import time
import re
from datetime import datetime

SERIAL_PORT = "/dev/ttyACM0"
BAUD_RATE = 115200

ROOM_ID = 1
DEVICE_ID = "arduino-mega-smart-room"

BASE_URL = "http://localhost:8080"
SENSOR_URL = f"{BASE_URL}/api/sensors"
ROOM_STATUS_URL = f"{BASE_URL}/api/rooms/{ROOM_ID}/status"
ROOM_GET_URL = f"{BASE_URL}/api/rooms/{ROOM_ID}"

temperature = None
humidity = None
occupation = None


def reset_cycle():
    global temperature, humidity, occupation
    temperature = None
    humidity = None
    occupation = None


def send_command_to_arduino(ser, room_data):
    heating = 1 if room_data.get("heatingOn", False) else 0
    ventilation = 1 if room_data.get("ventilationOn", False) else 0
    light = 1 if room_data.get("lightOn", False) else 0

    command = f"CMD:HEATING={heating};VENTILATION={ventilation};LIGHT={light}\n"
    ser.write(command.encode("utf-8"))
    print(f"[SERIAL OUT] {command.strip()}")


def try_send(ser):
    global temperature, humidity, occupation

    if temperature is None or humidity is None or occupation is None:
        return

    sensor_payload = {
        "deviceId": DEVICE_ID,
        "temperature": temperature,
        "humidity": humidity,
        "timestamp": datetime.now().isoformat(),
        "roomId": ROOM_ID
    }

    room_payload = {
        "occupancyCount": occupation,
        "doorOpen": False
    }

    try:
        sensor_response = requests.post(SENSOR_URL, json=sensor_payload, timeout=5)
        print(f"[SENSOR] POST -> {sensor_response.status_code}")
        print(f"[SENSOR] Payload : {sensor_payload}")

        room_response = requests.put(ROOM_STATUS_URL, json=room_payload, timeout=5)
        print(f"[ROOM] PUT -> {room_response.status_code}")
        print(f"[ROOM] Payload : {room_payload}")

        get_room_response = requests.get(ROOM_GET_URL, timeout=5)
        print(f"[ROOM] GET -> {get_room_response.status_code}")

        if get_room_response.status_code == 200:
            room_data = get_room_response.json()
            print(f"[ROOM] Etat courant : {room_data}")
            send_command_to_arduino(ser, room_data)
        else:
            print(f"Impossible de récupérer la room : {get_room_response.text}")

    except requests.RequestException as e:
        print(f"Erreur HTTP : {e}")

    reset_cycle()


def main():
    global temperature, humidity, occupation

    print(f"Connexion au port série : {SERIAL_PORT}")
    ser = serial.Serial(SERIAL_PORT, BAUD_RATE, timeout=1)
    time.sleep(2)

    print("Lecture série démarrée...")

    while True:
        try:
            line = ser.readline().decode("utf-8", errors="ignore").strip()

            if not line:
                continue

            print(f"Reçu : {line}")

            occ_match = re.search(r"Occupation:\s*(\d+)", line)
            if occ_match:
                occupation = int(occ_match.group(1))
                try_send(ser)
                continue

            temp_match = re.search(r"Temperature:\s*([0-9]+(?:\.[0-9]+)?)", line)
            if temp_match:
                temperature = float(temp_match.group(1))
                try_send(ser)
                continue

            hum_match = re.search(r"Humidite:\s*([0-9]+(?:\.[0-9]+)?)", line)
            if hum_match:
                humidity = float(hum_match.group(1))
                try_send(ser)
                continue

        except KeyboardInterrupt:
            print("Arrêt du script.")
            break
        except Exception as e:
            print(f"Erreur : {e}")

    ser.close()


if __name__ == "__main__":
    main()