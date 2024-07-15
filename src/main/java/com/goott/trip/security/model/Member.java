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
    private String vip;
    private double total;
    private String imgKey;
    private Timestamp createdAt;

}
