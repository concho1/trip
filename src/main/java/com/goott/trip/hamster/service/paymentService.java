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

    public boolean updatePaymentStatus(String orderUuid) {
        // 예약 날짜가 지나면 completed로 상태를 변경
        return this.mapper.updatePaymentStatus(orderUuid, "completed") > 0;
    }

    public int countCompletedPayments(String memberId) {
        // 회원의 2년간 completed 건수 조회
        return this.mapper.countCompletedPayments(memberId);
    }

}
