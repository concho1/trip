package com.goott.trip.concho.model.hotel;

import com.amadeus.resources.HotelOfferSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConHotelOffer {
  private String uuid;
  private Integer searchNum;
  private String hotelAdId; //
  private String offerId; //
  private String hotelType; //
  private Boolean available; //
  private Date checkIn; //
  private Date checkOut;  //
  private String category; //
  private Integer bedCnt; //
  private String bedCategory; //
  private String roomComment; //
  private Integer adultCnt; //
  private String currency; //
  private double totalCost; //
  private Double perNightCost; //
  public void conSetConOfferByHotelOfferSearch(HotelOfferSearch adOfferSearch) throws RuntimeException{
    HotelOfferSearch.Hotel hotel = adOfferSearch.getHotel();
    this.hotelAdId = hotel.getHotelId();
    this.hotelType = hotel.getType();
    this.available = adOfferSearch.isAvailable();
  }
  public void conSetConOfferByAdOffer(HotelOfferSearch.Offer offer) throws RuntimeException{
    this.uuid = UUID.randomUUID().toString();
    this.offerId = offer.getId();
    this.checkIn = Date.valueOf(offer.getCheckInDate());
    this.checkOut = Date.valueOf(offer.getCheckOutDate());
    this.category = offer.getRoom().getTypeEstimated().getCategory();
    this.bedCnt = offer.getRoom().getTypeEstimated().getBeds();
    this.bedCategory = offer.getRoom().getTypeEstimated().getBedType();
    // comment null 허용 Optional, 메서드 참조 이용
    this.roomComment = Optional.ofNullable(
            offer.getRoom())
            .map(HotelOfferSearch.RoomDetails::getDescription)
            .map(HotelOfferSearch.QualifiedFreeText::getText).orElse(null);
    this.adultCnt = offer.getGuests().getAdults();
    this.currency = offer.getPrice().getCurrency();
    this.totalCost = Double.parseDouble(offer.getPrice().getTotal());
    // 원래 람다식으로 썼는데 인텔리제이에서 메서드 참조로 바꾸라해서 바꿈
    this.perNightCost = Optional.ofNullable(offer.getPrice())
            .map(HotelOfferSearch.HotelPrice::getVariations)
            .map(HotelOfferSearch.PriceVariations::getAverage)
            .map(HotelOfferSearch.Price::getBase)
            .map(Double::valueOf)
            .orElse(null);
  }
}
