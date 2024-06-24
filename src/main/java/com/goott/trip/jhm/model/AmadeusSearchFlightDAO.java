package com.goott.trip.jhm.model;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AmadeusSearchFlightDAO {

    @Value("${jhm.api.key}")
    private String key;
    @Value("${jhm.api.secret}")
    private String secret;
    private Amadeus amadeus;
    @PostConstruct
    private void init() {this.amadeus = Amadeus.builder(key, secret).build();}

    public FlightOfferSearch[] searchFlight(Flight flight) throws ResponseException {

        FlightOfferSearch[] flightOfferSearches;

        if(flight.getComeback() == ""){

            if(flight.getAirplaneClass() == ""){
                flightOfferSearches = amadeus.shopping.flightOffersSearch.get(
                        Params.with("originLocationCode", flight.getOrigin())
                                .and("destinationLocationCode", flight.getDestination())
                                .and("departureDate", flight.getDeparture())
                                .and("adults", flight.getAdults())
                                .and("children", flight.getChildren())
                                .and("infants", flight.getInfants())
                                .and("currencyCode", flight.getCurrency())
                                .and("nonStop", flight.getNonStop()));
            }else {
                flightOfferSearches = amadeus.shopping.flightOffersSearch.get(
                        Params.with("originLocationCode", flight.getOrigin())
                                .and("destinationLocationCode", flight.getDestination())
                                .and("departureDate", flight.getDeparture())
                                .and("adults", flight.getAdults())
                                .and("children", flight.getChildren())
                                .and("infants", flight.getInfants())
                                .and("travelClass", flight.getAirplaneClass())
                                .and("currencyCode", flight.getCurrency())
                                .and("nonStop", flight.getNonStop()));
            }
        }else{

            if(flight.getAirplaneClass() == ""){
                flightOfferSearches = amadeus.shopping.flightOffersSearch.get(
                        Params.with("originLocationCode", flight.getOrigin())
                                .and("destinationLocationCode", flight.getDestination())
                                .and("departureDate", flight.getDeparture())
                                .and("returnDate", flight.getComeback())
                                .and("adults", flight.getAdults())
                                .and("children", flight.getChildren())
                                .and("infants", flight.getInfants())
                                .and("currencyCode", flight.getCurrency())
                                .and("nonStop", flight.getNonStop()));
            }else {
                flightOfferSearches = amadeus.shopping.flightOffersSearch.get(
                        Params.with("originLocationCode", flight.getOrigin())
                                .and("destinationLocationCode", flight.getDestination())
                                .and("departureDate", flight.getDeparture())
                                .and("returnDate", flight.getComeback())
                                .and("adults", flight.getAdults())
                                .and("children", flight.getChildren())
                                .and("infants", flight.getInfants())
                                .and("travelClass", flight.getAirplaneClass())
                                .and("currencyCode", flight.getCurrency())
                                .and("nonStop", flight.getNonStop()));
            }

        }

        return flightOfferSearches;
    }

}
