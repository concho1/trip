package com.goott.trip.concho.model;

import com.amadeus.resources.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConchoHotel{
    private String idKey;          // id_key
    private String iataCode;       // iata_code
    private String name;           // name
    private String hotelId;        // hotel_id
    private Double latitude;       // latitude
    private Double longitude;      // longitude
    private String countryCode;    // country_code
    private Double distanceValue;  // distance_value
    private int searchNum;

    public ConchoHotel(Hotel hotel, int searchNum) {
        this.idKey = UUID.randomUUID().toString();
        this.iataCode = hotel.getIataCode();
        this.name = hotel.getName();
        this.hotelId = hotel.getHotelId();
        this.searchNum = searchNum;

        if (hotel.getGeoCode() != null) {
            this.latitude = (double) hotel.getGeoCode().getLatitude();
            this.longitude = (double) hotel.getGeoCode().getLongitude();
        } else {
            this.latitude = null;
            this.longitude = null;
        }
        if (hotel.getAddress() != null) {
            this.countryCode = hotel.getAddress().getCountryCode();
        } else {
            this.countryCode = null;
        }
        if (hotel.getDistance() != null) {
            this.distanceValue = hotel.getDistance().getValue();
        } else {
            this.distanceValue = null;
        }
    }
}
