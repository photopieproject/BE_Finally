package com.sparta.be_finally.user.service;


import com.sparta.be_finally.user.dto.ConfirmRequestDto;

import com.sparta.be_finally.common.dto.PrivateResponseBody;
import com.sparta.be_finally.common.errorcode.StatusCode;
import com.sparta.be_finally.common.errorcode.UserStatusCode;
import com.sparta.be_finally.common.exception.RestApiException;
import com.sparta.be_finally.config.security.jwt.JwtUtil;
import com.sparta.be_finally.config.AES256;

import com.sparta.be_finally.user.dto.LoginRequestDto;
import com.sparta.be_finally.user.dto.LoginResponseDto;
import com.sparta.be_finally.user.dto.SignupRequestDto;
import com.sparta.be_finally.user.entity.Confirm;
import com.sparta.be_finally.user.entity.User;
import com.sparta.be_finally.user.repository.ConfirmRepository;
import com.sparta.be_finally.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.rmi.registry.LocateRegistry;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ConfirmRepository confirmRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AES256 aes256;
    private DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCSOBIR9F6CDQZZJ", "BGUS4HJRIOXPMGOHDAUO95B7DJXJRV3E", "https://api.coolsms.co.kr");
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


        if (userRepository.existsByPhoneNumber(newPhoneNumber)) {
            throw new RestApiException(UserStatusCode.REGISTERED_PHONENUM);
        }

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


    //회원가입 시 휴대폰번호로 인증 문자 받기
    public PrivateResponseBody sendOne(String phoneNumber) {
        LocalDateTime time = LocalDateTime.now().withNano(0).plusMinutes(3);
        Message message = new Message();
        message.setFrom("01023699764");
        message.setTo(phoneNumber);
        if (phoneNumber.length() == 11) {

            Random random = new Random();
            String numStr = "";
            for (int i = 0; i < 6; i++) {
                String ran = Integer.toString(random.nextInt(10));
                numStr += ran;
            }
            message.setText("[포토파이(PhotoPie)] 본인확인 인증번호 [" + numStr + "]를 화면에 입력해주세요");
            //        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));



                try {
                    newPhoneNumber = aes256.encrypt(phoneNumber);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            if (!userRepository.existsByPhoneNumber(newPhoneNumber)) {
                if (!confirmRepository.existsByPhoneNum(newPhoneNumber)) {
                    confirmRepository.save(new Confirm(numStr,newPhoneNumber));
                }


                confirmRepository.checkNumUpdate(numStr,newPhoneNumber);
                return new PrivateResponseBody(UserStatusCode.TEXT_SEND_SUCCESS);
            }
            return new PrivateResponseBody(UserStatusCode.REGISTERED_PHONENUM);
        }
        return new PrivateResponseBody(UserStatusCode.WRONG_PHONE_COUNT);
    }



    //회원가입시 인증문자 확인하기
    @Transactional
    public PrivateResponseBody checkNum(ConfirmRequestDto confirmRequestDto) {
        String storePhoneNum = "";
        String checkNumber = "";
        String userId = "";


        List<Confirm> confirmList = confirmRepository.findAll();
        for (Confirm c : confirmList) {
            storePhoneNum = c.getPhoneNum();
            checkNumber = c.getCheckNum();
            userId = c.getUserId();


            if (confirmRequestDto.getCheckNumber().equals(checkNumber) && userId == null) {
                confirmRepository.deleteByCheckNum(confirmRequestDto.getCheckNumber());
                return new PrivateResponseBody(UserStatusCode.AGREE_USER_TYPED);
            }
        }
        return new PrivateResponseBody(UserStatusCode.FAILE_USERID);
    }


    //아이디 찾기(인증번호)
    public PrivateResponseBody findUserNum(String phoneNumber) {
        Message message = new Message();
        message.setFrom("01023699764");
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
            if (u.getPhoneNumber() != null && u.getPhoneNumber().equals(newPhoneNumber)) {
                storeId = u.getUserId();

                Random random = new Random();
                String numStr = "";
                for (int i = 0; i < 6; i++) {
                    String ran = Integer.toString(random.nextInt(10));
                    numStr += ran;
                }
                message.setText("[포토파이(PhotoPie)] 본인확인 인증번호 [" + numStr + "]를 화면에 입력해주세요");

                // SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

                if (!confirmRepository.existsByUserId(storeId)) {
                    confirmRepository.save(new Confirm(numStr, newPhoneNumber, storeId));
                }
                confirmRepository.checkUserUpdate(numStr, storeId);
                return new PrivateResponseBody(UserStatusCode.TEXT_SEND_SUCCESS);
            }
        }
        return new PrivateResponseBody(UserStatusCode.FAILE_USERID);
    }


    // 아이디찾기 눌렀을 때 인증문자 확인
    @Transactional
    public PrivateResponseBody idCheckNum(ConfirmRequestDto confirmRequestDto) {
        String storePhoneNum = "";
        String checkNumber = "";
        String userId = "";
        String passWord="";


        List<Confirm> confirmList = confirmRepository.findAll();
        for (Confirm c : confirmList) {
            storePhoneNum = c.getPhoneNum();
            checkNumber = c.getCheckNum();
            userId = c.getUserId();
            passWord = c.getPassword();


            if (confirmRequestDto.getCheckNumber().equals(checkNumber) && userId != null&&passWord == null) {
                confirmRepository.deleteByCheckNum(confirmRequestDto.getCheckNumber());
                return new PrivateResponseBody(UserStatusCode.AGREE_USER_TYPED, userId);
            } else if (confirmRequestDto.getCheckNumber().isEmpty()) {
                return new PrivateResponseBody(UserStatusCode.FAILE_INSERT_NUMBER);
            }
             }
            return new PrivateResponseBody(UserStatusCode.FAIL_IDENTIFICATION);
        }







    // 비밀번호 찾기
    public PrivateResponseBody findPassword(String phoneNumber, String userId) {
        String passWord = "";
        // 핸드폰번호 암호화
        try {
            newPhoneNumber = aes256.encrypt(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<User> userList = userRepository.findAll();
        for (User u : userList) {
            passWord = u.getPassword();
        }

        if (userRepository.existsByUserIdAndPhoneNumber(userId, newPhoneNumber)) {
            Message message = new Message();
            message.setFrom("01023699764");
            message.setTo(phoneNumber);

            Random random = new Random();
            String numStr = "";
            for (int i = 0; i < 6; i++) {
                String ran = Integer.toString(random.nextInt(10));
                numStr += ran;
            }

            message.setText("[포토파이(PhotoPie)] 본인확인 인증번호 [" + numStr + "]를 화면에 입력해주세요");

            if (!confirmRepository.existsByUserIdAndPasswordAndPhoneNum(userId, passWord, newPhoneNumber)) {
                confirmRepository.save(new Confirm(numStr, newPhoneNumber, userId, passWord));
            }
            confirmRepository.checkPassWordUpdate(numStr, passWord);
            return new PrivateResponseBody(UserStatusCode.TEXT_SEND_SUCCESS, userId);
           // this.messageService.sendOne(new SingleMessageSendingRequest(message));
        } else {
            return new PrivateResponseBody(UserStatusCode.FAIL_IDENTIFICATION);
        }
    }


    // 비밀번호 찾기 인증번호보내기
    @Transactional
    public PrivateResponseBody passwordCheckNum(ConfirmRequestDto confirmRequestDto){
        String storeId = "";
        String storeCheckNum="";
        String passWord = "";
        String phoneNumber ="";


        List<Confirm> confirmList = confirmRepository.findAll();
        for (Confirm c: confirmList){
            storeId = c.getUserId();
            storeCheckNum= c.getCheckNum();
            passWord = c.getPassword();
            phoneNumber = c. getPhoneNum();
        }

        if (confirmRequestDto.getCheckNumber().equals(storeCheckNum)&& phoneNumber!=null&& storeId !=null&& passWord !=null){
            confirmRepository.deleteByCheckNum(storeCheckNum);
            return new PrivateResponseBody(UserStatusCode.AGREE_USER_TYPED,storeId);
        }
        else if (confirmRequestDto.getCheckNumber().isEmpty()) {
            return new PrivateResponseBody(UserStatusCode.FAILE_INSERT_NUMBER);
        }
        return new PrivateResponseBody(UserStatusCode.FAIL_IDENTIFICATION);
    }





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
}




