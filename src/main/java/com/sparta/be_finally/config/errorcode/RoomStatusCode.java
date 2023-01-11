package com.sparta.be_finally.config.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoomStatusCode implements StatusCode {
    SUCESS_ROOM("방 만들기 성공", HttpStatus.OK.value()),
    FAIL_ROOM("방 만들기 실패", HttpStatus.BAD_REQUEST.value());

    private final String statusMsg;
    private final int statusCode;

    public String statusMsg(){
        return statusMsg;
    }
}
