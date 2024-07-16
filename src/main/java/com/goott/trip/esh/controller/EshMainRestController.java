package com.goott.trip.esh.controller;

import com.goott.trip.esh.service.ExchangeService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user/esh")
@RequiredArgsConstructor
public class EshMainRestController {
    private final ExchangeService exchangeService;

    @GetMapping("/exchange-rate")
    public String exchangeData(Model model) {
        List<Map<String, Object>> exchangeRates = exchangeService.getExchangeData();
        model.addAttribute("exchangeRates", exchangeRates);
        System.out.println(exchangeRates);
        return String.valueOf(exchangeRates);
    }

    @PostMapping("/convert-currency")
    public double convertCurrency(@RequestParam("fromCurrency") String fromCurrency) {
        if(fromCurrency.equalsIgnoreCase("JPY") || fromCurrency.equalsIgnoreCase("IDR")) {
            return Math.round(Double.parseDouble(this.exchangeService.getExchageRate(fromCurrency))/100.0);
        }
        return Math.round(Double.parseDouble(this.exchangeService.getExchageRate(fromCurrency)));

    }
}
