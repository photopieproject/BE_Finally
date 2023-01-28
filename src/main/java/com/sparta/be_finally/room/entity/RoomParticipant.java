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

    private String role;

    @Builder
    public RoomParticipant(Long id, Room room, String userId, String role){
        this.id = id;
        this.room = room;
        this.userId = userId;
        this.role = role;
    }

    public static RoomParticipant createRoomParticipant(Room room, User user,String role){
        RoomParticipant roomParticipant = RoomParticipant.builder()
                .room(room)
                .role(role)
                .userId(user.getUserId())
                .build();
        return roomParticipant;
    }
}
