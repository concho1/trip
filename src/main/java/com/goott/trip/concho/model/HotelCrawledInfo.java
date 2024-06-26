package com.goott.trip.concho.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelCrawledInfo {
    private int crNum;
    private String crHotelId;
    private String crFacility;
    private String crFacilityInfo;
    private String crContent;
}
