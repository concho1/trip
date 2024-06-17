package com.goott.trip.concho.controller;

import com.amadeus.exceptions.ResponseException;
import com.goott.trip.common.model.Alarm;
import com.goott.trip.concho.service.main.HotelSearchService;
import com.goott.trip.concho.service.module.AmadeusApiModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("user/concho")
public class ConchoRestController {
    private final HotelSearchService hotelSearchService;
    @GetMapping("service/{num}")
    public ModelAndView getService(
            Model model,
            @PathVariable(required = false) Integer num){
        ModelAndView mv = new ModelAndView();
        Alarm alarm = new Alarm(model);

        switch (num == null ? 1 : num){
            case 1 ->{
                hotelSearchService.getHotelListByIataCode("ICN");
                //hotelSearchService.getHotelListByLatitudeAndLongitude(37.57023176017995F,126.97450461852581F);
                alarm.setMessageAndRedirect("테스트 완료 console 창 확인", "");
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
