package com.sparta.be_finally.mypage.controller;

import com.sparta.be_finally.common.dto.PrivateResponseBody;
import com.sparta.be_finally.mypage.service.MyPageService;
import com.sparta.be_finally.user.dto.ResetPasswordRequestDto;
import com.sparta.be_finally.mypage.dto.NickNameChangeRequestDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "닉네임 수정 ")
    @PutMapping("/nickname/change")
    public PrivateResponseBody changeNickName(@RequestBody @Valid NickNameChangeRequestDto nickNameChangeRequestDto){
        return myPageService.changeNickName(nickNameChangeRequestDto);
    }
    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping("/deleteUser")
    public PrivateResponseBody deleteUser() {
        return myPageService.deleteUser();
    }

}
