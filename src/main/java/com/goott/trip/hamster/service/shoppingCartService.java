package com.goott.trip.hamster.service;

import com.goott.trip.hamster.mapper.shoppingCartMapper;
import com.goott.trip.hamster.model.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class shoppingCartService {

    @Autowired
    private shoppingCartMapper mapper;

    public List<String> shoppingCartAirplane(String memId){return this.mapper.shoppingCartAirplane(memId);}

    public List<ShoppingCart> checkDup(String memId){return this.mapper.checkDup(memId);}

    public int insertCart(String memId,String key){return this.mapper.insertCart(memId,key); }

    public int deleteHotel(String uuid,String memId){return this.mapper.deleteHotel(uuid,memId);}

    public int deleteAir(String airKey,String memId){return this.mapper.deleteAir(airKey,memId); }

    public String getAirKey(String memId){return this.mapper.getAirKey(memId); }
}
