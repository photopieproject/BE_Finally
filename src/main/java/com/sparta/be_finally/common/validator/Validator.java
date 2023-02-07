package com.sparta.be_finally.common.validator;

import com.sparta.be_finally.common.errorcode.CommonStatusCode;
import com.sparta.be_finally.common.exception.RestApiException;
import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.room.repository.RoomRepository;
import com.sparta.be_finally.user.entity.User;
import com.sparta.be_finally.user.repository.UserRepository;
import io.openvidu.java.client.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@AllArgsConstructor
public class Validator {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    // roomcode 로 room 존재 여부 확인 후 Room return
    @Transactional(readOnly = true)
    public Room existsRoom(String roomCode) {
        if (roomRepository.existsByRoomCode(roomCode)) {
            Optional<Room> room = roomRepository.findByRoomCode(roomCode);
            return room.get();

        } else if (roomCode.equals("")) {
            // 코드 입력 하지 않고 방 입장시 코드 입력하라는 에러 메세지 응답
            throw new RestApiException(CommonStatusCode.ROOMCODE_IS_NULL);

        } else {
            // 틀린 방 코드로 입장시 에러 메세지 응답
            throw new RestApiException(CommonStatusCode.FAIL_ENTER2);
        }
    }

    // roomId 로 room 존재 여부 확인 후 Room return
    @Transactional(readOnly = true)
    public Room existsRoom(Long roomId) {
        if (roomRepository.existsById(roomId)) {
            Optional<Room> room = roomRepository.findById(roomId);
            return room.get();

        } else {
            // 존재하지 않는 방 - 에러 메세지 응답
            throw new RestApiException(CommonStatusCode.FAIL_ENTER2);
        }
    }

    // room 테이블의 sessionId에 openvidu SessionId 이 저장 되어있는지 확인
    @Transactional(readOnly = true)
    public void existsRoomSessionId(String sessionId) {
        if (!roomRepository.existsBySessionId(sessionId)) {
            throw new RestApiException(CommonStatusCode.FAIL_ENTER_OPENVIDU);
        }
    }

    // 세션(방)에 입장 할 수 있는 토큰 생성 후 입장한 user 에게 토큰 저장
    @Transactional
    public String getToken(Session session, User user) throws OpenViduJavaClientException, OpenViduHttpException {
        //serverData 및 역할을 사용하여 connectionProperties 객체를 빌드합니다.
        ConnectionProperties connectionProperties = new ConnectionProperties.Builder()
                .type(ConnectionType.WEBRTC)
                .data(user.getNickname())
                .build();

        String token = session.createConnection(connectionProperties).getToken();

        // 토큰 생성 후 입장한 유저에 token update
        userRepository.update(user.getId(), token);

        return token;
    }
}