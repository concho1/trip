package com.goott.trip.concho.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

@Mapper
public interface IataCodeMapper {
    void insertIataCode(@Param("iataCode") String iataCode,
                        @Param("city") String city,
                        @Param("country") String country,
                        @Param("countryKo") String countryKo);

    void insertAirplaneInfo(@Param("country") String country,
                        @Param("airplaneName") String airplaneName,
                        @Param("iataCode") String iataCode,
                        @Param("icaoCode") String icaoCode,
                        @Param("hubAirport") String hubAirport,
                        @Param("airplane") String airplane,
                        @Param("destination") String destination,
                        @Param("airlineAlliance") String airlineAlliance,
                        @Param("logo") String logo);



}