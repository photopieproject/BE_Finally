package com.sparta.be_finally.room.entity;

import com.sparta.be_finally.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class RoomParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name="user_id", nullable = false)
    private String userId;


    @Builder
    public RoomParticipant(Long id, Room room, String userId){
        this.id = id;
        this.room = room;
        this.userId = userId;
    }

    public static RoomParticipant createRoomParticipant(Room room, User user){
        RoomParticipant roomParticipant = RoomParticipant.builder()
                .room(room)
                .userId(user.getUserId())
                .build();
        return roomParticipant;
    }
}
