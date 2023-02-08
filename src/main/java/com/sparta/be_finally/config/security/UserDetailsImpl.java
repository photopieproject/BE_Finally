package com.sparta.be_finally.config.security;

import com.sparta.be_finally.user.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

     //인증이 완료된 사용자 추가
     private final User user; // 인증완료된 User 객체
     private final String username; // 인증완료된 User의 ID
     private final String password; // 인증완료된 User의 PWD

     public UserDetailsImpl(User kakaoUser) {
          this.user = kakaoUser;
          this.username = kakaoUser.getNickname();
          this.password = kakaoUser.getPassword();
     }

     //사용자의 권한 GrantedAuthority 로 추상화 및 반환
     @Override
     public Collection<? extends GrantedAuthority> getAuthorities() {
          String authority = "ROLE_USER";

          SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
          Collection<GrantedAuthority> authorities = new ArrayList<>();
          authorities.add(simpleGrantedAuthority);

          return authorities;
     }
     //사용자의 권한 GrantedAuthority 로 추상화 및 반환
     @Override
     public boolean isAccountNonExpired() {
          return false;
     }

     @Override
     public boolean isAccountNonLocked() {
          return false;
     }

     @Override
     public boolean isCredentialsNonExpired() {
          return false;
     }

     @Override
     public boolean isEnabled() {
          return false;
     }
}