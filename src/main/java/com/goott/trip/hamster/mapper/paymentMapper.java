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
    void updateTicketStatus(String airKey, String status);
    void updateTicketStatusByOrderUuid(String orderUuid, String status);
    List<Payment> findAllPayments();
    List<Payment> findByMemberId(String memberId);

    /*예약*/
    List<String> getPaymentAirKey(String memberId);
    List<Payment> payAir(String memberId);
    List<Payment> payHotel(String memberId);
    List<Payment> findByAirKey(String AirKey);
    List<Payment> findByAirKeyHam(String AirKey);
}

