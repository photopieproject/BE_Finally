package com.sparta.be_finally.photo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PhotoRequestDto {
    private MultipartFile photo_1;
    private MultipartFile photo_2;
    private MultipartFile photo_3;
    private MultipartFile photo_4;
}
