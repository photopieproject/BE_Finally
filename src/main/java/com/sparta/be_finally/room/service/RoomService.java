package com.sparta.be_finally.room.service;

import com.sparta.be_finally.config.controller.OpenviduController;
import com.sparta.be_finally.config.dto.PrivateResponseBody;
import com.sparta.be_finally.config.errorcode.CommonStatusCode;
import com.sparta.be_finally.config.exception.RestApiException;
import com.sparta.be_finally.config.util.SecurityUtil;
import com.sparta.be_finally.config.validator.Validator;
import com.sparta.be_finally.room.dto.*;
import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.room.entity.RoomParticipant;
import com.sparta.be_finally.room.repository.RoomParticipantRepository;
import com.sparta.be_finally.room.repository.RoomRepository;
import com.sparta.be_finally.user.entity.User;
import com.sparta.be_finally.user.repository.UserRepository;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomParticipantRepository roomParticipantRepository;
    private final Validator validator;
    private OpenVidu openVidu;

    // OpenVidu 서버가 수신하는 URL
    @Value("${openvidu.url}")
    private String OPENVIDU_URL;

    // OpenVidu 서버와 공유되는 비밀
    @Value("${openvidu.secret}")
    private String OPENVIDU_SECRET;

    @PostConstruct
    public OpenVidu openVidu() {
        return openVidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    // 방 생성시 세션(Openvidu room) 초기화
    @Transactional
    public RoomResponseDto createRoom(RoomRequestDto roomRequestDto) throws OpenViduJavaClientException, OpenViduHttpException {
        User user = SecurityUtil.getCurrentUser();

        // 방 이름 미입력시 에러 메세지 응답
        if (roomRequestDto.getRoomName().isEmpty()) {
            throw new RestApiException(CommonStatusCode.CREATE_ROOM_NAME);

        } else {
            // 사용자가 연결할 때 다른 사용자에게 전달할 선택적 데이터, 유저의 닉네임을 전달할 것
            String serverData = user.getNickname();

            // serverData 및 역할을 사용하여 connectionProperties 객체를 빌드합니다.
            ConnectionProperties connectionProperties = new ConnectionProperties.Builder()
                    .type(ConnectionType.WEBRTC)
                    .data(serverData)
                    .build();

            // 새로운 openvidu 세션 생성
            Session session = openVidu.createSession();

            // 생성된 세션과 해당 세션에 연결된 다른 peer 에게 보여줄 data 를 담은 token을 생성
            String token = session.createConnection(connectionProperties).getToken();

            // 방 생성
            Room room = roomRepository.save(new Room(roomRequestDto,user,session.getSessionId(),token));

            // 방장 token update (토큰이 있어야 방에 입장 가능)
            user.update(token);

            // 방장 방 입장 처리
            roomParticipantRepository.save(RoomParticipant.createRoomParticipant(room,user));

            return RoomResponseDto.builder()
                    .roomName(roomRequestDto.getRoomName())
                    .nickname(user.getNickname())
                    .roomCode(room.getRoomCode())
                    .userCount(room.getUserCount())
                    .sessionId(session.getSessionId())
                    .token(token)
                    .expireDate(room.getExpireDate())
                    .build();
        }
    }

    // 방 입장 하기
    @Transactional
    public RoomResponseDto roomEnter(RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto) throws OpenViduJavaClientException, OpenViduHttpException {
        User user = SecurityUtil.getCurrentUser();

        // 방 코드로 방 조회
        Room room = validator.existsRoom(roomCodeRequestDto.getRoomCode());

        // 방 나간 후 재입장 처리
        if (roomParticipantRepository.findRoomParticipantByUserIdAndRoom(user.getUserId(),room) != null) {
            throw new RestApiException(CommonStatusCode.REGISTERED_USER);

        } else if (roomParticipantRepository.findRoomParticipantByUserIdAndRoom(user.getUserId(),room) == null && room.getUserCount() < 4){
            // 방 첫 입장
            // Openvidu room 입장 전, session 여부 확인
            if (!roomRepository.existsByIdAndSessionId(room.getId(),room.getSessionId())) {
                throw new RestApiException(CommonStatusCode.FAIL_ENTER_OPENVIDU);
            }

            //serverData 및 역할을 사용하여 connectionProperties 객체를 빌드합니다.
            ConnectionProperties connectionProperties = new ConnectionProperties.Builder()
                    .type(ConnectionType.WEBRTC)
                    .data(user.getNickname())
                    .build();

            Session session = validator.getSession(room.getSessionId());

            //토큰 생성 후 입장한 유저에 token update
            user.update(session.createConnection(connectionProperties).getToken());

            // 방 입장 인원수 +1 업데이트
            room.enter();

            // 참여자 DB에 USER 저장
            roomParticipantRepository.save(RoomParticipant.createRoomParticipant(room,user));

            return RoomResponseDto.builder()
                    .nickname(user.getNickname())
                    .build();

        } else {
            // 인원수 초과시 에러 메세지 응답
            throw new RestApiException(CommonStatusCode.FAIL_MAN_ENTER);
        }
    }
    @Transactional
    public void roomExit(RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto) throws OpenViduJavaClientException, OpenViduHttpException {
        // 방 코드로 방 조회
        Room room = validator.existsRoom(roomCodeRequestDto.getRoomCode());

        if (!roomRepository.existsByIdAndSessionId(room.getId(),room.getSessionId())) {
            throw new RestApiException(CommonStatusCode.FAIL_ENTER_OPENVIDU);
        }

        Session session = validator.getSession(room.getSessionId());

        //Openvidu session 삭제 (방 종료) 아마도..?
        session.close();
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


















