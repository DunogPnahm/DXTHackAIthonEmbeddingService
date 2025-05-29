package com.hoangtu.chatroomservice.mapper;

import com.hoangtu.chatroomservice.dto.ChatHistoryDto;
import com.hoangtu.chatroomservice.dto.request.PythonReqChatHistoryDto;

public class ChatHistoryDto_PythonReqChatHistoryDto {
    public static PythonReqChatHistoryDto toPythonReqChatHistoryDto(ChatHistoryDto chatHistoryDto) {
        PythonReqChatHistoryDto pythonReqChatHistoryDto = new PythonReqChatHistoryDto();
        pythonReqChatHistoryDto.setSender(chatHistoryDto.getSender());
        pythonReqChatHistoryDto.setContent(chatHistoryDto.getContent());
        pythonReqChatHistoryDto.setTime(chatHistoryDto.getTime().toString());
        return pythonReqChatHistoryDto;
    }
}
