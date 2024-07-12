package com.goott.trip.esh.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.concho.model.hotel.ConHotelAndIata;
import com.goott.trip.concho.service.main.HotelListService;
import com.goott.trip.esh.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class EshMainController {

    @Autowired
    private ExchangeService exchangeService;
    @Autowired
    private HotelListService hotelListService;

    @GetMapping("/exchange-rate")
    public String exchangeData(Model model) {
        List<Map<String, Object>> exchangeRates = exchangeService.getExchangeData();
        model.addAttribute("exchangeRates", exchangeRates);
        return "esh/test";
    }

    @GetMapping("/map")
    public String showMap(
            Model model,
            Principal principal,
            @RequestParam(defaultValue = "SEL") String iataCode) {
        Alarm alarm = new Alarm(model);
        //String memberId = principal != null ? principal.getName() : null;
        List<ConHotelAndIata> conchoHotels = hotelListService.getHotelAndIataListByIataCode(iataCode);

        if(!conchoHotels.isEmpty()) {
            model.addAttribute("hotels", conchoHotels);
            return "esh/map";
        } else {
            alarm.setMessageAndRedirect("검색된 호텔이 없습니다.", "");
            return alarm.getMessagePage();
        }
    }
    /*
    @GetMapping("/user/concho/service/hotel-info")
    @ResponseBody
    public ConHotel showHotelInfo(
            @RequestParam("hotelIdKey") String hotelIdKey,
            @RequestParam("hotelId") String hotelId) {
        ConHotel hotel = globeService.getHotelById(hotelIdKey, hotelId);
        if (hotel != null) {
            return hotel;
        } else {
            throw new RuntimeException("호텔 정보를 찾을 수 없습니다.");
        }
    }
    */
}
