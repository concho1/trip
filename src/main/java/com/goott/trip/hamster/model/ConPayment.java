package com.goott.trip.hamster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConPayment {
    private String orderUuid;
    private String memberId;
    private String airKey;
    private String hotelIdKey;
    private String hotelContKey;
    private String crImgKey;
    private String callFirstName;
    private String callLastName;
    private String callCountry;
    private String callEmail;
    private String callNumber;
    private String rideFirstName;
    private String rideLastName;
    private String rideBirth;
    private String rideCountry;
    private String ridePassport;
    private String ridePassportCountry;
    private String ridePassportExdate;
    private String guestFirstName;
    private String guestLastName;
    private String guestCountry;
    private java.sql.Timestamp orderTime;
    private String status;
    private String paymentKey;
}
