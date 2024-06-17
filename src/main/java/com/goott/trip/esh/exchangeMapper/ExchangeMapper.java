package com.goott.trip.esh.exchangeMapper;

import com.goott.trip.esh.exchangeModel.Exchange;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExchangeMapper {
    void insertExchange(Exchange exchange);
    Exchange getExchangeByCountryName(String countryName);
    List<Exchange> getAllExchanges();
}
