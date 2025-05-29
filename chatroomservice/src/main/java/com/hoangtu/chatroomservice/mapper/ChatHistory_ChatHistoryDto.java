package com.hoangtu.chatroomservice.mapper;

import com.hoangtu.chatroomservice.dto.ChatHistoryDto;
import com.hoangtu.chatroomservice.model.ChatHistory;

public class ChatHistory_ChatHistoryDto {
    public static ChatHistoryDto toChatHistoryDto(ChatHistory chatHistory) {
        ChatHistoryDto chatHistoryDto = new ChatHistoryDto();
        chatHistoryDto.setSender(chatHistory.getSender());
        chatHistoryDto.setContent(chatHistory.getContent());
        chatHistoryDto.setTime(chatHistory.getTime());
        return chatHistoryDto;
    }
}
