package com.hoangtu.chatroomservice.service.impl;

import com.hoangtu.chatroomservice.dto.ChatHistoryDto;
import com.hoangtu.chatroomservice.dto.request.PythonReqData;
import com.hoangtu.chatroomservice.dto.request.PythonSaveChatHistoryReq;
import com.hoangtu.chatroomservice.dto.request.RoomCreate;
import com.hoangtu.chatroomservice.dto.response.RoomAndChatHistoryByRoomID;
import com.hoangtu.chatroomservice.dto.response.RoomByRoomID;
import com.hoangtu.chatroomservice.dto.response.RoomCreateRes;
import com.hoangtu.chatroomservice.dto.response.RoomsByUserID;
import com.hoangtu.chatroomservice.mapper.ChatHistoryDto_PythonReqChatHistoryDto;
import com.hoangtu.chatroomservice.mapper.ChatHistory_ChatHistoryDto;
import com.hoangtu.chatroomservice.model.Room;
import com.hoangtu.chatroomservice.service.ChatHistoryService;
import com.hoangtu.chatroomservice.service.ChatRoomService;
import com.hoangtu.chatroomservice.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private ChatHistoryService chatHistoryService;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public RoomCreateRes createRoom(RoomCreate roomCreateRequest, Long userId) {
        String urlcheckconnect = "http://127.0.0.1:5000/nlptosqlservice/check_connection";
        roomCreateRequest.setUserId(userId);
        HttpEntity<RoomCreate> entitycheckconnect = new HttpEntity<>(roomCreateRequest);
        restTemplate.exchange(urlcheckconnect, HttpMethod.POST, entitycheckconnect, String.class);
        RoomCreateRes roomCreateRes = roomService.createRoom(roomCreateRequest);
        chatHistoryService.saveChatHistory(roomCreateRequest.getChatHistoryDtos(), roomCreateRes.getRoomId());
        String url = "http://127.0.0.1:5000/nlptosqlservice/data";
        PythonReqData pythonReqData = new PythonReqData();
        pythonReqData.setRoomId(roomCreateRes.getRoomId());
        pythonReqData.setUserId(userId);
        pythonReqData.setType(roomCreateRes.getType());
        pythonReqData.setDbms(roomCreateRes.getDbms());
        pythonReqData.setHost(roomCreateRes.getHost());
        pythonReqData.setPort(roomCreateRes.getPort());
        pythonReqData.setUsername(roomCreateRes.getUsername());
        pythonReqData.setDatabaseName(roomCreateRes.getDatabaseName());
        pythonReqData.setPassword(roomCreateRequest.getPassword());
        pythonReqData.setStatus(roomCreateRes.getStatus());
        pythonReqData.setPythonReqChatHistoryDtos(roomCreateRequest.getChatHistoryDtos().stream().map(ChatHistoryDto_PythonReqChatHistoryDto::toPythonReqChatHistoryDto).toList());
        HttpEntity<PythonReqData> entity = new HttpEntity<>(pythonReqData);
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return roomCreateRes;
    }

    @Override
    public RoomAndChatHistoryByRoomID getRoomAndChatHistory(Long RoomId) {
        RoomByRoomID roomByRoomID = roomService.getRoomById(RoomId);
        List<ChatHistoryDto> chatHistoryDto = chatHistoryService.getChatHistoryByRoomId(RoomId);
        RoomAndChatHistoryByRoomID roomAndChatHistoryByRoomID = new RoomAndChatHistoryByRoomID();
        roomAndChatHistoryByRoomID.setType(roomByRoomID.getType());
        roomAndChatHistoryByRoomID.setDbms(roomByRoomID.getDbms());
        roomAndChatHistoryByRoomID.setDatabaseName(roomByRoomID.getDatabaseName());
        roomAndChatHistoryByRoomID.setHost(roomByRoomID.getHost());
        roomAndChatHistoryByRoomID.setPort(roomByRoomID.getPort());
        roomAndChatHistoryByRoomID.setUsername(roomByRoomID.getUsername());
        roomAndChatHistoryByRoomID.setStatus(roomByRoomID.getStatus());
        roomAndChatHistoryByRoomID.setChatHistoryDtos(chatHistoryDto);
        return roomAndChatHistoryByRoomID;
    }

    @Override
    public PythonSaveChatHistoryReq saveChatHistoryByRoomId(PythonSaveChatHistoryReq rq) {
        chatHistoryService.saveChatHistory(rq.getChatHistoryDtos(), rq.getRoomId());
        return rq;
    }

    @Override
    public Room setRoomStatus(Long roomId, String status) {
        return roomService.setRoomStatus(roomId, status);
    }

    @Override
    public PythonReqData prepareForPython(RoomsByUserID roomsByUserID) {
        List<ChatHistoryDto> chatHistoryDtos = chatHistoryService.getChatHistoryByRoomId(roomsByUserID.getRoomId());
        PythonReqData pythonReqData = new PythonReqData();
        pythonReqData.setRoomId(roomsByUserID.getRoomId());
        pythonReqData.setUserId(roomsByUserID.getUserId());
        pythonReqData.setType(roomsByUserID.getType());
        pythonReqData.setDbms(roomsByUserID.getDbms());
        pythonReqData.setHost(roomsByUserID.getHost());
        pythonReqData.setPort(roomsByUserID.getPort());
        pythonReqData.setUsername(roomsByUserID.getUsername());
        pythonReqData.setPassword("");
        pythonReqData.setDatabaseName(roomsByUserID.getDatabaseName());
        pythonReqData.setStatus(roomsByUserID.getStatus());
        pythonReqData.setPythonReqChatHistoryDtos(chatHistoryDtos.stream().map(ChatHistoryDto_PythonReqChatHistoryDto::toPythonReqChatHistoryDto).toList());
        return pythonReqData;
    }

}
