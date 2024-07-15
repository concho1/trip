package com.goott.trip.jhm.controller;


import com.amadeus.exceptions.ResponseException;
import com.goott.trip.hamster.model.ShoppingCart;
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
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("flight")
public class FlightController {

    @Autowired
    private FlightService service;

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetSerCount() {
        this.service.resetSerCount();
    }

    @RequestMapping("search_flight")
    public ModelAndView searchFlight() { return new ModelAndView("jhm/search_flight"); }

    @RequestMapping("upload")
    public ModelAndView upload() { return new ModelAndView("jhm/upload"); }

    @PostMapping("test")
    public ModelAndView search(Flight flight, Principal principal) throws ResponseException {

        ModelAndView mav = new ModelAndView("jhm/test_search");

        List<APIItinerary> iList;
        List<FlightForView> ffvList = new ArrayList<>();
        APIFlight fdto;
        int ffvCount = 0;
        String memberId;

        if(principal != null) {
            memberId = principal.getName();
        }else {
            memberId = "";
        }

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
            boolean t = createdAt.toLocalDate().isBefore(now.toLocalDate());
            System.out.println("min : "+min);
            System.out.println("t : "+t);
            if(min > 60 || t) {
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
            String fID = "ffv/" + UUID.randomUUID();
            ffv.setFfvId(fID);
            ffv.setTotalBase(iList.get(i).getTotalBase());
            ffv.setTotalPrice(iList.get(i).getTotalPrice());
            ffv.setApiPricings(this.service.findPricingForView(iCode));
            ffv.setApiSegments(this.service.findSegmentForView(iCode));
            ffv.setApiDurations(this.service.findDurationForView(iCode));
            ffvList.add(ffv);
            for(int j=0; j<ffv.getApiDurations().size(); j++) {
                System.out.println("img : "+ffv.getApiDurations().get(j).getAirlineImg());
            }
            ffvCount ++;
        }

        int serCount = this.service.getSerCount();

        System.out.println("총 " + ffvCount + "개의 검색결과");
        System.out.println("serCount : " + serCount);

        mav.addObject("flight", flight)
                .addObject("ffvList", ffvList)
                .addObject("orgKor", orgKor)
                .addObject("desKor", desKor)
                .addObject("memberId", memberId);

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

                    json.append("\"korCity\" : ");
                    json.append("\"").append(dto.getKorCity()).append("\"");
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

    @PostMapping("insertFfv")
    @ResponseBody
    public String insertFfv(@RequestParam("ffv") String data, @RequestParam("ffvId") String ffvId,
                            @RequestParam("origin") String origin, @RequestParam("des") String des,
                            @RequestParam("dep") String dep, @RequestParam("comb") String comb,
                            @RequestParam("adults") int adults, @RequestParam("children") int children,
                            @RequestParam("infants") int infants, @RequestParam("memberId") String memberId) {

        String[] ffvHead = {"ffvId", "totalBase", "totalPrice", "apiPricings", "apiSegments", "apiDurations"};
        String[] pricingHead = {"id", "itineraryCode", "flightCode", "type", "base", "total"};
        String[] segmentHead = {"id", "itineraryCode", "flightCode", "depOrComb", "departureIata", "departureAt", "arrivalIata",
                                    "arrivalAt", "duration", "carrierCode", "carrierNum", "airlineKor"};
        String[] durationHead = {"id", "itineraryCode", "flightCode", "depOrComb", "duration", "airline", "airlineKor", "airlineImg"};

        ShoppingCart cart = new ShoppingCart();
        cart.setMemberId(memberId);
        cart.setAirKey(ffvId);

        this.service.insertShoppingCart(cart);

        CartFlight cf = new CartFlight();
        cf.setFfvId(ffvId);
        cf.setOrigin(origin);
        cf.setDestination(des);
        cf.setDeparture(dep);
        cf.setComeback(comb);
        cf.setAdults(adults);
        cf.setChildren(children);
        cf.setInfants(infants);

        StringBuilder sb;

        for(int i = 0; i< ffvHead.length; i++) {
            int o = data.indexOf(ffvHead[i]);
            String ffvBod = data.substring(o);
            switch(ffvHead[i]) {
                case "ffvId" :
                    break;
                case "totalBase" :
                    sb = new StringBuilder();
                    for(char a : ffvBod.toCharArray()) {
                        if(a != ',') {
                            sb.append(a);
                        }else {
                            String[] bases = sb.toString().split("=");
                            cf.setTotalBase(Double.parseDouble(bases[1]));
                            break;
                        }
                    }
                    break;
                case "totalPrice" :
                    sb = new StringBuilder();
                    for(char a : ffvBod.toCharArray()) {
                        if(a != ',') {
                            sb.append(a);
                        }else {
                            String[] prices = sb.toString().split("=");
                            cf.setTotalPrice(Double.parseDouble(prices[1]));
                            break;
                        }
                    }
                    break;
                case "apiPricings" :
                    sb = new StringBuilder();
                    CartPricing cp;

                    for(char a : ffvBod.toCharArray()) {
                        if(a != ']') {
                            sb.append(a);
                        }else {
                            break;
                        }
                    }

                    int p = sb.indexOf("APIPricing");
                    String pricings = sb.substring(p);
                    String[] pris = pricings.split("APIPricing");
                    for(String pri : pris) {
                        if(!pri.isEmpty()) {
                            if(pri.substring(pri.length()-2, pri.length()-1).equals(",")) {
                                pri = pri.substring(0, pri.length()-3);
                            }else {
                                pri = pri.substring(0, pri.length()-1);
                            }
                            cp = new CartPricing();
                            cp.setFfvId(ffvId);
                            String cpId = "cp:"+UUID.randomUUID();
                            cp.setId(cpId);
                            for(String ph : pricingHead) {
                                int u = pri.indexOf(ph);
                                StringBuilder nsb = new StringBuilder();
                                for(char z : pri.substring(u).toCharArray()) {
                                    if(z != ',') {
                                        nsb.append(z);
                                    }else {
                                        break;
                                    }
                                }
                                String fin = nsb.toString();
                                String[] fins = fin.split("=");
                                switch(ph) {
                                    case "type" :
                                        cp.setType(fins[1]);
                                        break;
                                    case "base" :
                                        cp.setBase(Double.parseDouble(fins[1]));
                                        break;
                                    case "total" :
                                        cp.setTotal(Double.parseDouble(fins[1]));
                                        break;
                                }
                            }
                            this.service.insertCartPricing(cp);
                        }
                    }
                    break;
                case "apiSegments" :
                    sb = new StringBuilder();
                    CartSegment cs;

                    for(char a : ffvBod.toCharArray()) {
                        if(a != ']') {
                            sb.append(a);
                        }else {
                            break;
                        }
                    }

                    int p2 = sb.indexOf("APISegment");
                    String segments = sb.substring(p2);
                    String[] segs = segments.split("APISegment");
                    for(String seg : segs) {
                        if(!seg.isEmpty()) {
                            if(seg.substring(seg.length()-2, seg.length()-1).equals(",")) {
                                seg = seg.substring(0, seg.length()-3);
                            }else {
                                seg = seg.substring(0, seg.length()-1);
                            }
                            cs = new CartSegment();
                            String csId = "cs:"+UUID.randomUUID();
                            cs.setFfvId(ffvId);
                            cs.setId(csId);

                            for(String sh : segmentHead) {
                                int u = seg.indexOf(sh);
                                StringBuilder nsb = new StringBuilder();

                                for(char z : seg.substring(u).toCharArray()) {
                                    if(z != ',') {
                                        nsb.append(z);
                                    }else {
                                        break;
                                    }
                                }
                                String fin = nsb.toString();
                                String[] fins = fin.split("=");
                                switch(sh) {
                                    case "depOrComb" :
                                        cs.setDepOrComb(fins[1]);
                                        break;
                                    case "departureIata" :
                                        cs.setDepartureIata(fins[1]);
                                        break;
                                    case "departureAt" :
                                        cs.setDepartureAt(fins[1]);
                                        break;
                                    case "arrivalIata" :
                                        cs.setArrivalIata(fins[1]);
                                        break;
                                    case "arrivalAt" :
                                        cs.setArrivalAt(fins[1]);
                                        break;
                                    case "duration" :
                                        cs.setDuration(fins[1]);
                                        break;
                                    case "carrierCode" :
                                        cs.setCarrierCode(fins[1]);
                                        break;
                                    case "carrierNum" :
                                        cs.setCarrierNum(fins[1]);
                                        break;
                                    case "airlineKor" :
                                        cs.setAirlineKor(fins[1]);
                                        break;
                                }
                            }
                            this.service.insertCartSegment(cs);
                        }
                    }
                    break;
                case "apiDurations" :
                    sb = new StringBuilder();
                    CartDuration cd;

                    for(char a : ffvBod.toCharArray()) {
                        if(a != ']') {
                            sb.append(a);
                        }else {
                            break;
                        }
                    }

                    int p3 = sb.indexOf("APIDuration");
                    String durations = sb.substring(p3);
                    String[] durs = durations.split("APIDuration");

                    for(String dur : durs) {
                        if(!dur.isEmpty()) {
                            if(dur.substring(dur.length()-2, dur.length()-1).equals(",")) {
                                dur = dur.substring(0, dur.length()-3);
                            }else {
                                dur = dur.substring(0, dur.length()-1);
                            }
                            cd = new CartDuration();
                            String cdId = "cd:"+ UUID.randomUUID();
                            cd.setFfvId(ffvId);
                            cd.setId(cdId);

                            for(String dh : durationHead) {
                                int u = dur.indexOf(dh);
                                StringBuilder nsb = new StringBuilder();

                                for(char z : dur.substring(u).toCharArray()) {
                                    if(z != ',') {
                                        nsb.append(z);
                                    }else {
                                        break;
                                    }
                                }

                                String fin = nsb.toString();
                                String[] fins = fin.split("=");
                                switch(dh) {
                                    case "depOrComb" :
                                        cd.setDepOrComb(fins[1]);
                                        break;
                                    case "duration" :
                                        cd.setDuration(fins[1]);
                                        break;
                                    case "airline" :
                                        cd.setAirline(fins[1]);
                                        break;
                                    case "airlineKor" :
                                        cd.setAirlineKor(fins[1]);
                                        break;
                                    case "airlineImg" :
                                        cd.setAirlineImg(fins[1]);
                                        break;
                                }
                            }
                            this.service.insertCartDuration(cd);
                        }
                    }
                    break;
            }
        }

        this.service.insertCartFlight(cf);


        return null;
    }

}
