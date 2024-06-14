package com.goott.trip.hamster.controller;

import com.goott.trip.hamster.model.Testproduct;
import com.goott.trip.hamster.service.TossPayService;
import com.goott.trip.hamster.service.airplaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class Hamcontroller {

    @Autowired
    private airplaneService service;
    @Autowired
    private TossPayService tossService;

    @GetMapping("/airplane/list")
    public ModelAndView list() {

        List<Testproduct> list =  this.service.airplaneList();

        return new ModelAndView("Hamster/testAirplaneList").addObject("list",list);
    }

    @GetMapping("/airplane/ticketing")
    public ModelAndView airticketing(@RequestParam("key")String key){
        Testproduct cont = this.service.airplaneCont(key);
        return new ModelAndView("Hamster/testAirplaneTicket").addObject("cont",cont);
    }

    @GetMapping("/success")
    public ModelAndView PaymentSuccess(
            @RequestParam String paymentType,
            @RequestParam String orderId,
            @RequestParam String paymentKey,
            @RequestParam int amount) {

        String response = tossService.confirmPayment(paymentKey, orderId, amount);

        ModelAndView mav = new ModelAndView("Hamster/paymentSuccess");
        mav.addObject("response", response);
        return mav;
    }
}
