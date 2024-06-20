package com.goott.trip.hamster.mapper;

import com.goott.trip.hamster.model.Testproduct;
import com.goott.trip.hamster.model.shoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface shoppingCartMapper {

    public List<shoppingCart> checkDup(String memId);
    public int insertCart(String memId,String key);
    public String[] giveShoppingItem(String memId);
}
