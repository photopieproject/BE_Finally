package com.sparta.be_finally.room.service;

import com.sparta.be_finally.room.entity.Room;

import com.sparta.be_finally.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {


    private final RoomRepository roomRepository;

    @Scheduled(fixedRate = 3600000)
            //(cron = "0 0 0/1 * * *")// 1시간마다
            //(fixedRate = 10000) // 10 초
    public void runAfterTenSecondsRepeatTenSeconds() {
        //log.info("10초후 실행 -> time:" + LocalDateTime.now());


        List<Room> roomList = roomRepository.findAll();
        LocalDateTime time = LocalDateTime.now().withNano(0);


        for (Room room : roomList) {
            if (time.isAfter(room.getExpireDate())) {
                //room.setDeleted(true);
                roomRepository.deleteById(room.getId());
            }
        }


    }
}


