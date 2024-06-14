package com.goott.trip.jhm.model.travlerPricing;

import lombok.Data;

import java.util.List;

@Data
public class FareDetailBySegment {
    private String segmentId;
    private String cabin;
    private String farebasis;
    private String brandedFare;
    private String brandedFareLabel;
    private String className;
    private IncludedCheckedBags includedCheckedBags;
    private List<Amenity> amenities;
}
