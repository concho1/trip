package com.goott.trip.jhm.model.travlerPricing;

import lombok.Data;

@Data
public class Amenity {
    private String description;
    private boolean isChargeable;
    private String amenityType;
    private AmenityProvider amenityProvider;
}
