package com.goott.trip.hamster.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
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

    private String rideFirstNameStr;
    private String rideLastNameStr;
    private String rideBirthStr;
    private String rideCountryStr;
    private String ridePassportStr;
    private String ridePassportCountryStr;
    private String ridePassportExdateStr;

    public void AllArrayToStr(){
        rideFirstNameStr = String.join(",",this.rideFirstName);
        rideLastNameStr = String.join(",",this.rideLastName);
        rideBirthStr = String.join(",",this.rideBirth);
        rideCountryStr = String.join(",",this.rideCountry);
        ridePassportStr = String.join(",",this.ridePassport);
        ridePassportCountryStr = String.join(",",this.ridePassportCountry);
        ridePassportExdateStr = String.join(",",this.ridePassportExdate);

    }
}
