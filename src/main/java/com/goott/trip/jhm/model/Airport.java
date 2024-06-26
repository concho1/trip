package com.goott.trip.jhm.model;

import lombok.Data;

@Data
public class Airport {
    private String engName;
    private String korName;
    private String iata;
    private String icao;
    private String region;
    private String engCountry;
    private String korCountry;
    private String engCity;
}
