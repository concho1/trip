package com.goott.trip.concho.service.main;

import com.goott.trip.concho.mapper.IataCodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class IataCodeService {
    private final IataCodeMapper iataCodeMapper;

    public boolean uploadCsv(MultipartFile file){
        boolean result = false;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String city = data[0].trim();
                    String country = data[1].trim();
                    String iataCode = data[2].trim();
                    String countryKo = data[3].trim();
                    iataCodeMapper.insertIataCode(iataCode, city, country, countryKo);
                }
            }
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean uploadAirInfoCsv(MultipartFile file){
        boolean result = false;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 9) {
                    String country = data[0].trim();
                    String airplaneName = data[1].trim();
                    String iataCode = data[2].trim();
                    String icaoCode = data[3].trim();
                    String hubAirport = data[4].trim();
                    String airplane = data[5].trim();
                    String destination = data[6].trim();
                    String airlineAlliance = data[7].trim();
                    String logo = data[8].trim();

                    System.out.println(airplaneName);
                    iataCodeMapper.insertAirplaneInfo(country, airplaneName, iataCode, icaoCode, hubAirport,
                            airplane, destination, airlineAlliance, logo);
                }
            }
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}