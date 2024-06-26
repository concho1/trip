package com.goott.trip.jhm.model;

import lombok.Data;

@Data
public class APIPricing {
    private String id;
    private String itineraryCode;
    private String flightCode;
    private String type;
    private double base;
    private double total;
}
