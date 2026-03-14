package com.smartroom.smart_room_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartroom.smart_room_backend.model.SensorData;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    SensorData findTopByOrderByTimestampDesc();

    SensorData findTopByRoomIdOrderByTimestampDesc(Long roomId);
}