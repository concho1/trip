package com.goott.trip.concho.service.module;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referencedata.Locations;
import com.amadeus.resources.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
 * 1) 검색 요청시 검색 정보와 api 요청 결과를 DB에 저장하기
 * 2) 1시간 이내에 동일한 요청 => api 요청 X
 * 3) DB 에 저장한 정보를 다시 리턴
 * 4) api key 사용량 카운트 하기
 */
@Component
public class AmadeusApiModuleService {
    @Value("${concho.amadeus.key}")
    private String amadeusKey;
    @Value("${concho.amadeus.seq-key}")
    private String amadeusSeqKey;
    private Amadeus amadeus;
    @PostConstruct
    public void init() throws ResponseException {
        this.amadeus = Amadeus.builder(amadeusKey, amadeusSeqKey).setHostname("production").build();
    }
    /*
     * 호텔 list 도시 IATA 코드로 검색
     */
    public Hotel[] getHotelListByIataCode(String iataCode) throws Exception {
        Hotel[] hotels = amadeus.referenceData.locations.hotels.byCity.get(
                Params.with("cityCode", iataCode).and("radius", 40)
        );
        return hotels;
    }
    /*
     * 호텔 list 좌표로 검색 latitude, longitude
     */
    public Hotel[] getHotelListByLatitudeAndLongitude(double latitude, double longitude) throws Exception {
        Hotel[] hotels = amadeus.referenceData.locations.hotels.byGeocode.get(
                Params.with("longitude", longitude)
                        .and("latitude", latitude).and("radius", 40));
        return hotels;
    }
    /*
     * 호텔 list 좌표로 검색 latitude, longitude
     */
    public HotelOfferSearch[] getHotelRoomById(String hotelId, int personCnt, String startDate, String endDate) throws Exception {
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

    public HotelSentiment getHotelRatingByHotelId(String hotelId) throws Exception {
        // Hotel Ratings / Sentiments
        HotelSentiment[] hotelSentiments = amadeus.ereputation.hotelSentiments.get(Params.with("hotelIds", hotelId));
        return hotelSentiments[0];
    }
}
