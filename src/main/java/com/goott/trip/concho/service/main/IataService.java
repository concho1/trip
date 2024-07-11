package com.goott.trip.concho.service.main;

import com.goott.trip.concho.mapper.ConHotelMapper;
import com.goott.trip.concho.model.IataCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IataService {
    private final ConHotelMapper conHotelMapper;
    public List<IataCode> findIataCodeBySearchStr(String searchStr){
        return conHotelMapper.findIataCodeBySearchStr(searchStr);
    }
}
