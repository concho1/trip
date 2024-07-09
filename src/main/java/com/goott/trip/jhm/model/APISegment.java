package com.goott.trip.jhm.model;

import lombok.Data;

@Data
public class APISegment {
    private String id;
    private String itineraryCode;
    private String flightCode;
    private String depOrComb;
    private String departureIata;
    private String departureAt;
    private String arrivalIata;
    private String arrivalAt;
    private String duration;
    private String carrierCode;
    private String carrierNum;
    private String airlineKor;
}
