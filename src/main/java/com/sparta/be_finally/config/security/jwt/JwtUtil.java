package com.sparta.be_finally.config.security.jwt;

import com.sparta.be_finally.config.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
     private final UserDetailsServiceImpl userDetailsService;
     
     // Header KEY 값
     public static final String AUTHORIZATION_HEADER = "Authorization";
     // nick 저장용 key
     private static final String NICKNAME_KEY = "nick";
     // Token 식별자
     private static final String BEARER_PREFIX = "Bearer ";
     // 토큰 만료시간
     private static final long TOKEN_TIME = 60 * 60; // 60초 * 60번 = 1시간

     @Value ("${JWT_SECRET_KEY}")
     private String secretKey;
     private Key key;
     // 암호화 알고리즘
     private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
     
     @PostConstruct
     public void init() {
          // 비밀 키 만들기
          byte[] bytes = Base64.getDecoder().decode(secretKey);
          key = Keys.hmacShaKeyFor(bytes);
     }
     
     // header에서 토큰을 가져오기
     public String resolveToken(HttpServletRequest request) {
          // 헤더에서 토큰값 가져오기
          String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
          // 값 제대로 된경우
          if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
               // 앞에 bearer 뒷부분 잘라오기
               return bearerToken.substring(7);
          }
          return null;
     }
     
     // 토큰 생성
     public String createToken(String userId) {
          Date date = new Date();
          
          return BEARER_PREFIX +
               Jwts.builder()
                    // userId 넣기
                    .setSubject(userId)
                    .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                    .setIssuedAt(date)
                    .signWith(key, signatureAlgorithm)
                    .compact();
     }
     
     // 토큰 검증
     public boolean validateToken(String token) {
          try {
               Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
               return true;
          } catch (SecurityException | MalformedJwtException e) {
               log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
          } catch (ExpiredJwtException e) {
               log.info("Expired JWT token, 만료된 JWT token 입니다.");
          } catch (UnsupportedJwtException e) {
               log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
          } catch (IllegalArgumentException e) {
               log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
          }
          return false;
     }
     
     // 토큰에서 사용자 정보 가져오기
     public Claims getUserInfoFromToken(String token) {
          return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
     }
     
     // 인증 객체 생성
     public Authentication createAuthentication(String userId) {
          UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
          return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
     }
}
