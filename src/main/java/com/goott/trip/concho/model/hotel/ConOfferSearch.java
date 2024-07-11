package com.goott.trip.concho.model.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConOfferSearch {
  private Integer num;
  private Integer offerNum;
  private Integer hotelNum;
  private Integer levelNum;
  private String hotelId;
  private java.sql.Date checkIn;
  private java.sql.Date checkOut;
  private Integer guestCnt;
  private String memberId;
  private java.sql.Timestamp searchAt;

}
