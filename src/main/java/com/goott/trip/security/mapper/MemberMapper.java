package com.goott.trip.security.mapper;

import com.goott.trip.security.model.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface MemberMapper {
    Member findById(String id);
    void save(Member member);
    Boolean checkDupId(String id);
    Boolean checkDupPwd(String pwd);
    int changePwd(HashMap<String,String> map);

}
