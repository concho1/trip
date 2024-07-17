package com.goott.trip.hamster.model;

import com.goott.trip.concho.model.hotel.ConHotel;
import com.goott.trip.concho.model.hotel.ConHotelOffer;
import com.goott.trip.concho.model.hotel.ConIataCode;
import com.goott.trip.security.model.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConHotelCartAll {
    private String uuid;
    private Timestamp createdAt;
    private Member memberObj;
    private ConHotel hotelObj;
    private ConIataCode iataCodeObj;
    private ConHotelOffer offerObj;
    private ConPayment paymentObj;
}
