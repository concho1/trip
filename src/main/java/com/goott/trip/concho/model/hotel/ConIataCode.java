package com.goott.trip.concho.model.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConIataCode {
  private String iataCode;
  private String city;
  private String cityKo;
  private String country;
  private String countryKo;
  private Boolean exist;
}
