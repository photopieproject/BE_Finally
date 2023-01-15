/*
package com.sparta.be_finally.config.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// 2. 시그널링 서버에서 메시지 핸들러 생성
// 직접 WebRTC 연결을 설정하기 위해 서로 다른 클라이언트 간의 메타데이터 교환을 지원하는 데 필수적
// 클라이언트로부터 메시지를 받으면 자신을 제외한 다른 모든 클라이언트에게 메시지를 보낸다.
@Component
public class SocketHandler extends TextWebSocketHandler {

    // 모든 클라이언트를 추적 할 수 있도록 수신 된 세션을 세션 List에 추가
    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
                webSocketSession.sendMessage(message);
            }
        }
    }

    // 수신된 세션을 세션 목록에 추가하여 모든 클라이언트를 추적할 수 있다.
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }
}

*/
