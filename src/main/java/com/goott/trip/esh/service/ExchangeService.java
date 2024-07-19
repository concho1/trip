package com.goott.trip.esh.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.goott.trip.esh.mapper.ExchangeMapper;
import com.goott.trip.esh.model.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeService {

    @Autowired
    private ExchangeMapper mapper;

    @Value("${exchange.api.key}")
    private String exchangeApiKey;

    @Scheduled(fixedDelay = 86400000) // 24시간마다 실행
    public void updateExchangeRates() {
        getExchangeData();
    }

    private static final Logger logger = LoggerFactory.getLogger(ExchangeService.class);

    public List<Map<String, Object>> getExchangeData() {
        /*System.out.println("55555");*/
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
            conn.setConnectTimeout(10000); // 10초 연결 타임아웃
            conn.setReadTimeout(30000); // 30초 읽기 타임아웃

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
            List<Map<String, Object>> exchangeRates = gson.fromJson(sb.toString(), new TypeToken<List<Map<String, Object>>>(){}.getType());

            for(Map<String, Object> ex : exchangeRates) {
                String code = (String)ex.get("cur_unit");
                String buy = ((String)ex.get("ttb")).replace(",", "");
                String name = (String)ex.get("cur_nm");

                Exchange exchange = new Exchange();
                exchange.setCountryName(name);
                exchange.setCurrencyCode(code);
                if(code.contains("KRW")){
                    exchange.setBuyingTtRate(1.0);
                    ex.put("ttb", 1);
                }else{
                    exchange.setBuyingTtRate(Double.parseDouble(buy));
                }
                exchange.setExchangeDate(new Timestamp(System.currentTimeMillis()));

                // 중복 여부 확인
                Exchange existingExchange = mapper.selectExchangeDataByCurrencyCode(code);
                if (existingExchange == null) {
                    // 데이터가 없으면 삽입
                    mapper.insertExchangeData(exchange);
                } else {
                    // 데이터가 있으면 업데이트 (필요시)
                    mapper.updateExchangeData(exchange);
                }
            }

            return exchangeRates;
        } catch(Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    public double convertCurrency(String fromCurrency, String toCurrency, double amount) {
        Exchange fromExchange = mapper.selectExchangeDataByCurrencyCode(fromCurrency);
        Exchange toExchange = mapper.selectExchangeDataByCurrencyCode(toCurrency);

        if (fromExchange == null) {
            logger.error("환율 정보를 찾을 수 없습니다: {}", fromCurrency);
            throw new IllegalArgumentException("환율 정보를 찾을 수 없습니다: " + fromCurrency);
        }

        if (toExchange == null) {
            logger.error("환율 정보를 찾을 수 없습니다: {}", toCurrency);
            throw new IllegalArgumentException("환율 정보를 찾을 수 없습니다: " + toCurrency);
        }

        double fromRate = fromExchange.getBuyingTtRate();

        double KRW = fromRate * amount;
        double result = KRW/toExchange.getBuyingTtRate();

        return result;
    }

    public String getExchageRate(String str) { return this.mapper.getExchageRate(str); }
}



