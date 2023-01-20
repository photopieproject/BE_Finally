package com.sparta.be_finally.user.entity;

import com.sparta.be_finally.user.dto.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String token;

    public User(SignupRequestDto requestDto, String password) {
        this.userId = requestDto.getUserId();
        this.password = password;
        this.nickname = requestDto.getNickname();
    }

    public User(String userId, String nickname, Long kakaoId, String password) {
        this.userId = userId;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.password = password;
    }

    public User(String userId, String nickname, String googleId, String encodedPassword) {
        this.userId = userId;
        this.googleId = googleId;
        this.nickname = nickname;
        this.password = encodedPassword;
    }

    public void update(String token) {
        this.token = token;
    }
}
