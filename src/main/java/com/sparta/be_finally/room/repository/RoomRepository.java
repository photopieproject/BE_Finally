package com.sparta.be_finally.room.repository;

import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.room.dto.FrameRequestDto;
import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.user.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomCode(String roomCode);
    boolean existsByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndSessionId(Long id, String sessionId);
    boolean existsByRoomCode(String roomcode);
}