package com.goott.trip.esh.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ESHConchoHotel {
    private String idKey;          // id_key
    private String iataCode;       // iata_code
    private String name;           // name
    private String hotelId;        // hotel_id
    private Double latitude;       // latitude
    private Double longitude;      // longitude
    private String countryCode;    // country_code
    private Double distanceValue;  // distance_value
    private int searchNum;
}
