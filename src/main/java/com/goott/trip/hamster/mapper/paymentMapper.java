package com.goott.trip.hamster.mapper;

import com.goott.trip.hamster.model.Payment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface paymentMapper {

    int airplanePay(Payment payment);
    List<Payment> findByMemberIdAndStatus(String memberId, String status);
    void updateTicketStatus(String airKey, String status);

}
