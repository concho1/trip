package com.goott.trip.security.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.common.model.Image;
import com.goott.trip.common.service.ImageService;
import com.goott.trip.security.service.MemberService;
import com.goott.trip.security.model.Member;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("user/con")
public class UserRestController {

    private final MemberService memberService;
    private final ImageService imageService;
    private final ServletRequest httpServletRequest;

    @GetMapping("test")
    public ModelAndView getTest(){
        return new ModelAndView("security/user/user_page");
    }

    @GetMapping("sign-in")
    public ModelAndView getSignInPage(){
        ModelAndView modelAndView = new ModelAndView("security/user/user_signIn_page");

        String baseImgKey = "trip/2890e995-803d-4feb-91f3-046b49a883c6";
        Optional<Image> imageOp = imageService.findImageByKey(baseImgKey);

        if(imageOp.isPresent()){
            modelAndView.addObject("baseImgUrl", imageOp.get().getUrl());
        }
        return modelAndView;
    }
    @PostMapping("sign-in")
    public ModelAndView postSignIn(Member member, @RequestPart("file")MultipartFile file){

        Optional<Image> imageOp = imageService.insertFile(file);
        if (imageOp.isEmpty()) {
            String baseImgKey = "trip/2890e995-803d-4feb-91f3-046b49a883c6";
            member.setImgKey(baseImgKey);
        } else {
            member.setImgKey(imageOp.get().getImgKey());
        }

        String pwTm = member.getPw();
        memberService.saveMember(member);

        // 로그인 요청 자동
        ModelAndView modelAndView = new ModelAndView("security/user/user_auto_login");
        modelAndView.addObject("id", member.getId());
        modelAndView.addObject("pw", pwTm);
        return modelAndView;
    }

    /*@PostMapping("/sendEmail")
    public Map<String, String> sendEmail(@RequestParam("email") String email, HttpServletRequest request){

    }*/

    @GetMapping("log-in")
    public ModelAndView getLogIn(){
        return new ModelAndView("security/user/user_login_page");
    }
    @GetMapping("log-out")
    public ModelAndView logOut(){
        return new ModelAndView("security/user/user_login_page");
    }

}
