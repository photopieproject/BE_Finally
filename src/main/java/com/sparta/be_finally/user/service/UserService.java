package com.sparta.be_finally.user.service;

import com.sparta.be_finally.config.dto.PrivateResponseBody;
import com.sparta.be_finally.config.errorcode.StatusCode;
import com.sparta.be_finally.config.errorcode.UserStatusCode;
import com.sparta.be_finally.config.exception.RestApiException;
import com.sparta.be_finally.config.jwt.JwtUtil;
import com.sparta.be_finally.config.model.AES256;
import com.sparta.be_finally.user.dto.LoginRequestDto;
import com.sparta.be_finally.user.dto.LoginResponseDto;
import com.sparta.be_finally.user.dto.SignupRequestDto;
import com.sparta.be_finally.user.entity.User;
import com.sparta.be_finally.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
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
    private DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCSOBIR9F6CDQZZJ", "BGUS4HJRIOXPMGOHDAUO95B7DJXJRV3E", "https://api.coolsms.co.kr" );
    ;
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

    //아이디 찾기(인증번호)
    public PrivateResponseBody findUserNum(String phoneNumber) {
        Message message = new Message();
        message.setFrom("01023699764" );
        message.setTo(phoneNumber);

        String storeId = "";

        // 핸드폰번호 다시 암호화하여 암호환 번호가 있는지 확인
        try {
            newPhoneNumber = aes256.encrypt(phoneNumber);

        } catch (Exception e) {
            e.printStackTrace();
        }


        List<User> userList = userRepository.findAll();

        for (User u : userList) {

                if (u.getPhoneNumber()!=null&&u.getPhoneNumber().equals(newPhoneNumber)) {
                    storeId = u.getUserId();

                    Random random = new Random();
                    String numStr = "";
                    for (int i = 0; i < 6; i++) {
                        String ran = Integer.toString(random.nextInt(10));
                        numStr += ran;
                    }
                    message.setText("[포토파이(PhotoPie)] 본인확인 인증번호 [" + numStr + "]를 화면에 입력해주세요" );

//                SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

                    return new PrivateResponseBody(UserStatusCode.TEXT_SEND_SUCCESS, numStr, storeId);

                }
            }
            return new PrivateResponseBody(UserStatusCode.FAILE_USERID);
        }





    // 비밀번호 찾기
    public PrivateResponseBody findPassword(String phoneNumber, String userId) {
        // 핸드폰번호 암호화
        try {
            newPhoneNumber = aes256.encrypt(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Optional<User> user = userRepository.findByUserIdAndPhoneNumber(userId, newPhoneNumber);
        System.out.println("user:" + user);

        if (userRepository.existsByUserIdAndPhoneNumber(userId, newPhoneNumber)) {
            Message message = new Message();
            message.setFrom("01023699764");
            message.setTo(phoneNumber);

            Random random = new Random();
            String numStr = "";
            for(int i = 0; i < 6; i++){
                String ran = Integer.toString(random.nextInt(10));
                numStr += ran;
            }

            message.setText("[포토파이(PhotoPie)] 본인확인 인증번호 [" + numStr + "]를 화면에 입력해주세요");

//            this.messageService.sendOne(new SingleMessageSendingRequest(message));

            return new PrivateResponseBody(UserStatusCode.SUCCESS_IDENTIFICATION, numStr, userId);
        } else {
            return new PrivateResponseBody(UserStatusCode.FAIL_IDENTIFICATION);
        }
    }

    // 비밀번호 재설정
    public PrivateResponseBody resetPassword(String userId, String password) {
        Optional<User> user = userRepository.findByUserId(userId);
        System.out.println("비밀번호 변경 전 유저:" + user);

        System.out.println("암호화 전 비밀번호: " + password);

        // 패스워드 암호화
        String newPassword = passwordEncoder.encode(password);

        System.out.println("암호화 후 비밀번호: " + newPassword);

        userRepository.pwUpdate(newPassword, userId);

        Optional<User> newUser = userRepository.findByUserIdAndPassword(userId, newPassword);

        System.out.println("비밀번호 변경 후 유저:" + newUser);

        if (newUser.equals(user)) {
            return new PrivateResponseBody(UserStatusCode.FAIL_RESET_PASSWORD);
        } else {
            return new PrivateResponseBody(UserStatusCode.SUCCESS_RESET_PASSWORD);
        }



    }



}












