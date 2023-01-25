package com.sparta.be_finally.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoFriendListResponseDto {
    List<KakaoFriendResponseDto> kakaoFriendList = new ArrayList<>();

    public void addPostList(KakaoFriendResponseDto kakaoFriend) {
        kakaoFriendList.add(kakaoFriend);
    }
}
