package com.goott.trip.hamster.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface shoppingCartMapper {

    public String checkDup(String memId);
    public int insertCart(String memId,String key);
}
