package com.goott.trip.jhm.model;

import lombok.Data;

@Data
public class CartSegment {
    private String id;
    private String ffvId;
    private String departureIata;
    private String departureAt;
    private String arrivalIata;
    private String arrivalAt;
    private String duration;
    private String carrierCode;
    private String carrierNum;
}
