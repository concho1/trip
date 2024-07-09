package com.goott.trip.hamster.service;

import com.goott.trip.hamster.mapper.paymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class paymentService {

    @Autowired
    private paymentMapper mapper;

    /*public int insertPayment(String memberId, payment payment){return this.mapper.insertPayment(memberId,payment); }*/
}
