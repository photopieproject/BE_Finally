package com.sparta.be_finally.user.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Confirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String checkNum;

    private String phoneNum;

    private String userId;

    private String password;

//    @CreatedDate
//    private LocalDateTime createdAt;



    public Confirm(String numStr, String newPhoneNumber) {
        this.checkNum = numStr;
        this.phoneNum = newPhoneNumber;
    }

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

}
