package com.sparta.be_finally.config.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserStatusCode implements StatusCode {
     NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT("Security Context에 인증 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value()),
     USER_SIGNUP_SUCCESS("회원가입 성공", HttpStatus.OK.value()),
     USER_SIGNUP_FAIL("회원가입 실패", HttpStatus.BAD_REQUEST.value()),
     USER_LOGIN_SUCCESS("로그인 성공", HttpStatus.OK.value()),

     ONLY_FOR_ADMIN("관리자만 가능합니다.", HttpStatus.BAD_REQUEST.value()),
     WRONG_USERID_PATTERN("아이디는 최소 5자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다.", HttpStatus.BAD_REQUEST.value()),
     WRONG_PASSWORD_PATTERN("비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 구성되어야 합니다.", HttpStatus.BAD_REQUEST.value()),

     FAILE_USERID("본인 인증 실패",HttpStatus.BAD_REQUEST.value()),
     WRONG_LOGININFO("아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value()),

     WRONG_ADMIN_TOKEN("관리자 암호가 틀려 등록이 불가능합니다.", HttpStatus.BAD_REQUEST.value()),
     OVERLAPPED_USERID("중복된 userId 입니다.", HttpStatus.BAD_REQUEST.value()),
     AVAILABLE_USERID("사용 가능한 userId 입니다.", HttpStatus.OK.value()),

     PASSWORD_CHECK("입력된 비밀번호가 다릅니다.", HttpStatus.BAD_REQUEST.value()),
     INVALID_TOKEN("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value()),

     TEXT_SEND_SUCCESS("문자 전송 완료", HttpStatus.OK.value()),

     AGREE_USER_TYPED("인증 완료", HttpStatus.OK.value()),
     FAIL_USER_TYPED("인증 실패", HttpStatus.BAD_REQUEST.value()),

     NOT_FOUND_USER("일치하는 유저가 없습니다.", HttpStatus.BAD_REQUEST.value()),
     FAIL_IDENTIFICATION("본인 인증 실패!", HttpStatus.BAD_REQUEST.value()),
     SUCCESS_IDENTIFICATION("인증문자 전송 완료! 코드입력 후 비밀번호를 재설정 해주세요", HttpStatus.BAD_REQUEST.value());

     private final String statusMsg;
     private final int statusCode;
     
     public String statusMsg(){
          return statusMsg;
     }
}