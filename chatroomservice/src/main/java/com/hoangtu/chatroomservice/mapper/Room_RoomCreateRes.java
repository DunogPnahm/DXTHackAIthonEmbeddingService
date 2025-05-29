package com.hoangtu.chatroomservice.mapper;

import com.hoangtu.chatroomservice.dto.response.RoomCreateRes;
import com.hoangtu.chatroomservice.model.Room;

public class Room_RoomCreateRes {
    public static RoomCreateRes toRoomCreateRes(Room room) {
        RoomCreateRes roomCreateRes = new RoomCreateRes();
        roomCreateRes.setRoomId(room.getRoomId());
        roomCreateRes.setUserId(room.getUserId());
        roomCreateRes.setType(room.getType());
        roomCreateRes.setDbms(room.getDbms());
        roomCreateRes.setDatabaseName(room.getDatabaseName());
        roomCreateRes.setHost(room.getHost());
        roomCreateRes.setPort(room.getPort());
        roomCreateRes.setUsername(room.getUsername());
        roomCreateRes.setStatus(room.getStatus());
        return roomCreateRes;
    }
}
