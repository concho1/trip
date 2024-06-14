package com.goott.trip.jhm.model.price;

import lombok.Data;

import java.util.List;

@Data
public class Price {
    private String currency;
    private String total;
    private String base;
    private List<Fee> fees;
    private String grandTotal;
}
