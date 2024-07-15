package com.goott.trip.jhm.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.jhm.model.FAQ;
import com.goott.trip.jhm.model.Page;
import com.goott.trip.jhm.model.QNA;
import com.goott.trip.jhm.service.QNAService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@RestController
@RequestMapping("customer")
public class QNAController {

    private final int rowsize =6;

    private int totalRecord = 0;

    @Autowired
    private QNAService service;

    @RequestMapping("faq")
    public ModelAndView faq(Principal principal) {

        ModelAndView mav = new ModelAndView("jhm/FAQ");

        List<FAQ> faqList;

        String div = "flight";
        String role = this.service.findRole(principal.getName());

        faqList = this.service.getFAQByDiv(div);

        mav.addObject("faqList", faqList);
        mav.addObject("role", role);

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
            String role = this.service.findRole(principal.getName());
            mav.addObject("ID", principal.getName());
            mav.addObject("Role", role);
        }

        int page;

        if(request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }else {
            page = 1;
        }

        totalRecord = this.service.getQNACount();

        Page pdto = new Page(page, rowsize, totalRecord);

        List<Integer> pagList = IntStream.rangeClosed(pdto.getStartBlock(), pdto.getEndBlock()).boxed().toList();

        List<QNA> qList = this.service.getQNAList(pdto);

        mav.addObject("qList", qList);
        mav.addObject("paging", pdto);
        mav.addObject("pagList", pagList);

        return mav;
    }

    @RequestMapping("insert_qna")
    public ModelAndView insertQNA() { return new ModelAndView("jhm/QNAWrite"); }

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

    @RequestMapping("qna_content")
    public ModelAndView qnaContent(@RequestParam("no") int num, @RequestParam("page") int page,
                                   Principal principal, Model model) {
        ModelAndView mav = new ModelAndView("jhm/QNAContent");
        Alarm alarm = new Alarm(model);

        QNA cont = this.service.getQNAContent(num);

        String id = principal.getName();
        String role = this.service.findRole(id);
        String answer = "";

        if(!id.equals(cont.getMemberId()) && !role.equals("ADMIN")) {
            alarm.setMessageAndRedirect("비정상적인 접근 방식입니다.", "");
            mav.setViewName(alarm.getMessagePage());
            return mav;
        }

        if(cont.getStatus() == 1) {
            answer = this.service.getAnswer(num);
        }

        mav.addObject("QNA", cont);
        mav.addObject("page", page);
        mav.addObject("role", role);
        mav.addObject("id", id);
        mav.addObject("answer", answer);

        return mav;
    }

    @RequestMapping("qna_modify")
    public ModelAndView qnaModify(Model model, @RequestParam("no") int no, @RequestParam("page") int page) {
        ModelAndView mav = new ModelAndView("jhm/QNAModify");
        QNA cont = this.service.getQNAContent(no);
        mav.addObject("QNA", cont);
        mav.addObject("page", page);
        return mav;
    }

    @PostMapping("modify_qna_Ok")
    public ModelAndView QNAModOk(QNA qdto, Model model, @RequestParam("page") int page) {
        ModelAndView mav = new ModelAndView();
        Alarm alarm = new Alarm(model);

        int res = this.service.modQNA(qdto);

        if(res > 0) {
            alarm.setMessageAndRedirect("질문이 수정되었습니다.", "qna_content?no="+qdto.getNum()+"&page="+page);
            mav.setViewName(alarm.getMessagePage());
        }else {
            alarm.setMessageAndRedirect("질문 수정에 실패하였습니다.", "");
            mav.setViewName(alarm.getMessagePage());
        }
        return mav;
    }

    @RequestMapping("qna_delete")
    public ModelAndView qnaDelete(Model model, @RequestParam("no") int no, @RequestParam("page") int page) {
        ModelAndView mav = new ModelAndView();
        Alarm alarm = new Alarm(model);

        int res = this.service.delQNA(no);

        if(res > 0) {
            alarm.setMessageAndRedirect("질문이 삭제되었습니다.", "qna?page="+page);
            mav.setViewName(alarm.getMessagePage());
        }else {
            alarm.setMessageAndRedirect("질문 삭제에 실패하였습니다.", "");
            mav.setViewName(alarm.getMessagePage());
        }
        return mav;
    }

    @PostMapping("qna_answer")
    public ModelAndView qnaAnswer(Model model, QNA qdto, @RequestParam("page") int page) {
        ModelAndView mav = new ModelAndView();
        Alarm alarm = new Alarm(model);

        qdto.setTitle("문의에 대한 답변입니다.");

        int res = this.service.answerQNA(qdto);

        if(res > 0) {
            this.service.changeStatus(qdto.getQnaGroup());
            alarm.setMessageAndRedirect("답변이 등록되었습니다.", "qna?page="+page);
            mav.setViewName(alarm.getMessagePage());
        }else {
            alarm.setMessageAndRedirect("답변 등록에 실패하였습니다.", "");
            mav.setViewName(alarm.getMessagePage());
        }

        return mav;
    }

}
