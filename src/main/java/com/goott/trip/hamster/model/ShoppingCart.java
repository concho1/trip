package com.goott.trip.hamster.model;

import lombok.Data;

@Data
public class ShoppingCart {

    private String memberId;
    private String airKey;
    private String hotelKey;
    private String hotelContKey;
}
