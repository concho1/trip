package com.goott.trip.concho.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.concho.model.param.HotelResult;
import com.goott.trip.concho.model.param.Image64Result;
import com.goott.trip.concho.service.main.HotelListService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("con")
@RequiredArgsConstructor
public class ConHotelController {
    private final HotelListService hotelListService;
    @GetMapping("save-hotel-list")
    public ModelAndView getHotelListByIataCode(Model model, @RequestParam String iataCode){
        Alarm alarm = new Alarm(model);
        // api
        HotelResult hotelResult =
                hotelListService.saveHotelListToDataBaseByIataCode(iataCode);

        switch (hotelResult.getConState()){
            case OK ->{
                // 크롤링
                Image64Result image64Result =
                        hotelListService.crawlingHotelImageInGoogle(
                                hotelResult.getConHotelList()
                        );

                alarm.setMessageAndRedirect(
                        "저장 성공! 저장한 호텔 수: " + hotelResult.getConHotelList().size()+
                        "이미지 크롤링 성공한 호텔 수 : " + image64Result.getSuccessHotelCnt(),
                        ""
                );
            }
            case SEVER_ERROR -> {
                alarm.setMessageAndRedirect(
                        "저장 실패 ㅠㅠ!",
                        ""
                );
            }
        }
        return new ModelAndView(alarm.getMessagePage());
    }
}
