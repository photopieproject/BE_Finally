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

    // 방 입장 하기
    @Transactional
    public PrivateResponseBody roomEnter(Long roomid, RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto) {
        User user = SecurityUtil.getCurrentUser();
        Room room = roomRepository.findByIdAndRoomCode(roomid, roomCodeRequestDto.getRoomCode()).orElseThrow(
                () -> new RestApiException(CommonStatusCode.FAIL_ENTER2)
        );

        // 방번호가 같고, 유저가 있는 경우 room.getUser().equals7(user)

//            if (roomCodeRequestDto.getRoomCode() == room.getRoomCode() && userRepository.existsByUserId(user.getUserId())) {
//                return new PrivateResponseBody<>(RoomStatusCode.SUCESS_ENTER);
//            } else {
//                return new PrivateResponseBody<>(RoomStatusCode.FAIL_NUMBER);
//            }
//        }


        if (roomCodeRequestDto.getRoomCode() == room.getRoomCode()) {
            if (userRepository.existsByUserId(user.getUserId())) {
                return new PrivateResponseBody<>(CommonStatusCode.SUCESS_ENTER);
            } else {
                return new PrivateResponseBody<>(CommonStatusCode.FAIL_NUMBER);
            }
        }
        return new PrivateResponseBody<>(CommonStatusCode.FAIL_NUMBER);
    }
}


