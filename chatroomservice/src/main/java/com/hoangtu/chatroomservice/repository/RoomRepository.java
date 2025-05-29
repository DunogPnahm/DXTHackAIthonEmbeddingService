package com.hoangtu.chatroomservice.repository;

import com.hoangtu.chatroomservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findRoomsByUserId(Long userId);
}
