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
    public RoomResponseDto createRoom(RoomRequestDto roomRequestDto) {
        User user = SecurityUtil.getCurrentUser();
        Room room = roomRepository.save(new Room(roomRequestDto, user));

        return new RoomResponseDto(room);
    }

    //방 입장 하기
    @Transactional
    public PrivateResponseBody roomEnter(int roomCode) {
        User user = SecurityUtil.getCurrentUser();
        Room room = roomRepository.findByRoomCodeAndUser(roomCode, user).orElseThrow(
                () -> new RestApiException(CommonStatusCode.FAIL_ENTER2)
        );

        if (room.getRoomCode() == roomCode) {
            return new PrivateResponseBody<>(CommonStatusCode.ENTRANCE_ROOM,room.getId());
        } else {
            return new PrivateResponseBody<>(CommonStatusCode.FAIL_NUMBER);
        }
    }
}


//        if (roomCodeRequestDto.getRoomCode() == room.getRoomCode()) {
//            if (userRepository.existsByUserId(user.getUserId())) {
//                return new PrivateResponseBody<>(CommonStatusCode.SUCESS_ENTER);
//            } else {
//                return new PrivateResponseBody<>(CommonStatusCode.FAIL_NUMBER);
//            }
//        }
//        return new PrivateResponseBody<>(CommonStatusCode.FAIL_NUMBER);



//    @Transactional
//    public PrivateResponseBody roomExit(Long roomid) {
//        //방 나가기
//        User user = SecurityUtil.getCurrentUser();
//        if (roomRepository.existsByIdAndUserIsNull(roomid)) {
//            roomRepository.deleteById(roomid);
//            return new PrivateResponseBody<>(CommonStatusCode.SUCCESS_ROOM_TOTAL_EXIT);
//        } else {
//            roomRepository.deleteById(user.getId());
//            return new PrivateResponseBody<>(CommonStatusCode.SUCCESS_ROOM_EXIT);
//        }
//     }
//





