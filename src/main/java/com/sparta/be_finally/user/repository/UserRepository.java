package com.sparta.be_finally.user.repository;

import com.sparta.be_finally.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    boolean existsByUserId(String userId);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByGoogleId(String googleId);

    @Modifying
    @Query (
            nativeQuery = true,
            value = "UPDATE users " +
                    "SET token = :token " +
                    "WHERE id = :id"
    )
    void update(@Param("id") Long id, @Param("token") String token);
}
