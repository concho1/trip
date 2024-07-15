package com.goott.trip.concho.model.param;

import com.goott.trip.concho.model.hotel.ConImage64;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image64Result {
    private ConState conState;
    private int successHotelCnt;
    private List<ConImage64> conImage64List;
}
