package com.goott.trip.concho.mapper;

import com.goott.trip.concho.model.*;
import com.goott.trip.concho.model.hotel.ConHotel;
import com.goott.trip.concho.model.hotel.ConHotelLevel;
import com.goott.trip.concho.model.hotel.ConImage64;
import com.goott.trip.concho.model.hotel.ConOfferSearch;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ConHotelMapper {
    // 호텔 정보 IATA 코드로 조회
    List<ConHotel> findHotelListByIataCode(String iataCode);
    // IATA 코드로 아마데우스 api 에서 받아온 호텔 리스트 저장
    int saveHotelList(List<ConHotel> conHotelList);
    // 구글에서 크롤링한 이미지 리스트 이미지64 테이블에 저장
    int saveImage64List(List<ConImage64> conImage64List);
    // api 사용량 생성 및 업데이트
    int updateOrSaveApiUsage(String category);

    void updateExistToTrueByIataCode(String iataCode);
}
