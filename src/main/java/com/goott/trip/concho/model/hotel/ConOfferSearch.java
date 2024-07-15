package com.goott.trip.concho.model.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConOfferSearch {
  private Integer num;
  private String hotelUuid;
  private String hotelAdId;
  private java.sql.Date checkIn;
  private java.sql.Date checkOut;
  private Integer guestCnt;
  private String memberId;
  private java.sql.Timestamp searchAt;

  public ConOfferSearch(
          String hotelUuid,
          String hotelAdId,
          java.sql.Date checkIn,
          java.sql.Date checkOut,
          Integer guestCnt,
          String memberId){
    this.hotelUuid = hotelUuid;
    this.hotelAdId = hotelAdId;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
    this.guestCnt = guestCnt;
    this.memberId = memberId;
  }
}
