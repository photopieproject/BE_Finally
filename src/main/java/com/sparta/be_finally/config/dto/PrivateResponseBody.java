package com.sparta.be_finally.config.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.be_finally.config.errorcode.CommonStatusCode;
import com.sparta.be_finally.config.errorcode.StatusCode;
import com.sparta.be_finally.room.dto.RoomResponseDto;
import com.sparta.be_finally.room.entity.Room;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PrivateResponseBody<T> {
     private String statusMsg;
     private int statusCode;

     //@ApiModelProperty(value="실제 데이터")
     @JsonInclude (JsonInclude.Include.NON_EMPTY)
     private T data1; // null 일경우 json에 안보내지도록

     @JsonInclude (JsonInclude.Include.NON_EMPTY)
     private T data2; // null 일경우 json에 안보내지도록

     @JsonInclude (JsonInclude.Include.NON_EMPTY)
     private T data3; // null 일경우 json에 안보내지도록

     public PrivateResponseBody(StatusCode statusCode) {
          this.statusCode = statusCode.getStatusCode();
          this.statusMsg = statusCode.getStatusMsg();
     }

     public PrivateResponseBody(StatusCode statusCode, T data1) {
          this.statusCode = statusCode.getStatusCode();
          this.statusMsg = statusCode.getStatusMsg();
          this.data1 = data1;
     }

     public PrivateResponseBody(StatusCode statusCode, T data1, T data2 ) {
          this.statusCode = statusCode.getStatusCode();
          this.statusMsg = statusCode.getStatusMsg();
          this.data1 = data1;
          this.data2 = data2;
     }

     public PrivateResponseBody(StatusCode statusCode, T data1, T data2, T data3 ) {
          this.statusCode = statusCode.getStatusCode();
          this.statusMsg = statusCode.getStatusMsg();
          this.data1 = data1;
          this.data2 = data2;
          this.data3 = data3;
     }

}


