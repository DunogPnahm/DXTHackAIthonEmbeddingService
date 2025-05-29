package com.hoangtu.chatroomservice.controller;

import com.hoangtu.chatroomservice.dto.ChatHistoryDto;
import com.hoangtu.chatroomservice.dto.request.PythonReqData;
import com.hoangtu.chatroomservice.dto.request.PythonSaveChatHistoryReq;
import com.hoangtu.chatroomservice.dto.request.RoomCreate;
import com.hoangtu.chatroomservice.dto.response.RoomAndChatHistoryByRoomID;
import com.hoangtu.chatroomservice.dto.response.RoomCreateRes;
import com.hoangtu.chatroomservice.dto.response.RoomsByUserID;
import com.hoangtu.chatroomservice.model.Room;
import com.hoangtu.chatroomservice.service.ChatHistoryService;
import com.hoangtu.chatroomservice.service.ChatRoomService;
import com.hoangtu.chatroomservice.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/chatroomv2")
@RequestMapping("/chatroom")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatHistoryService chatHistoryService;

    @Autowired
    private RoomService roomService;



    @GetMapping("/{userId}")
    public ResponseEntity<List<RoomsByUserID>> getRoomByUserId(@PathVariable("userId") Long userId){
        List<RoomsByUserID> roomResponse = roomService.getRoomsByUserId(userId);
        return new ResponseEntity<>(roomResponse, HttpStatus.OK);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<RoomAndChatHistoryByRoomID> getRoomAndChatHistory(@PathVariable("roomId") Long roomId){
        RoomAndChatHistoryByRoomID roomAndChatHistory = chatRoomService.getRoomAndChatHistory(roomId);
        return new ResponseEntity<>(roomAndChatHistory, HttpStatus.OK);
    }

    @GetMapping("/chathistory/{roomId}")
    public ResponseEntity<List<ChatHistoryDto>> getChatHisToryByRoomid(@PathVariable("roomId") Long roomId){
        List<ChatHistoryDto> chatHistoryDtos = chatHistoryService.getChatHistoryByRoomId(roomId);
        return new ResponseEntity<>(chatHistoryDtos,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<RoomCreateRes> createRoom(@RequestHeader("Authorization") String authToken ,@RequestHeader("X-User-Id") String userid,@RequestBody RoomCreate roomCreateRequest){
        if (StringUtils.isEmpty(userid)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //String token = authToken;
        //TODO: update use token then parse to get userId later
        try {
            RoomCreateRes roomCreateRes = chatRoomService.createRoom(roomCreateRequest,Long.parseLong(userid));
            return new ResponseEntity<>(roomCreateRes,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save-chat-history")
    public ResponseEntity<PythonSaveChatHistoryReq> saveChatHistory(@RequestBody PythonSaveChatHistoryReq pythonSaveChatHistoryReq){
        PythonSaveChatHistoryReq saveChatHisReq1 = chatRoomService.saveChatHistoryByRoomId(pythonSaveChatHistoryReq);
        return new ResponseEntity<>(saveChatHisReq1,HttpStatus.CREATED);
    }

    @PutMapping("/set-room-status/{roomId}/{status}")
    public ResponseEntity<Room> setRoomStatus(@PathVariable("roomId") Long roomId, @PathVariable("status") String status){
        Room room = roomService.setRoomStatus(roomId,status);
        return new ResponseEntity<>(room,HttpStatus.OK);
    }

    @PostMapping("/prepare-for-python")
    public ResponseEntity<PythonReqData> prepareForPython(@RequestBody RoomsByUserID roomsByUserID){
        PythonReqData pythonReqData = chatRoomService.prepareForPython(roomsByUserID);
        return new ResponseEntity<>(pythonReqData,HttpStatus.OK);
    }
}
