package com.sparta.be_finally.photo.entity;

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

    @Column(nullable = false)
    private String photoOne;

    @Column(nullable = false)
    private String photoTwo;

    @Column(nullable = false)
    private String photoThree;

    @Column(nullable = false)
    private String photoFour;
}
