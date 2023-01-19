package com.sparta.be_finally.room.service;

import com.sparta.be_finally.config.dto.PrivateResponseBody;
import com.sparta.be_finally.config.errorcode.CommonStatusCode;
import com.sparta.be_finally.config.exception.RestApiException;
import com.sparta.be_finally.config.util.SecurityUtil;
import com.sparta.be_finally.room.dto.*;
import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.room.entity.RoomParticipant;
import com.sparta.be_finally.room.repository.RoomParticipantRepository;
import com.sparta.be_finally.room.repository.RoomRepository;
import com.sparta.be_finally.user.entity.User;
import com.sparta.be_finally.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomParticipantRepository roomParticipantRepository;


    @Transactional
    public PrivateResponseBody<?> createRoom(RoomRequestDto roomRequestDto) {
        User user = SecurityUtil.getCurrentUser();
        if (roomRequestDto.getRoomName().isEmpty()) {
            return new PrivateResponseBody<>(CommonStatusCode.CREATE_ROOM_NAME);
        } else {
            Room room = roomRepository.save(new Room(roomRequestDto, user));
            roomParticipantRepository.save(RoomParticipant.createRoomParticipant(room,user));
            return new PrivateResponseBody<>(CommonStatusCode.CREATE_ROOM, new RoomResponseDto(room));
        }
    }


    // 방 입장 하기


    @Transactional
    public PrivateResponseBody<?> roomEnter(RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto) {
        Room room = roomRepository.findByRoomCode(roomCodeRequestDto.getRoomCode()).orElseThrow(
                () -> new RestApiException(CommonStatusCode.FAIL_ENTER2));
        User user = SecurityUtil.getCurrentUser();
      //  RoomParticipant roomParticipant = roomParticipantRepository.findAllByUserId(user.getUserId());

        if (roomParticipantRepository.findRoomParticipantByUserIdAndRoom(user.getUserId(),room) != null){
            throw new RestApiException(CommonStatusCode.REGISTERED_USER);
    }    else if (roomParticipantRepository.findRoomParticipantByUserIdAndRoom(user.getUserId(),room) == null && room.getUserCount() < 4){
            room.enter();
            roomParticipantRepository.save(RoomParticipant.createRoomParticipant(room,user));
            return new PrivateResponseBody<>(CommonStatusCode.ENTRANCE_ROOM, new RoomResponseDto(room));
        } else {
            return new PrivateResponseBody<>(CommonStatusCode.FAIL_MAN_ENTER);
        }
    }

        //public PrivateResponseBody<?> roomEnter(RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto) {

//        User user = SecurityUtil.getCurrentUser();
//        Room room = roomRepository.findByRoomCode(roomCodeRequestDto.getRoomCode()).orElseThrow(
//                () -> new RestApiException(CommonStatusCode.FAIL_ENTER2)
//        );
//       HashMap<Long, Integer> userlist = new HashMap<>();
//        RoomParticipant roomParticipant = participantRepository.findBy()
//

        //입장 가능 인원 확인

//        if (roomCodeRequestDto.getRoomCode() == room.getRoomCode()){
//            //방 만들기를 한 유저가 아니라면
//            if(!room.getUser().getUserId().isEmpty()){
//
//            }
//        }


//
//        if (roomCodeRequestDto.getRoomCode() == room.getRoomCode()) {
//            if (room.getUserCount() <4) {
//                room.enter();
//                userlist.put(user.getId(), roomCodeRequestDto.getRoomCode());
//            } if (userlist.containsKey(user.getId())){
//                return new PrivateResponseBody<>(CommonStatusCode.ENTRANCE_ROOM,new RoomResponseDto(room));
//            }
//                return new PrivateResponseBody<>(CommonStatusCode.FAIL_MAN_ENTER);
//        }
//        return new PrivateResponseBody<>(CommonStatusCode.FAIL_NUMBER);
//    }


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


















