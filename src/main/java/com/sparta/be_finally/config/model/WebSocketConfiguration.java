package com.sparta.be_finally.config.model;

import com.sparta.be_finally.config.handler.SocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    // 1. 시그널링 서버 구축
    // 각 Peer간 SDP 메시지 Offer, Answer를 전달해주고, ICE 후보를 주고 받을 수 있도록 돕는 서버
    // Peer 관리를 해주는 포워딩 서버
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketHandler(), "/socket").setAllowedOrigins("*");
    }
}
