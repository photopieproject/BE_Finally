package com.sparta.be_finally.room.service;

import com.sparta.be_finally.config.S3.AwsS3Configuration;
import com.sparta.be_finally.config.S3.AwsS3Service;
import com.sparta.be_finally.photo.entity.Photo;
import com.sparta.be_finally.photo.repository.PhotoRepository;
import com.sparta.be_finally.room.entity.Room;

import com.sparta.be_finally.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final RoomRepository roomRepository;
    private final PhotoRepository photoRepository;
    private final AwsS3Service awsS3Service;

    @Scheduled(fixedRate = 1800000)
            //(fixedRate = 1800000)//
            //(cron = "0 0 0/1 * * *")// 1시간마다
            //(fixedRate = 30000) // 10 초
    public void runAfterTenSecondsRepeatTenSeconds() {
        //log.info("10초후 실행 -> time:" + LocalDateTime.now());

        List<Room> roomList = roomRepository.findAll();
        LocalDateTime time = LocalDateTime.now().withNano(0);

        for (Room room : roomList) {
            if (time.isAfter(room.getExpireDate())) {
                //room.setDeleted(true);

                Photo photos = photoRepository.findByRoomId(room.getId()).orElse(null);

                if (photos != null) {
                    List<String> photo_url = new ArrayList<>();
                    photo_url.add(photos.getPhoto_one().split(".com/")[1]);
                    photo_url.add(photos.getPhoto_two().split(".com/")[1]);
                    photo_url.add(photos.getPhoto_three().split(".com/")[1]);
                    photo_url.add(photos.getPhoto_four().split(".com/")[1]);

                    //S3 - 이미지 삭제 처리
                    for (String photo : photo_url) {
                        awsS3Service.deleteFile(photo);
                    }
                }

                roomRepository.deleteById(room.getId());
            }
        }


    }
}


