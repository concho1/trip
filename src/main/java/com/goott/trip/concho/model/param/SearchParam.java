package com.goott.trip.concho.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchParam {
    private String iataCode;
    private Date startDate;
    private Date endDate;
    private Integer personCnt;
}
