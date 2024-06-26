package com.goott.trip.esh.mapper;

import com.goott.trip.esh.model.ESHConchoHotel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GlobeMapper {
    List<ESHConchoHotel> selectAllHotels();

}
