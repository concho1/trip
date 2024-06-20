package com.goott.trip.concho.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelApiUsage {
    private int usageNum;
    private String usageCate;
    private Timestamp useAt;
}
