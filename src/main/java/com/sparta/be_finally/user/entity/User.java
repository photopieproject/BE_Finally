package com.sparta.be_finally.user.entity;

import com.sparta.be_finally.room.entity.Room;
import com.sparta.be_finally.user.dto.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity (name = "users")
@NoArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true, unique = true)
    private Long kakaoId;

    @Column(nullable = true, unique = true)
    private String googleId;

    @Column
    private String openvidu_token;

    @Column
    private String access_token;

    @Column
    private String phoneNumber;

    @Column
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    public User(SignupRequestDto requestDto, String password, String phoneNumber) {
        this.userId = requestDto.getUserId();
        this.password = password;
        this.nickname = requestDto.getNickname();
        this.phoneNumber = phoneNumber;
    }

    public User(String userId, String nickname, Long kakaoId, String encodedPassword) {
        this.userId = userId;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.password = encodedPassword;
    }

    public User(String userId, String nickname, String googleId, String encodedPassword) {
        this.userId = userId;
        this.googleId = googleId;
        this.nickname = nickname;
        this.password = encodedPassword;
    }

    public void update(String openvidu_token) {
        this.openvidu_token = openvidu_token;
    }
}
