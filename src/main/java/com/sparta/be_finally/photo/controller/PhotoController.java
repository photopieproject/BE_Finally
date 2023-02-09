package com.sparta.be_finally.photo.controller;

import com.sparta.be_finally.common.dto.PrivateResponseBody;
import com.sparta.be_finally.common.errorcode.CommonStatusCode;
import com.sparta.be_finally.photo.dto.CompletePhotoRequestDto;
import com.sparta.be_finally.photo.dto.PhotoRequestDto;
import com.sparta.be_finally.photo.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(tags = {"Photo API"})
@Slf4j
@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

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
    public PrivateResponseBody photoGet(@PathVariable Long roomId) throws IOException {
        return photoService.photoGet(roomId);
    }

    @ApiOperation(value = "완성 이미지 저장")
    @PostMapping("/room/{roomId}/completePhoto")
    public PrivateResponseBody completePhotoSave(@PathVariable Long roomId, @ModelAttribute CompletePhotoRequestDto completePhotoRequestDto) {
        return photoService.completePhotoSave(roomId, completePhotoRequestDto);
    }

    @ApiOperation(value = "QR코드 발급")
    @GetMapping("/room/{roomId}/qrCode")
    public PrivateResponseBody returnQr(@PathVariable Long roomId) {
        return photoService.returnQr(roomId);
    }

    @ApiOperation(value = "kakao 전송하기 url")
    @GetMapping("/room/{roomId}/kakaoUrl")
    public PrivateResponseBody kakaoUrl(@PathVariable Long roomId) {return photoService.kakaoUrl(roomId);}

    @ApiOperation(value = "프레임 인기 순위")
    @GetMapping("/frameRanking")
    public PrivateResponseBody getTop5() throws IOException {
        return new PrivateResponseBody(CommonStatusCode.TOP5_FRAME_RANKING, photoService.getTop5());
    }


}



