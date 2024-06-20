package com.goott.trip.concho.mapper;

import com.goott.trip.concho.model.IataCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface IataCodeMapper {
    void insertIataCode(@Param("iataCode") String iataCode,
                        @Param("city") String city,
                        @Param("cityKo") String cityKo,
                        @Param("country") String country,
                        @Param("countryKo") String countryKo);

    Optional<IataCode> findCityByIataCode(@Param("iataCode") String iataCode);
}