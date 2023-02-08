package com.sparta.be_finally.room.service;

import com.sparta.be_finally.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerTest {

    private final RoomService roomService;

    private RoomRepository roomRepository;



    @Scheduled(fixedDelay = 10000)
    public void runDelete() {
        roomService.roomdelete();
    }
}