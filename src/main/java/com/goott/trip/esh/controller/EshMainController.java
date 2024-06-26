package com.goott.trip.esh.controller;

import com.google.gson.Gson;
import com.goott.trip.concho.model.ConchoHotel;
import com.goott.trip.esh.model.ESHConchoHotel;
import com.goott.trip.esh.service.ExchangeService;
import com.goott.trip.esh.service.GlobeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class EshMainController {

    @Autowired
    private ExchangeService exchangeService;
    @Autowired
    private GlobeService globeService;

    @GetMapping("/exchangeRate")
    public String exchangeData(Model model) {
        List<Map<String, Object>> exchangeRates = exchangeService.getExchangeData();
        model.addAttribute("exchangeRates", exchangeRates);
        return "esh/test";
    }

    @GetMapping("/map")
    public String showMap(Model model) {
        List<ESHConchoHotel> hotels = globeService.getAllHotelData();
        String hotelJson = new Gson().toJson(hotels);
        System.out.println(hotelJson);
        model.addAttribute("hotels", hotelJson);
        return "esh/map";
    }

}
