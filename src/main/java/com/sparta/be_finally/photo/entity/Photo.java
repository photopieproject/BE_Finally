package com.sparta.be_finally.photo.entity;

import com.sparta.be_finally.room.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String photo_one;
    private String photo_two;
    private String photo_three;
    private String photo_four;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
}
