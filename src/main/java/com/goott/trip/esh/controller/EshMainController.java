package com.goott.trip.esh.controller;

import com.goott.trip.esh.service.ExchangeService;
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

    @GetMapping("/exchangeRate")
    public String exchangeData(Model model) {
        List<Map<String, Object>> exchangeRates = exchangeService.getExchangeData();
        model.addAttribute("exchangeRates", exchangeRates);
        return "esh/test";
    }


}
