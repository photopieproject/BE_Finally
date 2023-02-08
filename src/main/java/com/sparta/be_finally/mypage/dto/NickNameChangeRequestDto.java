package com.sparta.be_finally.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class NickNameChangeRequestDto {

    @Size(min = 1, max = 10)
    @NotBlank
    private String changeNickName;
}
