package com.sparta.be_finally.photo.controller;

import com.sparta.be_finally.config.dto.PrivateResponseBody;
import com.sparta.be_finally.config.errorcode.CommonStatusCode;
import com.sparta.be_finally.photo.dto.FrameResponseDto;
import com.sparta.be_finally.photo.dto.PhotoRequestDto;
import com.sparta.be_finally.photo.service.PhotoService;
import com.sparta.be_finally.user.dto.KakaoFriendListResponseDto;
import com.sparta.be_finally.user.service.KakaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(tags = {"Photo API"})
@Slf4j
@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class PhotoController {
    private final KakaoService kakaoService;
    private final PhotoService photoService;

    @ApiOperation(value = "카카오톡 메시지 보내기")
    @GetMapping("/kakaoMessage")
    public void kakaoMessage(@RequestParam String code, HttpServletResponse response) throws IOException {

        // 로그인 한 사용자의 카톡 로그인 유무 확인

        // code: 카카오 서버로부터 받은 인가 코드
        String accessToken = kakaoService.kakaoLoginCheck(code, response);

        // 친구 목록 조회
        KakaoFriendListResponseDto getFriend = kakaoService.requestFriendList(accessToken);



    }

    @ApiOperation(value = "선택한 프레임 조회")
    @GetMapping("/room/{roomId}")
    public PrivateResponseBody photoShoot(@PathVariable Long roomId) {
        return new PrivateResponseBody<>(CommonStatusCode.CHOICE_FRAME2,photoService.photoShoot(roomId));
    }

    @ApiOperation(value = "사진 촬영 후 이미지 저장")
    @PostMapping("/room/{roomId}/shoot")
    public PrivateResponseBody photoShootSave(@PathVariable Long roomId, @ModelAttribute PhotoRequestDto photoRequestDto) {
        return photoService.photoShootSave(roomId, photoRequestDto);
    }

    @ApiOperation(value = "사진 전송(조회)" )
    @GetMapping("/room/{roomId}/shoot")
    public PrivateResponseBody photoGet(@PathVariable Long roomId){
        return photoService.photoGet(roomId);
    }
}



