package com.goott.trip.concho.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.concho.model.hotel.AllHotelInfo;
import com.goott.trip.concho.model.hotel.ConIataCode;
import com.goott.trip.concho.model.param.ConState;
import com.goott.trip.concho.model.param.SearchParam;
import com.goott.trip.concho.service.con_main.HotelListService;
import com.goott.trip.concho.service.con_main.HotelOfferService;
import com.goott.trip.concho.service.con_main.IataService;
import com.goott.trip.hamster.model.ConHotelCart;
import com.goott.trip.hamster.service.ConHotelCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("con")
@RequiredArgsConstructor
public class ConHotelController {
    private final IataService iataService;
    private final HotelListService hotelListService;
    private final HotelOfferService hotelOfferService;
    private final ConHotelCartService hotelCartService;
    @PostMapping("search-iatacode")
    public Map<String, Object> findIataCodeBySearchApi(@RequestParam Map<String, String> paramMap){
        var resultMap = new HashMap<String, Object>();
        String searchStr = paramMap.getOrDefault("searchStr", null);
        if(searchStr == null){
            resultMap.put("message", "no");
            return resultMap;
        }
        List<ConIataCode> iaraCodeList = iataService.findIataCodeBySearchStr(searchStr);
        resultMap.put("result", iaraCodeList);
        return resultMap;
    }
    @GetMapping("hotel-search")
    public ModelAndView getHotelSearchPage(){
        return new ModelAndView("concho/user/con_hotel_search");
    }
    @GetMapping("hotel-list")
    public ModelAndView getHotelListPage(SearchParam searchParam){
        ModelAndView modelAndView = new ModelAndView("concho/user/con_hotel_list");
        modelAndView.addObject(
                "hotelAndIataList",
                hotelListService.getHotelAndIataListByIataCode(searchParam.getIataCode())
        );
        // 중간에 검색 설정 바꾸기 가능하게 하기 위해 보내줌
        modelAndView.addObject("searchParam", searchParam);
        return modelAndView;
    }
    @GetMapping("hotel-info")
    public ModelAndView getHotelOfferInfo(
            SearchParam searchParam,
            Principal principal){
        ModelAndView modelAndView = new ModelAndView();
        Alarm alarm = new Alarm(modelAndView);
        searchParam.setMemberId(
                Optional.ofNullable(principal)
                        .map(Principal :: getName).orElse(null));
        // 검색된 호텔의 모든 정보를 가져오기
        AllHotelInfo allHotelInfo
                = hotelOfferService.getAllHotelInfoBySearchParam(searchParam);

        if(allHotelInfo.getConState().equals(ConState.OK)){
            modelAndView.addObject("allHotelInfo", allHotelInfo);
            modelAndView.setViewName("concho/user/con_hotel_info");
        } else if (allHotelInfo.getConState().equals(ConState.NOT_FOUND)) {
            alarm.setMessageAndRedirect("해당 조건의 가능한 예약을 찾을 수 없습니다.","");
            modelAndView.setViewName(alarm.getMessagePage());
        }else {
            alarm.setMessageAndRedirect("서버 오류로 인해 예약을 찾을 수 없습니다. 다시 시도해주세요!", "");
            modelAndView.setViewName(alarm.getMessagePage());
        }
        return modelAndView;
    }
    @PostMapping("save-hotel-cart")
    public Map<String, String> saveHotelCart(
            Principal principal, ConHotelCart hotelCart){
        var resultMap = new HashMap<String, String>();
        Optional<String> memberIdOp = Optional.ofNullable(principal).map(Principal::getName);
        if(memberIdOp.isEmpty()){
            resultMap.put("message", "회원만 이용할 수 있는 서비스입니다.");
            resultMap.put("isOk", "no");
            return resultMap;
        }else{
            hotelCart.setMemberId(memberIdOp.get());
            Optional<ConHotelCart> hotelCartOp = hotelCartService.findConHotelCartByMemberIdAndOfferUuid(
                    memberIdOp.get(), hotelCart.getOfferUuid());
            if(hotelCartOp.isEmpty()){
                if(hotelCartService.saveConHotelCart(hotelCart)){
                    resultMap.put("message", "카트담기 성공");
                    resultMap.put("isOk", "ok");
                    resultMap.put("buyOk", "ok");
                    resultMap.put("uuid", hotelCart.getUuid());
                    // 가져오는거 테스트
                    // System.out.println(hotelCartService.getConHotelCartAllByMemberId(memberIdOp.get()).toString());
                }else{
                    resultMap.put("message", "카트담기 실패");
                    resultMap.put("isOk", "no");
                    resultMap.put("buyOk", "no");
                }
            }else{
                resultMap.put("message", "이미 장바구니에 있는 정보입니다.");
                resultMap.put("uuid", hotelCartOp.get().getUuid());
                resultMap.put("buyOk", "ok");
                resultMap.put("isOk", "no");
            }
            return resultMap;
        }
    }
}
