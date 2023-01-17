package com.sparta.be_finally.room.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.be_finally.room.dto.FrameRequestDto;
import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class Room {
    private static final int VALID_HOUR = 24;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String roomName;

    private int roomCode;
    private int frame = 0;
    private int userCount =0;

    @NotNull
    private LocalDateTime expireDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomParticipant> roomParticipants = new ArrayList<>();



    public Room(RoomRequestDto roomRequestDto, User user) {
        this.roomName = roomRequestDto.getRoomName();
        this.roomCode = (int)(Math.random()*100000);
        //UUID.randomUUID().toString();
        this.user = user;
        this.userCount ++;
        this.expireDate = LocalDateTime.now().plusHours(VALID_HOUR);
    }

    public Room(RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto, User user) {
        this.roomCode = roomCodeRequestDto.getRoomCode();
        this.user = user;
    }



    public void enter() {
        this.userCount++;
    }
//    public void exit() {
//        this.userCount--;
//        if (userCount < 0) {
//            userCount = 0;
//        }
//    }


    public void updateFrame(FrameRequestDto frameRequestDto) {
        this.frame = frameRequestDto.getFrame();
    }

}



