package com.goott.trip.concho.model.hotel;

import com.goott.trip.concho.model.param.ConState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllHotelInfo {
    private ConState conState;
    private ConHotel hotel;
    private ConHotelLevel hotelLevel;
    private List<ConHotelOffer> hotelOfferList;
    private List<ConImage64> image64List;
}
