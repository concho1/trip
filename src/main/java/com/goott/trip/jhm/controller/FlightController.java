package com.goott.trip.jhm.controller;


import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.goott.trip.jhm.model.Airport;
import com.goott.trip.jhm.model.Flight;
import com.goott.trip.jhm.service.FlightService;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FlightController {

    @Autowired
    private FlightService service;

    @RequestMapping("/flight")
    public ModelAndView flight() { return new ModelAndView("jhm/test"); }

    @PostMapping("test")
    public ModelAndView search(Flight flight) throws ResponseException {

        ModelAndView mav = new ModelAndView("jhm/test_search");

        System.out.println(flight);

        FlightOfferSearch[] list = this.service.searchFlight(flight);

        String korOrigin = this.service.findAirportByIATA(flight.getOrigin());
        String korDestination = this.service.findAirportByIATA(flight.getDestination());

        mav.addObject("origin", flight.getOrigin());
        mav.addObject("destination", flight.getDestination());
        mav.addObject("korOrigin", korOrigin);
        mav.addObject("korDestination", korDestination);
        mav.addObject("fList", list);
        return mav;
    }

    @PostMapping("upload_airport")
    public void uploadAirport(@RequestParam("file") MultipartFile file) throws IOException {this.service.uploadAirport(file);}

    @PostMapping("upload_airline")
    public void uploadAirline(@RequestParam("file") MultipartFile file) throws IOException {this.service.uploadAirline(file);}

}
