package com.goott.trip.esh.controller;

import com.google.gson.Gson;
import com.goott.trip.common.model.Alarm;
import com.goott.trip.concho.model.ConchoHotel;
import com.goott.trip.concho.service.main.HotelCrawlingService;
import com.goott.trip.concho.service.main.HotelSearchService;
import com.goott.trip.esh.service.ExchangeService;
import com.goott.trip.esh.service.GlobeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class EshMainController {

    @Autowired
    private ExchangeService exchangeService;
    @Autowired
    private GlobeService globeService;
    @Autowired
    private HotelSearchService hotelSearchService;
    @Autowired
    private HotelCrawlingService hotelCrawlingService;

    @GetMapping("/exchangeRate")
    public String exchangeData(Model model) {
        List<Map<String, Object>> exchangeRates = exchangeService.getExchangeData();
        model.addAttribute("exchangeRates", exchangeRates);
        return "esh/test";
    }

    @GetMapping("/map")
    public String showMap(
            Model model,
            Principal principal,
            @RequestParam(value = "iataCode", defaultValue = "ICN") String iataCode) {
        Alarm alarm = new Alarm(model);
        String memberId = principal != null ? principal.getName() : null;
        Optional<List<ConchoHotel>> conchoHotels = hotelSearchService.getHotelListByIataCode(iataCode, memberId);
        if(conchoHotels.isPresent()) {
            for(ConchoHotel conchoHotel : conchoHotels.get()) {
                String url = hotelCrawlingService.getHotelExampleImgUrl(conchoHotel.getHotelId());
                conchoHotel.setCountryCode(url);
            }
            System.out.println(conchoHotels.get().size());
            model.addAttribute("hotels", conchoHotels.get());
            return "esh/map";
        } else {
            alarm.setMessageAndRedirect("검색된 호텔이 없습니다.", "");
            return alarm.getMessagePage();
        }
    }

}
