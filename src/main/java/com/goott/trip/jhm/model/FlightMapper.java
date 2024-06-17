package com.goott.trip.jhm.model;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface FlightMapper {

    public void uploadAirport(List<Airport> list);
    public void uploadAirline(List<AirLine> list);
    public String findAirportByIATA(String iata);
}
