package com.goott.trip.hamster.controller;

import com.goott.trip.hamster.model.Testproduct;
import com.goott.trip.hamster.service.TossPayService;
import com.goott.trip.hamster.service.airplaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.UUID;

@RequestMapping("member/hamster")
@RestController
public class Hamcontroller {

    @Autowired
    private airplaneService airservice;
    @Autowired
    private TossPayService tossService;

    @GetMapping("airplane/list")
    public ModelAndView list() {

        List<Testproduct> list =  this.airservice.airplaneList();

        return new ModelAndView("Hamster/testAirplaneList").addObject("list",list);
    }

    @GetMapping("airplane/ticketing")
    public ModelAndView airticketing(@RequestParam("key")String key){
        List<String> country = this.airservice.getCountry();
        Testproduct cont = this.airservice.airplaneCont(key);
        return new ModelAndView("Hamster/PlaneReservation").addObject("cont",cont).
                addObject("country",country);
    }

    @PostMapping("airplane/payment")
    public ModelAndView airplanePayment(@RequestParam("key")String key,@RequestParam("callFname")String callFname,
                                        @RequestParam("callLname")String callLname,@RequestParam("country")String country,
                                        @RequestParam("phone")String phone,@RequestParam("email")String email){
        Testproduct cont = this.airservice.airplaneCont(key);

        UUID uuid = UUID.randomUUID();

        return new ModelAndView("Hamster/airplanePayment").addObject("cont",cont)
                .addObject("callFname",callFname).addObject("callLname",callLname)
                .addObject("country",country).addObject("phone",phone)
                .addObject("email",email).addObject("UUID",uuid);
    }

    @GetMapping("success")
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

    @GetMapping("/hotel/{id_key}")
    public ModelAndView hotel(@PathVariable String id_key) {

        return new ModelAndView("Hamster/testHotel").addObject("hotelID",id_key);
    }
}
