package com.goott.trip.hamster.controller;

import com.goott.trip.hamster.model.Testproduct;
import com.goott.trip.hamster.service.airplaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class Hamcontroller {

    @Autowired
    private airplaneService service;

    @GetMapping("/airplane/list")
    public ModelAndView list() {

        List<Testproduct> list =  this.service.airplaneList();

        return new ModelAndView("Hamster/testAirplaneList").addObject("list",list);
    }
}
