package com.hoangtu.chatroomservice.service.impl;

import com.hoangtu.chatroomservice.dto.request.RoomCreate;
import com.hoangtu.chatroomservice.dto.response.RoomByRoomID;
import com.hoangtu.chatroomservice.dto.response.RoomCreateRes;
import com.hoangtu.chatroomservice.dto.response.RoomsByUserID;
import com.hoangtu.chatroomservice.mapper.Room_RoomCreateRes;
import com.hoangtu.chatroomservice.mapper.Room_RoomsByUserId;
import com.hoangtu.chatroomservice.model.Room;
import com.hoangtu.chatroomservice.repository.RoomRepository;
import com.hoangtu.chatroomservice.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public RoomCreateRes createRoom(RoomCreate roomCreateRequest) {
        Room room = new Room();
        room.setUserId(roomCreateRequest.getUserId());
        room.setType(roomCreateRequest.getType());
        room.setDbms(roomCreateRequest.getDbms());
        room.setDatabaseName(roomCreateRequest.getDatabaseName());
        room.setHost(roomCreateRequest.getHost());
        room.setPort(roomCreateRequest.getPort());
        room.setUsername(roomCreateRequest.getUsername());
        room.setStatus(roomCreateRequest.getStatus());
        roomRepository.save(room);
        RoomCreateRes roomCreateRes = Room_RoomCreateRes.toRoomCreateRes(room);
        roomCreateRes.setPassword(roomCreateRequest.getPassword());
        roomCreateRes.setChatHistoryDtos(roomCreateRequest.getChatHistoryDtos());
        return roomCreateRes;
    }

    @Override
    public RoomByRoomID getRoomById(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        RoomByRoomID roomByRoomID = new RoomByRoomID();
        roomByRoomID.setType(room.get().getType());
        roomByRoomID.setDbms(room.get().getDbms());
        roomByRoomID.setDatabaseName(room.get().getDatabaseName());
        roomByRoomID.setHost(room.get().getHost());
        roomByRoomID.setPort(room.get().getPort());
        roomByRoomID.setUsername(room.get().getUsername());
        roomByRoomID.setStatus(room.get().getStatus());
        return roomByRoomID;
    }

    @Override
    public List<RoomsByUserID> getRoomsByUserId(Long userId) {
        List<Room> rooms = roomRepository.findRoomsByUserId(userId);
        return rooms.stream().map(Room_RoomsByUserId::toRoomsByUserID).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Room setRoomStatus(Long roomId, String status) {
        Optional<Room> room = roomRepository.findById(roomId);
        room.get().setStatus(status);
        return roomRepository.save(room.get());
    }
}
