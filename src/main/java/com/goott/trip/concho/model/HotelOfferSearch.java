package com.goott.trip.concho.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelOfferSearch {
    private int searchNum;
    private String hotelId;
    private String offerId;
    private String startDate;
    private String endDate;
    private Integer personCnt;
    private Timestamp searchAt;
    private String memberId;

    public HotelOfferSearch(
        int searchNum,String hotelId,String startDate,
        String endDate,int personCnt, String memberId){
        this.searchNum = searchNum;
        this.hotelId = hotelId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.personCnt = personCnt;
        this.memberId = memberId;
    }
}
