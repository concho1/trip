package com.goott.trip.hamster.controller;

import com.goott.trip.hamster.model.Testproduct;
import com.goott.trip.hamster.service.TossPayService;
import com.goott.trip.hamster.service.airplaneService;
import com.goott.trip.security.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    private EmailService emailService;

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

    @GetMapping("/success")
    public ModelAndView PaymentSuccess(
            @RequestParam String airKey,
            @RequestParam String email,
            @RequestParam String UUID,
            @RequestParam String paymentType,
            @RequestParam String orderId,
            @RequestParam String paymentKey,
            HttpServletRequest request) throws MessagingException {

        HttpSession session = request.getSession();
        String newAirKey = airKey.replaceAll("[{}]","");
        String newEmail = email.replaceAll("[{}]","");

        Testproduct cont = this.airservice.airplaneCont(newAirKey);
        this.emailService.sendAirplaneEmail(newEmail,session,UUID,cont);

        ModelAndView modelAndView = new ModelAndView("Hamster/airplanePaymentSuccess");
        modelAndView.addObject("paymentKey", paymentKey)
                .addObject("orderId", orderId)
                .addObject("email", newEmail)
                .addObject("amount", cont.getAirplanePrice()).addObject("cont",cont);

        return modelAndView;
    }

    @GetMapping("/hotel/{id_key}")
    public ModelAndView hotel(@PathVariable String id_key) {

        return new ModelAndView("Hamster/testHotel").addObject("hotelID",id_key);
    }
}
