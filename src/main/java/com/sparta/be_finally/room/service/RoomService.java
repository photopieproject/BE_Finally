package com.sparta.be_finally.room.service;

import com.sparta.be_finally.config.dto.PrivateResponseBody;
import com.sparta.be_finally.config.errorcode.CommonStatusCode;
import com.sparta.be_finally.config.exception.RestApiException;
import com.sparta.be_finally.config.util.SecurityUtil;
import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.room.dto.RoomResponseDto;
import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.room.repository.RoomRepository;
import com.sparta.be_finally.user.entity.User;
import com.sparta.be_finally.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class RoomService {
    public final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public PrivateResponseBody<RoomResponseDto> createRoom(RoomRequestDto roomRequestDto) {
        User user = SecurityUtil.getCurrentUser();
        if(roomRequestDto.getRoomName().isEmpty()){
            return new PrivateResponseBody<>(CommonStatusCode.CREATE_ROOM_NAME);
        }
        Room room = roomRepository.save(new Room(roomRequestDto, user));
        return new PrivateResponseBody<>(CommonStatusCode.CREATE_ROOM, new RoomResponseDto(room));

    }

    // 방 입장 하기
    @Transactional
    public PrivateResponseBody<RoomResponseDto> roomEnter(RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto) {

        User user = SecurityUtil.getCurrentUser();
        Room room = roomRepository.findByRoomCode(roomCodeRequestDto.getRoomCode()).orElseThrow(
                () -> new RestApiException(CommonStatusCode.FAIL_ENTER2)
        );

        if (room.getRoomCode() == roomCodeRequestDto.getRoomCode()) {
            if (userRepository.existsByUserId(user.getUserId())) {
            }
            return new PrivateResponseBody<>(CommonStatusCode.ENTRANCE_ROOM, new RoomResponseDto(room));
        } else {
            return new PrivateResponseBody<>(CommonStatusCode.FAIL_NUMBER);
        }
    }


//    @Transactional
//    public PrivateResponseBody roomExit(int roomCode) {
//        //방 나가기
//        User user = SecurityUtil.getCurrentUser();
//        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow(
//                () -> new RestApiException(CommonStatusCode.INCORRECT_ROOM_CODE)
//        );
//        if (roomCode == room.getRoomCode()) {
//            roomRepository.deleteByUser(user);
//        }
//        return new PrivateResponseBody<>(CommonStatusCode.SUCCESS_ROOM_EXIT);
//         }
    }









