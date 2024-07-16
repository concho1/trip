package com.goott.trip.jhm.controller;

import com.goott.trip.concho.model.api.ConApiUsage;
import com.goott.trip.jhm.model.Page;
import com.goott.trip.jhm.model.PeopleCnt;
import com.goott.trip.jhm.model.QNA;
import com.goott.trip.jhm.service.AdminService;
import com.goott.trip.jhm.service.QNAService;
import com.goott.trip.security.model.Member;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@RestController
@RequestMapping("admin")
public class AdminController {

    private final int rowsize = 5;

    @Autowired
    private AdminService service;

    @Autowired
    private QNAService qnaService;

    @RequestMapping("dashboard")
    public ModelAndView adminMain(HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("jhm/admin_main");

        // 접속자
        int count = 9;
        List<PeopleCnt> peopleCntList = this.service.getPeopleCnt(count);

        // API 이용률
        LocalDate today = LocalDate.now();
        String[] hotel_category = {"HOTEL_LIST_IATA", "HOTEL_LIST_GPS", "HOTEL_OFFER", "HOTEL_LEVEL"};
        Map<String, Integer> usages = new HashMap<>();

        String year_month = today.toString().substring(0, 7);

        for(int i=0; i<hotel_category.length; i++) {
            Map<String, String> serMap = new HashMap<>();
            serMap.put("category", hotel_category[i]);
            serMap.put("month", year_month);
            int cnt = this.service.countHotelAPI(serMap);
            if(cnt == 0) {
                cnt = 1;
            }
            usages.put(hotel_category[i], cnt);
        }

        int fCnt = this.service.countFlightAPI(year_month);

        usages.put("FLIGHT", fCnt);

        System.out.println(usages);


        // Q&A
        List<QNA> qnaList = this.qnaService.getQNAListForAdmin();

        // 회원관리
        List<Member> members = this.service.getMembers();

        mav.addObject("people", peopleCntList);
        mav.addObject("APIUsage", usages);
        mav.addObject("qnaList", qnaList);
        mav.addObject("members", members);

        return mav;
    }
}
