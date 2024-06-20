package com.goott.trip.esh.exchangeMapper;

import com.goott.trip.esh.exchangeModel.Exchange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeMapper {
    void insertExchangeData(Exchange exchange);
    Exchange selectExchangeDataByCurrencyCode(@Param("currencyCode") String currencyCode);

    void updateExchangeData(Exchange exchange);

}
