package com.sparta.be_finally.room.dto;

import com.sparta.be_finally.room.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Map;


@Setter
@Getter
@NoArgsConstructor
public class RoomRequestDto {

    @Size(min = 1, max = 10)
    @NotBlank
    private String roomName;

//    private int maxPeople;

    public RoomRequestDto(Room room){
        this.roomName = room.getRoomName();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RoomCodeRequestDto {
        private String roomCode;
    }
}



