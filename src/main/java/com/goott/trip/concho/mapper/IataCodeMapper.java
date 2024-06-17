package com.goott.trip.concho.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IataCodeMapper {
    void insertIataCode(@Param("iataCode") String iataCode,
                        @Param("city") String city,
                        @Param("country") String country,
                        @Param("countryKo") String countryKo);
}