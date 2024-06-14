package com.goott.trip.jhm.model.itinerary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.goott.trip.jhm.model.Arrival;
import com.goott.trip.jhm.model.Departure;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Segment {
    private Departure departure;
    private Arrival arrival;
    private String carrierCode;
    private String number;
    private String duration;
    private String id;
    private int numberOfStop;
    private boolean blacklistedInEU;
}
