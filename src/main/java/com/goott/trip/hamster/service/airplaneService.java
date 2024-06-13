package com.goott.trip.hamster.service;

import com.goott.trip.hamster.mapper.AirplaneMapper;
import com.goott.trip.hamster.model.Testproduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class airplaneService {

    @Autowired
    private AirplaneMapper mapper;

    public List<Testproduct> airplaneList(){return this.mapper.airplaneList(); };

    public Testproduct airplaneCont(String key){return this.mapper.airplaneCont(key); };
}
