package com.goott.trip.jhm.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.jhm.model.FAQ;
import com.goott.trip.jhm.model.Page;
import com.goott.trip.jhm.model.QNA;
import com.goott.trip.jhm.service.QNAService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("customer")
public class QNAConroller {

    private final int rowsize = 10;

    private int totalRecord = 0;

    @Autowired
    private QNAService service;

    @RequestMapping("faq")
    public ModelAndView faq() {

        ModelAndView mav = new ModelAndView("jhm/FAQ");

        List<FAQ> faqList;

        String div = "flight";

        faqList = this.service.getFAQByDiv(div);

        mav.addObject("faqList", faqList);

        return mav;
    }

    @PostMapping("insertFaq")
    public ModelAndView insertFaq(@RequestParam("title") String title, @RequestParam("cont") String cont,
                          @RequestParam("div") String div, Model model) {

        ModelAndView mav = new ModelAndView();

        FAQ fdto = new FAQ();
        Alarm alarm = new Alarm(model);

        fdto.setFaqTitle(title);
        fdto.setFaqCont(cont);
        fdto.setFaqDiv(div);

        int res = this.service.insertFAQ(fdto);

        if(res > 0) {
            alarm.setMessageAndRedirect("FAQ가 추가되었습니다.", "faq");
            mav.setViewName(alarm.getMessagePage());
        }else {
            alarm.setMessageAndRedirect("FAQ 등록에 실패하였습니다.", "");
            mav.setViewName(alarm.getMessagePage());
        }
        return mav;
    }

    @PostMapping("showFAQByDiv")
    public List<Map<String, String>> showFAQByDiv(@RequestParam("div") String div) {

        List<Map<String, String>> mapList = new ArrayList<>();

        List<FAQ> faqList = this.service.getFAQByDiv(div);

        for(FAQ faq : faqList) {
            Map<String, String> map = new HashMap<>();
            map.put("title", faq.getFaqTitle());
            map.put("cont", faq.getFaqCont());
            mapList.add(map);
        }

        return mapList;
    }

    @RequestMapping("qna")
    public ModelAndView qna(Principal principal, Model model, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("jhm/QNA");
        Alarm alarm = new Alarm(model);

        if(principal == null) {
            alarm.setMessageAndRedirect("회원가입 후 이용하실 수 있습니다.", "/user/ming/log-in");
            mav.setViewName(alarm.getMessagePage());
        }else {
            mav.addObject("ID", principal.getName());
        }

        int page;

        if(request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }else {
            page = 1;
        }

        totalRecord = this.service.getQNACount();

        Page pdto = new Page(page, rowsize, totalRecord);

        List<QNA> qList = this.service.getQNAList(pdto);

        mav.addObject("qList", qList);
        mav.addObject("paging", pdto);

        return mav;
    }

    @RequestMapping("insert_qna")
    public ModelAndView insertQNA() { return new ModelAndView("jhm/QNA_write"); }

    @PostMapping("insert_qna_Ok")
    public ModelAndView insertQNAOk(Model model, Principal principal, QNA qdto) {

        ModelAndView mav = new ModelAndView();
        Alarm alarm = new Alarm(model);

        qdto.setMemberId(principal.getName());
        int res = this.service.insertQNA(qdto);

        if(res > 0) {
            alarm.setMessageAndRedirect("질문이 등록되었습니다.", "qna");
            mav.setViewName(alarm.getMessagePage());
        }else {
            alarm.setMessageAndRedirect("질문 등록에 실패했습니다.", "");
            mav.setViewName(alarm.getMessagePage());
        }
        return mav;
    }

}
