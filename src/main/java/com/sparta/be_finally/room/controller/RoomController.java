package com.sparta.be_finally.room.controller;

import com.sparta.be_finally.config.dto.PrivateResponseBody;
import com.sparta.be_finally.config.errorcode.CommonStatusCode;
import com.sparta.be_finally.room.dto.FrameRequestDto;
import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.room.service.RoomService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@Api(tags = {"Room API"})
@Slf4j
@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    // 오픈비두 과정
    // 1. 세션 : 참가자가 연결하여 오디오 및 비디오 스트림을 보내고 받을 수 있는 가상 공간 (= 사진 촬영 방)
    // 2.

    @PostMapping("/room")
    public PrivateResponseBody<?> createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        return roomService.createRoom(roomRequestDto);
    }

    //방 입장//
    @PostMapping("/room/roomCode")
    public PrivateResponseBody roomEnter(@RequestBody RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto) {
        return roomService.roomEnter(roomCodeRequestDto);
    }

    //방 나가기
//    @GetMapping("/room/{roomCode}/exit")
//    public PrivateResponseBody roomExit(@PathVariable int roomCode){
//        return roomService.roomExit(roomCode);
//    }



    // 프레임 선택
    @PutMapping("/room/{roomId}")
    public PrivateResponseBody choiceFrame(@PathVariable Long roomId, @RequestBody FrameRequestDto frameRequestDto) {
        return roomService.choiceFrame(roomId, frameRequestDto);
    }

}