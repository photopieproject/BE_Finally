package com.sparta.be_finally.mypage.service;

import com.sparta.be_finally.common.dto.PrivateResponseBody;
import com.sparta.be_finally.common.errorcode.UserStatusCode;
import com.sparta.be_finally.common.util.SecurityUtil;
import com.sparta.be_finally.user.entity.User;
import com.sparta.be_finally.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    // 비밀번호 재설정
    public PrivateResponseBody resetPassword(String userId, String password) {
        Optional<User> user = userRepository.findByUserId(userId);

        // 패스워드 암호화
        String newPassword = passwordEncoder.encode(password);

        userRepository.pwUpdate(newPassword, userId);

        Optional<User> newUser = userRepository.findByUserIdAndPassword(userId, newPassword);

        if (newUser.equals(user)) {
            return new PrivateResponseBody(UserStatusCode.FAIL_RESET_PASSWORD);
        } else {
            return new PrivateResponseBody(UserStatusCode.SUCCESS_RESET_PASSWORD);
        }
    }

    // 닉네임 재설정
//    public PrivateResponseBody resetNickName(String newNickName) {
//        User user = SecurityUtil.getCurrentUser();
//
//        return new PrivateResponseBody(UserStatusCode.SUCCESS_IDENTIFICATION);
//    }

    // 회원 탈퇴
    @Transactional
    public PrivateResponseBody deleteUser() {
        User user = SecurityUtil.getCurrentUser();
        if(user == null) {
            return new PrivateResponseBody(UserStatusCode.FAIL_FIND_LOGIN_USER);
        }

        userRepository.delete(user);
        // 아래를 추가해줘야 탈퇴시 로그아웃됨
//        SecurityContextHolder.clearContext();
        return new PrivateResponseBody(UserStatusCode.DELETE_USER);
    }
}
