package com.sparta.be_finally.config.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.be_finally.config.errorcode.CommonStatusCode;
import com.sparta.be_finally.config.errorcode.StatusCode;
import com.sparta.be_finally.room.dto.RoomResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;

@Getter
@RequiredArgsConstructor
public class PrivateResponseBody<T> {
     private String statusMsg;
     private int statusCode;


     //@ApiModelProperty(value="실제 데이터")
     @JsonInclude (JsonInclude.Include.NON_EMPTY)
     private T data; // null 일경우 json에 안보내지도록
     
     public PrivateResponseBody(StatusCode statusCode) {
          this.statusCode = statusCode.getStatusCode();
          this.statusMsg = statusCode.getStatusMsg();
     }
     

     public PrivateResponseBody(StatusCode statusCode, T data) {
          this.statusCode = statusCode.getStatusCode();
          this.statusMsg = statusCode.getStatusMsg();
          this.data = data;
     }
}


