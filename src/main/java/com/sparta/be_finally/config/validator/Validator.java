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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class Validator {
    private final RoomRepository roomRepository;
    private final OpenVidu openVidu;

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
        if (roomRepository.existsByRoomId(roomId)) {
            Optional<Room> room = roomRepository.findByRoomId(roomId);
            return room.get();

        } else {
            // 존재하지 않는 방 - 에러 메세지 응답
            throw new RestApiException(CommonStatusCode.FAIL_ENTER2);
        }
    }

    public Session getSession(String sessionId) throws OpenViduJavaClientException, OpenViduHttpException {
        //오픈비두에 활성화된 세션을 모두 가져와 리스트에 담는다.
        //활성화된 session의 sessionId들을 room에서 get한 sessionId(입장할 채팅방의 sessionId)와 비교
        //같을 경우 해당 session으로 새로운 토큰을 생성한다.
        openVidu.fetch();

        List<Session> activeSessionList = openVidu.getActiveSessions();

        Session session = null;

        for (Session getSession : activeSessionList) {
            if (getSession.getSessionId().equals(sessionId)) {
                session = getSession;
            }
        }
        return session;
    }
}