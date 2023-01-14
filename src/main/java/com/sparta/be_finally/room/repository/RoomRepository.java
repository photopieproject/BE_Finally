package com.sparta.be_finally.room.repository;

import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {


    Optional <Room> findByIdAndRoomCode(Long roomid,int roomCode);

    Optional <Room> deleteByUser(User user);

    boolean existsByRoomCode(int roomCode);

    boolean existsByRoomCodeAndUserNull(int roomCode);

}