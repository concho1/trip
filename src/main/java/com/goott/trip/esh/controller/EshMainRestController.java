package com.goott.trip.esh.controller;

import com.goott.trip.esh.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/esh")
@RequiredArgsConstructor
public class EshMainRestController {
    private final ExchangeService exchangeService;

    @GetMapping("/doConvert")
    public String convertCurrency(@RequestParam("fromCurrency") String fromCurrency,
                                  @RequestParam("toCurrency") String toCurrency,
                                  @RequestParam("amount") double amount) {
        double convertedAmount = exchangeService.convertCurrency(fromCurrency, toCurrency, amount);
        return String.valueOf(convertedAmount); // 결과를 문자열로 반환
    }



}
