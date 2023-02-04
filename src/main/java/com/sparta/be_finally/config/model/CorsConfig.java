package com.sparta.be_finally.config.model;

import com.sparta.be_finally.config.jwt.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class CorsConfig implements WebMvcConfigurer { 
     @Override
     public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/**")
               // 서버에서 응답하는 리소스에 접근가능한 출처 명시
               .allowedOrigins("http://localhost:3000") // 프론트 로컬주소
               .allowedOrigins("https://dev.djcf93g3uh9mz.amplifyapp.com") // 프론트 배포 주소
               .allowedOrigins("https://www.photo-pie.store") // 프론트 도메인 주소
               .allowedOrigins("https://photo-pie.store") // 프론트 도메인 주소
               .allowedHeaders("*")
               .allowedMethods("*")
          .exposedHeaders(JwtUtil.AUTHORIZATION_HEADER); //JSON 으로 Token 내용 전달
     }
}
