package com.goott.trip.hamster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
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

    private String status;
    private String paymentKey;

    private String rideFirstNameStr;
    private String rideLastNameStr;
    private String rideBirthStr;
    private String rideCountryStr;
    private String ridePassportStr;
    private String ridePassportCountryStr;
    private String ridePassportExdateStr;

    public void AllArrayToStr() {
        if (this.rideFirstName != null) rideFirstNameStr = String.join(",", this.rideFirstName);
        if (this.rideLastName != null) rideLastNameStr = String.join(",", this.rideLastName);
        if (this.rideBirth != null) rideBirthStr = String.join(",", this.rideBirth);
        if (this.rideCountry != null) rideCountryStr = String.join(",", this.rideCountry);
        if (this.ridePassport != null) ridePassportStr = String.join(",", this.ridePassport);
        if (this.ridePassportCountry != null) ridePassportCountryStr = String.join(",", this.ridePassportCountry);
        if (this.ridePassportExdate != null) ridePassportExdateStr = String.join(",", this.ridePassportExdate);
    }

    public void AllStrToArray() {
        if (this.rideFirstNameStr != null) this.rideFirstName = this.rideFirstNameStr.split(",");
        if (this.rideLastNameStr != null) this.rideLastName = this.rideLastNameStr.split(",");
        if (this.rideBirthStr != null) this.rideBirth = this.rideBirthStr.split(",");
        if (this.rideCountryStr != null) this.rideCountry = this.rideCountryStr.split(",");
        if (this.ridePassportStr != null) this.ridePassport = this.ridePassportStr.split(",");
        if (this.ridePassportCountryStr != null) this.ridePassportCountry = this.ridePassportCountryStr.split(",");
        if (this.ridePassportExdateStr != null) this.ridePassportExdate = this.ridePassportExdateStr.split(",");
    }
}
