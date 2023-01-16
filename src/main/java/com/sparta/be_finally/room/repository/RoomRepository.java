package com.sparta.be_finally.room.repository;

import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.room.dto.FrameRequestDto;
import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.user.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {


    Optional <Room> findByRoomCode(int roomCode);

    List<Room> findAllByUser(User user);


    Optional <Room> deleteByUser(User user);

    boolean existsByRoomCode(int roomCode);

    boolean existsByRoomCodeAndUserNull(int roomCode);

    boolean existsByUserNull();

    boolean existsByUserId(Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);


    List<Room> findAllById(Long id);
}