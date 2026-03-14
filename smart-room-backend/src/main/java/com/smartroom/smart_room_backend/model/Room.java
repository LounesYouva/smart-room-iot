package com.smartroom.smart_room_backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer capacity;

    @Column(nullable = false)
    private Boolean occupied;

    @Column(nullable = false)
    private Integer occupancyCount;

    @Column(nullable = false)
    private Boolean heatingOn;

    @Column(nullable = false)
    private Boolean ventilationOn;

    @Column(nullable = false)
    private Boolean lightOn;

    @Column(nullable = false)
    private Boolean doorOpen;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference
    private List<SensorData> sensorDataList = new ArrayList<>();

    public Room() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public Integer getOccupancyCount() {
        return occupancyCount;
    }

    public Boolean getHeatingOn() {
        return heatingOn;
    }

    public Boolean getVentilationOn() {
        return ventilationOn;
    }

    public Boolean getLightOn() {
        return lightOn;
    }

    public Boolean getDoorOpen() {
        return doorOpen;
    }

    public List<SensorData> getSensorDataList() {
        return sensorDataList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public void setOccupancyCount(Integer occupancyCount) {
        this.occupancyCount = occupancyCount;
    }

    public void setHeatingOn(Boolean heatingOn) {
        this.heatingOn = heatingOn;
    }

    public void setVentilationOn(Boolean ventilationOn) {
        this.ventilationOn = ventilationOn;
    }

    public void setLightOn(Boolean lightOn) {
        this.lightOn = lightOn;
    }

    public void setDoorOpen(Boolean doorOpen) {
        this.doorOpen = doorOpen;
    }

    public void setSensorDataList(List<SensorData> sensorDataList) {
        this.sensorDataList = sensorDataList;
    }
}