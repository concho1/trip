package com.goott.trip.concho.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.concho.model.hotel.AllHotelInfo;
import com.goott.trip.concho.model.hotel.ConIataCode;
import com.goott.trip.concho.model.param.ConState;
import com.goott.trip.concho.model.param.SearchParam;
import com.goott.trip.concho.service.con_main.HotelListService;
import com.goott.trip.concho.service.con_main.HotelOfferService;
import com.goott.trip.concho.service.con_main.IataService;
import lombok.RequiredArgsConstructor;
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
}
