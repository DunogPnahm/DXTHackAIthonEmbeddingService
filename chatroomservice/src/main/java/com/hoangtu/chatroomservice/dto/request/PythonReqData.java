package com.hoangtu.chatroomservice.dto.request;

import com.hoangtu.chatroomservice.dto.ChatHistoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PythonReqData {
    private Long roomId;
    private Long userId;
    private String type;
    private String dbms;
    private String host;
    private String port;
    private String username;
    private String password;
    private String databaseName;
    private String status;
    private List<PythonReqChatHistoryDto> pythonReqChatHistoryDtos;
}
