package com.sparta.be_finally.room.service;


import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.room.repository.RoomRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component // 추가
@EnableAsync // 추가
public class SchedulerTest {
    private final RoomRepository roomRepository;


    @Scheduled(fixedDelay = 180000)
    @Transactional
    public void scheduleFixedDelayTask() {
        log.info("Fixed delay task - {}", System.currentTimeMillis() / 1000);
        roomesc();

    }


    public void roomesc() {
        List<Room>roomList = roomRepository.findRooms();
        LocalDateTime time = LocalDateTime.now().withNano(0);
        for (Room room: roomList) {

            if (time.isAfter(room.getExpireDate()));
            roomRepository.delete(room);
        }
    }
}


