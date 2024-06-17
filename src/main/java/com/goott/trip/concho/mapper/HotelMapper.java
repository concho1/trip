package com.goott.trip.concho.mapper;

import com.goott.trip.concho.model.ConchoHotel;
import com.goott.trip.concho.model.HotelSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface HotelMapper {
    public int insertHotelList(List<ConchoHotel> hotelList);
    public List<ConchoHotel> findHotelListBySearchNum(int searchNum);

    public int insertHotelsSearch(HotelSearch hotelSearch);
    public int getMaxNum();
    public Optional<Integer> findSearchNumByIataCode(String searchIataCode);
    public Optional<Integer> findSearchNumByLatitudeAndLongitude(int searchLatitude, int searchLongitude);
}
