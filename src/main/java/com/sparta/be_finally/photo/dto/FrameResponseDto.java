package com.sparta.be_finally.photo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;

@Getter
@Setter
@NoArgsConstructor
public class FrameResponseDto {
    private int frameNum;

    private String frameUrl;

    public FrameResponseDto(int frameNum) {
        this.frameNum = frameNum;
    }


    public FrameResponseDto(int frameNum, URL frameUrl) {
        this.frameNum = frameNum;
        this.frameUrl = String.valueOf(frameUrl);
    }

}
