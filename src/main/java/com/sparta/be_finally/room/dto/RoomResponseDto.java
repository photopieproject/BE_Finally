package com.sparta.be_finally.room.dto;

import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.user.entity.User;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.DoubleStream;

@Getter
@Setter
@Builder
public class RoomResponseDto {

    private Long id;
    private String roomName;
    private String nickname;
    private String roomCode;
    private int userCount;
    private String sessionId;
    private String token;
    private LocalDateTime expireDate;
    private String role;

}
