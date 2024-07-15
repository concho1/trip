package com.goott.trip.concho.model.hotel;

import com.amadeus.resources.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConHotel {
  private String uuid;
  private String iataFk;
  private String hotelAdId;
  private String hotelName;
  private String sampleImage;
  private double latitude;
  private double longitude;
  private java.sql.Timestamp createdAt;

  public ConHotel(Hotel adHotel, String iataFk) {
    this.uuid = UUID.randomUUID().toString();
    this.iataFk = iataFk;
    this.hotelAdId = adHotel.getHotelId();
    this.hotelName = adHotel.getName();
    if(adHotel.getGeoCode() != null){
      this.latitude = adHotel.getGeoCode().getLatitude();
      this.longitude = adHotel.getGeoCode().getLongitude();
    }
  }
}
