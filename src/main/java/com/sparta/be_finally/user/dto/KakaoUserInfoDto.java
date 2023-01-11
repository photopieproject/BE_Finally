package com.sparta.be_finally.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
     private Long id;
     private String nickname;
     
     public KakaoUserInfoDto(Long id, String nickname) {
          this.id = id;
          this.nickname = nickname;
     }

}
