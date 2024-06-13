package com.goott.trip.security.mapper;

import com.goott.trip.security.model.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    Member findById(String id);
    void save(Member member);
    /*Boolean checkDupEmail(String email);*/

}
