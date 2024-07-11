package com.goott.trip.esh.model;

import com.amadeus.resources.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConHotel {
    /*private String name;           // name
    private Double latitude;       // latitude
    private Double longitude;
    private String startDate;
    private String endDate;
    private int personCnt;
    private String idKey;
    private String HotelId;*/

    private int num;
    private String iataFk;
    private String hotelAdId;
    private String hotelName;
    private String sampleImage;
    private double latitude;
    private double longitude;
    private String createdAt;

    public ConHotel(Hotel hotel, int searchNum) {

    }
}
