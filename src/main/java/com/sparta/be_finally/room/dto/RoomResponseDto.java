package com.sparta.be_finally.room.dto;

import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class RoomResponseDto {
    private Long id;
    private String roomName;
    private String nickname;

    private int roomCode;
    private int userCount;

    private LocalDateTime expireDate;

    //private HashMap<Integer, Long> userList;


    public RoomResponseDto(Room room) {
        this.id = room.getId();
        this.roomName = room.getRoomName();
        this.nickname = room.getUser().getNickname();
        this.roomCode = room.getRoomCode();
        this.userCount = room.getUserCount();
        this.expireDate = room.getExpireDate();

    }

    public RoomResponseDto(Room room, User user) {
        this.id = room.getId();
        this.roomName = room.getRoomName();
        this.nickname = room.getUser().getNickname();
        this.roomCode = room.getRoomCode();
        this.userCount = room.getUserCount();
        this.expireDate = room.getExpireDate();



    }

    public RoomResponseDto(Room room, RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto, User user) {
        this.id = room.getId();
        this.roomName = room.getRoomName();
        this.nickname = room.getUser().getNickname();
        this.roomCode = room.getRoomCode();
        this.userCount = room.getUserCount();
        this.expireDate = room.getExpireDate();
       // this.userList = new HashMap<Integer, Long>(roomCodeRequestDto.getRoomCode(),user.getId());
    }
    }
