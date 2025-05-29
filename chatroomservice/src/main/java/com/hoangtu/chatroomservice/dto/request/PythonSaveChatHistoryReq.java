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
public class PythonSaveChatHistoryReq {
    private Long roomId;
    private List<ChatHistoryDto> chatHistoryDtos;
}
