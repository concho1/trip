package com.goott.trip.hamster.service;

import com.goott.trip.hamster.mapper.paymentMapper;
import com.goott.trip.hamster.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class paymentService {

    @Autowired
    private paymentMapper mapper;

    public int airplanePay(Payment payment) {payment.AllArrayToStr(); return this.mapper.airplanePay(payment); }
}
