package com.goott.trip.concho.service.component;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.*;
import com.goott.trip.concho.mapper.ConHotelMapper;
import com.goott.trip.concho.model.api.ApiCategory;
import com.goott.trip.concho.model.hotel.ConOfferSearch;
import com.goott.trip.concho.model.param.SearchParam;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

/*
 * 1) 검색 요청시 검색 정보와 api 요청 결과를 DB에 저장하기
 * 2) 1시간 이내에 동일한 요청 => api 요청 X
 * 3) DB 에 저장한 정보를 다시 리턴
 * 4) api key 사용량 카운트 하기
 */

@Component
@RequiredArgsConstructor
public class AmadeusApiComponent {
    @Value("${concho.amadeus.key}")
    private String amadeusKey;
    @Value("${concho.amadeus.seq-key}")
    private String amadeusSeqKey;
    private Amadeus amadeus;
    private final ConHotelMapper conHotelMapper;
    @PostConstruct
    public void init() throws ResponseException {
        this.amadeus = Amadeus.builder(amadeusKey, amadeusSeqKey).setHostname("production").build();
    }
    /*
     * 호텔 list 도시 IATA 코드로 검색
     */
    public Hotel[] getHotelListByIataCode(String iataCode) throws Exception {
        conHotelMapper.updateOrSaveApiUsage(ApiCategory.HOTEL_LIST_IATA.toString());
        Hotel[] hotels = amadeus.referenceData.locations.hotels.byCity.get(
                Params.with("cityCode", iataCode).and("radius", 40)
        );
        return hotels;
    }
    /*
     * 호텔 list 좌표로 검색 latitude, longitude
     */
    public Hotel[] getHotelListByLatitudeAndLongitude(double latitude, double longitude) throws Exception {
        conHotelMapper.updateOrSaveApiUsage(ApiCategory.HOTEL_LIST_GPS.toString());
        Hotel[] hotels = amadeus.referenceData.locations.hotels.byGeocode.get(
                Params.with("longitude", longitude)
                        .and("latitude", latitude).and("radius", 40));
        return hotels;
    }
    /*
     * 호텔 offer list 검색
     */
    public Optional<HotelOfferSearch> getHotelOfferBySearchParam(SearchParam searchParam) throws Exception {
        conHotelMapper.updateOrSaveApiUsage(ApiCategory.HOTEL_OFFER.toString());
        HotelOfferSearch[] offers = amadeus.shopping.hotelOffersSearch.get(
                Params
                        .with("hotelIds", searchParam.getHotelAdId())
                        .and("adults", searchParam.getPersonCnt())
                        .and("checkInDate", searchParam.getCheckIn())
                        .and("checkOutDate", searchParam.getCheckOut())
                        .and("roomQuantity", 1)
                        .and("paymentPolicy", "NONE")
                        .and("bestRateOnly", false)
                        .and("currency","USD"));
        if(offers.length == 0){
            return Optional.empty();
        }else{
            //System.out.println(offers[0].toString());
            return Optional.ofNullable(offers[0]);
        }
    }
    public HotelSentiment getHotelRatingByHotelId(String hotelId) throws Exception {
        conHotelMapper.updateOrSaveApiUsage(ApiCategory.HOTEL_LEVEL.toString());
        // Hotel Ratings / Sentiments
        HotelSentiment[] hotelSentiments = amadeus.ereputation.hotelSentiments.get(Params.with("hotelIds", hotelId));
        //System.out.println(hotelSentiments.toString());
        return hotelSentiments[0];
    }


    public HotelOfferSearch[] getHotelRoomById(String hotelId, int personCnt, String startDate, String endDate) throws Exception {
        conHotelMapper.updateOrSaveApiUsage(ApiCategory.HOTEL_OFFER.toString());
        HotelOfferSearch[] offers = amadeus.shopping.hotelOffersSearch.get(
                Params
                        .with("hotelIds", hotelId)
                        .and("adults", personCnt)
                        .and("checkInDate", startDate)
                        .and("checkOutDate", endDate)
                        .and("roomQuantity", 1)
                        .and("paymentPolicy", "NONE")
                        .and("bestRateOnly", false)
                        .and("currency","KRW"));
        return offers;
    }
}
