package com.goott.trip.esh.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.esh.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("user/esh")
@RequiredArgsConstructor
public class EshMainRestController {
    private final ExchangeService exchangeService;

    /*@GetMapping*/
    /*public ModelAndView esh(Model model) {
        Alarm alarm = new Alarm(model);
        if(exchangeService.test()) {
            alarm.setMessageAndRedirect("성공 콘솔창 확인", "");
        } else {
            alarm.setMessageAndRedirect("실패", "");
        }
        return new ModelAndView(alarm.getMessagePage());
    }*/
}
