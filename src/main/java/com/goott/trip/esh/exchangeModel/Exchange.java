package com.goott.trip.esh.exchangeModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {
    private String countryName;
    private String countryCode;
    private Timestamp exchangeDate;
    private double exchangeRate;
    private String memberId;
}
