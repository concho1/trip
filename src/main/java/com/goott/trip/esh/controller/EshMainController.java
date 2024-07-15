package com.goott.trip.esh.controller;

import com.goott.trip.common.model.Alarm;
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
            @RequestParam(value = "iataCode", defaultValue = "ICN") String iataCode) {
        return null;
    }

}
