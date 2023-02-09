//package com.sparta.be_finally.room.service;
//
//import com.sparta.be_finally.common.intercept.AwsS3Service;
//import com.sparta.be_finally.photo.entity.Photo;
//import com.sparta.be_finally.photo.repository.PhotoRepository;
//import com.sparta.be_finally.room.entity.Room;
//import com.sparta.be_finally.room.repository.RoomRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalTime;
//import java.util.List;
//
//@Component
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class SchedulerTest {
//
//
//    private final RoomRepository roomRepository;
//    private final PhotoRepository photoRepository;
//    private final AwsS3Service awsS3Service;
//
//
//    @Scheduled(fixedRate = 1000)
//    public void runEveryTenSecondsOne() {
//        log.info("runEveryTenSecondsOne time : " + LocalTime.now());
//        log.info("runEveryTenSecondsOne thread: " + Thread.currentThread().getName());
//    }
//
//    @Scheduled(fixedRate = 1000)
//    public void runEveryTenSecondsTwo() {
//        String roomName = "";
//        List<Room> roomList = roomRepository.findAll();
//        for (Room r : roomList) {
//            r.getRoomName();
//
//            Photo photos = photoRepository.findByRoom(r);
//
//            // S3 - 이미지 삭제 처리
//            awsS3Service.deleteFolder("/CompletePhoto/" + photos.getRoom().getId() + "/");
//            awsS3Service.deleteFolder("/photo/" + photos.getRoom().getId() + "/");
//
//        }
//    }
//}
//
