package com.goott.trip.concho.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelSearch {
    private int searchNum;
    private String searchCategory;
    private String searchIataCode;
    private Integer searchLatitude;
    private Integer searchLongitude;
    private Timestamp searchAt;
    private String memberId;
    public HotelSearch(int searchNum,String searchCategory, String searchIataCode, String memberId){
        this.searchNum = searchNum;
        this.searchCategory = searchCategory;
        this.searchIataCode = searchIataCode;
        this.memberId = memberId;
    }
    public HotelSearch(int searchNum,String searchCategory, Integer searchLatitude, Integer searchLongitude, String memberId){
        this.searchNum = searchNum;
        this.searchCategory = searchCategory;
        this.searchLatitude = searchLatitude;
        this.searchLongitude = searchLongitude;
        this.memberId = memberId;
    }
}
