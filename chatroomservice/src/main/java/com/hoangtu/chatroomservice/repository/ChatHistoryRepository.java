package com.hoangtu.chatroomservice.repository;

import com.hoangtu.chatroomservice.model.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    @Query("SELECT ch FROM ChatHistory ch WHERE ch.room.roomId = :roomId")
    List<ChatHistory> findChatHistoriesByRoomId(@Param("roomId") Long roomId);
}
