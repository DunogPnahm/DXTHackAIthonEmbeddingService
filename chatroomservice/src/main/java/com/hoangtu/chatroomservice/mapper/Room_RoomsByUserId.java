package com.hoangtu.chatroomservice.mapper;

import com.hoangtu.chatroomservice.dto.response.RoomsByUserID;
import com.hoangtu.chatroomservice.model.Room;

public class Room_RoomsByUserId {
    public static RoomsByUserID toRoomsByUserID(Room room) {
        RoomsByUserID roomsByUserID = new RoomsByUserID();
        roomsByUserID.setRoomId(room.getRoomId());
        roomsByUserID.setUserId(room.getUserId());
        roomsByUserID.setType(room.getType());
        roomsByUserID.setDbms(room.getDbms());
        roomsByUserID.setHost(room.getHost());
        roomsByUserID.setPort(room.getPort());
        roomsByUserID.setUsername(room.getUsername());
        roomsByUserID.setDatabaseName(room.getDatabaseName());
        roomsByUserID.setStatus(room.getStatus());
        return roomsByUserID;
    }
}
