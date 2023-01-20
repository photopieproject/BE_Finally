package com.sparta.be_finally.config.validator;

import com.sparta.be_finally.config.errorcode.CommonStatusCode;
import com.sparta.be_finally.config.errorcode.StatusCode;
import com.sparta.be_finally.config.exception.RestApiException;
import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.room.repository.RoomRepository;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.Session;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class Validator {
    private final RoomRepository roomRepository;

    // roomcode 로 room 존재 여부 확인 후 Room return
    @Transactional(readOnly = true)
    public Room existsRoom(String roomcode) {
        if (roomRepository.existsByRoomCode(roomcode)) {
            Optional<Room> room = roomRepository.findByRoomCode(roomcode);
            return room.get();

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
}