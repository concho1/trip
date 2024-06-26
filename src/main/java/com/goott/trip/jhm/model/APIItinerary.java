package com.goott.trip.jhm.model;

import lombok.Data;

@Data
public class APIItinerary {
    private String id;
    private String flightCode;
    private double totalBase;
    private double totalPrice;
}
