package com.goott.trip.hamster.service;

import com.goott.trip.hamster.mapper.shoppingCartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class shoppingCartService {

    @Autowired
    private shoppingCartMapper mapper;

    public String checkDup(String memId){return this.mapper.checkDup(memId);}

    public int insertCart(String memId,String key){return this.mapper.insertCart(memId,key); }
}
