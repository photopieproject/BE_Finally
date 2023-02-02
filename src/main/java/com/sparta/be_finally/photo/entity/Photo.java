package com.sparta.be_finally.photo.entity;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.sparta.be_finally.room.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String photoOne;
    private String photoTwo;
    private String photoThree;
    private String photoFour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;


    public Photo(Room room, String photo_one_imgUrl) {
        this.room = room;
        this.photoOne = photo_one_imgUrl;
    }

    public Photo(Room room, PutObjectResult photo_one_imgUrl) {
        this.room = room;
        this.photoOne = String.valueOf(photo_one_imgUrl);
    }


    public Photo(Room room) {
        this.room = room;
    }

    public Photo(String photoOneImgUrl) {
        this.photoOne = photoOneImgUrl;
    }

    public void photo_one_update(String photo_one_imgUrl){
        this.photoOne = photo_one_imgUrl;
    }

    public void photo_two_update(String photo_two_imgUrl) {
        this.photoTwo = photo_two_imgUrl;
    }

    public void photo_three_update(String photo_three_imgUrl) {
        this.photoThree = photo_three_imgUrl;
    }

    public void photo_four_update(String photo_four_imgUrl) {
        this.photoFour = photo_four_imgUrl;
    }

}
