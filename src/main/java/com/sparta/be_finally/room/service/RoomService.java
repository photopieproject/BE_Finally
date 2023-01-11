package com.sparta.be_finally.room.service;

import com.sparta.be_finally.config.util.SecurityUtil;
import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.room.dto.RoomResponseDto;
import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.room.repository.RoomRepository;
import com.sparta.be_finally.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RoomService {
    public final RoomRepository roomRepository;

    @Transactional
    public RoomResponseDto createRoom(RoomRequestDto roomRequestDto) {
        User user = SecurityUtil.getCurrentUser();
        Room room = roomRepository.save(new Room(roomRequestDto, user));

        return new RoomResponseDto(room);
    }
}
