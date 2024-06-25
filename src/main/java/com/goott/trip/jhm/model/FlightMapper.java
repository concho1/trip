package com.goott.trip.jhm.model;

import com.goott.trip.hamster.model.shoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface FlightMapper {
    public int insertFlightCode(String str);
    public APIFlight findAPIFlight(String str);
    public void insertAPIItinerary(APIItinerary itinerary);
    public void insertAPIPricing(APIPricing pdto);
    public void insertAPISegment(APISegment sdto);
    public void insertAPIDuration(APIDuration ddto);
    public void deleteAPIFlight(String str);
    public void deleteAPIItinerary(String str);
    public void deleteAPIPricing(String str);
    public void deleteAPISegment(String str);
    public void deleteAPIDuration(String str);
    public List<APIItinerary> findItineraryForView(String str);
    public List<APIPricing> findPricingForView(String str);
    public List<APISegment> findSegmentForView(String str);
    public List<APIDuration> findDurationForView(String str);
    public void uploadAirport(List<Airport> list);
    public void uploadAirline(List<AirLine> list);
    public String findAirportByIATA(String iata);
    public List<Airport> findAirportByKor(String kor);
    public String findIcaoByIata(String str);
    public String findImgByIcao(String str);
    public void insertShoppingCart(shoppingCart cart);
    public void insertCartPricing(CartPricing cp);
    public void insertCartSegment(CartSegment cs);
    public void insertCartDuration(CartDuration cd);
    public void insertCartFlight(CartFlight cf);
}
