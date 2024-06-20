package com.goott.trip.hamster.mapper;

import com.goott.trip.hamster.model.Testproduct;
import com.goott.trip.hamster.model.airplaneInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AirplaneMapper {

    public List<Testproduct> airplaneList();

    public Testproduct airplaneCont(String key);

    public List<airplaneInfo> airplaneInfoList();
}
