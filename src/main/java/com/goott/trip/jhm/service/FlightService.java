package com.goott.trip.jhm.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.goott.trip.jhm.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

    private int serCount = 0;

    @Autowired
    private FlightModuleService module;

    @Autowired
    private FlightMapper mapper;

    public int getSerCount() {
        return serCount;
    }

    public void resetSerCount() {
        serCount = 0;
    }

    public List<APIItinerary> getSearch(String sb, Flight flight) throws ResponseException {
        int itiCount = 0;
        int priCount = 0;
        int segCount = 0;
        int durCount = 0;
        APIFlight fdto = null;
        List<APIItinerary> iList = new ArrayList<>();

        // APIFlight
        int res = this.insertFlightCode(sb);

        if(res > 0) {
            fdto = this.findAPIFlight(sb);
        }

        FlightOfferSearch[] list = this.searchFlight(flight);

        // APIItinerary
        for(int i = 0; i < list.length; i++) {
            APIItinerary idto = new APIItinerary();
            idto.setFlightCode(fdto.getCode());
            idto.setTotalBase(Double.parseDouble(list[i].getPrice().getBase()));
            idto.setTotalPrice(Double.parseDouble(list[i].getPrice().getTotal()));
            String itiId = "iti" + fdto.getCode() + i;
            idto.setId(itiId);
            this.insertAPIItinerary(idto);
            itiCount ++;
            iList.add(idto);
        }

        // APIPricing
        for(int i=0;i<iList.size();i++) {
            String iCode = iList.get(i).getId();
            for(int j=0; j<list[i].getTravelerPricings().length; j++) {
                APIPricing pdto = new APIPricing();
                pdto.setItineraryCode(iCode);
                pdto.setFlightCode(fdto.getCode());
                pdto.setType(list[i].getTravelerPricings()[j].getTravelerType());
                pdto.setBase(Double.parseDouble(list[i].getTravelerPricings()[j].getPrice().getBase()));
                pdto.setTotal(Double.parseDouble(list[i].getTravelerPricings()[j].getPrice().getTotal()));
                String priId = "pri" + iCode + j;
                pdto.setId(priId);
                this.insertAPIPricing(pdto);
                priCount ++;
            }
        }

        // APISegment
        for(int i=0; i<iList.size(); i++) {
            String iCode = iList.get(i).getId();
            for(int j=0; j<list[i].getItineraries().length;j++) {
                for(int k=0; k<list[i].getItineraries()[j].getSegments().length; k++) {
                    APISegment sdto = new APISegment();
                    sdto.setItineraryCode(iCode);
                    sdto.setFlightCode(fdto.getCode());
                    sdto.setDepartureIata(list[i].getItineraries()[j].getSegments()[k].getDeparture().getIataCode());
                    sdto.setDepartureAt(list[i].getItineraries()[j].getSegments()[k].getDeparture().getAt());
                    sdto.setArrivalIata(list[i].getItineraries()[j].getSegments()[k].getArrival().getIataCode());
                    sdto.setArrivalAt(list[i].getItineraries()[j].getSegments()[k].getArrival().getAt());
                    sdto.setDuration(list[i].getItineraries()[j].getSegments()[k].getDuration());
                    sdto.setCarrierCode(list[i].getItineraries()[j].getSegments()[k].getCarrierCode());
                    sdto.setCarrierNum(list[i].getItineraries()[j].getSegments()[k].getNumber());
                    String segId = "seg" + iCode + j + k;
                    sdto.setId(segId);
                    this.insertAPISegment(sdto);
                    segCount ++;
                }
            }
        }

        // APIDuration
        for(int i=0; i<iList.size(); i++) {
            String iCode = iList.get(i).getId();
            for(int j=0; j<list[i].getItineraries().length; j++) {
                APIDuration ddto = new APIDuration();
                ddto.setItineraryCode(iCode);
                ddto.setFlightCode(fdto.getCode());
                if(j == 0) {
                    ddto.setDepOrComb("Dep");
                }else {
                    ddto.setDepOrComb("Comb");
                }
                ddto.setDuration(list[i].getItineraries()[j].getDuration());
                String durId = "dur" + iCode + j;
                ddto.setId(durId);
                String airIATA = list[i].getItineraries()[j].getSegments()[0].getCarrierCode();
                ddto.setAirline(airIATA);
                String airICAO = this.mapper.findIcaoByIata(airIATA);
                String logo = "https:" + this.mapper.findImgByIcao(airICAO);
                ddto.setAirlineImg(logo);
                this.insertAPIDuration(ddto);
                durCount ++;
            }
        }

        System.out.println("총 " + itiCount + "개의 APIItinerary 저장 완료");
        System.out.println("총 " + priCount + "개의 APIPricing 저장 완료");
        System.out.println("총 " + segCount + "개의 APISegment 저장 완료");
        System.out.println("총 " + durCount + "개의 APIDuration 저장 완료");

        serCount ++;

        return iList;
    }

    public void deleteAllByFlight(String str) {
        this.mapper.deleteAPIFlight(str);
        this.mapper.deleteAPIItinerary(str);
        this.mapper.deleteAPIPricing(str);
        this.mapper.deleteAPISegment(str);
        this.mapper.deleteAPIDuration(str);
    }

    public int insertFlightCode(String str) { return this.mapper.insertFlightCode(str); }

    public APIFlight findAPIFlight(String str) { return this.mapper.findAPIFlight(str); }

    public FlightOfferSearch[] searchFlight(Flight flight) throws ResponseException { return this.module.searchFlight(flight); }

    public void insertAPIItinerary(APIItinerary itinerary) { this.mapper.insertAPIItinerary(itinerary); }

    public void insertAPIPricing(APIPricing pdto) { this.mapper.insertAPIPricing(pdto); }

    public void insertAPISegment(APISegment sdto) { this.mapper.insertAPISegment(sdto); }

    public void insertAPIDuration(APIDuration ddto) { this.mapper.insertAPIDuration(ddto); }

    public List<APIItinerary> findItineraryForView(String str) { return this.mapper.findItineraryForView(str); }
    public List<APIPricing> findPricingForView(String str) { return this.mapper.findPricingForView(str); }
    public List<APISegment> findSegmentForView(String str) { return this.mapper.findSegmentForView(str); }
    public List<APIDuration> findDurationForView(String str) { return this.mapper.findDurationForView(str); }

    public void uploadAirport(MultipartFile file) throws IOException {this.module.uploadAirport(file);}

    public void uploadAirline(MultipartFile file) throws IOException {this.module.uploadAirline(file);}

    public String findAirportByIATA(String iata) { return this.mapper.findAirportByIATA(iata); }

    public List<Airport> findAirportByKor(String kor) { return this.mapper.findAirportByKor(kor); }

}
