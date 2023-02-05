package com.sparta.be_finally.room.service;

import com.sparta.be_finally.common.intercept.AwsS3Service;
import com.sparta.be_finally.photo.entity.Photo;
import com.sparta.be_finally.photo.repository.PhotoRepository;
import com.sparta.be_finally.room.entity.Room;

import com.sparta.be_finally.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final RoomRepository roomRepository;
    private final PhotoRepository photoRepository;
    private final AwsS3Service awsS3Service;

    //(fixedRate = 1800000)//
    //(cron = "0 0 0/1 * * *")// 1시간마다
    //(fixedRate = 30000) // 30 초

    @Scheduled(fixedRate = 1800000)
    @Transactional
    public void runAfterTenSecondsRepeatTenSeconds() {

        // List<Room> roomList = roomRepository.findAll();
        LocalDateTime time = LocalDateTime.now().withNano(0);
        List <Room> roomList = roomRepository.findRooms();

        for (Room room : roomList) {
            if (time.isAfter(room.getExpireDate())) {

                Photo photos = photoRepository.findByRoomId(room.getId()).orElse(null);

                if (photos != null) {
                    List<String> photo_url = new ArrayList<>();
                    photo_url.add(photos.getPhotoOne().split(".com/")[1]);
                    photo_url.add(photos.getPhotoTwo().split(".com/")[1]);
                    photo_url.add(photos.getPhotoThree().split(".com/")[1]);
                    photo_url.add(photos.getPhotoFour().split(".com/")[1]);

                    //S3 - 이미지 삭제 처리
                    for (String photo : photo_url) {
                        awsS3Service.deleteFile(photo);
                    }
                    photoRepository.delete(photos);
                }

                roomRepository.delete(room);
            }
        }
    }
}


