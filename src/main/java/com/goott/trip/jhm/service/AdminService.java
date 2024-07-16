package com.goott.trip.jhm.service;

import com.goott.trip.jhm.mapper.AdminMapper;
import com.goott.trip.jhm.model.PeopleCnt;
import com.goott.trip.security.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private AdminMapper mapper;

    public void countBot(String today) { this.mapper.countBot(today); }
    public void countGuest(String today) { this.mapper.countGuest(today); }
    public void countUser(String str) { this.mapper.countUser(str); }
    public int getMemberCount() { return this.mapper.getMemberCount(); }
    public List<Member> getMembers() { return this.mapper.getMembers(); }
    public List<PeopleCnt> getPeopleCnt(int n) { return this.mapper.getPeopleCnt(n); }
    public int countHotelAPI(Map<String, String> map) { return this.mapper.countHotelAPI(map); }
    public int countFlightAPI(String month) { return this.mapper.countFlightAPI(month); }

}
