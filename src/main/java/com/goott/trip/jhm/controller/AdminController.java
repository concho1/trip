package com.goott.trip.jhm.controller;

import com.goott.trip.jhm.model.Page;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

        int qnaPage;
        int memPage;

        if(request.getParameter("qnaPage") != null) {
            qnaPage = Integer.parseInt(request.getParameter("qnaPage"));
        }else {
            qnaPage = 1;
        }

        if(request.getParameter("memPage") != null) {
            memPage = Integer.parseInt(request.getParameter("memPage"));
        }else {
            memPage = 1;
        }

        // Q&A

        int qnaTotalRecord = this.qnaService.getQNACountForAdmin();

        Page qnaPdto = new Page(qnaPage, rowsize, qnaTotalRecord);

        List<Integer> qnaPagList = IntStream.rangeClosed(qnaPdto.getStartBlock(), qnaPdto.getEndBlock()).boxed().toList();

        List<QNA> qnaList = this.qnaService.getQNAListForAdmin(qnaPdto);

        // 회원관리

        int memTotalRecord = this.service.getMemberCount();

        Page memdto = new Page(memPage, rowsize, memTotalRecord);

        List<Member> members = this.service.getMembers(memdto);

        List<Integer> memPagList = IntStream.rangeClosed(memdto.getStartBlock(), memdto.getEndBlock()).boxed().toList();

        mav.addObject("qnaList", qnaList);
        mav.addObject("qnaPagList", qnaPagList);
        mav.addObject("qnaPaging", qnaPdto);
        mav.addObject("members", members);
        mav.addObject("memPagList", memPagList);
        mav.addObject("memPaging", memdto);

        return mav;
    }

}
