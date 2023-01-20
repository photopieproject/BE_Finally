package com.sparta.be_finally.config.handler;

import com.sparta.be_finally.config.dto.ErrorResponseDto;
import com.sparta.be_finally.config.errorcode.CommonStatusCode;
import com.sparta.be_finally.config.errorcode.StatusCode;
import com.sparta.be_finally.config.errorcode.UserStatusCode;
import com.sparta.be_finally.config.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
     
     // RestApiException 에러 핸들링
     @ExceptionHandler (RestApiException.class)
     public ResponseEntity<Object> handleCustomException(RestApiException e) {
          StatusCode statusCode = e.getStatusCode();
          return handleExceptionInternal(statusCode);
     }
     
     //ResponseStatusException 에러 핸들링
     @ExceptionHandler(ResponseStatusException.class)
     public ResponseEntity<Object> handleResponseStatus(ResponseStatusException e) {
          log.warn("handleResponseStatus", e);
          return ResponseEntity.status( e.getStatus())
               .body(ErrorResponseDto.builder()
                    .statusCode(e.getRawStatusCode())
                    .statusMsg(e.getMessage())
                    .build());
     }
     // MissingServletRequestPartException 에러 핸들링
     @ExceptionHandler (IllegalStateException.class)
     public ResponseEntity<Object> missingServletRequestPartException(IllegalStateException e) {
          log.warn("missingServletRequestPartException", e);
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
               .body(ErrorResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .statusMsg(e.getMessage())
                    .build());
     }
     // IllegalArgumentException 에러 핸들링
     @ExceptionHandler(IllegalArgumentException.class)
     public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
          log.warn("handleIllegalArgument", e);
          StatusCode statusCode = CommonStatusCode.INVALID_PARAMETER;
          return handleExceptionInternal(statusCode, e.getMessage());
     }
     
     // MethodArgumentNotValid 에러 핸들링
     @Override
     protected ResponseEntity<Object> handleMethodArgumentNotValid(
                    MethodArgumentNotValidException e,
                    HttpHeaders headers,
                    HttpStatus status,
                    WebRequest request) {
          log.warn("handleMethodArgumentNotValid", e);
          String errorFieldName = e.getBindingResult().getFieldError().getField();
          StatusCode statusCode = CommonStatusCode.INVALID_PARAMETER;
          if(errorFieldName.equals("userId")){
               statusCode = UserStatusCode.WRONG_USERID_PATTERN;
          }else if(errorFieldName.equals("password")){
               statusCode = UserStatusCode.WRONG_PASSWORD_PATTERN;
          }
          return handleExceptionInternal(statusCode);
     }
     
     // ConstraintViolationException 에러 핸들링
     @ExceptionHandler(ConstraintViolationException.class)
     public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e) {
          log.warn("handleConstraintViolation", e);
          StatusCode statusCode = CommonStatusCode.INVALID_PARAMETER;
          String interpolatedMessage = e.getMessage().split("interpolatedMessage=\'")[1].split("\', propertyPath")[0];
          System.out.println(e.getMessage());
          return handleExceptionInternal(statusCode, interpolatedMessage);
     }
     
     
     
     // 그외 에러들 핸들링
     @ExceptionHandler({Exception.class})
     public ResponseEntity<Object> handleAllException(Exception ex) {
          log.warn(">>>>>>>>>handleAllException", ex);
          ex.printStackTrace();
          StatusCode statusCode = CommonStatusCode.INTERNAL_SERVER_ERROR;
          return handleExceptionInternal(statusCode);
     }
     
     // ErrorCode 만 있는 에러 ResponseEntity 생성
     private ResponseEntity<Object> handleExceptionInternal(StatusCode statusCode) {
          return ResponseEntity.status(statusCode.getStatusCode())
               // ErrorCode 만 있는 에러 responseEntity body만들기
               .body(makeErrorResponse(statusCode));
     }
     
     private ErrorResponseDto makeErrorResponse(StatusCode statusCode) {
          return ErrorResponseDto.builder()
               .statusCode(statusCode.getStatusCode())
               .statusMsg(statusCode.getStatusMsg())
               .build();
     }
     
     // ErrorCode + message따로 있는 에러 ResponseEntity 생성
     private ResponseEntity<Object> handleExceptionInternal(StatusCode statusCode, String message) {
          return ResponseEntity.status(statusCode.getStatusCode())
               .body(makeErrorResponse(statusCode, message));
     }
     
     private ErrorResponseDto makeErrorResponse(StatusCode statusCode, String message) {
          return ErrorResponseDto.builder()
               .statusCode(statusCode.getStatusCode())
               .statusMsg(message)
               .build();
     }
}
