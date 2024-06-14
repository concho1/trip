package com.goott.trip.jhm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goott.trip.jhm.model.Response;
import com.goott.trip.jhm.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
public class FlightController {

    @Autowired
    private FlightService service;

    @RequestMapping("/flight")
    public ModelAndView flight() { return new ModelAndView("jhm/test"); }

    @RequestMapping(value = "test", method = RequestMethod.POST, produces = "application/vnd.amadeus+json; charset=utf-8")
    public void test(@RequestParam("token") String token) throws IOException, URISyntaxException, InterruptedException {
        String strUrl = "https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=SYD&destinationLocationCode=BKK&departureDate=2024-07-25&adults=1&nonStop=false&max=1";
        URI uri = new URI(strUrl);

        System.out.println("access_token : "+token);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).header("Authorization", "Bearer " + token).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(ResponseEntity.ok().body(response.body()));

        ObjectMapper mapper = new ObjectMapper();

        Response responsedata = mapper.readValue(response.body(), Response.class);

        System.out.println(responsedata);


    }

}
