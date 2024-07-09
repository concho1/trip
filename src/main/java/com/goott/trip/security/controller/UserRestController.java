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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    /*@GetMapping("test")
    public ModelAndView getTest(){
        return new ModelAndView("security/user/user_page");
    }
*/
    // 회원가입
    @GetMapping("check")
    public ModelAndView getSingInCheck(){
        return new ModelAndView("security/user/user_signInCheck_page");
    }
    @GetMapping("sign-in")
    public ModelAndView getSignInPage(){
        ModelAndView modelAndView = new ModelAndView("security/user/user_signIn_page");

        String baseImgKey = "trip/f0fcf1b5-42c6-4a49-a9d1-7dadf703c35a";
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
            String baseImgKey = "trip/f0fcf1b5-42c6-4a49-a9d1-7dadf703c35a";
            member.setImgKey(baseImgKey);
        } else {
            member.setImgKey(imageOp.get().getImgKey());
        }

        String pwTm = member.getPw();
        memberService.saveMember(member);

        // 로그인 요청 자동
        /*ModelAndView modelAndView = new ModelAndView("security/user/user_auto_login");*/
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
    /*// 비밀번호 중복 체크
    @PostMapping("pwdDupPwd")
    public Boolean checkDupPwd(@RequestParam("pw") String pw) {
        Boolean result = this.memberService.checkDupPwd(pw);
        return result;
    }*/

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

    // 비밀번호 찾기
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
    /*@PostMapping("changePwdOk")
    public ModelAndView changePwdOk(@RequestParam("id")String id,
                                    @RequestParam("pw")String pwd,
                                    Model model){
        Alarm alarm = new Alarm(model);
        HashMap<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("pwd",pwd);
        int check = this.memberService.changePwd(map);

        if(check > 0){
            alarm.setMessageAndRedirect("비밀번호가 변경되었습니다","user/con/log-in");
        }else{
            alarm.setMessageAndRedirect("비밀번호가 변경 실패","");
        }

        return new ModelAndView(alarm.getMessagePage());
    }*/
    /*public String changePassword(@RequestParam("pw") String newPassword,
                                 @RequestParam("rePwd") String retypePassword,
                                 Principal principal,
                                 Model model) {
        if (!newPassword.equals(retypePassword)) {
            model.addAttribute("error", "입력한 비밀번호가 일치하지 않습니다.");
            return "redirect:/user/con/changePwd";
        }

        Authentication authentication = (Authentication) principal;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String id = userDetails.getUsername();

        boolean success = memberService.changePwd(map);

        if (!success) {
            model.addAttribute("error", "비밀번호 변경에 실패했습니다.");
            return "redirect:/user/con/changePwd";
        }

        return "redirect:user/con/log-in";
    }*/

    /*// 로그아웃
    @GetMapping("log-out")
    public ModelAndView logOut(){return new ModelAndView("security/user/user_login_page");}*/

}
