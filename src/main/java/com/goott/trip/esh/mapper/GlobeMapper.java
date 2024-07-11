package com.goott.trip.esh.mapper;

import com.goott.trip.esh.model.ESHConchoHotel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GlobeMapper {
    List<ESHConchoHotel> selectAllHotels();

    ESHConchoHotel selectHotelById(@Param("hotelIdKey") String hotelIdKey, @Param("hotelId") String hotelId);
}
