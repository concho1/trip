package com.goott.trip.concho.model.param;

import com.goott.trip.concho.model.hotel.ConHotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelResult {
    private ConState conState;
    private List<ConHotel> conHotelList;
    public HotelResult (ConState conState){
        this.conHotelList = new ArrayList<>();
        this.conState = conState;
    }
}
