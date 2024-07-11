package com.goott.trip.concho.model.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConImage64 {
  private String uuid;
  private String hotelUuid;
  private String base64;
  private java.sql.Timestamp createdAt;
}
