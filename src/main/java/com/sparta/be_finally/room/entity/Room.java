package com.sparta.be_finally.room.entity;

import com.sparta.be_finally.room.dto.RoomRequestDto;
import com.sparta.be_finally.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String roomName;
    private int roomCode;
    private int frame;

<<<<<<< HEAD
    private int userCount;

=======
    private int userCount = 4;
>>>>>>> 0854bebf7f625b623a685db6d0cfc18c122df290

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;



    public Room(RoomRequestDto roomRequestDto, User user) {
        this.roomName = roomRequestDto.getRoomName();
        this.roomCode = (int)(Math.random()*100000);
        this.user = user;
        this.userCount--;
    }

}
