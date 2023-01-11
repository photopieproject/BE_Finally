package com.sparta.be_finally.config.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonStatusCode implements StatusCode {
     OK("정상", HttpStatus.OK.value()),
     CREATE_ROOM("방 개설 완료", HttpStatus.OK.value()),
     ENTRANCE_ROOM("방 입장 완료", HttpStatus.OK.value()),
     INCORRECT_ROOM_CODE("잘못된 방코드 입니다.",HttpStatus.BAD_REQUEST.value()),
     CHOICE_FRAME("프레임 선택 완료", HttpStatus.OK.value()),
     INVALID_PARAMETER("Invalid parameter included",HttpStatus.BAD_REQUEST.value()),
     INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()),
     DELETE_USER("회원 탈퇴 성공", HttpStatus.OK.value());

     private final String StatusMsg;
     private final int statusCode;
     
}
