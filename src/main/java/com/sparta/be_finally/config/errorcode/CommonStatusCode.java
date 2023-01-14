package com.sparta.be_finally.config.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonStatusCode implements StatusCode {
     OK("정상", HttpStatus.OK.value()),
     CREATE_ROOM_NAME("방 이름을 입력해주세요",HttpStatus.BAD_REQUEST.value()),
     CREATE_ROOM("방 개설 완료", HttpStatus.OK.value()),
     ENTRANCE_ROOM("방 입장 완료", HttpStatus.OK.value()),
     INCORRECT_ROOM_CODE("잘못된 방코드 입니다.",HttpStatus.BAD_REQUEST.value()),

     FAIL_ROOM("방 만들기 실패", HttpStatus.BAD_REQUEST.value()),
     FAIL_ENTER("방 입장 실패", HttpStatus.BAD_REQUEST.value()),

     FAIL_NUMBER("방 코드가 틀렸습니다",HttpStatus.BAD_REQUEST.value()),
     FAIL_ENTER2("방 코드 또는 방이 있는지 확인하세요",HttpStatus.BAD_REQUEST.value()),
     SUCCESS_ROOM_EXIT("방 나가기 완료", HttpStatus.OK.value()),
     SUCCESS_ROOM_TOTAL_EXIT("방 나가기 완료", HttpStatus.OK.value()),
     CHOICE_FRAME("프레임 선택 완료", HttpStatus.OK.value()),
     FAIL_CHOICE_FRAME("방장만 프레임을 선택할 수 있습니다.", HttpStatus.BAD_REQUEST.value()),
     INVALID_PARAMETER("Invalid parameter included",HttpStatus.BAD_REQUEST.value()),
     INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()),
     DELETE_USER("회원 탈퇴 성공", HttpStatus.OK.value());


     private final String StatusMsg;
     private final int statusCode;
     
}
