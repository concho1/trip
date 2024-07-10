package com.goott.trip.concho.controller;

import com.goott.trip.hamster.model.HotelShoppingCartDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("member/concho/service")
public class ConchoMemberRestController {
    @RequestMapping("hotel-reservation")
    public Map<String, String> makeHotelReservation(
            Model model,
            Principal principal,
            @ModelAttribute HotelShoppingCartDTO hotelShoppingCart){
        System.out.println(hotelShoppingCart.toString());
        var resultMap = new HashMap<String,String>(
                Map.of("result", "ok")
        );
        return resultMap;
    }
}
