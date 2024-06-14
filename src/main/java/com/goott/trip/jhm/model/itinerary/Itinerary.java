package com.goott.trip.jhm.model.itinerary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Itinerary {
    private String duration;
    private List<Segment> segments;
}
