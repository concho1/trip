package com.goott.trip.concho.controller;

import com.goott.trip.concho.model.IataCode;
import com.goott.trip.concho.model.param.SearchParam;
import com.goott.trip.concho.service.main.HotelListService;
import com.goott.trip.concho.service.main.SaveHotelListService;
import com.goott.trip.concho.service.main.IataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("con")
@RequiredArgsConstructor
public class ConHotelController {
    private final IataService iataService;
    private final HotelListService hotelListService;
    @PostMapping("search-iatacode")
    public Map<String, Object> findIataCodeBySearchApi(@RequestParam Map<String, String> paramMap){
        var resultMap = new HashMap<String, Object>();
        String searchStr = paramMap.getOrDefault("searchStr", null);
        if(searchStr == null){
            resultMap.put("message", "no");
            return resultMap;
        }
        List<IataCode> iaraCodeList = iataService.findIataCodeBySearchStr(searchStr);
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
        return modelAndView;
    }
}
