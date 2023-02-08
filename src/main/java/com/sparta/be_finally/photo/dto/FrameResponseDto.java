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

    private int maxPeople;

    public FrameResponseDto(int frameNum) {
        this.frameNum = frameNum;
    }

    public FrameResponseDto(int frameNum, String frameUrl) {
        this.frameNum = frameNum;
        this.frameUrl = String.valueOf(frameUrl);
    }

    public FrameResponseDto(int frameNum, String frameUrl, int maxPeople) {
        this.frameNum = frameNum;
        this.frameUrl = String.valueOf(frameUrl);
        this.maxPeople = maxPeople;
    }
}
