package com.goott.trip.concho.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchParam {
    private String hotelUuid;
    private String hotelAdId;
    private String iataCode;
    private Date checkIn;
    private Date checkOut;
    private Integer personCnt;
    private String memberId;
}
