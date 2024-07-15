package com.goott.trip.concho.model.hotel;

import com.amadeus.resources.HotelSentiment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConHotelLevel {
    private String hotelUuid;
    private String hotelAdId;
    private Integer overall;
    private Integer staff;
    private Integer location;
    private Integer service;
    private Integer comforts;
    private Integer sleep;
    private Integer pool;
    private Integer money;
    private Integer facilities;
    private Integer meal;
    private Integer nearAttraction;

  public void conSetConHotelLevelByHotelSentiment(HotelSentiment hotelSentiment) throws RuntimeException{
    this.hotelAdId = hotelSentiment.getHotelId();
    this.overall = hotelSentiment.getOverallRating();
    HotelSentiment.Sentiment sentiment = hotelSentiment.getSentiments();
    this.staff = sentiment.getStaff();
    this.location = sentiment.getLocation();
    this.service = sentiment.getService();
    this.comforts = sentiment.getRoomComforts();
    this.sleep = sentiment.getSleepQuality();
    this.pool = sentiment.getSwimmingPool();
    this.money = sentiment.getValueForMoney();
    this.facilities = sentiment.getFacilities();
    this.meal = sentiment.getCatering();
    this.nearAttraction = sentiment.getPointsOfInterest();
  }
}
