package com.goott.trip.concho.model.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConHotelOffer {
  private Integer num;
  private String hotelId;
  private String offerId;
  private String hotelType;
  private Integer available;
  private java.sql.Date checkIn;
  private java.sql.Date checkOut;
  private String category;
  private Integer bedCnt;
  private String bedCategory;
  private String roomComment;
  private Integer adultCnt;
  private String currency;
  private double totalCost;
  private double perNightCost;
}
