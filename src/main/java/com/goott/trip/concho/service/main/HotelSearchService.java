package com.goott.trip.concho.service.main;

import com.amadeus.exceptions.ClientException;
import com.amadeus.resources.Hotel;
import com.amadeus.resources.HotelOfferSearch;
import com.goott.trip.concho.mapper.HotelMapper;
import com.goott.trip.concho.model.ConchoHotel;
import com.goott.trip.concho.model.ConchoHotelOffer;
import com.goott.trip.concho.model.HotelSearch;
import com.goott.trip.concho.service.module.AmadeusApiModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelSearchService {
    private final HotelMapper hotelMapper;
    private final AmadeusApiModuleService amadeusApiModuleService;
    private final HotelCrawlingService crawlingModuleService;

    public boolean checkAndUpdateUsage(String usageCate, int usageLimit){
        // key 사용량 확인 후 갱신
        boolean result = false;
        Integer usageCnt = hotelMapper.getUsageCntByCate(usageCate);
        if(usageCnt == null){
            hotelMapper.insertHotelApiUsageByCate(usageCate, usageLimit);
        }else{
            // 사용량 초과됬으면
            if((hotelMapper.getUsageLimitByCate(usageCate)-100) <= usageCnt){
                System.out.println("api "+usageCate+" 사용량 초과");
            }else{
                // 아니면 + 1
                hotelMapper.usageUpByCate(usageCate);
                result = true;
            }
        }
        return result;
    }

    public ConchoHotel findHotelByIdKey(String hotelIdKey){
        return hotelMapper.findHotelById(hotelIdKey);
    }

    @Transactional
    public Optional<List<ConchoHotel>> getHotelListByIataCode(String iataCode, String memberId){
        String usageCate = "iataCode";
        List<ConchoHotel> hotelList = new ArrayList<>();
        Optional<Integer> searchNumOp = hotelMapper.findSearchNumByIataCode(iataCode);
        // 이미 2시간 이내에 동일한 검색이 있었을 경우 기존 검색 결과 리턴
        if(searchNumOp.isPresent()){
            Integer searchNum = searchNumOp.get();
            hotelList = hotelMapper.findHotelListBySearchNum(searchNum);
            if( !(hotelList == null || hotelList.isEmpty()) ){
                // DB 검색시 값이 있을떄
                return Optional.of(hotelList);
            }
        }
        // DB 에 값이 없거나 처음 검색일때
        try{
            // key 사용량 초과시 null 리턴
            if(!checkAndUpdateUsage(usageCate,2000)){
                System.out.println("키 무료 사용량이 초과되었습니다. 관리자는 확인 부탁드립니다.");
                return Optional.empty();
            }

            // 검색 요청 저장 후 사용된 searchNum 반환
            HotelSearch hotelSearch = new HotelSearch(usageCate, iataCode, memberId);
            hotelMapper.insertHotelsSearch(hotelSearch);
            int searchNum = hotelSearch.getSearchNum();
            
            Hotel[] hotels = amadeusApiModuleService.getHotelListByIataCode(iataCode);
            for(Hotel hotel : hotels){
                ConchoHotel conchoHotel = new ConchoHotel(hotel, searchNum);
                hotelList.add(conchoHotel);
            }
            // 호텔 검색 결과 데이터 저장
            hotelMapper.insertHotelList(hotelList);
        }catch (ClientException e){
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(hotelList);
    }
    @Transactional
    public List<ConchoHotel> getHotelListByLatitudeAndLongitude(double latitude, double longitude, String memberId){
        String usageCate = "gps";
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
            // key 사용량 초과시 null 리턴
            if(!checkAndUpdateUsage(usageCate,2000)){
                return null;
            }
            // 검색 요청 저장
            HotelSearch hotelSearch = new HotelSearch(searchNum, usageCate, (int)Math.round(latitude*10.0), (int)Math.round(longitude*10.0), memberId);
            hotelMapper.insertHotelsSearch(hotelSearch);

            Hotel[] hotels =  amadeusApiModuleService.getHotelListByLatitudeAndLongitude(latitude, longitude);
            for(Hotel hotel : hotels){
                ConchoHotel conchoHotel = new ConchoHotel(hotel, 1);
                hotelList.add(conchoHotel);
            }
            // 호텔 검색 결과 데이터 저장
            hotelMapper.insertHotelList(hotelList);
        }catch (ClientException e){
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotelList;
    }


    @Transactional
    public Optional<List<ConchoHotelOffer>> getHotelRoomsById(String hotelId, int personCnt, String startDate, String endDate, String memberId){
        String usageCate = "hotel_info";
        List<ConchoHotelOffer> hotelOfferList = new ArrayList<>();
        Optional<Integer> searchNumOp = hotelMapper.findOfferSearchNumByHotelIdAndStartDateAndEndDateAndPersonCnt(
                hotelId,
                startDate,
                endDate,
                personCnt);
        // 이미 2시간 이내에 동일한 검색이 있었을 경우 기존 검색 결과 리턴
        if(searchNumOp.isPresent()){
            Integer searchNum = searchNumOp.get();
            hotelOfferList = hotelMapper.findHotelOfferListBySearchNum(searchNum);
            if( !(hotelOfferList == null || hotelOfferList.isEmpty()) ){
                // DB 검색시 값이 있을떄
                return Optional.of(hotelOfferList);
            }
        }
        // 처음검색 or 재검색
        try{
            // key 사용량 초과시 null 리턴
            if(!checkAndUpdateUsage(usageCate,2000)){
                System.out.println("키 무료 사용량이 초과되었습니다. 관리자는 확인 부탁드립니다.");
                return Optional.empty();
            }
            // 검색 요청 저장
            var hotelOfferSearch = new com.goott.trip.concho.model.HotelOfferSearch(
                    hotelId, startDate, endDate, personCnt, memberId
            );
            // 요청 정보 저장 후 searchNum 반환
            hotelMapper.insertHotelOfferSearch(hotelOfferSearch);
            int searchNum = hotelOfferSearch.getSearchNum();
            System.out.println("searchNum : " + searchNum);
            // 해당 호텔 id 로 객실정보 저장
            HotelOfferSearch[] hotelOfferSearches = amadeusApiModuleService.getHotelRoomById(hotelId, personCnt, startDate, endDate);

            for(HotelOfferSearch hotelOffer : hotelOfferSearches){
                Boolean available = hotelOffer.isAvailable();
                for(HotelOfferSearch.Offer offer : hotelOffer.getOffers()){
                    ConchoHotelOffer conchoHotelOffer = new ConchoHotelOffer();
                    conchoHotelOffer.setIdKey(offer.getId());
                    conchoHotelOffer.setAvailable(available);
                    conchoHotelOffer.setStartDate(offer.getCheckInDate());
                    conchoHotelOffer.setEndDate(offer.getCheckOutDate());
                    conchoHotelOffer.setRateCode(offer.getRateCode());
                    conchoHotelOffer.setRoomType(offer.getRoom() != null ? offer.getRoom().getType() : null);
                    conchoHotelOffer.setDescription(String.valueOf(offer.getDescription() != null ? offer.getDescription().getText() : null));
                    conchoHotelOffer.setPersonCnt(offer.getGuests() != null ? String.valueOf(offer.getGuests().getAdults()) : null);
                    conchoHotelOffer.setCurrency(offer.getPrice() != null ? offer.getPrice().getCurrency() : null);
                    conchoHotelOffer.setBasePrice(offer.getPrice() != null ? offer.getPrice().getBase() : null);
                    conchoHotelOffer.setTotalPrice(offer.getPrice() != null ? offer.getPrice().getTotal() : null);

                    if (offer.getPolicies() != null && offer.getPolicies().getCancellation() != null) {
                        conchoHotelOffer.setDeadline(Timestamp.valueOf(offer.getPolicies().getCancellation().getDeadline()));
                        conchoHotelOffer.setCancelPay(Double.valueOf(offer.getPolicies().getCancellation().getAmount()));
                    } else {
                        conchoHotelOffer.setDeadline(null);
                        conchoHotelOffer.setCancelPay(null);
                    }

                    conchoHotelOffer.setPaymentType(offer.getPolicies() != null ? offer.getPolicies().getPaymentType() : null);

                    if (offer.getRoom() != null && offer.getRoom().getTypeEstimated() != null) {
                        conchoHotelOffer.setCategory(offer.getRoom().getTypeEstimated().getCategory());
                        conchoHotelOffer.setBedCnt(offer.getRoom().getTypeEstimated().getBeds());
                        conchoHotelOffer.setBedType(offer.getRoom().getTypeEstimated().getBedType());
                    } else {
                        conchoHotelOffer.setCategory(null);
                        conchoHotelOffer.setBedCnt(null);
                        conchoHotelOffer.setBedType(null);
                    }
                    conchoHotelOffer.setSearchNum(searchNum);
                    hotelOfferList.add(conchoHotelOffer);
                }
            }
            // 호텔 검색 결과 데이터 저장
            hotelMapper.insertHotelOfferList(hotelOfferList);
        }catch (ClientException e){
            // 객실 정보가 없을때
            System.out.println(e.getMessage());
        }catch (Exception e) {
            // 예상치 못한 오류
            e.printStackTrace();
        }
        return Optional.ofNullable(hotelOfferList);
    }
}
