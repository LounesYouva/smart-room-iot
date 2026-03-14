package com.smartroom.smart_room_backend.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.smartroom.smart_room_backend.dto.SensorDataRequest;
import com.smartroom.smart_room_backend.model.Room;
import com.smartroom.smart_room_backend.model.SensorData;
import com.smartroom.smart_room_backend.repository.RoomRepository;
import com.smartroom.smart_room_backend.repository.SensorDataRepository;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;
    private final RoomRepository roomRepository;

    public SensorDataService(SensorDataRepository sensorDataRepository, RoomRepository roomRepository) {
        this.sensorDataRepository = sensorDataRepository;
        this.roomRepository = roomRepository;
    }

    public SensorData saveFromRequest(SensorDataRequest request) {
        SensorData data = new SensorData();

        data.setDeviceId(request.getDeviceId());
        data.setTemperature(request.getTemperature());
        data.setHumidity(request.getHumidity());
        data.setTimestamp(request.getTimestamp());

        if (request.getRoomId() != null) {
            Room room = roomRepository.findById(request.getRoomId()).orElse(null);
            data.setRoom(room);
        }

        return sensorDataRepository.save(data);
    }

    public List<SensorData> getAll() {
        return sensorDataRepository.findAll();
    }

    public SensorData getLatest() {
        return sensorDataRepository.findTopByOrderByTimestampDesc();
    }
}