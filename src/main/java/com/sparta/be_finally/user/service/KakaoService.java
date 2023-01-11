package com.sparta.be_finally.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.be_finally.config.jwt.JwtUtil;
import com.sparta.be_finally.config.security.UserDetailsImpl;
import com.sparta.be_finally.user.dto.KakaoUserInfoDto;
import com.sparta.be_finally.user.dto.LoginResponseDto;
import com.sparta.be_finally.user.entity.User;
import com.sparta.be_finally.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
     final String RequestFriendListURL = "https://kapi.kakao.com/v1/api/talk/friends"; // => 친구목록 엔드 포인트
     private final PasswordEncoder passwordEncoder;
     private final UserRepository userRepository;
     private final JwtUtil jwtUtil;
     @Value ("${kakao.api.key}")
     private String KAKAO_REST_API_KEY;

     public String kakaoLoginCheck(String code, HttpServletResponse response) throws IOException {
          // 1. "인가 코드"로 "액세스 토큰" 요청
          String accessToken = getToken(code);

          // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
          KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

          // 3. JWT 토큰 반환
          String createToken = jwtUtil.createToken(kakaoUserInfo.getNickname());
          response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);

          return createToken;
     }
     public String requestFriendList(String accessToken, int friendNum) throws IOException {
          String returnMessageLog = "친구목록 불러오기 성공";
          String inPutAccessToken = accessToken;
          URL url = null;
          HttpURLConnection conn = null;

          try {
               url = new URL(RequestFriendListURL);
               conn = (HttpURLConnection) url.openConnection();
               conn.setRequestMethod("GET");
               //--------------------------------------요청에 필요한 Header에 포함될 내용 헤더에 포함----------------------------
               log.info("...accessTokem : " + inPutAccessToken);
               conn.setRequestProperty("Authorization", inPutAccessToken);
               conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
               //---------------------------------------------------데이터를 보내는 작업부분------------------------------------
               conn.setDoOutput(true);
               BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
               StringBuilder sb = new StringBuilder();
               sb.append("&limit=" + friendNum);
               bw.write(sb.toString());
               bw.flush();
               bw.close();
               //--------------------------------------------응답코드 받는 곳-----------------------------------------------------
               int responseCode = conn.getResponseCode();
               log.info("...responseCode : " + responseCode); // => 2xx면 성공, 4xx면 실패

               if(responseCode == 403) {
                    returnMessageLog = "친구목록 불러오기 실패....";
               }
               //------------------------------------------------------------------------------------------------------------
          } catch(IOException e) {
               returnMessageLog = "친구목록 불러오기 실패....";
               log.info("Error : " + e.toString());
          } finally {
               log.info(this.getClass().getName() + "...Req Process End");
          }
          log.info(this.getClass().getName() + "...request Kakao Freind List End");
          return returnMessageLog;
     }
     
     public LoginResponseDto kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
          log.info("kakaoLogin service!!!!!!!");
          // 1. "인가 코드"로 "액세스 토큰" 요청
          String accessToken = getToken(code);
          
          // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
          KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
          
          // 3. 필요시에 회원가입
          User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);
          
          // 4. JWT 토큰 반환
          String createToken = jwtUtil.createToken(kakaoUser.getNickname());
          response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);

          // 강제로그인
          forceLogin(kakaoUser);

          return new LoginResponseDto(kakaoUser.getNickname(), kakaoUser.getUserId(), createToken);
     }
     
     // 1. "인가 코드"로 "액세스 토큰" 요청
     private String getToken(String code) throws JsonProcessingException {
          // HTTP Header 생성
          HttpHeaders headers = new HttpHeaders();
          headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
          
          // HTTP Body 생성
          MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
          body.add("grant_type", "authorization_code");
          body.add("client_id", KAKAO_REST_API_KEY);
          //body.add("redirect_uri", "https://dev.djcf93g3uh9mz.amplifyapp.com/user/kakao/callback");
          //body.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
          body.add("redirect_uri", "http://localhost:3000/user/kakao/callback");
          body.add("code", code);
          
          // HTTP 요청 보내기
          HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
               new HttpEntity<>(body, headers);
          RestTemplate rt = new RestTemplate();
          ResponseEntity<String> response = rt.exchange(
               "https://kauth.kakao.com/oauth/token",
               HttpMethod.POST,
               kakaoTokenRequest,
               String.class
          );
          
          // HTTP 응답 (JSON) -> 액세스 토큰 파싱
          String responseBody = response.getBody();
          ObjectMapper objectMapper = new ObjectMapper();
          JsonNode jsonNode = objectMapper.readTree(responseBody);
          return jsonNode.get("access_token").asText();
     }
     
     // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
     private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
          // HTTP Header 생성
          HttpHeaders headers = new HttpHeaders();
          headers.add("Authorization", "Bearer " + accessToken);
          headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
          
          // HTTP 요청 보내기
          HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
          RestTemplate rt = new RestTemplate();
          ResponseEntity<String> response = rt.exchange(
               "https://kapi.kakao.com/v2/user/me",
               HttpMethod.POST,
               kakaoUserInfoRequest,
               String.class
          );
          
          String responseBody = response.getBody();
          ObjectMapper objectMapper = new ObjectMapper();
          JsonNode jsonNode = objectMapper.readTree(responseBody);
          Long id = jsonNode.get("id").asLong();
          String nickname = jsonNode.get("properties")
               .get("nickname").asText();
          /*String email = jsonNode.get("kakao_account")
               .get("email").asText();*/
          
          log.info("카카오 사용자 정보: " + id + ", " + nickname);
          return new KakaoUserInfoDto(id, nickname);
     }
     
     // 3. 필요시에 회원가입
     @Transactional
     User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
          // DB 에 중복된 Kakao Id 가 있는지 확인
          Long kakaoId = kakaoUserInfo.getId();
          User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);
          log.info("kakao User : {}", kakaoUser);

          if (kakaoUser == null) {
               log.info("kakao User : {}", kakaoUser);
               // 신규 회원가입
               // password: random UUID
               String password = UUID.randomUUID().toString();
               String encodedPassword = passwordEncoder.encode(password);

               //userId 랜덤값
               String userId = UUID.randomUUID().toString().replaceAll("-", "");
               userId = userId.substring(0, 7) + "kko";

               kakaoUser = new User(userId, kakaoUserInfo.getNickname(), kakaoId, encodedPassword);
               userRepository.save(kakaoUser);
          }
          return kakaoUser;
     }

     // 강제 로그인 및 토큰생성
     private void forceLogin(User kakaoUser) {
          UserDetails userDetails = new UserDetailsImpl(kakaoUser);
          Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(authentication);
     }


}