package com.goott.trip.jhm.model.travlerPricing;

import lombok.Data;

import java.util.List;

@Data
public class TravlerPricing {
    private String travlerId;
    private String fareOption;
    private String travelerType;
    private TravlerPrice travlerPrice;
    private List<FareDetailBySegment> fareDetailsBySegment;
}
