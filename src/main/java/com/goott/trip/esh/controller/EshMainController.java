package com.goott.trip.esh.controller;

import com.goott.trip.esh.service.ExchangeService;
import com.goott.trip.esh.service.GlobeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class EshMainController {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private GlobeService globeService;

    @GetMapping("/exchangeRate")
    public String getExchangeRates(Model model) {
        List<Map<String, Object>> exchangeRates = exchangeService.getExchangeRates();
        model.addAttribute("exchangeRates", exchangeRates);
        return "esh/test";
    }

    @GetMapping("/doExchange")
    public String Exchange(
            @RequestParam("fromCurrency") String fromCurrency,
            @RequestParam("toCurrency") String toCurrency,
            @RequestParam("amount") double amount,
            Model model) {

        try {
            double result = exchangeService.convertCurrency(fromCurrency, toCurrency, amount);
            model.addAttribute("result", result);
            model.addAttribute("fromCurrency", fromCurrency);
            model.addAttribute("toCurrency", toCurrency);
            model.addAttribute("amount", amount);
        } catch(Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "esh/tester";
    }

}
