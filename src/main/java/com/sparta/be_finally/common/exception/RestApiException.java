package com.sparta.be_finally.common.exception;

import com.sparta.be_finally.common.errorcode.StatusCode;

public class RestApiException extends RuntimeException{
     
     // 필드값
     private final StatusCode statusCode;

     // getter
     public StatusCode getStatusCode(){
          return this.statusCode;
     }

     // 생성자
     public RestApiException(StatusCode statusCode){
          this.statusCode = statusCode;
     }
}
