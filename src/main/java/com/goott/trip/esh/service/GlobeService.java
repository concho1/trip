package com.goott.trip.esh.service;

import com.amadeus.exceptions.ClientException;
import com.amadeus.resources.Hotel;
import com.goott.trip.concho.mapper.HotelMapper;
import com.goott.trip.concho.model.ConchoHotel;
import com.goott.trip.concho.model.HotelSearch;
import com.goott.trip.concho.service.main.HotelSearchService;
import com.goott.trip.concho.service.module.AmadeusApiModuleService;
import com.goott.trip.esh.mapper.GlobeMapper;
import com.goott.trip.esh.model.ConHotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GlobeService {

    @Autowired
    private GlobeMapper mapper;
    @Autowired
    HotelSearchService hotelSearchService;
    @Autowired
    private AmadeusApiModuleService amadeusApiModuleService;

    public List<ConHotel> getAllHotelData() {
        return mapper.selectAllHotels();
    }

    public ConHotel getHotelById(String hotelIdKey, String hotelId) {
        return mapper.selectHotelById(hotelIdKey, hotelId);
    }

    @Transactional
    public Optional<List<ConHotel>> getHotelListByIataFk(String iataFk, String memberId){
        String usageCate = "iataCode";
        List<ConHotel> hotelList = new ArrayList<>();
        Optional<Integer> searchNumOp = mapper.findSearchNumByIataFk(iataFk);
        // 이미 2시간 이내에 동일한 검색이 있었을 경우 기존 검색 결과 리턴
        if(searchNumOp.isPresent()){
            Integer searchNum = searchNumOp.get();
            hotelList = mapper.findHotelListBySearchNum(searchNum);
            if( !(hotelList == null || hotelList.isEmpty()) ){
                // DB 검색시 값이 있을떄
                return Optional.of(hotelList);
            }
        }
        // DB 에 값이 없거나 처음 검색일때
        try{
            // key 사용량 초과시 null 리턴
            if(!hotelSearchService.checkAndUpdateUsage(usageCate, 2000)){
                System.out.println("키 무료 사용량이 초과되었습니다. 관리자는 확인 부탁드립니다.");
                return Optional.empty();
            }

            // 검색 요청 저장 후 사용된 searchNum 반환
            HotelSearch hotelSearch = new HotelSearch(usageCate, iataFk, memberId);
            mapper.insertHotelsSearch(hotelSearch);
            int searchNum = hotelSearch.getSearchNum();

            Hotel[] hotels = amadeusApiModuleService.getHotelListByIataCode(iataFk);
            for(Hotel hotel : hotels) {
                ConHotel conHotel = new ConHotel(hotel, searchNum);

                hotelList.add(conHotel);
            }
            // 호텔 검색 결과 데이터 저장
            mapper.insertHotelList(hotelList);
        }catch (ClientException e){
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(hotelList);
    }


}
