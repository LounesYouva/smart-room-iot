package com.smartroom.smart_room_backend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.smartroom.smart_room_backend.dto.RoomStatusUpdateRequest;
import com.smartroom.smart_room_backend.model.Room;
import com.smartroom.smart_room_backend.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public Room createRoom(@RequestBody Room room) {
        return roomService.save(room);
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAll();
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomService.getById(id);
    }

    @PutMapping("/{id}/status")
    public Room updateRoomStatus(@PathVariable Long id, @RequestBody RoomStatusUpdateRequest request) {
        return roomService.updateRoomStatus(id, request);
    }
}