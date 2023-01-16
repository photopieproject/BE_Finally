package com.sparta.be_finally.room.service;

import com.sparta.be_finally.config.dto.PrivateResponseBody;
import com.sparta.be_finally.config.errorcode.CommonStatusCode;
import com.sparta.be_finally.config.exception.RestApiException;
import com.sparta.be_finally.config.util.SecurityUtil;
import com.sparta.be_finally.room.dto.FrameRequestDto;
import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.room.dto.RoomResponseDto;
import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.room.repository.RoomRepository;
import com.sparta.be_finally.user.entity.User;
import com.sparta.be_finally.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    public final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public PrivateResponseBody<?> createRoom(RoomRequestDto roomRequestDto) {
        User user = SecurityUtil.getCurrentUser();
        if (roomRequestDto.getRoomName().isEmpty()) {
            return new PrivateResponseBody<>(CommonStatusCode.CREATE_ROOM_NAME);
        } else {
            Room room = roomRepository.save(new Room(roomRequestDto, user));
            return new PrivateResponseBody<>(CommonStatusCode.CREATE_ROOM, new RoomResponseDto(room));
        }
    }


    //    public void enter(String id) {
//        Room room = roomRepository.findById(id)
//                .orElseThrow(() -> new CustomException(NOT_FOUND_CHAT_ROOM));
//        room.enter();
    //}
    // 방 입장 하기
    @Transactional
    public PrivateResponseBody<?> roomEnter(RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto) {

        User user = SecurityUtil.getCurrentUser();
        Room room = roomRepository.findByRoomCode(roomCodeRequestDto.getRoomCode()).orElseThrow(
                () -> new RestApiException(CommonStatusCode.FAIL_ENTER2)
        );

       // room.getRoomCode() == roomCodeRequestDto.getRoomCode() &&
        if (!roomRepository.existsByUserId(user.getId())) {
            if (room.getUserCount() < 4 && !roomRepository.existsById(user.getId())) {
                room.enter();
                return new PrivateResponseBody<>(CommonStatusCode.ENTRANCE_ROOM, new RoomResponseDto(room));
            } else {
                if (roomRepository.existsById(user.getId())) {
                    return new PrivateResponseBody<>(CommonStatusCode.REGISTERED_USER);
                } else {
                if (room.getUserCount() == 4) {
                    return new PrivateResponseBody<>(CommonStatusCode.FAIL_MAN_ENTER);
                    }
                }
            }
        }
        return new PrivateResponseBody<>(CommonStatusCode.FAIL_NUMBER);
    }



        @Transactional
        public PrivateResponseBody choiceFrame (Long roomId, FrameRequestDto frameRequestDto){
            User user = SecurityUtil.getCurrentUser();
            if (!roomRepository.existsByIdAndUserId(roomId, user.getId())) {
                return new PrivateResponseBody(CommonStatusCode.FAIL_CHOICE_FRAME);
            }
            Room room = roomRepository.findById(roomId).orElse(null);
            room.updateFrame(frameRequestDto);

            return new PrivateResponseBody(CommonStatusCode.CHOICE_FRAME);
        }
    }















