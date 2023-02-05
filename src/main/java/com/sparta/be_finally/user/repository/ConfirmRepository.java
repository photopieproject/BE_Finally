package com.sparta.be_finally.user.repository;

import com.sparta.be_finally.user.entity.Confirm;
import com.sparta.be_finally.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ConfirmRepository extends JpaRepository<Confirm,Long> {

    boolean existsByPhoneNum(String phoneNumber);

    boolean existsByUserId(String userId);

    boolean existsByUserIdAndPasswordAndPhoneNum(String userId, String passWord, String PhoneNumber);


    void deleteByCheckNum(String checkNumber);


//회원가입시 인증번호
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query (value = "UPDATE Confirm c SET c.checkNum = :checkNum WHERE c.phoneNum = :phoneNum" )
    void checkNumUpdate(@Param("checkNum") String checkNum, @Param("phoneNum") String phoneNum);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query (value = "UPDATE Confirm c SET c.checkNum = :checkNum WHERE c.userId =:userId")
    void checkUserUpdate(@Param("checkNum") String checkNum, @Param("userId") String userId);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query (value = "UPDATE Confirm c SET c.checkNum =:checkNum WHERE c.password =:passWord")
    void checkPassWordUpdate(@Param("checkNum") String checkNum, @Param("passWord") String passWord);


}
