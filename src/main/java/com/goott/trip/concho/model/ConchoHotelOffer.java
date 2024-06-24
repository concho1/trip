package com.goott.trip.concho.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.amadeus.resources.HotelOfferSearch;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConchoHotelOffer {
    private String idKey;
    private String startDate;
    private String endDate;
    private String rateCode;
    private String roomType;
    private String description;
    private String personCnt;
    private String currency;
    private String basePrice;
    private String totalPrice;
    private Timestamp deadline;
    private Double cancelPay;
    private String paymentType;
    private Integer searchNum;
    private String category;
    private Integer bedCnt;
    private String bedType;
    private Boolean available;
}
