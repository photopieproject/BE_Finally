package com.sparta.be_finally.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker//@EnableWebSocketMessageBroker is used to enable our WebSocket server
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
    registry.setApplicationDestinationPrefixes("/app");
    registry.enableSimpleBroker("/topic");
    }
}


