package com.goott.trip.concho.service.main;

import com.goott.trip.concho.mapper.IataCodeMapper;
import com.goott.trip.concho.model.ConchoHotel;
import com.goott.trip.concho.model.IataCode;
import lombok.RequiredArgsConstructor;
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

    public boolean uploadCsv(MultipartFile file){
        boolean result = false;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    String city = data[0].trim();
                    String country = data[1].trim();
                    String iataCode = data[2].trim();
                    String countryKo = data[3].trim();
                    String cityKo = data[4].trim();
                    iataCodeMapper.insertIataCode(iataCode, city, cityKo, country, countryKo);
                }
            }
            result = true;
        }catch (Exception e){
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
}