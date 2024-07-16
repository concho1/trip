package com.goott.trip.jhm.model;

import lombok.Data;

import java.sql.Date;

@Data
public class FlightApiUsages {
    private Date useDate;
    private int cnt;
}
