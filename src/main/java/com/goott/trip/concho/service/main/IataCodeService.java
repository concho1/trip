package com.goott.trip.concho.service.main;

import com.goott.trip.concho.mapper.IataCodeMapper;
import com.goott.trip.concho.model.ConchoHotel;
import com.goott.trip.concho.model.IataCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IataCodeService {
    private final IataCodeMapper iataCodeMapper;

    public List<IataCode> findIataCodeBySearchStr(String searchStr){
        return iataCodeMapper.findIataCodeBySearchStr(searchStr);
    }

    public boolean uploadCsv(MultipartFile file) {
        boolean result = false;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build();
            CSVParser csvParser = new CSVParser(reader, format);

            for (CSVRecord record : csvParser) {
                String city = record.get(0).trim();
                String country = record.get(1).trim();
                String iataCode = record.get(2).trim();
                String countryKo = record.get(3).trim();
                String cityKo = record.get(4).trim();
                Integer destId = null;
                try {
                    if(record.get(5) != null && !record.get(5).isEmpty()){
                        destId = Integer.valueOf(record.get(5).trim());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("숫자가 아님 : " + record.get(5).trim());
                }

                iataCodeMapper.insertIataCode(iataCode, city, cityKo, country, countryKo, destId);
            }

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    // iata 코드로 국가명, 도시명 갱신
    public void updateHotelListToKoByIataCode(List<ConchoHotel> hotelList){
        var iataMap = new HashMap<String, IataCode>();
        for(ConchoHotel hotel : hotelList){
            if(iataMap.containsKey(hotel.getIataCode())){
                IataCode iataCode = iataMap.get(hotel.getIataCode());
                hotel.setCountryCode(iataCode.getCountryKo());
                hotel.setIataCode(iataCode.getCityKo());
            }else{
                Optional<IataCode> iataCodeOptional = iataCodeMapper.findCityByIataCode(hotel.getIataCode());
                if(iataCodeOptional.isPresent()){
                    IataCode iataCode = iataCodeOptional.get();
                    iataMap.put(iataCode.getIataCode(), iataCode);
                    hotel.setCountryCode(iataCode.getCountryKo());
                    hotel.setIataCode(iataCode.getCityKo());
                }
            }
        }
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