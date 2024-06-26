package com.goott.trip.common.main_controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {
    @GetMapping("/home")
    public ModelAndView getHome(){
        return new ModelAndView("common/home");
    }
    @GetMapping("/sample")
    public ModelAndView getSample(){
        return new ModelAndView("common/sample");
    }
}
