package com.goott.trip.security.controller;

import com.goott.trip.security.service.MemberService;
import com.goott.trip.security.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RestController
@RequestMapping("user/con")
public class UserRestController {
    private final MemberService memberService;
    @GetMapping("test")
    public ModelAndView getTest(){
        return new ModelAndView("security/user/user_page");
    }
    @GetMapping("sign-in")
    public ModelAndView getSignInPage(){
        return new ModelAndView("security/user/user_signin_page");
    }
    @PostMapping("sign-in")
    public ModelAndView postSignIn(Member member){
        String pwTm = member.getPw();
        memberService.saveMember(member);

        // 로그인 요청 자동
        ModelAndView modelAndView = new ModelAndView("security/user/user_auto_login");
        modelAndView.addObject("id", member.getId());
        modelAndView.addObject("pw", pwTm);
        return modelAndView;
    }
    @GetMapping("log-in")
    public ModelAndView getLogIn(){
        return new ModelAndView("security/user/user_login_page");
    }
    @GetMapping("log-out")
    public ModelAndView logOut(){
        return new ModelAndView("security/user/user_login_page");
    }
}
