package com.goott.trip.jhm.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PeopleCnt {
    private Timestamp createdAt;
    private int guest;
    private int user;
    private int bot;
}
