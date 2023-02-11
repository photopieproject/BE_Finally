package com.sparta.be_finally.room.entity;

import com.amazonaws.services.s3.AmazonS3Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.be_finally.photo.dto.FrameResponseDto;
import com.sparta.be_finally.photo.entity.Photo;
import com.sparta.be_finally.room.dto.FrameRequestDto;
import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.utility.nullability.MaybeNull;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class Room {
    private static final int VALID_HOUR = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomName;

    @Column(nullable = false)
    private String roomCode;

    private int userCount = 0;

    private int frame;

    private String frameUrl;


    // Openvidu 의 roomId 이라고 생각하면 됨
    private String sessionId;

    @NotNull
    private LocalDateTime expireDate;

    private int maxPeople;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "photo_id", unique = true)
//    private Photo photo;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomParticipant> roomParticipants = new ArrayList<>();

    public Room(Long id) {
        this.id = id;
    }

    public Room(int frameNum, String frameUrl) {
        this.frame = frameNum;
        this.frameUrl = String.valueOf(frameUrl);
    }

    public Room(RoomRequestDto.RoomCodeRequestDto roomCodeRequestDto, User user) {
        this.roomCode = roomCodeRequestDto.getRoomCode();
        this.user = user;
    }

    public Room(RoomRequestDto roomRequestDto, User user, String sessionId) {
        this.roomName = roomRequestDto.getRoomName();
//        this.maxPeople = roomRequestDto.getMaxPeople();
        this.roomCode = UUID.randomUUID().toString().substring(0, 5);
        this.user = user;
        this.userCount++;
        this.sessionId = sessionId;
        this.expireDate = LocalDateTime.now().withNano(0).plusHours(VALID_HOUR);
    }

    public void updateFrameAndMaxPeople(FrameRequestDto frameRequestDto, String frameUrl, int maxPeople) {
        this.frame = frameRequestDto.getFrameNum();
        this.frameUrl = frameUrl;
        this.maxPeople = maxPeople;
    }

    // 방 입장
    public void enter() {
        this.userCount++;
    }

    // 방 나가기
    public void exit() {
        this.userCount--;
    }
}



