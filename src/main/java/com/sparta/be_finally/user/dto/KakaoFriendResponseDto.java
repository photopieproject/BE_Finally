package com.sparta.be_finally.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor
public class KakaoFriendResponseDto {
    private Long id; // 회원번호
    private String uuid; // 친구마다 고유한 값을 가지는 참고용 코드(Code), 카카오톡 메시지 전송 시 사용
    private Boolean favorite; // 해당 친구 즐겨찾기 여부
    private String profile_nickname; // 프로필 닉네임
    private String profile_thumbnail_image; // 프로필 썸네일(Thumbnail) 이미지, HTTPS만 지원

    public void setKakaoFriend(Long id, String uuid, Boolean favorite, String profile_nickname, String profile_thumbnail_image) {
        this.id = id;
        this.uuid = uuid;
        this.favorite = favorite;
        this.profile_nickname = profile_nickname;
        this.profile_thumbnail_image = profile_thumbnail_image;
    }
}
