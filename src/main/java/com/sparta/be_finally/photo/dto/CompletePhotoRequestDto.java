package com.sparta.be_finally.photo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class CompletePhotoRequestDto {
    private MultipartFile completePhoto;
}
