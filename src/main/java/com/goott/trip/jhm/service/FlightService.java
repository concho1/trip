package com.goott.trip.jhm.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.goott.trip.common.service.ImageService;
import com.goott.trip.hamster.model.ShoppingCart;
import com.goott.trip.jhm.mapper.FlightMapper;

import com.goott.trip.jhm.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FlightService {

    @Autowired
    private FlightModuleService module;

    @Autowired
    private ImageService img;

    @Autowired
    private FlightMapper mapper;

    public List<APIItinerary> getSearch(String sb, Flight flight) throws ResponseException {
        int itiCount = 0;
        AtomicInteger priCount = new AtomicInteger();
        AtomicInteger segCount = new AtomicInteger();
        AtomicInteger durCount = new AtomicInteger();
        APIFlight fdto;
        List<APIItinerary> iList = new CopyOnWriteArrayList<>();

        // APIFlight
        int res = this.insertFlightCode(sb);

        if(res > 0) {
            fdto = this.findAPIFlight(sb);
        } else {
            fdto = null;
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

                final int idxI = i;
                final int idxJ = j;

                APIPricing pdto = new APIPricing();
                pdto.setItineraryCode(iCode);
                pdto.setFlightCode(fdto.getCode());
                pdto.setType(list[idxI].getTravelerPricings()[idxJ].getTravelerType());
                pdto.setBase(Double.parseDouble(list[idxI].getTravelerPricings()[idxJ].getPrice().getBase()));
                pdto.setTotal(Double.parseDouble(list[idxI].getTravelerPricings()[idxJ].getPrice().getTotal()));
                String priId = "pri" + iCode + idxJ;
                pdto.setId(priId);
                synchronized (this) {
                    this.insertAPIPricing(pdto);
                    priCount.getAndIncrement();
                }

            }
        }

        // APIDuration
        for(int i=0; i<iList.size(); i++) {
            String iCode = iList.get(i).getId();
            for(int j=0; j<list[i].getItineraries().length; j++) {

                final int idxI = i;
                final int idxJ = j;

                APIDuration ddto = new APIDuration();
                ddto.setItineraryCode(iCode);
                ddto.setFlightCode(fdto.getCode());
                if(idxJ == 0) {
                    ddto.setDepOrComb("Dep");
                }else {
                    ddto.setDepOrComb("Comb");
                }
                ddto.setDuration(list[idxI].getItineraries()[idxJ].getDuration());
                String durId = "dur" + iCode + idxJ;
                ddto.setId(durId);
                String airIATA = list[idxI].getItineraries()[idxJ].getSegments()[0].getCarrierCode();
                ddto.setAirline(airIATA);
                ddto.setAirlineKor(this.mapper.findAirlineKor(airIATA));
                String logo = this.mapper.findImgByIata(airIATA);
                String imgKey = null;
                if(logo != null){
                    imgKey = this.img.findImageByKey(logo).get().getUrl();
                }
                if(imgKey == null) {
                    imgKey = "/common/images/air.png";
                }
                ddto.setAirlineImg(imgKey);
                synchronized (this) {
                    this.insertAPIDuration(ddto);
                    durCount.getAndIncrement();
                }

                // APISegment
                for(int k=0; k<list[idxI].getItineraries()[idxJ].getSegments().length; k++) {

                    final int idxK = k;

                    APISegment sdto = new APISegment();
                    sdto.setItineraryCode(iCode);
                    sdto.setFlightCode(fdto.getCode());
                    sdto.setDepOrComb(ddto.getDepOrComb());
                    sdto.setDepartureIata(list[idxI].getItineraries()[idxJ].getSegments()[idxK].getDeparture().getIataCode());
                    sdto.setDepartureAt(list[idxI].getItineraries()[idxJ].getSegments()[idxK].getDeparture().getAt());
                    sdto.setArrivalIata(list[idxI].getItineraries()[idxJ].getSegments()[idxK].getArrival().getIataCode());
                    sdto.setArrivalAt(list[idxI].getItineraries()[idxJ].getSegments()[idxK].getArrival().getAt());
                    sdto.setDuration(list[idxI].getItineraries()[idxJ].getSegments()[idxK].getDuration());
                    sdto.setCarrierCode(list[idxI].getItineraries()[idxJ].getSegments()[idxK].getCarrierCode());
                    sdto.setCarrierNum(list[idxI].getItineraries()[idxJ].getSegments()[idxK].getNumber());
                    sdto.setAirlineKor(this.mapper.findAirlineKor(list[idxI].getItineraries()[idxJ].getSegments()[idxK].getCarrierCode()));
                    String segId = "seg" + iCode + idxJ + idxK;
                    sdto.setId(segId);
                    synchronized (this) {
                        this.insertAPISegment(sdto);
                        segCount.getAndIncrement();
                    }
                }

            }
        }

        System.out.println("총 " + itiCount + "개의 APIItinerary 저장 완료");
        System.out.println("총 " + priCount.get() + "개의 APIPricing 저장 완료");
        System.out.println("총 " + segCount.get() + "개의 APISegment 저장 완료");
        System.out.println("총 " + durCount.get() + "개의 APIDuration 저장 완료");

        this.mapper.updateUsages();

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
    public List<APISegment> findSegmentForView(String str) {
        List<APISegment> sList = this.mapper.findSegmentForView(str);

        for(APISegment sdto : sList) {
            sdto.setDepartureIata(this.findAirportByIATA(sdto.getDepartureIata()));
            sdto.setArrivalIata(this.findAirportByIATA(sdto.getArrivalIata()));
        }

        return sList;
    }
    public List<APIDuration> findDurationForView(String str) { return this.mapper.findDurationForView(str); }

    public void uploadAirport(MultipartFile file) throws IOException {this.module.uploadAirport(file);}

    public void uploadAirline(MultipartFile file) throws IOException {this.module.uploadAirline(file);}

    public String findAirportByIATA(String iata) { return this.mapper.findAirportByIATA(iata); }

    public List<Airport> findAirportByKor(String kor) { return this.mapper.findAirportByKor(kor); }
    public void insertShoppingCart(ShoppingCart cart) { this.mapper.insertShoppingCart(cart); }
    public void insertCartPricing(CartPricing cp) { this.mapper.insertCartPricing(cp); }
    public void insertCartSegment(CartSegment cs) { this.mapper.insertCartSegment(cs); }
    public String findKeyByUrl(String url) { return this.mapper.findKeyByUrl(url); }
    public void insertCartDuration(CartDuration cd) { this.mapper.insertCartDuration(cd); }
    public void insertCartFlight(CartFlight cf) { this.mapper.insertCartFlight(cf); }

}
