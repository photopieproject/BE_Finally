package com.sparta.be_finally.photo.entity;

import com.amazonaws.services.s3.model.PutObjectResult;
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


    public Photo(Room room, String photo_one_imgUrl) {
        this.room = room;
        this.photo_one = photo_one_imgUrl;
    }

    public Photo(Room room, PutObjectResult photo_one_imgUrl) {
        this.room = room;
        this.photo_one = String.valueOf(photo_one_imgUrl);
    }


    public Photo(Room room) {
        this.room = room;
    }

    public Photo(String photoOneImgUrl) {
        this.photo_one = photoOneImgUrl;
    }

    public void photo_one_update(String photo_one_imgUrl){
        this.photo_one = photo_one_imgUrl;
    }


    public void photo_two_update(String photo_two_imgUrl) {
        this.photo_two = photo_two_imgUrl;
    }

    public void photo_three_update(String photo_three_imgUrl) {
        this.photo_three = photo_three_imgUrl;
    }

    public void photo_four_update(String photo_four_imgUrl) {
        this.photo_four = photo_four_imgUrl;
    }

}
