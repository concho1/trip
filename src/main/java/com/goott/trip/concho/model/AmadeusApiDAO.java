package com.goott.trip.concho.model;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ClientException;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Hotel;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

public class AmadeusApiDAO {
    // 싱글톤 class
    private static final AmadeusApiDAO singletonDAO = new AmadeusApiDAO();
    @Value("${concho.amadeus.key}")
    private String amadeusKey;
    @Value("${concho.amadeus.seq-key}")
    private String amadeusSeqKey;
    private final Amadeus amadeus;
    private AmadeusApiDAO() {
        this.amadeus = Amadeus.builder(amadeusKey, amadeusSeqKey).build();
    }
    public static AmadeusApiDAO getInstance(){
        return singletonDAO;
    }
    public List<ConchoHotel> getHotelListByIataCode(String iataCode){
        List<ConchoHotel> hotelList = new ArrayList<>();

        try{
            // 호텔 위치로 검색
            Hotel[] hotels = amadeus.referenceData.locations.hotels.byCity.get(
                    Params.with("cityCode", iataCode)
            );
            for(Hotel hotel : hotels){
                ConchoHotel conchoHotel = new ConchoHotel(hotel);
                hotelList.add(conchoHotel);
            }
        }catch (ClientException e){
            System.out.println(e.getMessage());
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        return hotelList;
    }
}
