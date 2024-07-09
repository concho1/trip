package com.goott.trip.hamster.mapper;

import com.goott.trip.hamster.model.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface paymentMapper {

    public int airplanePay(Payment payment);
}
