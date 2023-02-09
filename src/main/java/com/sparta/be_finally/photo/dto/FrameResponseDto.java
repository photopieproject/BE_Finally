package com.sparta.be_finally.photo.dto;

import lombok.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getTop5 {
        private int ranking;
        private int frameNum;
        private String frameName;
        private String frameUrl;

    }
}
