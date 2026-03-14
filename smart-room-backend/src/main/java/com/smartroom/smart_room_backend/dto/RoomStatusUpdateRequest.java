package com.smartroom.smart_room_backend.dto;

public class RoomStatusUpdateRequest {

    private Integer occupancyCount;
    private Boolean doorOpen;

    public RoomStatusUpdateRequest() {
    }

    public Integer getOccupancyCount() {
        return occupancyCount;
    }

    public void setOccupancyCount(Integer occupancyCount) {
        this.occupancyCount = occupancyCount;
    }

    public Boolean getDoorOpen() {
        return doorOpen;
    }

    public void setDoorOpen(Boolean doorOpen) {
        this.doorOpen = doorOpen;
    }
}