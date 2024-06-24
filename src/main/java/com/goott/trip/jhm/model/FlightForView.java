package com.goott.trip.jhm.model;

import lombok.Data;

import java.util.List;

@Data
public class FlightForView {
    private double totalBase;
    private double totalPrice;
    private List<APIPricing> apiPricings;
    private List<APISegment> apiSegments;
    private List<APIDuration> apiDurations;
}
