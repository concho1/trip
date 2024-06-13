package com.goott.trip.hamster.model;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Testproduct {
    private String airplaneKey;
    private String airplaneName;
    private String airplaneDepart;
    private String airplaneArrive;
    private Timestamp airplaneDepartTime;
    private Timestamp airplaneArriveTime;
    private String airplanePrice;
    private String nicktitle;
    private int payment;
    private int wish;
}
