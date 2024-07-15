package com.goott.trip.concho.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConApiUsage {
  private long num;
  private long cnt;
  private java.sql.Date useDate;
  private String category;
}
