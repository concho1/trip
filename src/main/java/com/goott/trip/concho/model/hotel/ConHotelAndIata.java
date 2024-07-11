package com.goott.trip.concho.model.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConHotelAndIata {
    private String uuid;
    private String iataFk;
    private String hotelAdId;
    private String hotelName;
    private String sampleImage;
    private double latitude;
    private double longitude;
    private java.sql.Timestamp createdAt;
    private ConIataCode conIataCode;
}
