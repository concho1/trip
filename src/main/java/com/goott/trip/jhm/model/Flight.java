package com.goott.trip.jhm.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Flight {
    private String nonStop = "false";
    private String origin;
    private String destination;
    private String departure;
    private String comeback;
    private int adults;
    private int children;
    private int infants;
    private String airplaneClass;
    private String currency;
}
