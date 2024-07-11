package com.goott.trip.esh.mapper;

import com.goott.trip.concho.model.ConchoHotel;
import com.goott.trip.concho.model.HotelSearch;
import com.goott.trip.esh.model.ConHotel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface GlobeMapper {
    List<ConHotel> selectAllHotels();
    public List<ConHotel> findHotelListBySearchNum(int searchNum);
    public Optional<Integer> findSearchNumByIataFk(String searchIataFk);
    public int insertHotelList(List<ConHotel> hotelList);
    public int insertHotelsSearch(HotelSearch hotelSearch);
    ConHotel selectHotelById(@Param("hotelIdKey") String hotelIdKey, @Param("hotelId") String hotelId);
}
