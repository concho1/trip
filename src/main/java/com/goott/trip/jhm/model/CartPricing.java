package com.goott.trip.jhm.model;

import lombok.Data;

@Data
public class CartPricing {
    private String id;
    private String ffvId;
    private String type;
    private double base;
    private double total;
}
