package com.goott.trip.concho.service.main;

import com.amadeus.Params;
import com.amadeus.exceptions.ClientException;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Hotel;
import com.goott.trip.concho.mapper.HotelMapper;
import com.goott.trip.concho.model.ConchoHotel;
import com.goott.trip.concho.model.HotelSearch;
import com.goott.trip.concho.service.module.AmadeusApiModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelSearchService {
    private final HotelMapper hotelMapper;
    private final AmadeusApiModuleService amadeusApiModuleService;
    @Transactional
    public List<ConchoHotel> getHotelListByIataCode(String iataCode){
        List<ConchoHotel> hotelList = new ArrayList<>();
        Optional<Integer> searchNumOp = hotelMapper.findSearchNumByIataCode(iataCode);
        // 이미 2시간 이내에 동일한 검색이 있었을 경우 기존 검색 결과 리턴
        if(searchNumOp.isPresent()){
            Integer searchNum = searchNumOp.get();
            hotelList = hotelMapper.findHotelListBySearchNum(searchNum);
            return hotelList;
        }
        // 기존 검색 결과가 없을경우 api 에 요청
        int searchNum = hotelMapper.getMaxNum() + 1;
        try{
            // 검색결과 저장
            HotelSearch hotelSearch = new HotelSearch(searchNum, "iataCode", iataCode);
            hotelMapper.insertHotelsSearch(hotelSearch);
            
            Hotel[] hotels = amadeusApiModuleService.getHotelListByIataCode(iataCode);
            for(Hotel hotel : hotels){
                ConchoHotel conchoHotel = new ConchoHotel(hotel, searchNum);
                hotelList.add(conchoHotel);
            }
            // 호텔 데이터 저장
            hotelMapper.insertHotelList(hotelList);
        }catch (ClientException e){
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotelList;
    }
    @Transactional
    public List<ConchoHotel> getHotelListByLatitudeAndLongitude(double latitude, double longitude){
        List<ConchoHotel> hotelList = new ArrayList<>();
        // 위도 경도의 최대값은 180 이기 때문에 (int) 로 캐스팅 가능
        // 10곱한 후 반올림시 0.05 도 손실 => 50km 의 손실
        // 최대 140km 손실 가능 ==> 한마디로 최대 140km 이내의 값은 대충 모을 수 있음 => 클러스터링은 시간이 너무 오래걸림
        Optional<Integer> searchNumOp = hotelMapper.findSearchNumByLatitudeAndLongitude((int)Math.round(latitude*10.0),(int)Math.round(longitude*10.0));

        // 이미 2시간 이내에 동일한 검색이 있었을 경우 기존 검색 결과 리턴
        if(searchNumOp.isPresent()){
            Integer searchNum = searchNumOp.get();
            hotelList = hotelMapper.findHotelListBySearchNum(searchNum);
            return hotelList;
        }
        // 기존 검색 결과가 없을경우 api 에 요청
        int searchNum = hotelMapper.getMaxNum() + 1;
        try{
            // 검색결과 저장
            HotelSearch hotelSearch = new HotelSearch(searchNum, "gps", (int)Math.round(latitude*10.0), (int)Math.round(longitude*10.0));
            hotelMapper.insertHotelsSearch(hotelSearch);

            Hotel[] hotels =  amadeusApiModuleService.getHotelListByLatitudeAndLongitude(latitude, longitude);
            for(Hotel hotel : hotels){
                ConchoHotel conchoHotel = new ConchoHotel(hotel, 1);
                hotelList.add(conchoHotel);
            }
            // 호텔 데이터 저장
            hotelMapper.insertHotelList(hotelList);
        }catch (ClientException e){
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotelList;
    }

    

}
