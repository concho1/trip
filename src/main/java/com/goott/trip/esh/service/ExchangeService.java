package com.goott.trip.esh.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeService {

    @Value("${exchange.api.key}")
    private String exchangeApiKey;

    public List<Map<String, Object>> getExchangeRates() {
        try {
            URL url = new URL(
                    "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON" +
                            "?authkey=" + exchangeApiKey +
                            "&data=AP01"
            );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");

            // 타임아웃 설정 (밀리초 단위)
            conn.setConnectTimeout(1800000); // 30분 연결 타임아웃
            conn.setReadTimeout(1800000); // 30분 읽기 타임아웃

            BufferedReader rd;

            // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            /*System.out.println(sb.toString());*/

            // Gson을 사용하여 JSON 문자열을 List<Map<String, Object>>으로 변환
            Gson gson = new Gson();
            /*List<Map<String, Object>> exchangeRates = gson.fromJson(sb.toString(), new TypeToken<List<Map<String, Object>>>(){}.getType());*/

            return gson.fromJson(sb.toString(), new TypeToken<List<Map<String, Object>>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //환전 기능 추가
    public double convertCurrency(String fromCurrency, String toCurrency, double amount) {
        List<Map<String, Object>> exchangeRates = getExchangeRates();
        if (exchangeRates == null) {
            throw new RuntimeException("Failed to fetch exchange rates");
        }

        double fromRate = 0.0;
        double toRate = 0.0;

        for (Map<String, Object> rate : exchangeRates) {
            if (rate.get("cur_unit").equals(fromCurrency)) {
                fromRate = Double.parseDouble(((String) rate.get("ttb")).replace(",", ""));
            }
            if (rate.get("cur_unit").equals(toCurrency)) {
                toRate = Double.parseDouble(((String) rate.get("ttb")).replace(",", ""));
            }
        }

        if (fromRate == 0.0 || toRate == 0.0) {
            throw new IllegalArgumentException("Invalid currency code");
        }

        return (amount / fromRate) * toRate;
    }
}



