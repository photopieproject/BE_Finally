package com.sparta.be_finally.photo.service;

import com.sparta.be_finally.config.S3.AwsS3Service;
import com.sparta.be_finally.config.errorcode.CommonStatusCode;
import com.sparta.be_finally.config.errorcode.StatusCode;
import com.sparta.be_finally.config.exception.RestApiException;
import com.sparta.be_finally.config.util.SecurityUtil;
import com.sparta.be_finally.photo.dto.PhotoRequestDto;
import com.sparta.be_finally.photo.entity.Photo;
import com.sparta.be_finally.photo.repository.PhotoRepository;
import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.room.repository.RoomRepository;
import com.sparta.be_finally.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoService {
    private final AwsS3Service awsS3Service;
    private final PhotoRepository photoRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public StatusCode photoShoot(Long roomId, PhotoRequestDto photoRequestDto) {
        User user = SecurityUtil.getCurrentUser();

        // 1. roomId 존재 여부 확인
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new RestApiException(CommonStatusCode.FAIL_ENTER2)
        );

        // 2. Photo 테이블 - room_id 에서 촬영한 사진 조회
        //    사진을 한 컷 이상 찍은 상태 : isExist 에 정보 저장 됨
        //    photo_one 촬영 한 상태 : isExist = null
        Photo photo = photoRepository.findByRoomId(roomId).orElse(null);

        // 3. photoRequestDto 에 있는 파일 S3에 업로드
        if (!photoRequestDto.getPhoto_one().isEmpty() && !photoRequestDto.getPhoto_one().getContentType().isEmpty()) {
            String photo_one_imgUrl = awsS3Service.uploadFile(photoRequestDto.getPhoto_one());
            photoRepository.saveAndFlush(new Photo(room, photo_one_imgUrl));

        } else if (!photoRequestDto.getPhoto_two().isEmpty() && !photoRequestDto.getPhoto_two().getContentType().isEmpty()) {
            String photo_two_imgUrl = awsS3Service.uploadFile(photoRequestDto.getPhoto_two());
            photo.photo_two_update(photo_two_imgUrl);

        } else if (!photoRequestDto.getPhoto_three().isEmpty() && !photoRequestDto.getPhoto_three().getContentType().isEmpty()) {
            String photo_three_imgUrl = awsS3Service.uploadFile(photoRequestDto.getPhoto_three());
            photo.photo_three_update(photo_three_imgUrl);

        } else if (!photoRequestDto.getPhoto_four().isEmpty() && !photoRequestDto.getPhoto_four().getContentType().isEmpty()) {
            String photo_four_imgUrl = awsS3Service.uploadFile(photoRequestDto.getPhoto_four());
            photo.photo_four_update(photo_four_imgUrl);

        }

        return CommonStatusCode.SHOOT_PHOTO_SUCCESS;
    }
}
