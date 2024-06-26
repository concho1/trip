package com.goott.trip.concho.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelCrawledRoom {
    private int crRoomNum;
    private String crRoomInfo;
    private String crHotelId;
}