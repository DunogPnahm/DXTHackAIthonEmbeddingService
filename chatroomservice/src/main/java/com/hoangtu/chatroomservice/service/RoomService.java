package com.hoangtu.chatroomservice.service;

import com.hoangtu.chatroomservice.dto.request.RoomCreate;
import com.hoangtu.chatroomservice.dto.response.RoomByRoomID;
import com.hoangtu.chatroomservice.dto.response.RoomCreateRes;
import com.hoangtu.chatroomservice.dto.response.RoomsByUserID;
import com.hoangtu.chatroomservice.model.Room;

import java.util.List;

public interface RoomService {
    RoomCreateRes createRoom(RoomCreate roomCreateRequest);
    RoomByRoomID getRoomById(Long roomId);
    List<RoomsByUserID> getRoomsByUserId(Long userId);
    Room setRoomStatus(Long roomId, String status);
}
