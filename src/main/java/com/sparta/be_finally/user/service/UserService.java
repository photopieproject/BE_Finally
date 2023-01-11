package com.sparta.be_finally.user.service;

import com.sparta.be_finally.config.errorcode.StatusCode;
import com.sparta.be_finally.config.errorcode.UserStatusCode;
import com.sparta.be_finally.config.exception.RestApiException;
import com.sparta.be_finally.config.jwt.JwtUtil;
import com.sparta.be_finally.user.dto.LoginRequestDto;
import com.sparta.be_finally.user.dto.SignupRequestDto;
import com.sparta.be_finally.user.entity.User;
import com.sparta.be_finally.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    public void signUp(SignupRequestDto requestDto) {
        //userId 중복확인
        if (userRepository.existsByUserId(requestDto.getUserId())) {
            throw new RestApiException(UserStatusCode.OVERLAPPED_USERID);
        }

        // 패스워드 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원가입
        userRepository.save(new User(requestDto, password));
    }

    //userId 중복확인
    public StatusCode idCheck(String userId) {
        if (userRepository.existsByUserId(userId)) {
            return UserStatusCode.OVERLAPPED_USERID;

        } else {
            return UserStatusCode.AVAILABLE_USERID;
        }
    }

    //로그인
    public StatusCode login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        // 사용자 확인
        User user = userRepository.findByUserId(loginRequestDto.getUserId()).orElseThrow(
                () -> new RestApiException(UserStatusCode.NO_USER)
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new RestApiException(UserStatusCode.WRONG_PASSWORD);
        }

        // header 에 토큰추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUserId()));

        return UserStatusCode.USER_LOGIN_SUCCESS;
    }


}
