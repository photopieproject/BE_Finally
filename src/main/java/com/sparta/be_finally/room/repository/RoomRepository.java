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

    HashMap<Long, Integer> map = new HashMap<>();


    Optional <Room> findByRoomCode(int roomCode);

    List<Room> findAllByUser(User user);

    boolean existsByUserId(Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);


}