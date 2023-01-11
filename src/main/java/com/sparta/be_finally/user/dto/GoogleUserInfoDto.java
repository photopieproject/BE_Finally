package com.sparta.be_finally.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleUserInfoDto {
     private String id;
     private String nickname;

     public GoogleUserInfoDto(String id, String nickname) {
          this.id = id;
          this.nickname = nickname;
     }

}
