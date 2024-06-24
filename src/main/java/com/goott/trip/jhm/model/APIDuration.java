package com.goott.trip.jhm.model;

import lombok.Data;

@Data
public class APIDuration {
    private String id;
    private String itineraryCode;
    private String flightCode;
    private String depOrComb;
    private String duration;
    private String airline;
    private String airlineImg;
}
