package com.goott.trip.hamster.service;

import com.goott.trip.hamster.mapper.ConHotelCartMapper;
import com.goott.trip.hamster.model.ConHotelCart;
import com.goott.trip.hamster.model.ConHotelCartAll;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConHotelCartService {
    private final ConHotelCartMapper conHotelCartMapper;

    public Optional<ConHotelCart> findConHotelCartByMemberIdAndOfferUuid(String memberId, String offerUuid){
        return conHotelCartMapper.findConHotelCartByMemberIdAndOfferUuid(memberId, offerUuid);
    }
    public boolean saveConHotelCart(ConHotelCart conHotelCart){
        return (conHotelCartMapper.saveConHotelCart(conHotelCart) > 0);
    }
    public List<ConHotelCartAll> getConHotelCartAllByMemberId(String memberId){
        return conHotelCartMapper.getConHotelCartAllByMemberId(memberId);
    }
}
