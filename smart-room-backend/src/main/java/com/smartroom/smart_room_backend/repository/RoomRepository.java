package com.smartroom.smart_room_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartroom.smart_room_backend.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findByName(String name);

}