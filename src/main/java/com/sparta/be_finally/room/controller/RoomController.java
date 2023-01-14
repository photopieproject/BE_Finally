package com.sparta.be_finally.room.controller;

import com.sparta.be_finally.config.dto.PrivateResponseBody;
import com.sparta.be_finally.config.errorcode.CommonStatusCode;
import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.room.service.RoomService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"Room API"})
@Slf4j
@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/room")
    public PrivateResponseBody createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        return roomService.createRoom(roomRequestDto);
    }


    //방 입장

    @PostMapping("/room/roomCode")
    public PrivateResponseBody roomEnter(@RequestBody RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto) {
        return roomService.roomEnter(roomCodeRequestDto);
    }


    //방 나가기
//    @GetMapping("/room/{roomCode}/exit")
//    public PrivateResponseBody roomExit(@PathVariable int roomCode){
//        return roomService.roomExit(roomCode);
//    }



}