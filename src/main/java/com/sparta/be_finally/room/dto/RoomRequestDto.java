package com.sparta.be_finally.room.dto;

import com.sparta.be_finally.room.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;


@Setter
@Getter
@NoArgsConstructor
public class RoomRequestDto {

    private String roomName;



    public RoomRequestDto(Room room){
        this.roomName = room.getRoomName();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RoomCodeRequestDto {
        private int roomCode;


    }

}



