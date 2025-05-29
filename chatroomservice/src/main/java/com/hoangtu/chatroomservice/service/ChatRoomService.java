package com.hoangtu.chatroomservice.service;

import com.hoangtu.chatroomservice.dto.request.PythonReqChatHistoryDto;
import com.hoangtu.chatroomservice.dto.request.PythonReqData;
import com.hoangtu.chatroomservice.dto.request.PythonSaveChatHistoryReq;
import com.hoangtu.chatroomservice.dto.request.RoomCreate;
import com.hoangtu.chatroomservice.dto.response.RoomAndChatHistoryByRoomID;
import com.hoangtu.chatroomservice.dto.response.RoomCreateRes;
import com.hoangtu.chatroomservice.dto.response.RoomsByUserID;
import com.hoangtu.chatroomservice.model.Room;

public interface ChatRoomService {
    RoomCreateRes createRoom(RoomCreate roomCreateRequest, Long userId);
    RoomAndChatHistoryByRoomID getRoomAndChatHistory(Long RoomId);
    PythonSaveChatHistoryReq saveChatHistoryByRoomId(PythonSaveChatHistoryReq rq);
    Room setRoomStatus(Long roomId, String status);
    PythonReqData prepareForPython(RoomsByUserID roomsByUserID);
}
