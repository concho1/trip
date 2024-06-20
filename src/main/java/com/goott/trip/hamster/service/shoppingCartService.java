package com.goott.trip.hamster.service;

import com.goott.trip.hamster.mapper.shoppingCartMapper;
import com.goott.trip.hamster.model.Testproduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class shoppingCartService {

    @Autowired
    private shoppingCartMapper mapper;

    public List<Testproduct> checkDup(String memId){return this.mapper.checkDup(memId);}

    public int insertCart(String memId,String key){return this.mapper.insertCart(memId,key); }

    public String[] giveShoppingItem(String memId){return this.mapper.giveShoppingItem(memId); }
}
