package com.goott.trip.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    private String id;
    private String pw;
    private Role role;
    private String name;
    private String phone;
    private String rank;
    private String imgKey;
    private Timestamp createdAt;

    public Member(String email, String img_key){
        this.id = email;
        this.imgKey = img_key;
    }

}
