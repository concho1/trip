package com.goott.trip.jhm.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.goott.trip.jhm.model.Airport;
import com.goott.trip.jhm.model.Flight;
import com.goott.trip.jhm.model.FlightMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FlightService {

    @Autowired
    private FlightModuleService module;

    @Autowired
    private FlightMapper mapper;

    public FlightOfferSearch[] searchFlight(Flight flight) throws ResponseException {
        return this.module.searchFlight(flight);
    }

    public void uploadAirport(MultipartFile file) throws IOException {this.module.uploadAirport(file);}

    public void uploadAirline(MultipartFile file) throws IOException {this.module.uploadAirline(file);}

    public String findAirportByIATA(String iata) {
        return this.mapper.findAirportByIATA(iata);
    }

}
