package com.smartroom.smart_room_backend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.smartroom.smart_room_backend.dto.SensorDataRequest;
import com.smartroom.smart_room_backend.model.SensorData;
import com.smartroom.smart_room_backend.service.SensorDataService;

@RestController
@RequestMapping("/api/sensors")
public class SensorDataController {

    private final SensorDataService sensorDataService;

    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @PostMapping
    public SensorData saveSensorData(@RequestBody SensorDataRequest request) {
        return sensorDataService.saveFromRequest(request);
    }

    @GetMapping
    public List<SensorData> getAllSensorData() {
        return sensorDataService.getAll();
    }

    @GetMapping("/latest")
    public SensorData getLatestSensorData() {
        return sensorDataService.getLatest();
    }
}