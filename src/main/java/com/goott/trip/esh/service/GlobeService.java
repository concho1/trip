package com.goott.trip.esh.service;

import com.goott.trip.esh.mapper.GlobeMapper;
import com.goott.trip.esh.model.ESHConchoHotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobeService {

    @Autowired
    private GlobeMapper mapper;

    public List<ESHConchoHotel> getAllHotelData() {
        return mapper.selectAllHotels();
    }
}
