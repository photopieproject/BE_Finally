package com.sparta.be_finally.photo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FrameResponseDto {
    private int frameNum;

    public FrameResponseDto(int frameNum) {
        this.frameNum = frameNum;
    }
}
