package com.sparta.be_finally.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDto {
     private final String statusMsg;
     private final int statusCode;

     public ErrorResponseDto(String statusMsg, int statusCode){
          this.statusMsg = statusMsg;
          this.statusCode = statusCode;
     }
}
