package com.sparta.be_finally.common.util;

import com.sparta.be_finally.common.errorcode.UserStatusCode;
import com.sparta.be_finally.common.exception.RestApiException;
import com.sparta.be_finally.config.security.UserDetailsImpl;
import com.sparta.be_finally.user.entity.User;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@NoArgsConstructor
public class SecurityUtil {
     public static Boolean isLogin() {
          final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication == null) {
               return false;
          }else {
               return true;
          }
     }
     
     public static User getCurrentUser() {
          //실험용
          if(SecurityContextHolder.getContext()==null){
               log.info(">>>>>>>>>>>SecurityContextHolder.getContext() : {}", SecurityContextHolder.getContext());
          }
          final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication == null) {
               throw new RestApiException(UserStatusCode.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          }
          
          if (authentication.getPrincipal() instanceof UserDetails) {
               UserDetailsImpl springSecurityUser = (UserDetailsImpl) authentication.getPrincipal();
               return springSecurityUser.getUser();
          }else {
               throw new RestApiException(UserStatusCode.NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT);
          }
     }
}

