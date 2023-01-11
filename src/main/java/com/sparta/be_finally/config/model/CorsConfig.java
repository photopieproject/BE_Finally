package com.sparta.be_finally.config.model;

import com.sparta.be_finally.config.jwt.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer { 
     @Override
     public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/**")
               // 서버에서 응답하는 리소스에 접근가능한 출처 명시
               .allowedOrigins("http://localhost:3000")
               .allowedOrigins("https://dev.djcf93g3uh9mz.amplifyapp.com") // 프론트배포 주소
               .allowedHeaders("*")
               .allowedMethods("*")
          .exposedHeaders(JwtUtil.AUTHORIZATION_HEADER); //JSON 으로 Token 내용 전달
     }
}
