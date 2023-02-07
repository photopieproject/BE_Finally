package com.sparta.be_finally.user.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Confirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private static final int VALID_MINUTES = 3;

    private String checkNum;

    private String phoneNum;

    private String userId;

    private String password;

//    @Column(name = "updated_at")
//    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
//    private LocalDateTime updateTime;




    public Confirm (String numStr, String newPhoneNumber, String userId){
        this.checkNum = numStr;
        this.phoneNum = newPhoneNumber;
        this.userId = userId;
    }


    public Confirm(String numStr, String newPhoneNumber, String userId, String passWord) {

        this.checkNum = numStr;
        this.phoneNum = newPhoneNumber;
        this.userId = userId;
        this.password = passWord;
    }


    public Confirm(String numStr, String newPhoneNumber) {
        this.checkNum = numStr;
        this.phoneNum = newPhoneNumber;
    }




    public Timestamp getNowTimestamp(){
        System.out.println("현재시간 가져오기");
        return new Timestamp(System.currentTimeMillis());
    }

}
