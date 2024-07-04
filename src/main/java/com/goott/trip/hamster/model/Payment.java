package com.goott.trip.hamster.model;

import lombok.Data;

@Data
public class Payment {
    private String memberId;
    private String orderUuid;

    private String airKey;
    private String hotelIdKey;
    private String hotelContKey;
    private String crImgKey;

    private String callFirstName;
    private String callLastName;
    private String callCountry;
    private String callNumber;
    private String callEmail;

    private String[] rideFirstName;
    private String[] rideLastName;
    private String[] rideBirth;
    private String[] rideCountry;
    private String[] ridePassport;
    private String[] ridePassportCountry;
    private String[] ridePassportExdate;

    private String guestFirstName;
    private String guestLastName;
    private String guestCountry;

}
