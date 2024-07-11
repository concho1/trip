package com.goott.trip.concho.model.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConHotelLevel {

  private Integer num;
  private String hotelId;
  private Integer overall;
  private Integer staff;
  private Integer location;
  private Integer service;
  private Integer comforts;
  private Integer sleep;
  private Integer pool;
  private Integer money;
  private Integer facilities;
  private Integer meal;
  private Integer nearAttraction;

}
