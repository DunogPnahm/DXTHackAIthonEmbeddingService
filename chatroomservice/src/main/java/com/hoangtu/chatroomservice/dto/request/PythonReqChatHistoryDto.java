package com.hoangtu.chatroomservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PythonReqChatHistoryDto {
    private String sender; // user or AI assistant
    private String content;
    private String time;
}
