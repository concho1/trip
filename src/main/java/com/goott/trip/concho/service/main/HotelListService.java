package com.goott.trip.concho.service.main;

import com.amadeus.resources.Hotel;
import com.goott.trip.concho.mapper.ConHotelMapper;
import com.goott.trip.concho.model.api.ApiCategory;
import com.goott.trip.concho.model.api.ConApiUsage;
import com.goott.trip.concho.model.hotel.ConHotel;
import com.goott.trip.concho.model.hotel.ConImage64;
import com.goott.trip.concho.model.param.ConState;
import com.goott.trip.concho.model.param.HotelResult;
import com.goott.trip.concho.model.param.Image64Result;
import com.goott.trip.concho.service.component.AmadeusApiComponent;
import com.goott.trip.concho.service.component.GoogleCrawlingComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelListService {
    private final AmadeusApiComponent amadeusApiComponent;
    private final GoogleCrawlingComponent googleCrawlingComponent;
    private final ConHotelMapper conHotelMapper;
    /*
     * 아마데우스 api 에서 호텔 정보를 받아 저장하는 서비스
     * 1) iata 코드로 호텔 정보를 api 에 요청
     * 2) 해당 정보를 con_hotel 테이블에 저장
     */
    public HotelResult saveHotelListToDataBaseByIataCode(String iataCode){
        HotelResult result = new HotelResult(ConState.OK);
        try{
            Hotel[] hotels = amadeusApiComponent.getHotelListByIataCode(iataCode);
            // 여기서 api 사용량 업데이트
            conHotelMapper.updateOrSaveApiUsage(ApiCategory.HOTEL_LIST_IATA.toString());

            if(hotels.length == 0){
                result.setConState(ConState.NOT_FOUND);
                return result;
            }
            for(Hotel hotel : hotels){
                ConHotel conHotel = new ConHotel(hotel, iataCode);
                result.getConHotelList().add(conHotel);
            }
            // 여기서 DB 저장
            conHotelMapper.saveHotelList(result.getConHotelList());
        }catch (Exception e){
            e.printStackTrace();
            result.setConState(ConState.SEVER_ERROR);
        }
        return result;
    }
    /*
    http://localhost:8787/con/save-hotel-list?iataCode=ICN
     * 구글에서 해당 호텔 이름으로 검색해서 나오는 사진 크롤링 하는 서비스
     * 1) Hotel 의 sampleImage 컬럼에 사진 업데이트
     * 2) con_image64 테이블에 이미지 리스트 저장
     * 3) con_iata_code 테이블의 exist => true 로 업데이트
     */
    public Image64Result crawlingHotelImageInGoogle(List<ConHotel> conHotelList){
        Image64Result image64Result = new Image64Result();
        int resultCnt = 0;
        var allConImage64List = new ArrayList<ConImage64>();
        try{
            for(ConHotel conHotel : conHotelList){
                List<ConImage64> conImage64List =
                        googleCrawlingComponent
                                .findImage64ListByGoogleCrawlingWithConHotel(conHotel);
                if(!conImage64List.isEmpty()){
                    resultCnt++;
                    allConImage64List.addAll(conImage64List);
                    // 이미지 저장까지 성공적으로 마치면 con_iata_code 테이블에서
                    // exist 컬럼을 true 로 업데이트
                    conHotelMapper
                            .updateExistToTrueByIataCode(conHotel.getIataFk());
                }
            }
            image64Result.setConState(ConState.OK);
            image64Result.setConImage64List(allConImage64List);
            // 여기서 DB 저장
            conHotelMapper.saveImage64List(allConImage64List);
        }catch (Exception e){
            image64Result.setConState(ConState.SEVER_ERROR);
            e.printStackTrace();
        }

        image64Result.setSuccessHotelCnt(resultCnt);
        return image64Result;
    }
}
