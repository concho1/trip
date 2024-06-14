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
                if (data.length == 3) {
                    String city = data[0].trim();
                    String country = data[1].trim();
                    String iataCode = data[2].trim();
                    iataCodeMapper.insertIataCode(iataCode, city, country);
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