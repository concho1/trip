package com.goott.trip.esh.globeModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Globe {
    private String markerId;
    private String coordSys;
    private double longtitude;
    private double latitude;
    private String category;
    private String activity;
}
