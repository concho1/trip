package com.goott.trip.hamster.mapper;

import com.goott.trip.hamster.model.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface shoppingCartMapper {

    public List<ShoppingCart> checkDup(String memId);
    public int insertCart(String memId,String key);
    public int deleteHotel(String hotelKey,String memId);
    public String getAirKey(String memId);
}
