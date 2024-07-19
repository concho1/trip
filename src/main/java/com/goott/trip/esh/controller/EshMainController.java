package com.goott.trip.esh.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.concho.model.hotel.ConHotel;
import com.goott.trip.concho.model.hotel.ConHotelAndIata;
import com.goott.trip.concho.model.param.SearchParam;
import com.goott.trip.concho.service.con_main.HotelListService;
import com.goott.trip.esh.service.ExchangeService;
import com.goott.trip.esh.service.GlobeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private HotelListService hotelListService;

    @GetMapping("/map")
    public String showMap(
            Model model,
            Principal principal,
            SearchParam searchParam) {
        List<ConHotelAndIata> conHotelList = hotelListService.getHotelAndIataListByIataCode(searchParam.getIataCode());
        model.addAttribute("searchParam", searchParam);
        model.addAttribute("hotels", conHotelList);
        return "esh/map";
    }

}
