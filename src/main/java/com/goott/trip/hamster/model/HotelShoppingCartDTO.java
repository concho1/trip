package com.goott.trip.hamster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HotelShoppingCartDTO {
    private String cartKey;
    private String memberId;
    private Integer personCnt;
    private String imageKey;
    private String hotelName;
    private String roomName;
    private Double totalPrice;
    private LocalDate checkIn;
    private LocalDate checkOut;
}