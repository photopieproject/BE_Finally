package com.sparta.be_finally.room.repository;

import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.room.entity.RoomParticipant;
import com.sparta.be_finally.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {

  //  RoomParticipant findRoomParticipantByUserId(String userId);

    RoomParticipant findRoomParticipantByUserIdAndRoom(String userId, Room room);

   // boolean findRoomParticipantByUserId(String userId);

    RoomParticipant findAllByUserId(String userId);

    List<RoomParticipant> findAllByRoomId(Long id);

    // List<RoomParticipant> findRoomParticipantByRoom(Room room);
}
