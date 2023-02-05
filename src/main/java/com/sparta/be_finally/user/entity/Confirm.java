package com.sparta.be_finally.user.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
