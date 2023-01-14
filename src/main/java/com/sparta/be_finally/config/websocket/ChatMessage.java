package com.sparta.be_finally.config.websocket;

import java.awt.*;

public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getSender(){
        return sender;
    }
    public void setSender(String sender){
        this.sender = sender;
    }
}
