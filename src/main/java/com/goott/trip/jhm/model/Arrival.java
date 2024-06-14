package com.goott.trip.jhm.model;

import lombok.Data;

@Data
public class Arrival {
    private String iataCode;
    private String terminal;
    private String at;
}
