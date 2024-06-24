package com.goott.trip.jhm.controller;


import com.amadeus.exceptions.ResponseException;
import com.goott.trip.jhm.model.*;
import com.goott.trip.jhm.service.FlightService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FlightController {

    @Autowired
    private FlightService service;

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetSerCount() {
        this.service.resetSerCount();
    }

    @RequestMapping("/flight")
    public ModelAndView flight() { return new ModelAndView("jhm/test"); }

    @PostMapping("test")
    public ModelAndView search(Flight flight) throws ResponseException {

        ModelAndView mav = new ModelAndView("jhm/test_search");

        List<APIItinerary> iList;
        List<FlightForView> ffvList = new ArrayList<>();
        APIFlight fdto;
        int ffvCount = 0;

        LocalDateTime now = LocalDateTime.now();

        String orgKor = flight.getOrigin();
        String desKor = flight.getDestination();

        flight.setOrigin(flight.getOrigin().substring(flight.getOrigin().length()-4, flight.getOrigin().length()-1));
        flight.setDestination(flight.getDestination().substring(flight.getDestination().length()-4, flight.getDestination().length()-1));

        flight.setCurrency("KRW");

        System.out.println(flight);

        StringBuilder sb = new StringBuilder();

        sb.append(flight.getNonStop()).append(flight.getOrigin()).append(flight.getDestination()).append(flight.getDeparture())
                .append(flight.getComeback()).append(flight.getAdults()).append(flight.getChildren()).append(flight.getInfants())
                .append(flight.getAirplaneClass());

        if(this.service.findAPIFlight(sb.toString()) != null) {
            fdto = this.service.findAPIFlight(sb.toString());
            LocalDateTime createdAt = fdto.getCreatedAt().toLocalDateTime();
            Duration diff = Duration.between(createdAt.toLocalTime(), now.toLocalTime());
            long min = diff.toMinutes();
            if(min > 60) {
                this.service.deleteAllByFlight(fdto.getCode());
                iList = this.service.getSearch(sb.toString(), flight);
            }else {
                iList = this.service.findItineraryForView(fdto.getCode());
            }
        }else {
            iList = this.service.getSearch(sb.toString(), flight);
        }

        for(int i=0; i<iList.size(); i++) {
            FlightForView ffv = new FlightForView();
            String iCode = iList.get(i).getId();
            ffv.setTotalBase(iList.get(i).getTotalBase());
            ffv.setTotalPrice(iList.get(i).getTotalPrice());
            ffv.setApiPricings(this.service.findPricingForView(iCode));
            ffv.setApiSegments(this.service.findSegmentForView(iCode));
            ffv.setApiDurations(this.service.findDurationForView(iCode));
            ffvList.add(ffv);
            ffvCount ++;
        }

        int serCount = this.service.getSerCount();

        System.out.println("총 " + ffvCount + "개의 검색결과");
        System.out.println("serCount : " + serCount);

        mav.addObject("flight", flight)
                .addObject("ffvList", ffvList)
                .addObject("orgKor", orgKor)
                .addObject("desKor", desKor);

        return mav;
    }

    @PostMapping("upload_airport")
    public void uploadAirport(@RequestParam("file") MultipartFile file) throws IOException {this.service.uploadAirport(file);}

    @PostMapping("upload_airline")
    public void uploadAirline(@RequestParam("file") MultipartFile file) throws IOException {this.service.uploadAirline(file);}

    @PostMapping("findAirport")
    @ResponseBody
    public ModelAndView findAirport(@RequestParam("input") String input, HttpServletResponse response) throws IOException {

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        List<Airport> res = this.service.findAirportByKor(input);

        StringBuilder json = new StringBuilder("[");

        if(res != null) {
            for(Airport dto : res) {
                if(dto != null) {
                    json.append("{");

                    json.append("\"korName\" : ");
                    json.append("\"").append(dto.getKorName()).append("\"");
                    json.append(",");

                    json.append("\"iata\" : ");
                    json.append("\"").append(dto.getIata()).append("\"");
                    json.append(",");

                    json.append("\"korCountry\" : ");
                    json.append("\"").append(dto.getKorCountry()).append("\"");
                    json.append(",");

                    json.append("\"engCity\" : ");
                    json.append("\"").append(dto.getEngCity()).append("\"");
                    json.append("}, ");
                }
            }
        }

        if(json.length() > 1 && json.charAt(json.length() - 2) == ',') {
            json.setLength(json.length() - 2);
        }

        json.append("]");

        out.println(json.toString());

        out.close();

        return null;
    }

}
