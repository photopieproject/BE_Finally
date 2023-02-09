package com.sparta.be_finally.scheduler;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.collect.Lists;
import com.sparta.be_finally.common.dto.PrivateResponseBody;
import com.sparta.be_finally.common.errorcode.CommonStatusCode;
import com.sparta.be_finally.common.intercept.AwsS3Service;
import com.sparta.be_finally.common.validator.Validator;
import com.sparta.be_finally.photo.dto.FrameResponseDto;
import com.sparta.be_finally.photo.entity.Photo;
import com.sparta.be_finally.photo.repository.PhotoRepository;
import com.sparta.be_finally.room.entity.Room;

import com.sparta.be_finally.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final RoomRepository roomRepository;
    private final PhotoRepository photoRepository;
    private final AwsS3Service awsS3Service;
    private final Validator validator;

    private final AmazonS3Client amazonS3Client;

    //(fixedRate = 1800000)//
    //(cron = "0 0 0/1 * * *")// 1시간마다
    //(fixedRate = 30000) // 30 초

    @Scheduled(fixedDelay = 1800000)
    public void runAutomaticDeletion() {

        LocalDateTime time = LocalDateTime.now().withNano(0);
        List<Room> roomList = roomRepository.findRooms();

        for (Room room : roomList) {
            if (time.isAfter(room.getExpireDate())) {

                Photo photos = photoRepository.findByRoom(room);


                // S3 - 이미지 삭제 처리
                ObjectListing completePhotObjectListing = amazonS3Client.listObjects(bucket, "CompletePhoto/");
                ObjectListing photoObjectListing = amazonS3Client.listObjects(bucket, "photo/");


                List<S3ObjectSummary> completePhotoObjectListingObjectSummaries = completePhotObjectListing.getObjectSummaries();
                List<S3ObjectSummary> photoObjectListingObjectSummaries = photoObjectListing.getObjectSummaries();


                List<String> photoImgUrlList = Lists.newArrayList();
                List<String> completePhotoImgUrlList = Lists.newArrayList();

                for (S3ObjectSummary photoS3Object : photoObjectListingObjectSummaries) {
                    String photoImgUrl = amazonS3Client.getResourceUrl(bucket, photoS3Object.getKey());
                    photoImgUrlList.add(photoImgUrl);
                }

                awsS3Service.deletePhotos(photoImgUrlList, "photo/" + photos.getRoom().getId());

                for (S3ObjectSummary completePhotoS3Object : completePhotoObjectListingObjectSummaries) {
                    String completePhotoImgUrl = amazonS3Client.getResourceUrl(bucket, completePhotoS3Object.getKey());
                    completePhotoImgUrlList.add(completePhotoImgUrl);


                    awsS3Service.deletePhotos(completePhotoImgUrlList, "CompletePhoto/" + photos.getRoom().getId());

                }
                roomRepository.delete(room);
            }
        }
    }
}


