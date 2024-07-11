package com.goott.trip.hamster.mapper;

import com.goott.trip.hamster.model.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface paymentMapper {

    int airplanePay(Payment payment);

    int updatePaymentStatus(String orderUuid, String status);

    int countCompletedPayments(String memberId);

}
