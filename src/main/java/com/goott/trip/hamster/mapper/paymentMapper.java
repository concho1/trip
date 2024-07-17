package com.goott.trip.hamster.mapper;

import com.goott.trip.hamster.model.Payment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface paymentMapper {

    int airplanePay(Payment payment);
    int hotelPay(String UUID,String memId,String firstName,String lastName,String country,String email,String paymentKey);
    int insertHotel(String UUID,String memId,String cartUUID);

    /*VIP*/
    List<Payment> findByMemberIdAndStatus(String memberId, String status);
    void updateTicketStatus(String airKey, String status);
    List<Payment> findByStatus(String status);
    /*예약*/
    List<Payment> findByMemberId(String memberId);
}

