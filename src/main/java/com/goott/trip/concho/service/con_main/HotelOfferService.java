package com.goott.trip.concho.service.con_main;

import com.amadeus.resources.HotelOfferSearch;
import com.amadeus.resources.HotelSentiment;
import com.goott.trip.concho.mapper.ConHotelMapper;
import com.goott.trip.concho.model.hotel.AllHotelInfo;
import com.goott.trip.concho.model.hotel.ConHotelLevel;
import com.goott.trip.concho.model.hotel.ConHotelOffer;
import com.goott.trip.concho.model.hotel.ConOfferSearch;
import com.goott.trip.concho.model.param.ConState;
import com.goott.trip.concho.model.param.SearchParam;
import com.goott.trip.concho.service.component.AmadeusApiComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelOfferService {
    private final AmadeusApiComponent amadeusApiComponent;
    private final ConHotelMapper conHotelMapper;

    public AllHotelInfo getAllHotelInfoBySearchParam(SearchParam searchParam) {
        AllHotelInfo allHotelInfo = new AllHotelInfo();
        // 1시간 지난 데이터 삭제 진행 => 삭제하면 장바구니에 못넣음..
        //conHotelMapper.deleteOfferSearchByHour(1);

        // List<ConHotelOffer> 저장하기 전에 searchNum set 해야 함
        ConOfferSearch offerSearchForSave = new ConOfferSearch(
                searchParam.getHotelUuid(),
                searchParam.getHotelAdId(),
                searchParam.getCheckIn(),
                searchParam.getCheckOut(),
                searchParam.getPersonCnt(),
                searchParam.getMemberId());
        try {
            if(searchParam.getIsCart()){
                Optional<ConOfferSearch> offerSearchOp = conHotelMapper.findOfferSearchBySearchParamAndCart(searchParam);
                // 있으면 DB 에 있는 데이터 꺼내서 주기
                if (offerSearchOp.isPresent()) {
                    return getAllHotelInfoFromDB(offerSearchOp.get());
                }
            }else{
                // DB 에 1시간 이내 데이터 있는지 확인
                Optional<ConOfferSearch> offerSearchOp = conHotelMapper.findOfferSearchBySearchParam(searchParam);
                // 있으면 DB 에 있는 데이터 꺼내서 주기
                if (offerSearchOp.isPresent()) {
                    return getAllHotelInfoFromDB(offerSearchOp.get());
                }
                // 없으면 아마데우스에서 받아서 주기
                else {
                    return getAllHotelInfoFromAmadeus(searchParam, offerSearchForSave);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // 오류시 con_offer_search 삭제
        conHotelMapper.deleteOfferSearchByNum(offerSearchForSave.getNum());
        allHotelInfo.setConState(ConState.SEVER_ERROR);
        return allHotelInfo;
    }

    private AllHotelInfo getAllHotelInfoFromDB(ConOfferSearch offerSearch) {
        AllHotelInfo allHotelInfo = new AllHotelInfo();
        String hotelUuid = offerSearch.getHotelUuid();

        // 호텔 데이터 꺼내기
        allHotelInfo.setHotel(conHotelMapper.findHotelByHotelUuid(hotelUuid));
        // 호텔 평점 꺼내기
        allHotelInfo.setHotelLevel(conHotelMapper.findHotelLevelByHotelUuid(hotelUuid));
        // 호텔 이미지 list 꺼내기
        allHotelInfo.setImage64List(conHotelMapper.findImage64ListByHotelUuid(hotelUuid));
        // 호텔 offer list 꺼내기
        allHotelInfo.setHotelOfferList(conHotelMapper.findOfferListBySearchNum(offerSearch.getNum()));

        allHotelInfo.setConState(ConState.OK);
        return allHotelInfo;
    }

    private AllHotelInfo getAllHotelInfoFromAmadeus(SearchParam searchParam, ConOfferSearch offerSearchForSave) {
        AllHotelInfo allHotelInfo = new AllHotelInfo();

        // 우선 호텔의 해당 날짜에 offer 가 있나 확인
        List<ConHotelOffer> hotelOfferList = getHotelOfferList(searchParam);
        if (hotelOfferList.isEmpty()) {
            allHotelInfo.setConState(ConState.NOT_FOUND);
            return allHotelInfo;
        }

        // 평점도 있나 확인
        Optional<ConHotelLevel> conHotelLevelOp = getHotelLevel(searchParam);
        if (conHotelLevelOp.isEmpty()) {
            allHotelInfo.setConState(ConState.NOT_FOUND);
            return allHotelInfo;
        }

        // allHotelInfo 에 모두 저장
        allHotelInfo.setHotel(conHotelMapper.findHotelByHotelUuid(searchParam.getHotelUuid()));
        allHotelInfo.setImage64List(conHotelMapper.findImage64ListByHotelUuid(searchParam.getHotelUuid()));
        allHotelInfo.setHotelOfferList(hotelOfferList);
        allHotelInfo.setHotelLevel(conHotelLevelOp.get());

        // 검색 요청 DB 저장
        conHotelMapper.saveOfferSearch(offerSearchForSave);

        // searchNum set
        for (ConHotelOffer hotelOffer : hotelOfferList) {
            hotelOffer.setSearchNum(offerSearchForSave.getNum());
        }

        // hotel_level, hotel_offer_list DB 저장
        conHotelMapper.saveOfferList(hotelOfferList);
        conHotelMapper.saveHotelLevel(conHotelLevelOp.get());

        allHotelInfo.setConState(ConState.OK);
        return allHotelInfo;
    }

    private List<ConHotelOffer> getHotelOfferList(SearchParam searchParam) {
        List<ConHotelOffer> conHotelOfferList = new ArrayList<>();
        try {
            Optional<HotelOfferSearch> hotelOfferSearchOp = amadeusApiComponent.getHotelOfferBySearchParam(searchParam);
            if (hotelOfferSearchOp.isEmpty()) return conHotelOfferList;

            HotelOfferSearch hotelOfferSearch = hotelOfferSearchOp.get();
            for (HotelOfferSearch.Offer offer : hotelOfferSearch.getOffers()) {
                // offer 중 runtime ex 가 뜨면 없는 offer 로 취급 => nullAble 은 이미 optional 로 처리해둠
                try {
                    ConHotelOffer conHotelOffer = new ConHotelOffer();
                    conHotelOffer.conSetConOfferByAdOffer(offer);
                    conHotelOffer.conSetConOfferByHotelOfferSearch(hotelOfferSearch);
                    conHotelOfferList.add(conHotelOffer);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conHotelOfferList;
    }

    private Optional<ConHotelLevel> getHotelLevel(SearchParam searchParam) {
        Optional<ConHotelLevel> conHotelLevelOp = Optional.empty();
        try {
            HotelSentiment hotelSentiment = amadeusApiComponent.getHotelRatingByHotelId(searchParam.getHotelAdId());
            ConHotelLevel conHotelLevel = new ConHotelLevel();
            conHotelLevel.setHotelUuid(searchParam.getHotelUuid());
            conHotelLevel.conSetConHotelLevelByHotelSentiment(hotelSentiment);
            conHotelLevelOp = Optional.of(conHotelLevel);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conHotelLevelOp;
    }
}
