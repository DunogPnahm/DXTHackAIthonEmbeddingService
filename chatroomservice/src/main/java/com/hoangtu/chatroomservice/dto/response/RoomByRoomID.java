package com.hoangtu.chatroomservice.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomByRoomID {
    private String type;
    private String dbms;
    private String host;
    private String port;
    private String username;
    private String databaseName;
    private String status;
}
