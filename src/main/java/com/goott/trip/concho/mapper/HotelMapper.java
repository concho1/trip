package com.goott.trip.concho.mapper;

import com.goott.trip.concho.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface HotelMapper {
    // 호텔 검색 결과 데이터
    public int insertHotelList(List<ConchoHotel> hotelList);
    public List<ConchoHotel> findHotelListBySearchNum(int searchNum);
    public ConchoHotel findHotelById(String hotelIdKey);


    // 호텔 검색 데이터
    public int insertHotelsSearch(HotelSearch hotelSearch);
    public int getMaxNum();
    public Optional<Integer> findSearchNumByIataCode(String searchIataCode);
    public Optional<Integer> findSearchNumByLatitudeAndLongitude(int searchLatitude, int searchLongitude);

    // 호텔 offer 검색 결과 데이터
    public int insertHotelOfferList(List<ConchoHotelOffer> hotelOfferList);
    public List<ConchoHotelOffer> findHotelOfferListBySearchNum(
            int searchNum);

    // 호텔 offer 검색 데이터
    public int insertHotelOfferSearch(HotelOfferSearch hotelOfferSearch);
    public int getOfferSearchMaxNum();
    public Optional<Integer> findOfferSearchNumByHotelIdAndStartDateAndEndDateAndPersonCnt(
            String hotelId,
            String startDate,
            String endDate,
            int personCnt);
    public Optional<Integer> findOfferSearchNumByOfferId(
            String offerId);



    // key 사용량
    public void insertHotelApiUsageByCate(String usageCate, Integer usageLimit);
    public Integer getUsageCntByCate(String usageCate);
    public Integer getUsageLimitByCate(String usageCate);
    public void usageUpByCate(String usageCate);
}
