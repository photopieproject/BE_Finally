package com.sparta.be_finally.user.repository;

import com.sparta.be_finally.user.entity.Confirm;
import com.sparta.be_finally.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface ConfirmRepository extends JpaRepository<Confirm,Long> {


    boolean existsByPhoneNum(String phoneNumber);

    boolean existsByUserId(String userId);

    boolean existsByUserIdAndPasswordAndPhoneNum(String userId, String passWord, String PhoneNumber);

    void deleteByCheckNum(String checkNumber);


//회원가입시 인증번호

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query (value = "UPDATE Confirm c SET c.checkNum = :checkNum  WHERE c.phoneNum = :phoneNum" )
    void checkNumUpdate(@Param("checkNum") String checkNum, @Param("phoneNum")String phoneNum );


//    @Modifying
//    @Transactional
//    @Query(value = "UPDATE Confirm c SET c.checkNum=?1,c.updateTime=?2 WHERE c.phoneNum=?3")
//    void updatePhoneTempNum(String checkNum,LocalDateTime time,String phoneNum);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query (value = "UPDATE Confirm c SET c.checkNum = :checkNum WHERE c.userId =:userId")
    void checkUserUpdate(@Param("checkNum") String checkNum, @Param("userId") String userId);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query (value = "UPDATE Confirm c SET c.checkNum =:checkNum WHERE c.password =:passWord")
    void checkPassWordUpdate(@Param("checkNum") String checkNum, @Param("passWord") String passWord);


//    @Modifying
//    @Transactional
//    @Query(value = "UPDATE confrim c SET phone_temp_num=?1,requesttime=?2,created=?3 WHERE c.phone_num=?4",nativeQuery = true)
//    void updatePhoneTempNum(String tempNum,int requestTime,Timestamp timestamp,String phoneNum);

}
