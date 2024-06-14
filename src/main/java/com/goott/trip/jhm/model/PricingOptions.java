package com.goott.trip.jhm.model;

import lombok.Data;

@Data
public class PricingOptions {
    private String[] fareType;
    private boolean includedCheckedBagsOnly;
}
