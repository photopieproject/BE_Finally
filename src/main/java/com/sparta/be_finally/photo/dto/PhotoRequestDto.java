package com.sparta.be_finally.photo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PhotoRequestDto {
    private MultipartFile photo_one;
    private MultipartFile photo_two;
    private MultipartFile photo_three;
    private MultipartFile photo_four;
}
