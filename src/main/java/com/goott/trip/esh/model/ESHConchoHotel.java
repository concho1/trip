package com.goott.trip.esh.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ESHConchoHotel {
    private String name;           // name
    private Double latitude;       // latitude
    private Double longitude;
    private String startDate;
    private String endDate;
    private int personCnt;
    private String idKey;
    private String HotelId;
}
