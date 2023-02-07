package com.sparta.be_finally.room.repository;

import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.room.entity.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {
    RoomParticipant findRoomParticipantByUserIdAndRoom(String userId, Room room);

    RoomParticipant findRoomParticipantByUserIdAndRoomAndRole(String userId, Room room, String role);

    RoomParticipant findByUserIdAndRoom(String userId, Room room);

}
