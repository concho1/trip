package com.goott.trip.concho.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IataCode {
    private int iataKey;
    private String iataCode;
    private String city;
    private String cityKo;
    private String country;
    private String countryKo;
    private Integer destId;
}
