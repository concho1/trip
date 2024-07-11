package com.goott.trip.concho.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.concho.model.param.HotelResult;
import com.goott.trip.concho.model.param.Image64Result;
import com.goott.trip.concho.service.main.SaveHotelListService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("con")
@RequiredArgsConstructor
public class ConHotelSaveController {
    private final SaveHotelListService hotelListService;

    //http://localhost:8787/con/save-hotel-list?iataCode=ICN
    @GetMapping("save-hotel-list")
    public ModelAndView getSaveHotelPage(){
        return new ModelAndView("concho/com/save_hotel_list");
    }
    @PostMapping("save-hotel-list")
    public ModelAndView getHotelListByIataCode(
            Model model,
            @RequestParam(required = false) String iataCode){
        Alarm alarm = new Alarm(model);
        // api
        HotelResult hotelResult =
                hotelListService.saveHotelListToDataBaseByIataCode(iataCode);

        switch (hotelResult.getConState()){
            case OK ->{
                System.out.println(
                        "호텔 " +
                        hotelResult.getConHotelList().size() +
                        "개 크롤링 시작"
                );
                // 크롤링
                Image64Result image64Result =
                        hotelListService.crawlingHotelImageInGoogle(
                                hotelResult.getConHotelList()
                        );
                switch (image64Result.getConState()){
                    case OK ->
                        alarm.setMessageAndRedirect(
                                "저장 성공! 저장한 호텔 수: " + hotelResult.getConHotelList().size()+
                                        "이미지 크롤링 성공한 호텔 수 : " + image64Result.getSuccessHotelCnt(),
                                ""
                        );
                    case SEVER_ERROR ->
                        alarm.setMessageAndRedirect(
                                "이미지 저장 실패 ㅠㅠ!",
                                ""
                        );
                }

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
