package com.sparta.be_finally.photo.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.collect.Lists;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sparta.be_finally.config.S3.AwsS3Service;
import com.sparta.be_finally.common.dto.PrivateResponseBody;
import com.sparta.be_finally.common.errorcode.CommonStatusCode;
import com.sparta.be_finally.config.AES256;
import com.sparta.be_finally.common.util.SecurityUtil;
import com.sparta.be_finally.common.validator.Validator;
import com.sparta.be_finally.photo.dto.CompletePhotoRequestDto;
import com.sparta.be_finally.photo.dto.FrameResponseDto;
import com.sparta.be_finally.photo.dto.PhotoRequestDto;
import com.sparta.be_finally.photo.entity.Photo;
import com.sparta.be_finally.photo.repository.PhotoRepository;
import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.user.entity.User;
import io.openvidu.java.client.OpenVidu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoService {
    @Value("${cloud.aws.s3.bucket}" )
    private String bucket;
    private final AwsS3Service awsS3Service;
    private final Validator validator;
    private final PhotoRepository photoRepository;
    private final AmazonS3Client amazonS3Client;
    private OpenVidu openVidu;

    private final AES256 aes256;

    // QR 색상
    private static int backgroundColor = 0xFF000002;
    private static int paintColor = 0xFFF8F9FA;

    // OpenVidu 서버가 수신하는 URL
    @Value("${openvidu.url}" )
    private String OPENVIDU_URL;

    // OpenVidu 서버와 공유되는 비밀
    @Value("${openvidu.secret}" )
    private String OPENVIDU_SECRET;

    @PostConstruct
    public OpenVidu openVidu() {
        return openVidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    // 사진 촬영 준비
    @Transactional
    public FrameResponseDto photoShoot(Long roomId) {
        User user = SecurityUtil.getCurrentUser();
        // 1. roomId 존재 여부 확인
        Room room = validator.existsRoom(roomId);

        // 2. 입장한 방 - 선택한 프레임 번호
        //int frameNum = room.getFrame();

        return new FrameResponseDto(room.getFrame(), room.getFrameUrl());
    }

    // 찍은 사진 S3 저장
    @Transactional
    public PrivateResponseBody photoShootSave(Long roomId, PhotoRequestDto photoRequestDto) {
        // 1. roomId 존재 여부 확인
        Room room = validator.existsRoom(roomId);

        // 2. Photo 테이블 - room_id 에서 촬영한 사진 조회
        Photo photo = photoRepository.findByRoom(room);

        // 3. photoRequestDto 에 있는 파일 S3에 업로드
        if (photoRequestDto.getPhoto_1()!=null || photoRequestDto.getPhoto_2() !=null|| photoRequestDto.getPhoto_3() !=null|| photoRequestDto.getPhoto_4()!=null) {
            if (photo.getPhotoOne() == null && photoRequestDto.getPhoto_1() != null && !photoRequestDto.getPhoto_1().getContentType().isEmpty()) {
                String photo_one_imgUrl = awsS3Service.uploadFile(photoRequestDto.getPhoto_1(), room.getId());
                photo.photo_one_update(photo_one_imgUrl);
                return new PrivateResponseBody(CommonStatusCode.SHOOT_PHOTO_SUCCESS);

            } else if (photo.getPhotoTwo() == null && photoRequestDto.getPhoto_2() != null && !photoRequestDto.getPhoto_2().getContentType().isEmpty()) {
                String photo_two_imgUrl = awsS3Service.uploadFile(photoRequestDto.getPhoto_2(), room.getId());
                photo.photo_two_update(photo_two_imgUrl);
                return new PrivateResponseBody(CommonStatusCode.SHOOT_PHOTO_SUCCESS);

            } else if (photo.getPhotoThree() == null && photoRequestDto.getPhoto_3() != null && !photoRequestDto.getPhoto_3().getContentType().isEmpty()) {
                String photo_three_imgUrl = awsS3Service.uploadFile(photoRequestDto.getPhoto_3(), room.getId());
                photo.photo_three_update(photo_three_imgUrl);
                return new PrivateResponseBody(CommonStatusCode.SHOOT_PHOTO_SUCCESS);

            } else if (photo.getPhotoFour() == null && photoRequestDto.getPhoto_4() != null && !photoRequestDto.getPhoto_4().getContentType().isEmpty()) {
                String photo_four_imgUrl = awsS3Service.uploadFile(photoRequestDto.getPhoto_4(), room.getId());
                photo.photo_four_update(photo_four_imgUrl);
                return new PrivateResponseBody(CommonStatusCode.SHOOT_PHOTO_SUCCESS);

            } else {
                return new PrivateResponseBody(CommonStatusCode.FAIL_SAVE_PHOTO);
            }
        }
        return new PrivateResponseBody(CommonStatusCode.SHOOT_PHOTO_FAIL);
    }

    // 사진 4장 & 프레임 뿌려주기
    @Transactional(readOnly = true)
    public PrivateResponseBody photoGet(Long roomId) throws IOException {

        Room room = validator.existsRoom(roomId);

        ObjectListing objectListing = amazonS3Client.listObjects(bucket, "photo/"+ room.getId() + "/");
        List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

        List<String> imgUrlList = Lists.newArrayList();
        List<String> imgTransList = Lists.newArrayList();

        for (S3ObjectSummary s3Object : s3ObjectSummaries) {
            String imgKey = s3Object.getKey();
            String imgUrl = amazonS3Client.getResourceUrl(bucket, imgKey);

            String baseList= aes256.getBase64(imgUrl);

            imgUrlList.add(imgUrl);
            imgTransList.add(baseList);
        }

        String url = room.getFrameUrl();
        String urlConversion= aes256.getBase64(url);

        // return imgUrlList;
        return new PrivateResponseBody(CommonStatusCode.SHOOT_PHOTO_GET, imgTransList, new FrameResponseDto(room.getFrame(),urlConversion), room.getId());
    }

    // 완성 사진 저장
    @Transactional
    public PrivateResponseBody completePhotoSave(Long roomId, CompletePhotoRequestDto completePhotoRequestDto) {
        // 1. roomId 존재 여부 확인
        Room room = validator.existsRoom(roomId);

        // 2. Room 테이블 - 완성 이미지 저장
        if(photoRepository.existsByRoomIdAndCompletePhotoIsNull(roomId)) {
            String completePhoto = awsS3Service.uploadCompleteFile(completePhotoRequestDto.getCompletePhoto(), room.getId());
            photoRepository.updateCompletePhoto(completePhoto, roomId);

            // 3. QR코드 생성
            String qrCode = createQr(roomId); // base64 인코딩
            System.out.println("qrCode : " + qrCode);
            photoRepository.saveQrCode(qrCode, roomId);

        } else {
            return new PrivateResponseBody(CommonStatusCode.EXISTS_COMPLETE_PHOTO);
        }

        if(photoRepository.findByRoomIdAndCompletePhotoNull(roomId) != null) {
            return new PrivateResponseBody(CommonStatusCode.COMPLETE_PHOTO_SUCCESS);
        } else {
            return new PrivateResponseBody(CommonStatusCode.COMPLETE_PHOTO_FAIL);
        }
    }

    // QRCode 반환
    @Transactional
    public PrivateResponseBody returnQr(Long roomId) {
        String qrcode = photoRepository.findByRoomIdAndQrCode(roomId);

        if (qrcode != null) {
            return new PrivateResponseBody(CommonStatusCode.CREATE_QRCODE, qrcode);
        } else {
            return new PrivateResponseBody(CommonStatusCode.FAIL_QRCODE);
        }
    }

    // QR코드 생성
    private String createQr(Long roomId) {
        byte[] image = new byte[0];

        // url = complete_photo Url
        String url = photoRepository.createQrPhotoUrl(roomId);

        try {
            // base 64 로 저장
            image = PhotoService.getQRCodeImage(url, 250, 250);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        System.out.println("url:" + url);
        System.out.println("image:" + image);

        String qrcode = Base64.getEncoder().encodeToString(image);

        return qrcode;
    }

    // QR이미지 생성
    private static byte[] getQRCodeImage(String url, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(backgroundColor, paintColor);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, matrixToImageConfig);
        byte[] pngData = pngOutputStream.toByteArray();

        return pngData;
    }

    // base64
    private String createBase64(String imgUrl) throws IOException {
        // base64
        URL urlInput = new URL(imgUrl);
        BufferedImage urlImage = ImageIO.read(urlInput);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(urlImage, "jpg", bos);
        Base64.Encoder encoder = Base64.getEncoder();
        String encodeString = encoder.encodeToString(bos.toByteArray());

//        String qrcode = Base64.getEncoder().encodeToString(imgUrl);

        return encodeString;
    }
}

