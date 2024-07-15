package com.goott.trip.concho.service.con_main;

import com.goott.trip.concho.mapper.ConHotelMapper;
import com.goott.trip.concho.model.hotel.ConHotel;
import com.goott.trip.concho.model.hotel.ConHotelAndIata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelListService {
    private final ConHotelMapper conHotelMapper;

    public List<ConHotelAndIata> getHotelAndIataListByIataCode(String iataCode){
        return conHotelMapper.findHotelAndIataListByIataCode(iataCode);
    }
}
