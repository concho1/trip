package com.goott.trip.security.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.common.model.Image;
import com.goott.trip.common.service.ImageService;
import com.goott.trip.security.service.EmailService;
import com.goott.trip.security.service.MemberService;
import com.goott.trip.security.model.Member;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("user/ming")
public class UserRestController {

    private final MemberService memberService;
    private final ImageService imageService;
    private final EmailService emailService;

    // 회원가입
    @GetMapping("check")
    public ModelAndView getSingInCheck(){
        return new ModelAndView("security/user/user_signInCheck_page");
    }
    @GetMapping("sign-in")
    public ModelAndView getSignInPage(){
        ModelAndView modelAndView = new ModelAndView("security/user/user_signIn_page");

        String baseImgKey = "trip/b9bb4aa5-475e-44ce-adce-7aba575d26a6";
        Optional<Image> imageOp = imageService.findImageByKey(baseImgKey);

        if(imageOp.isPresent()){
            modelAndView.addObject("baseImgUrl", imageOp.get().getUrl());
            /*System.out.println(imageOp.get().getUrl());*/
        }
        return modelAndView;
    }
    @PostMapping("sign-in")
    public ModelAndView postSignIn(Member member, @RequestPart("file")MultipartFile file){

        Optional<Image> imageOp = imageService.insertFile(file);
        if (imageOp.isEmpty()) {
            String baseImgKey = "trip/b9bb4aa5-475e-44ce-adce-7aba575d26a6";
            member.setImgKey(baseImgKey);
        } else {
            member.setImgKey(imageOp.get().getImgKey());
        }

        String pwTm = member.getPw();
        memberService.saveMember(member);

        ModelAndView modelAndView = new ModelAndView("security/user/user_signInOk_page");
        modelAndView.addObject("id", member.getId());
        modelAndView.addObject("pw", pwTm);
        return modelAndView;
    }

    // 이메일 중복 체크
    @PostMapping("checkDupId")
    public Boolean checkDupId(@RequestParam("id") String id) {
        Boolean result = this.memberService.checkDupId(id);
        return result;
    }

    // 이메일 인증
    @PostMapping("sendEmail")
    public Map<String, String> sendEmail(@RequestParam("email") String email, HttpServletRequest httpServletRequest) throws MessagingException {
        HttpSession session = httpServletRequest.getSession();

        emailService.sendEmail(email, session);
        return new HashMap<>(Map.of("message", "이메일 전송 성공"));
    }
    // 코드 확인
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

    // 로그인
    @GetMapping("log-in")
    public ModelAndView getLogIn(){return new ModelAndView("security/user/user_login_page");}

    // 비밀번호 찾기 - 이메일 인증
    @GetMapping("searchPw")
    public ModelAndView searchPw(){return new ModelAndView("security/user/user_searchPwd"); }
    @PostMapping("sendPwdEmail")
    public Map<String, String> sendPwdEmail(@RequestParam("email") String email, HttpServletRequest httpServletRequest) throws MessagingException {
        HttpSession session = httpServletRequest.getSession();
        emailService.sendPwdEmail(email, session);
        return new HashMap<>(Map.of("message", "이메일 전송 성공"));
    }

    @PostMapping("changePwd")
    public ModelAndView changePwd(@RequestParam("id")String id){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("id",id);
        modelAndView.setViewName("security/user/user_changePwd");
        return modelAndView;
    }
    @PostMapping("changePwdOk")
    public ModelAndView changePwd(@RequestParam("id") String id,
                                  @RequestParam("pw") String pw,
                                  @RequestParam("rePwd") String rePwd,
                                  Model model) {
        Alarm alarm = new Alarm(model);

        // 새 비밀번호와 비밀번호 확인이 일치하는지 확인
        if (!pw.equals(rePwd)) {
            alarm.setMessageAndRedirect("입력한 비밀번호가 일치하지 않습니다.", "");
            return new ModelAndView(alarm.getMessagePage());
        }

        // 새 비밀번호가 기존 비밀번호와 동일한지 확인
        if (memberService.checkPwd(id, pw)) {
            alarm.setMessageAndRedirect("새 비밀번호가 기존 비밀번호와 동일합니다. 다른 비밀번호를 입력해주세요.", "/user/ming/searchPw");
            return new ModelAndView(alarm.getMessagePage());
        }

        // 비밀번호 변경 시도
        int check = memberService.changePwd(id, pw);
        if (check > 0) {
            alarm.setMessageAndRedirect("비밀번호가 변경되었습니다.", "log-in");
        } else {
            alarm.setMessageAndRedirect("비밀번호 변경 실패", "/user/ming/searchPw");
        }

        return new ModelAndView(alarm.getMessagePage());
    }

    /*// 로그아웃
    @GetMapping("log-out")
    public ModelAndView logOut(){return new ModelAndView("security/user/user_login_page");}*/
}
