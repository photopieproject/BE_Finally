package com.sparta.be_finally.room.dto;

import com.sparta.be_finally.room.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RoomResponseDto {
    private Long id;
    private String roomName;
    private String nickname;
    private int roomCode;
    private int userCount;

    private  LocalDateTime expireDate;


    public RoomResponseDto(Room room) {
        this.id = room.getId();
        this.roomName = room.getRoomName();
        this.nickname = room.getUser().getNickname();
        this.roomCode = room.getRoomCode();
        this.userCount = room.getUserCount();
        this.expireDate = room.getExpireDate();

    }

}
