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

import java.util.*;

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


    // 방 입장 하기
    @Transactional
    public PrivateResponseBody<?> roomEnter(RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto) {

        User user = SecurityUtil.getCurrentUser();
        Room room = roomRepository.findByRoomCode(roomCodeRequestDto.getRoomCode()).orElseThrow(
                () -> new RestApiException(CommonStatusCode.FAIL_ENTER2)
        );


        ArrayList<HashMap<Long, Integer>> list = new ArrayList<HashMap<Long, Integer>>();
        HashMap<Long, Integer> userListMap = new HashMap<Long, Integer>();

        if(roomCodeRequestDto.getRoomCode() == room.getRoomCode()) {
            if (room.getUserCount() == 4 && userListMap.equals(user.getId())) {

                return new PrivateResponseBody<>(CommonStatusCode.REGISTERED_USER, userListMap);
            }
            if (userListMap.equals(user.getId())){
                return new PrivateResponseBody<>(CommonStatusCode.REGISTERED_USER);
            }

        }
        return new PrivateResponseBody<>(CommonStatusCode.FAIL_MAN_ENTER);
    }


//        map = new HashMap<String,String>();
//        for (int i = 0; i <5; i++) {
//            map.put(i+ "Hello","Hello"+i);
//        }
//        list.add(map);




//        List<Map<String, Object>> listMapInsert = new ArrayList<Map<String, Object>>();
//        // [for 반복문을 순회하면서 데이터 삽입 실시]
//        for (int i = 1; i <= 4; i++) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put(String.valueOf(i), i + "value");
//
//            listMapInsert.add(map);
//        }
//        // [List Map 결과 출력 실시]
//        System.out.println("List Map Insert : " + listMapInsert.toString());
//        System.out.println("");
//
//
//    }




//        // [List Map 파싱 수행 실시]
//        List<Map<String, Object>> listMapSelect = listMapInsert; // [1번 방법]
//        // List<Map<String, Object>> listMapSelect = new ArrayList<Map<String, Object>>(listMapInsert); // [2번 방법]
//        System.out.println("List Map Select : " + listMapSelect.toString());
//        System.out.println("");
//
//        // [for 문을 돌면서 List 배열 데이터 (Map) 출력 실시]
//        for (int i = 0; i < listMapSelect.size(); i++) {
//            // [hashmap 객체 선언 및 데이터 삽입]
//            HashMap<String, Object> hashmap = new HashMap<String, Object>(listMapSelect.get(i));
//            System.out.println("Map : " + hashmap.toString());
//        }
//        return null;
//    }




//        HashMap<Integer, User> userList = new HashMap<>();
//        // 채팅방 유저 리스트에 유저 추가
//
//        if (roomCodeRequestDto.getRoomCode() == room.getRoomCode()) {
//            userList.put(roomCodeRequestDto.getRoomCode(), user);
//        }
//        return new PrivateResponseBody<>(CommonStatusCode.ENTRANCE_ROOM,new RoomResponseDto(room,roomCodeRequestDto,user));
//    }

//
//            userList.put(roomCode,userId);
//
//            return ..
//        }
//        // 채팅방 전체 userlist 조회
//        public ArrayList<String> getUserList(Map<String, RoomRequestDto> chatRoomMap, String roomId){
//            ArrayList<String> list = new ArrayList<>();
//
//            RoomRequestDto room = chatRoomMap.get(roomId);
//
//            // hashmap 을 for 문을 돌린 후
//            // value 값만 뽑아내서 list 에 저장 후 reutrn
//            room.getUserList().forEach((key, value) -> list.add((String) value));
//            return list;
//        }


        //    public void enter(String id) {
//        Room room = roomRepository.findById(id)
//                .orElseThrow(() -> new CustomException(NOT_FOUND_CHAT_ROOM));
//        room.enter();
        //}


//
//        //채팅방 전체 userlist 조회
//        public ArrayList<String> getUserList(Map<String, ChatRoomDto> chatRoomMap, String roomId)
//
//        (Map<String, ChatRoomDto> chatRoomMap, String roomId){
//            ArrayList<String> list = new ArrayList<>();
//
//            ChatRoomDto room = chatRoomMap.get(roomId);
//
//            // hashmap 을 for 문을 돌린 후
//            // value 값만 뽑아내서 list 에 저장 후 reutrn
//            room.getUserList().forEach((key, value) -> list.add((String) value));
//            return list;


        // room.getRoomCode() == roomCodeRequestDto.getRoomCode() &&
//        if (!roomRepository.existsByUserId(user.getId())) {
//            if (room.getUserCount() < 4 && !roomRepository.existsById(user.getId())) {
//                room.enter();
//                return new PrivateResponseBody<>(CommonStatusCode.ENTRANCE_ROOM, new RoomResponseDto(room));
//            } else {
//                if (roomRepository.existsById(user.getId())) {
//                    return new PrivateResponseBody<>(CommonStatusCode.REGISTERED_USER);
//                } else {
//                if (room.getUserCount() == 4) {
//                    return new PrivateResponseBody<>(CommonStatusCode.FAIL_MAN_ENTER);
//                    }
//                }
//            }
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


















