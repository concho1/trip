package com.goott.trip.jhm.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CartFlight {
    private String ffvId;
    private String origin;
    private String destination;
    private String departure;
    private String comeback;
    private int adults;
    private int children;
    private int infants;
    private double totalBase;
    private double totalPrice;
    private Timestamp createdAt;
}
