package com.hoangtu.chatroomservice.dto.response;

import com.hoangtu.chatroomservice.dto.ChatHistoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomAndChatHistoryByRoomID {
    private String type;
    private String dbms;
    private String host;
    private String port;
    private String username;
    private String databaseName;
    private String status;
    private List<ChatHistoryDto> chatHistoryDtos;
}
