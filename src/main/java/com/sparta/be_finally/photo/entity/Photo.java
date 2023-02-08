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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photoOne;
    private String photoTwo;
    private String photoThree;
    private String photoFour;
    private String completePhoto;
    @Column(columnDefinition = "LONGTEXT")
    private String qrCode;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Room room;

    public Photo(Room room) {
        this.room = room;
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
