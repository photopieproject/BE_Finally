package com.sparta.be_finally.room.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.sparta.be_finally.common.intercept.AwsS3Service;
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

import java.time.LocalDateTime;

import java.util.ArrayList;
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

    private final AmazonS3Client amazonS3Client;

    //(fixedRate = 1800000)//
    //(cron = "0 0 0/1 * * *")// 1시간마다
    //(fixedRate = 30000) // 30 초

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void runAfterTenSecondsRepeatTenSeconds() {
        // List<Room> roomList = roomRepository.findAll();
//        LocalDateTime time = LocalDateTime.now().withNano(0);
//        List<Room> roomList = roomRepository.findRooms();

//        for (Room room : roomList) {
//            if (time.isAfter(room.getExpireDate())) {
//
//                Photo photos = photoRepository.findByRoom(room);
//
//                if (photos != null) {








        // S3 - 이미지 삭제 처리
//                    awsS3Service.deleteFolder("/CompletePhoto/" + photos.getRoom().getId() + "/");
//                    awsS3Service.deleteFolder("/photo/" + photos.getRoom().getId() + "/");
//        awsS3Service.deleteFolder(bucket,"CompletePhoto/"+phots.getRoom().getId()+"/");
//        awsS3Service.deleteFolder(bucket,"photo/"+phots.getRoom().getId()+"/");

//        awsS3Service.deleteDirName("photo/"+1000+"/");
//        amazonS3Client.deleteObject(bucket,"photo/"+1000+ "/");
//        amazonS3Client.deleteObject(bucket,"photo/"+"1000/");
//        amazonS3Client.deleteObjects("photo/"+"1000/");
        awsS3Service.deleteDirFolder("suye/"+ "suye/");
//        s3.deleteObject(AWS_BUCKETNAME, "폴더/폴더/폴더/파일명");
//
//        ex) bucktname = "test" ,  s3 폴더 구조 : test/d1/d2/test.png
//
// s3.deleteObject("test", "/d1/d2/test.png");





        // db - photo 삭제 처리
//                    photoRepository.delete(photos);
//                }
//
//                // db - room 삭제 처리
//                roomRepository.delete(room);
//            }
//        }
//    }
    }
}
