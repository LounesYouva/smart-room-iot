package com.smartroom.smart_room_backend.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.smartroom.smart_room_backend.dto.RoomStatusUpdateRequest;
import com.smartroom.smart_room_backend.model.Room;
import com.smartroom.smart_room_backend.model.SensorData;
import com.smartroom.smart_room_backend.repository.RoomRepository;
import com.smartroom.smart_room_backend.repository.SensorDataRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final SensorDataRepository sensorDataRepository;

    public RoomService(RoomRepository roomRepository, SensorDataRepository sensorDataRepository) {
        this.roomRepository = roomRepository;
        this.sensorDataRepository = sensorDataRepository;
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    public Room getById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    public Room getByName(String name) {
        return roomRepository.findByName(name);
    }

    public Room updateRoomStatus(Long id, RoomStatusUpdateRequest request) {
        Room room = roomRepository.findById(id).orElse(null);

        if (room == null) {
            return null;
        }

        if (request.getOccupancyCount() != null) {
            room.setOccupancyCount(request.getOccupancyCount());
            room.setOccupied(request.getOccupancyCount() > 0);
            room.setLightOn(request.getOccupancyCount() > 0);
        }

        if (request.getDoorOpen() != null) {
            room.setDoorOpen(request.getDoorOpen());
        }

        SensorData latestSensorData = sensorDataRepository.findTopByRoomIdOrderByTimestampDesc(id);

        if (latestSensorData != null) {
            Double temperature = latestSensorData.getTemperature();
            Double humidity = latestSensorData.getHumidity();

            if (temperature != null) {
                room.setHeatingOn(temperature < 20);
            }

            if (temperature != null && humidity != null) {
                room.setVentilationOn(temperature > 26 || humidity > 70);
            }
        }

        return roomRepository.save(room);
    }
}