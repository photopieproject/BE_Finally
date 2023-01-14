package com.sparta.be_finally.room.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FrameRequestDto {
    private int frame;

    public FrameRequestDto(int frame) {
        this.frame = frame;
    }
}
