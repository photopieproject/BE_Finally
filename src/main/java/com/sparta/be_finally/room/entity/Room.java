package com.sparta.be_finally.room.entity;

import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String roomName;
    private int roomCode;
    private int frame;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Room(RoomRequestDto roomRequestDto, User user) {
        this.roomName = roomRequestDto.getRoomName();
        this.roomCode = (int)(Math.random()*100000);
        this.user = user;
    }

}
