package com.hoangtu.chatroomservice.service;

import com.hoangtu.chatroomservice.dto.ChatHistoryDto;
import com.hoangtu.chatroomservice.dto.request.RoomCreate;
import com.hoangtu.chatroomservice.model.ChatHistory;

import java.util.List;

public interface ChatHistoryService {
    List<ChatHistoryDto> getChatHistoryByRoomId(Long roomId);
    List<ChatHistory> saveChatHistory(List<ChatHistoryDto> chatHistories, Long roomId);
}
