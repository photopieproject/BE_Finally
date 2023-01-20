package com.sparta.be_finally.room.controller;

import com.sparta.be_finally.config.dto.PrivateResponseBody;
import com.sparta.be_finally.config.errorcode.CommonStatusCode;
import com.sparta.be_finally.room.dto.FrameRequestDto;
import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.room.service.RoomService;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
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

    // 오픈비두
    // 1. 세션 : 참가자가 연결하여 오디오 및 비디오 스트림을 보내고 받을 수 있는 가상 공간 (= 사진 촬영 방)
    // 2. 연결 : 참가자가 연결할 수 있도록 하려면 특정 세션에 대한 연결을 생성해야 합니다.
    //          각 참가자는 토큰을 사용하여 하나의 연결을 사용하여 연결합니다

    // 방 생성
    @PostMapping("/room")
    public PrivateResponseBody createRoom(@RequestBody RoomRequestDto roomRequestDto) throws OpenViduJavaClientException, OpenViduHttpException {
        return new PrivateResponseBody(CommonStatusCode.CREATE_ROOM,roomService.createRoom(roomRequestDto));
    }

    // 방 입장
    @PostMapping("/room/roomCode")
    public PrivateResponseBody roomEnter(@RequestBody RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto) throws OpenViduJavaClientException, OpenViduHttpException {
        return new PrivateResponseBody(CommonStatusCode.ENTRANCE_ROOM,roomService.roomEnter(roomCodeRequestDto));
    }

    // 방 종료 (Openvidu session만 삭제, DB는 24시간 후에 삭제 됨)
    @DeleteMapping("/room/roomCode")
    public PrivateResponseBody roomExit(@RequestBody RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto) throws OpenViduJavaClientException, OpenViduHttpException {
        roomService.roomExit(roomCodeRequestDto);
        return new PrivateResponseBody(CommonStatusCode.DELETE_ROOM_OPENVIDU);
    }

    // 프레임 선택
    @PutMapping("/room/{roomId}")
    public PrivateResponseBody choiceFrame(@PathVariable Long roomId, @RequestBody FrameRequestDto frameRequestDto) {
        return roomService.choiceFrame(roomId, frameRequestDto);
    }

}