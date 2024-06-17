package com.goott.trip.security.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.common.model.Image;
import com.goott.trip.common.service.ImageService;
import com.goott.trip.security.service.EmailService;
import com.goott.trip.security.service.MemberService;
import com.goott.trip.security.model.Member;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final EmailService emailService;

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

    @PostMapping("sendEmail")
    public Map<String, String> sendEmail(@RequestParam("email") String email, HttpServletRequest httpServletRequest) throws MessagingException {
        HttpSession session = httpServletRequest.getSession();

        emailService.sendEmail(email, session);
        return new HashMap<>(Map.of("message", "이메일 전송 성공"));
    }

    @PostMapping("codeCatch")
    public Map<String, String> codeCatcher(@RequestParam("code") String inCode, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        System.out.println("사용자가 입력한 Code: " + inCode);

        if (inCode.equals(session.getAttribute("sCode"))) {
            return new HashMap<>(Map.of("good", "이메일 인증이 완료되었습니다."));
        } else {
            return new HashMap<>(Map.of("fail", "인증코드가 일치하지않습니다."));
        }

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
