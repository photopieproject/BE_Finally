package com.sparta.be_finally.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.sparta.be_finally.user.dto.*;

import com.sparta.be_finally.common.dto.PrivateResponseBody;
import com.sparta.be_finally.common.errorcode.UserStatusCode;
import com.sparta.be_finally.user.dto.FindPasswordRequestDto;
import com.sparta.be_finally.user.dto.LoginRequestDto;
import com.sparta.be_finally.user.dto.ResetPasswordRequestDto;
import com.sparta.be_finally.user.dto.SignupRequestDto;

import com.sparta.be_finally.user.service.GoogleService;
import com.sparta.be_finally.user.service.KakaoService;
import com.sparta.be_finally.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Random;

@Api(tags = {"User API"})
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;
    private DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCSOBIR9F6CDQZZJ", "BGUS4HJRIOXPMGOHDAUO95B7DJXJRV3E", "https://api.coolsms.co.kr");
    private final GoogleService googleService; 

    @ApiOperation(value = "회원가입 id 중복체크")
    @GetMapping("/id-check/{userId}")
    public PrivateResponseBody idCheck(@PathVariable String userId) {
        return new PrivateResponseBody(userService.idCheck(userId));
    }

    @ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public PrivateResponseBody signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signUp(requestDto);
        return new PrivateResponseBody(UserStatusCode.USER_SIGNUP_SUCCESS);
    }
    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public PrivateResponseBody login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return new PrivateResponseBody(UserStatusCode.USER_LOGIN_SUCCESS,userService.login(loginRequestDto, response));
    }
    @ApiOperation(value = "카카오톡 로그인")
    @GetMapping("/kakao/callback")
    public PrivateResponseBody kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return new PrivateResponseBody(UserStatusCode.USER_LOGIN_SUCCESS,kakaoService.kakaoLogin(code, response));
    }

    @ApiOperation(value = "구글 로그인")
    @GetMapping("/google/callback")
    public PrivateResponseBody googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return new PrivateResponseBody(UserStatusCode.USER_LOGIN_SUCCESS, googleService.googleLogin(code, response));
    }

    @ApiOperation(value = "휴대폰 본인 확인")
    @PostMapping("/smsmessage")
    public PrivateResponseBody sendOne(@RequestParam @Size(min = 11, max = 11) String phoneNumber) {
        return userService.sendOne(phoneNumber);
    }

    @ApiOperation(value = "본인확인 시 보낼 인증번호")
    @PostMapping("/smsmessage/check")
    public PrivateResponseBody checkNum(@RequestBody ConfirmRequestDto confirmRequestDto){
        return userService.checkNum(confirmRequestDto);
    }


    @ApiOperation(value = "아이디 찾기")
    @PostMapping("/find-id")
    public PrivateResponseBody findUserNum(@RequestParam String phoneNumber){
        return userService.findUserNum(phoneNumber);
    }

    @ApiOperation(value = "아이디 찾기 인증번호 ")
    @PostMapping("/find-id/check")
    public PrivateResponseBody idCheckNum(@RequestBody ConfirmRequestDto confirmRequestDto ){
        return userService.idCheckNum(confirmRequestDto);
    }

    // 비밀번호 찾기
    @ApiOperation(value = "비밀번호 찾기")
    @PostMapping("/find-pw")
    public PrivateResponseBody findPassword(@RequestParam String phoneNumber, @RequestBody FindPasswordRequestDto findPasswordDto) {
        return userService.findPassword(phoneNumber, findPasswordDto.getUserId());
    }

    @ApiOperation(value ="비밀번호 찾기 인증번호" )
    @PostMapping("/find-pw/check")
    public PrivateResponseBody passwordCheckNum(@RequestBody ConfirmRequestDto confirmRequestDto){
        return userService.passwordCheckNum(confirmRequestDto);
    }
    // 비밀번호 재설정
    @ApiOperation(value = "비밀번호 재설정")
    @PutMapping("/reset-pw")
    public PrivateResponseBody resetPassword(@RequestBody @Valid ResetPasswordRequestDto resetPasswordRequestDto) {
        return userService.resetPassword(resetPasswordRequestDto.getUserId(), resetPasswordRequestDto.getPassword());
    }
}
