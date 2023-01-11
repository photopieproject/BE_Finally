package com.sparta.be_finally.room.repository;

import com.sparta.be_finally.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByIdAndRoomCode(Long roomid, int roomCode);

}