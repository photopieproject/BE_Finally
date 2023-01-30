package com.sparta.be_finally.user.service;

import com.sparta.be_finally.config.dto.PrivateResponseBody;
import com.sparta.be_finally.config.errorcode.StatusCode;
import com.sparta.be_finally.config.errorcode.UserStatusCode;
import com.sparta.be_finally.config.exception.RestApiException;
import com.sparta.be_finally.config.jwt.JwtUtil;
import com.sparta.be_finally.config.model.AES256;
import com.sparta.be_finally.user.dto.FindPasswordRequestDto;
import com.sparta.be_finally.user.dto.LoginRequestDto;
import com.sparta.be_finally.user.dto.LoginResponseDto;
import com.sparta.be_finally.user.dto.SignupRequestDto;
import com.sparta.be_finally.user.entity.User;
import com.sparta.be_finally.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;



@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AES256 aes256;
    String newPhoneNumber = null;

    // 회원가입
    public void signUp(SignupRequestDto requestDto) {
        // userId 중복확인
        if (userRepository.existsByUserId(requestDto.getUserId())) {
            throw new RestApiException(UserStatusCode.OVERLAPPED_USERID);
        }
        // 패스워드 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 핸드폰번호 암호화
        try {
            newPhoneNumber = aes256.encrypt(requestDto.getPhoneNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(newPhoneNumber);

        // 회원가입
        userRepository.save(new User(requestDto, password, newPhoneNumber));
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
    public LoginResponseDto.commonLogin login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        // 사용자 확인
        User user = userRepository.findByUserId(loginRequestDto.getUserId()).orElseThrow(
                () -> new RestApiException(UserStatusCode.WRONG_LOGININFO)
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new RestApiException(UserStatusCode.WRONG_LOGININFO);
        }

        // header 에 토큰추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUserId()));

        return new LoginResponseDto.commonLogin(user);
    }

    public boolean findPassword(String phoneNumber, String userId) {
        // 핸드폰번호 암호화
        try {
            newPhoneNumber = aes256.encrypt(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userRepository.existsByUserIdAndPhoneNumber(userId, newPhoneNumber)) {
            return true;
        } else {
            return false;
        }
    }

    public PrivateResponseBody findUserNum(String phoneNumber) {

        String storeId = "";

        // 핸드폰번호 암호화
        try {
            newPhoneNumber = aes256.encrypt(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<User> userList = userRepository.findAll();


        for (User u : userList) {
            if (u.getPhoneNumber().equals(newPhoneNumber)) {
                storeId = u.getUserId();
                return new PrivateResponseBody(UserStatusCode.AGREE_USER_TYPED, storeId);
            }
        }
            return new PrivateResponseBody(UserStatusCode.FAILE_USERID);
        }
}




//        for (User u : userList) {
//            if (u.getPhoneNumber().equals(newPhoneNumber)) {
//                storeId = u.getUserId();
//            }
//        } return new PrivateResponseBody(UserStatusCode.INVALID_TOKEN, storeId);
//    }



//            if (!u.getPhoneNumber().equals(newPhoneNumber)) {
//                return new PrivateResponseBody(UserStatusCode.FAILE_USERID,userLists);
//            }
//            return new PrivateResponseBody(UserStatusCode.AGREE_USER_TYPED, userLists);
//


//    List<User> userList = userRepository.findAll(); // 리스트를 받아옴
//
//        for (User u : userList) {
//                if (u.getPhoneNumber().equals(newPhoneNumber)) {
//                storeId = u.getUserId();
//                }
//                }
//                return new PrivateResponseBody(UserStatusCode.AGREE_USER_TYPED, storeId);
//                }
//                }
//


