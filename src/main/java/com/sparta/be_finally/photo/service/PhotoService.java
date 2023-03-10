package com.sparta.be_finally.photo.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.google.common.collect.Lists;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sparta.be_finally.common.intercept.AwsS3Service;
import com.sparta.be_finally.common.dto.PrivateResponseBody;
import com.sparta.be_finally.common.errorcode.CommonStatusCode;
import com.sparta.be_finally.config.AES256;
import com.sparta.be_finally.common.validator.Validator;
import com.sparta.be_finally.photo.dto.*;
import com.sparta.be_finally.photo.entity.Photo;
import com.sparta.be_finally.photo.repository.PhotoRepository;
import com.sparta.be_finally.room.entity.Room;
import io.openvidu.java.client.OpenVidu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;

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
        // 1. roomId 존재 여부 확인
        Room room = validator.existsRoom(roomId);

        // Log: 프레임 번호 확인
        log.info("fameNum = " + room.getFrameUrl());

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

                // Log: photoOne의 이미지url이 잘 들어갔나 확인
                log.info("photoOneImgUrl = " + photo_one_imgUrl);

                return new PrivateResponseBody(CommonStatusCode.SHOOT_PHOTO_SUCCESS);

            } else if (photo.getPhotoTwo() == null && photoRequestDto.getPhoto_2() != null && !photoRequestDto.getPhoto_2().getContentType().isEmpty()) {
                String photo_two_imgUrl = awsS3Service.uploadFile(photoRequestDto.getPhoto_2(), room.getId());
                photo.photo_two_update(photo_two_imgUrl);

                // Log: photoTwo의 이미지url이 잘 들어갔나 확인
                log.info("photoTwoImgUrl = " + photo_two_imgUrl);

                return new PrivateResponseBody(CommonStatusCode.SHOOT_PHOTO_SUCCESS);

            } else if (photo.getPhotoThree() == null && photoRequestDto.getPhoto_3() != null && !photoRequestDto.getPhoto_3().getContentType().isEmpty()) {
                String photo_three_imgUrl = awsS3Service.uploadFile(photoRequestDto.getPhoto_3(), room.getId());
                photo.photo_three_update(photo_three_imgUrl);

                // Log: photoThree의 이미지url이 잘 들어갔나 확인
                log.info("photoThreeImgUrl = " + photo_three_imgUrl);

                return new PrivateResponseBody(CommonStatusCode.SHOOT_PHOTO_SUCCESS);

            } else if (photo.getPhotoFour() == null && photoRequestDto.getPhoto_4() != null && !photoRequestDto.getPhoto_4().getContentType().isEmpty()) {
                String photo_four_imgUrl = awsS3Service.uploadFile(photoRequestDto.getPhoto_4(), room.getId());
                photo.photo_four_update(photo_four_imgUrl);

                // Log: photoFour의 이미지url이 잘 들어갔나 확인
                log.info("photoFourImgUrl = " + photo_four_imgUrl);

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

        HashMap<Date, String> imgListOrderBy = new HashMap<>();

        for (S3ObjectSummary s3Object : s3ObjectSummaries) {
            String imgUrl = amazonS3Client.getResourceUrl(bucket, s3Object.getKey());
            Date lastModified = s3Object.getLastModified();

            // imgUrl > base64 로 인코딩
            String imgUrl_base64 = getBase64(imgUrl);

            imgUrlList.add(imgUrl);

            imgListOrderBy.put(lastModified, imgUrl_base64);
        }

        List<Date> keyList = new ArrayList<>(imgListOrderBy.keySet());
        keyList.sort(Date::compareTo);
        for (Date key : keyList) {
            log.info("key : {}, value : {}", key, imgListOrderBy.get(key));
            imgTransList.add(imgListOrderBy.get(key));
        }

        String url = room.getFrameUrl();
        String urlConversion= getBase64(url);

        // Log: 4장 이미지url이 잘 들어갔나 확인
        log.info("imgUrlList = " + imgUrlList);

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

            // Log: 완성이미지 저장 확인
            log.info("completePhoto = " + completePhoto);

            // 3. QR코드 생성
            String qrCode = createQr(roomId); // base64 인코딩
            System.out.println("qrCode : " + qrCode);
            photoRepository.saveQrCode(qrCode, roomId);

        } else {
            return new PrivateResponseBody(CommonStatusCode.EXISTS_COMPLETE_PHOTO, photoRepository.createQrPhotoUrl(roomId));
        }

        if(photoRepository.findByRoomIdAndCompletePhotoNull(roomId) != null) {
            return new PrivateResponseBody(CommonStatusCode.COMPLETE_PHOTO_SUCCESS, photoRepository.createQrPhotoUrl(roomId));
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

    @Transactional
    public PrivateResponseBody kakaoUrl(Long roomId) {
        String url = photoRepository.findByRoomIdAndCompletePhoto(roomId);
        if (url != null) {
            return new PrivateResponseBody(CommonStatusCode.COMPLETE_PHOTO, url);
        } else {
            return new PrivateResponseBody(CommonStatusCode.COMPLETE_PHOTO_NULL);

        }

    }

    // QRCode 생성
    private String createQr(Long roomId) {
        byte[] image = new byte[0];

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

        return encodeString;
    }

    // base64 변환
    public String getBase64(String imageURL) throws IOException {
        URL url = new URL(imageURL);
        InputStream is = url.openStream();
        byte[] bytes = IOUtils.toByteArray(is);
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }

    // 프레임 인기 Top5
    public List<FrameResponseDto.getTop5> getTop5() throws IOException {
        List<FrameTop5Interface> getTop5s = photoRepository.getTop5();

        List<FrameResponseDto.getTop5> top5s = new ArrayList<>();

        for (FrameTop5Interface top5ResponseDto : getTop5s) {
            FrameResponseDto.getTop5 top5 = new FrameResponseDto.getTop5();

            top5.setRanking(top5ResponseDto.getRanking());
            top5.setFrameNum(top5ResponseDto.getFrameNum());
            top5.setFrameName(top5ResponseDto.getFrameUrl().split("/")[4].split("[.]")[0]);
            top5.setFrameUrl(getBase64(top5ResponseDto.getFrameUrl()));

            top5s.add(top5);
        }

        return top5s;
    }
}

