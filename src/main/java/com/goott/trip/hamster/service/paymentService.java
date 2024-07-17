package com.goott.trip.hamster.service;

import com.goott.trip.hamster.mapper.paymentMapper;
import com.goott.trip.hamster.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class paymentService {

    @Autowired
    private paymentMapper mapper;

    public int airplanePay(Payment payment) {
        payment.AllArrayToStr();
        payment.setStatus("ready");
        return this.mapper.airplanePay(payment);
    }

    public int hotelPay(String UUID,String memId,String firstName,String lastName,String country,String email,String paymentKey){

        return this.mapper.hotelPay(UUID,memId, firstName,lastName,country,email,paymentKey);
    }

    public int insertHotel(String UUID,String memId,String cartUUID){

        return this.mapper.insertHotel(UUID,memId,cartUUID);
    }

}
