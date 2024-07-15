package com.goott.trip.concho.mapper;

import com.goott.trip.concho.model.hotel.*;
import com.goott.trip.concho.model.param.SearchParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ConHotelMapper {
    // 호텔 정보 IATA 코드로 조회
    List<ConHotelAndIata> findHotelAndIataListByIataCode(String iataCode);
    // IATA 코드로 아마데우스 api 에서 받아온 호텔 리스트 저장
    int saveHotelList(List<ConHotel> conHotelList);
    // 구글에서 크롤링한 이미지 리스트 이미지64 테이블에 저장
    int saveImage64List(List<ConImage64> conImage64List);
    // api 사용량 생성 및 업데이트
    int updateOrSaveApiUsage(String category);
    void updateExistToTrueByIataCode(String iataCode);

    // iata 코드 검색
    List<ConIataCode> findIataCodeBySearchStr(@Param("searchStr") String searchStr);

    // searchParam 으로 offerSearch 검색
    Optional<ConOfferSearch> findOfferSearchBySearchParam(SearchParam searchParam);
    // 호텔 아이디로 검색
    ConHotel findHotelByHotelUuid(String hotelUuid);
    ConHotelLevel findHotelLevelByHotelUuid(String hotelUuid);
    List<ConImage64> findImage64ListByHotelUuid(String hotelUuid);
    List<ConHotelOffer> findOfferListBySearchNum(int searchNum);

    int saveOfferSearch(ConOfferSearch conOfferSearch);
    int saveOfferList(List<ConHotelOffer> conHotelOfferList);
    int saveHotelLevel(ConHotelLevel conHotelLevel);
    int deleteOfferSearchByHour(int hour);
    int deleteOfferSearchByNum(int num);
}
