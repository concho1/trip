package com.goott.trip.concho.model.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConImage64 {
  private UUID uuid;
  private UUID hotelUuid;
  private String base64;
  private java.sql.Timestamp createdAt;
}
