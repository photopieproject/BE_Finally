package com.sparta.be_finally.config.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.OpenViduRole;
import io.openvidu.java.client.Session;
import io.openvidu.java.client.ConnectionProperties;
import io.openvidu.java.client.ConnectionType;

@RestController
@RequestMapping("/api-sessions")
public class SessionController {
/*
    // SDK의 진입점인 OpenVidu 개체
    private OpenVidu openVidu;

    // 세션 이름과 OpenVidu 세션 개체를 페어링하기 위한 컬렉션
    private Map<String, Session> mapSessions = new ConcurrentHashMap<>();
    //세션 이름과 토큰을 페어링하는 컬렉션(내부 맵은 토큰과 연결된 역할)

    private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();
    //OpenVidu 서버가 수신하는 URL
    private String OPENVIDU_URL;

    // OpenVidu 서버와 공유되는 비밀
    private String SECRET;

    public SessionController(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {
        this.SECRET = secret;
        this.OPENVIDU_URL = openviduUrl;
        this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
    }

    @RequestMapping(value = "/get-token", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> getToken(@RequestBody String sessionNameParam, HttpSession httpSession)
            throws ParseException {

        try {
            checkUserLogged(httpSession);
        } catch (Exception e) {
            return getErrorResponse(e);
        }

        //OpenVidu 서버에서 토큰 받기
        System.out.println("Getting a token from OpenVidu Server | {sessionName}=" + sessionNameParam);

        //OpenVidu 서버에서 받은 토큰
        JSONObject sessionJSON = (JSONObject) new JSONParser().parse(sessionNameParam);

        // 연결할 영상통화
        String sessionName = (String) sessionJSON.get("sessionName");

        // 이 사용자와 연결된 역할
        OpenViduRole role = LoginController.users.get(httpSession.getAttribute("loggedUser")).role;

        // 이 사용자가 연결할 때 다른 사용자에게 전달할 선택적 데이터
        // 영상 통화. 이 경우 HttpSession에 저장한 값을 가진 JSON
        // 로그인 시 객체
        String serverData = "{\"serverData\": \"" + httpSession.getAttribute("loggedUser") + "\"}";

        // serverData 및 역할을 사용하여 connectionProperties 개체 빌드
        ConnectionProperties connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC).data(serverData).role(role).build();

        JSONObject responseJson = new JSONObject();

        if (this.mapSessions.get(sessionName) != null) {
            // 세션이 이미 존재합니다.
            System.out.println("Existing session " + sessionName);
            try {

                // 최근에 생성된 connectionProperties로 새 연결 생성
                String token = this.mapSessions.get(sessionName).createConnection(connectionProperties).getToken();

                // 새 토큰을 저장하는 컬렉션 업데이트
                this.mapSessionNamesTokens.get(sessionName).put(token, role);

                // 토큰으로 응답 준비
                responseJson.put(0, token);

                // 클라이언트에게 응답을 반환
                return new ResponseEntity<>(responseJson, HttpStatus.OK);
            } catch (OpenViduJavaClientException e1) {
                // 내부 오류가 발생하면 오류 메시지를 생성하고 클라이언트에 반환
                return getErrorResponse(e1);
            } catch (OpenViduHttpException e2) {
                if (404 == e2.getStatus()) {
                    // 잘못된 sessionId(사용자가 예기치 않게 떠났음). 세션 개체가 잘못되었습니다.
                    // 더 이상. 컬렉션 정리 및 새 세션으로 계속
                    this.mapSessions.remove(sessionName);
                    this.mapSessionNamesTokens.remove(sessionName);
                }
            }
        }

        // 새 세션
        System.out.println("New session " + sessionName);
        try {

            // 새 OpenVidu 세션 생성
            Session session = this.openVidu.createSession();
            // 최근에 생성된 connectionProperties로 새 연결 생성
            String token = session.createConnection(connectionProperties).getToken();

            // 세션과 토큰을 컬렉션에 저장
            this.mapSessions.put(sessionName, session);
            this.mapSessionNamesTokens.put(sessionName, new ConcurrentHashMap<>());
            this.mapSessionNamesTokens.get(sessionName).put(token, role);

            // 토큰으로 응답 준비
            responseJson.put(0, token);

            // 클라이언트에게 응답을 반환
            return new ResponseEntity<>(responseJson, HttpStatus.OK);

        } catch (Exception e) {
            // 오류가 발생하면 오류 메시지를 생성하고 클라이언트에 반환
            return getErrorResponse(e);
        }
    }

    @RequestMapping(value = "/remove-user", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> removeUser(@RequestBody String sessionNameToken, HttpSession httpSession)
            throws Exception {

        try {
            checkUserLogged(httpSession);
        } catch (Exception e) {
            return getErrorResponse(e);
        }
        System.out.println("Removing user | {sessionName, token}=" + sessionNameToken);

        // BODY에서 매개변수 검색
        JSONObject sessionNameTokenJSON = (JSONObject) new JSONParser().parse(sessionNameToken);
        String sessionName = (String) sessionNameTokenJSON.get("sessionName");
        String token = (String) sessionNameTokenJSON.get("token");

        // 세션이 존재하는 경우
        if (this.mapSessions.get(sessionName) != null && this.mapSessionNamesTokens.get(sessionName) != null) {

            // 토큰이 존재하는 경우
            if (this.mapSessionNamesTokens.get(sessionName).remove(token) != null) {
                // 사용자가 세션을 나갔습니다.
                if (this.mapSessionNamesTokens.get(sessionName).isEmpty()) {
                    // 마지막 사용자가 남음: 세션을 제거해야 합니다.
                    this.mapSessions.remove(sessionName);
                }
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                // 토큰이 유효하지 않습니다
                System.out.println("Problems in the app server: the TOKEN wasn't valid");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            // SESSION이(가) 존재하지 않습니다.
            System.out.println("Problems in the app server: the SESSION does not exist");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<JSONObject> getErrorResponse(Exception e) {
        JSONObject json = new JSONObject();
        json.put("cause", e.getCause());
        json.put("error", e.getMessage());
        json.put("exception", e.getClass());
        return new ResponseEntity<>(json, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void checkUserLogged(HttpSession httpSession) throws Exception {
        if (httpSession == null || httpSession.getAttribute("loggedUser") == null) {
            throw new Exception("User not logged");
        }
    }
*/
}