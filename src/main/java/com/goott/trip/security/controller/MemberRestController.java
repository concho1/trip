package com.goott.trip.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("member/con")
public class MemberRestController {
    @GetMapping("test")
    public ModelAndView getTest(Principal principal){
        String memberId = principal.getName();          //getName 이 member Id 임
        System.out.println("memberId : " + memberId);
        ModelAndView modelAndView = new ModelAndView("security/member/member_page");
        modelAndView.addObject("memberId",memberId);
        return modelAndView;
    }

}
