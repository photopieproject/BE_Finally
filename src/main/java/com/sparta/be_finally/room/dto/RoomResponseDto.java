package com.sparta.be_finally.room.dto;

import com.sparta.be_finally.room.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomResponseDto {
    private Long id;
    private String roomName;
    private String nickName;
    private int roomCode;

    public RoomResponseDto(Room room) {
        this.id = room.getId();
        this.roomName = room.getRoomName();
        this.nickName = room.getUser().getNickname();
        this.roomCode = room.getRoomCode();
    }
}
