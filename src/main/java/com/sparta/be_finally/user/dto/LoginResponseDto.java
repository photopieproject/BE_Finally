package com.sparta.be_finally.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginResponseDto {
     @Schema (description = "유저명")
     private String nickname;

     @Schema (description = "아이디")
     private String userId;
     
     @JsonInclude (JsonInclude.Include.NON_EMPTY)
     private String jwtToken;
     
     public LoginResponseDto(String nickname, String userId) {
          this.nickname = nickname;
          this.userId = userId;
     }
}
