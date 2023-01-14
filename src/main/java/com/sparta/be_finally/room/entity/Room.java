package com.sparta.be_finally.room.entity;

import com.sparta.be_finally.room.dto.FrameRequestDto;
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

    @Column(nullable = false)
    private String roomName;
    private int roomCode;
    private int frame = 0;

    private int userCount = 4;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;



    public Room(RoomRequestDto roomRequestDto, User user) {
        this.roomName = roomRequestDto.getRoomName();
        this.roomCode = (int)(Math.random()*100000);
        this.user = user;
        this.userCount--;
    }

    public void updateFrame(FrameRequestDto frameRequestDto) {
        this.frame = frameRequestDto.getFrame();
    }

}
