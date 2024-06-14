package com.goott.trip.hamster.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class TossPayService {

    private final RestTemplate restTemplate;

    @Value("${toss.secretKey}")
    private String secretKey;

    public TossPayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getEncodedSecretKey() {
        return "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
    }

    public String confirmPayment(String paymentKey, String orderId, int amount) {
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getEncodedSecretKey());
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("paymentKey", paymentKey);
        requestBody.put("orderId", orderId);
        requestBody.put("amount", amount);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return response.getBody();
    }
}
