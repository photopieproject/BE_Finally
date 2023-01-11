package com.sparta.be_finally.room.controller;

import com.sparta.be_finally.config.dto.PrivateResponseBody;
import com.sparta.be_finally.config.errorcode.RoomStatusCode;
import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.room.service.RoomService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Api(tags = {"Room API"})
@Slf4j
@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    @PostMapping("/room")
    public PrivateResponseBody createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        return new PrivateResponseBody(RoomStatusCode.SUCESS_ROOM, roomService.createRoom(roomRequestDto));
    }

}
