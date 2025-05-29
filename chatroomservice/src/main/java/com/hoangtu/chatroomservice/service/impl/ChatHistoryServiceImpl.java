package com.hoangtu.chatroomservice.service.impl;

import com.hoangtu.chatroomservice.dto.ChatHistoryDto;
import com.hoangtu.chatroomservice.dto.request.RoomCreate;
import com.hoangtu.chatroomservice.mapper.ChatHistory_ChatHistoryDto;
import com.hoangtu.chatroomservice.model.ChatHistory;
import com.hoangtu.chatroomservice.model.Room;
import com.hoangtu.chatroomservice.repository.ChatHistoryRepository;
import com.hoangtu.chatroomservice.repository.RoomRepository;
import com.hoangtu.chatroomservice.service.ChatHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {

    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    @Autowired
    private RoomRepository roomRepository;


    @Override
    public List<ChatHistoryDto> getChatHistoryByRoomId(Long roomId) {
        List<ChatHistory> chatHistories = chatHistoryRepository.findChatHistoriesByRoomId(roomId);
        return  chatHistories.stream().map(ChatHistory_ChatHistoryDto::toChatHistoryDto).toList();
    }

    @Override
    public List<ChatHistory> saveChatHistory(List<ChatHistoryDto> chatHistories, Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        List<ChatHistory> chatHistoryList = new ArrayList<>();
        for (ChatHistoryDto chatHistoryDto : chatHistories) {
            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setRoom(room);
            chatHistory.setSender(chatHistoryDto.getSender());
            chatHistory.setContent(chatHistoryDto.getContent());
            chatHistory.setTime(chatHistoryDto.getTime());
            chatHistoryList.add(chatHistory);
        }
        return chatHistoryRepository.saveAll(chatHistoryList);
    }


}
