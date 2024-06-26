package com.goott.trip.hamster.mapper;

import com.goott.trip.hamster.model.Testproduct;
import com.goott.trip.hamster.model.airplaneInfo;
import com.goott.trip.jhm.model.CartDuration;
import com.goott.trip.jhm.model.CartFlight;
import com.goott.trip.jhm.model.CartSegment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AirplaneMapper {

    public List<Testproduct> airplaneList();

    public Testproduct airplaneCont(String key);

    public List<airplaneInfo> airplaneInfoList();

    public List<CartFlight> getAirInfo(String AirKey);

    public List<CartDuration> getDurationInfo(String AirKey);

    public List<CartSegment> getSegment(String AirKey);
}
