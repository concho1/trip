package com.goott.trip.hamster.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

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

    private Timestamp orderTime;

    public Payment() {
    }


    @JsonCreator
    public Payment(@JsonProperty("memberId") String memberId,
                   @JsonProperty("orderUuid") String orderUuid,
                   @JsonProperty("airKey") String airKey,
                   @JsonProperty("hotelIdKey") String hotelIdKey,
                   @JsonProperty("hotelContKey") String hotelContKey,
                   @JsonProperty("crImgKey") String crImgKey,
                   @JsonProperty("callFirstName") String callFirstName,
                   @JsonProperty("callLastName") String callLastName,
                   @JsonProperty("callCountry") String callCountry,
                   @JsonProperty("callNumber") String callNumber,
                   @JsonProperty("callEmail") String callEmail,
                   @JsonProperty("rideFirstName") String[] rideFirstName,
                   @JsonProperty("rideLastName") String[] rideLastName,
                   @JsonProperty("rideBirth") String[] rideBirth,
                   @JsonProperty("rideCountry") String[] rideCountry,
                   @JsonProperty("ridePassport") String[] ridePassport,
                   @JsonProperty("ridePassportCountry") String[] ridePassportCountry,
                   @JsonProperty("ridePassportExdate") String[] ridePassportExdate,
                   @JsonProperty("guestFirstName") String guestFirstName,
                   @JsonProperty("guestLastName") String guestLastName,
                   @JsonProperty("guestCountry") String guestCountry,
                   @JsonProperty("orderTime") Timestamp orderTime) {
        this.memberId = memberId;
        this.orderUuid = orderUuid;
        this.airKey = airKey;
        this.hotelIdKey = hotelIdKey;
        this.hotelContKey = hotelContKey;
        this.crImgKey = crImgKey;
        this.callFirstName = callFirstName;
        this.callLastName = callLastName;
        this.callCountry = callCountry;
        this.callNumber = callNumber;
        this.callEmail = callEmail;
        this.rideFirstName = rideFirstName;
        this.rideLastName = rideLastName;
        this.rideBirth = rideBirth;
        this.rideCountry = rideCountry;
        this.ridePassport = ridePassport;
        this.ridePassportCountry = ridePassportCountry;
        this.ridePassportExdate = ridePassportExdate;
        this.guestFirstName = guestFirstName;
        this.guestLastName = guestLastName;
        this.guestCountry = guestCountry;
        this.orderTime = orderTime;
    }
}
