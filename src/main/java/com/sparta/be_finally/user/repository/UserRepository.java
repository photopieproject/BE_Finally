package com.sparta.be_finally.user.repository;

import com.sparta.be_finally.user.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(String userId);

    boolean existsByUserId(String userId);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByGoogleId(String googleId);
}
