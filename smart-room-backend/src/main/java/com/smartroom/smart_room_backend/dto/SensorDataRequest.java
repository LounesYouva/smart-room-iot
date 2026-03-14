package com.smartroom.smart_room_backend.dto;

import java.time.LocalDateTime;

public class SensorDataRequest {

    private String deviceId;
    private Double temperature;
    private Double humidity;
    private LocalDateTime timestamp;
    private Long roomId;

    public SensorDataRequest() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}