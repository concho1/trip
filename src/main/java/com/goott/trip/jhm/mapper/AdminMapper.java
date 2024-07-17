package com.goott.trip.jhm.mapper;

import com.goott.trip.jhm.model.PeopleCnt;
import com.goott.trip.security.model.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {
    void countBot(String str);
    void countGuest(String str);
    void countUser(String str);
    int getMemberCount();
    List<Member> getMembers();
    List<PeopleCnt> getPeopleCnt(int n);
    int countHotelAPI(Map<String, String> map);
    int countFlightAPI(String month);
    int deleteUser(String id);
}
