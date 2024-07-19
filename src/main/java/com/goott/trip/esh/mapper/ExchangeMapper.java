package com.goott.trip.esh.mapper;

import com.goott.trip.esh.model.Exchange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExchangeMapper {
    void insertExchangeData(Exchange exchange);
    Exchange selectExchangeDataByCurrencyCode(@Param("currencyCode") String currencyCode);
    void updateExchangeData(Exchange exchange);
    String getExchageRate(String str);

}
