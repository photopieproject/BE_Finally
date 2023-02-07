package com.sparta.be_finally.mypage.controller;

import com.sparta.be_finally.common.dto.PrivateResponseBody;
import com.sparta.be_finally.mypage.service.MyPageService;
import com.sparta.be_finally.user.dto.ResetPasswordRequestDto;
import com.sparta.be_finally.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Api(tags = {"MyPage API"})
@Slf4j
@RestController
@RequestMapping("/api/user/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;


    // 비밀번호 재설정
    @ApiOperation(value = "비밀번호 재설정")
    @PutMapping("/reset-pw")
    public PrivateResponseBody resetPassword(@RequestBody @Valid ResetPasswordRequestDto resetPasswordRequestDto) {
        return myPageService.resetPassword(resetPasswordRequestDto.getUserId(), resetPasswordRequestDto.getPassword());
    }
}
