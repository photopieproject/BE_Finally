package com.sparta.be_finally.config.security;

import com.sparta.be_finally.user.entity.User;
import com.sparta.be_finally.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
     
     private final UserRepository userRepository;
     
     // userId 으로 UserDetails 반환
     @Override
     public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
          User user = userRepository.findByUserId(userId).orElseThrow(
                  () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")
          );
          
          return new UserDetailsImpl(user, user.getUserId(), user.getPassword());
     }
}