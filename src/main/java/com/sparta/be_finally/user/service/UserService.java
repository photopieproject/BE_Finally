package com.sparta.be_finally.user.service;

import com.sparta.be_finally.common.dto.PrivateResponseBody;
import com.sparta.be_finally.common.errorcode.StatusCode;
import com.sparta.be_finally.common.errorcode.UserStatusCode;
import com.sparta.be_finally.common.exception.RestApiException;
import com.sparta.be_finally.config.security.jwt.JwtUtil;
import com.sparta.be_finally.config.AES256;
import com.sparta.be_finally.user.dto.LoginRequestDto;
import com.sparta.be_finally.user.dto.LoginResponseDto;
import com.sparta.be_finally.user.dto.SignupRequestDto;
import com.sparta.be_finally.user.entity.User;
import com.sparta.be_finally.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AES256 aes256;
    private DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCSOBIR9F6CDQZZJ", "BGUS4HJRIOXPMGOHDAUO95B7DJXJRV3E", "https://api.coolsms.co.kr" );

    String newPhoneNumber = null;

    // 이메일 인증
    private final JavaMailSender javaMailSender;

    // 이메일 인증번호 난수생성
    private final String ePw = createKey();

    // 이메일 아이디
    @Value("${spring.mail.username}")
    private String email_id;

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

        //유저 데이터베이스에서 휴대폰번호 확인.
        //만약에 저장된 휴대폰번호가 있으면 ->등록된 휴대폰번호라고 알려주기
        //없으면 회원가입 가능
        if (userRepository.existsByPhoneNumber(newPhoneNumber)){
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

    // 로그인
    public LoginResponseDto.commonLogin login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        // 사용자 확인
        User user = userRepository.findByUserId(loginRequestDto.getUserId()).orElseThrow(
                () -> new RestApiException(UserStatusCode.WRONG_LOGININFO)
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new RestApiException(UserStatusCode.WRONG_LOGININFO);
        }

        // JWT Token
        String access_token = jwtUtil.createToken(user.getUserId());

        // header 에 토큰추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, access_token);

        return new LoginResponseDto.commonLogin(user);
    }

    // 이메일 인증
    /* 메일 발송
       sendSimpleMessage의 매개변수로 들어온 to는 인증번호를 받을 메일주소
       MimeMessage 객체 안에 내가 전송할 메일의 내용을 담아준다.
       bean으로 등록해둔 javaMailSender 객체를 사용하여 이메일 send
     */
    public String checkEmail(String to) throws Exception {
        MimeMessage message = createMessage(to);
        try{
            javaMailSender.send(message); // 메일 발송
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw; // 메일로 보냈던 인증 코드를 서버로 리턴
    }

    // 아이디 찾기(인증번호)
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
                    message.setText("[포토파이(PHOTO-PIE)] 본인확인 인증번호 [" + numStr + "]를 화면에 입력해주세요" );

                SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

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

            message.setText("[포토파이(PHOTO-PIE)] 본인확인 인증번호 [" + numStr + "]를 화면에 입력해주세요");

            this.messageService.sendOne(new SingleMessageSendingRequest(message));

            return new PrivateResponseBody(UserStatusCode.SUCCESS_IDENTIFICATION, numStr, userId);
        } else {
            return new PrivateResponseBody(UserStatusCode.FAIL_IDENTIFICATION);
        }
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

    // ************************************ private
    // 이메일 메세지 만들기
    private MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : "+ to);
        log.info("인증 번호 : " + ePw);
        MimeMessage  message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
        message.setSubject("[포토파이(PHOTO-PIE)] 회원가입 인증 코드"); // 메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); // 내용, charset타입, subtype
        message.setFrom(new InternetAddress(email_id,"prac_Admin")); // 보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    // 이메일 인증코드 만들기
    private static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

}












