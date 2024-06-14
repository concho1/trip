package com.goott.trip.jhm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.goott.trip.jhm.model.itinerary.Itinerary;
import com.goott.trip.jhm.model.price.Price;
import com.goott.trip.jhm.model.travlerPricing.TravlerPricing;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private String numberOfBookableSeats;
    private List<Itinerary> itineraries;
    private Price price;
    private PricingOptions pricingOptions;
    private String[] validatingAirlineCodes;
    private List<TravlerPricing> travlerPricings;
}
