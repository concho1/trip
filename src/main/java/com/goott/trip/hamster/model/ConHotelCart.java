package com.goott.trip.hamster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConHotelCart {
  private String uuid;
  private java.sql.Timestamp createdAt;
  private String memberId;
  private String hotelUuid;
  private String iataCode;
  private String offerUuid;
  private String orderUuid;
  {
    this.uuid = UUID.randomUUID().toString();
  }
}
