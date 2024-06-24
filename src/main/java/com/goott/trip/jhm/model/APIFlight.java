package com.goott.trip.jhm.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class APIFlight {
    private String code;
    private Timestamp createdAt;
}
