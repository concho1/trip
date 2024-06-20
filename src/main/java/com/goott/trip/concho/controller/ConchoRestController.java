package com.goott.trip.concho.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.concho.model.ConchoHotel;
import com.goott.trip.concho.service.main.HotelSearchService;
import com.goott.trip.concho.service.main.IataCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("user/concho")
public class ConchoRestController {
    private final HotelSearchService hotelSearchService;
    private final IataCodeService iataCodeService;
    @GetMapping("service/{cate}")
    public ModelAndView getServicePages(
            Model model,
            Principal principal,
            @RequestParam(required = false) Map<String, String> paramMap,
            @PathVariable(required = false) String cate){
        ModelAndView mv = new ModelAndView();
        Alarm alarm = new Alarm(model);
        String memberId = principal != null ? principal.getName() : null;

        switch(cate == null ? "search" : cate){
            case "search" -> {
                mv.setViewName("concho/user/hotel_search");
            }
            case "hotel-list-iata" ->{
                String iataCode = paramMap.get("iataCode");
                String startDate = paramMap.get("startDate");
                String endDate = paramMap.get("endDate");
                String personCnt = paramMap.get("personCnt");

                List<ConchoHotel> hotelList = hotelSearchService.getHotelListByIataCode(iataCode, memberId);
                if (hotelList.size() > 10) {
                    hotelList = hotelList.subList(0, 10);
                }
                iataCodeService.updateHotelListToKoByIataCode(hotelList);
                mv.addObject("hotelList", hotelList);
                mv.addObject("iataCode", iataCode);
                mv.addObject("startDate", startDate);
                mv.addObject("endDate", endDate);
                mv.addObject("personCnt", personCnt);
                mv.setViewName("concho/user/hotel_list");
            }
            default -> {
                alarm.setMessageAndRedirect("서비스 오류", "");
                mv.setViewName(alarm.getMessagePage());
            }
        }
        return mv;
    }

    @PostMapping("service/{cate}")
    public ModelAndView postService(
            Model model,
            Principal principal,
            @RequestParam(required = false) Map<String, String> paramMap,
            @PathVariable(required = false) String cate){
        ModelAndView mv = new ModelAndView();
        Alarm alarm = new Alarm(model);
        String memberId = principal != null ? principal.getName() : null;

        switch (cate == null ? "search" : cate){
            case "search" ->{
                //hotelSearchService.getHotelListByIataCode("LON");
                //hotelInfoService.getHotelRoomsById("MCLONGHM");
                //hotelSearchService.getHotelListByLatitudeAndLongitude(37.57023176017995F,126.97450461852581F, memberId);
                alarm.setMessageAndRedirect("테스트 완료 console 창 확인", "");
                mv.setViewName(alarm.getMessagePage());
            }
            case "hotel-list-iata" ->{

            }
            case "hotel-list-gps" ->{

            }
            case "hotel-info" ->{
                String hotelId = paramMap.get("hotelId");
                String startDate = paramMap.get("startDate");
                String endDate = paramMap.get("endDate");
                Integer personCnt = Integer.valueOf(paramMap.get("personCnt"));
                hotelSearchService.getHotelRoomsById(hotelId,personCnt,startDate,endDate, memberId);

                alarm.setMessageAndRedirect("호텔 아이디 : "+ hotelId, "");
                mv.setViewName(alarm.getMessagePage());
            }
            default -> {
                alarm.setMessageAndRedirect("서비스 오류", "");
                mv.setViewName(alarm.getMessagePage());
            }
        }
        return mv;
    }
}
