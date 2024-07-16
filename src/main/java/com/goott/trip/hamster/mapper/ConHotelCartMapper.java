package com.goott.trip.hamster.mapper;

import com.goott.trip.hamster.model.ConHotelCart;
import com.goott.trip.hamster.model.ConHotelCartAll;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ConHotelCartMapper {
    int saveConHotelCart(ConHotelCart conHotelCart);
    List<ConHotelCartAll> getConHotelCartAllListByMemberId(String memberId);
    ConHotelCartAll getConHotelContListByUuid(String uuid);
    Optional<ConHotelCart> findConHotelCartByMemberIdAndOfferUuid(String memberId, String offerUuid);
}
