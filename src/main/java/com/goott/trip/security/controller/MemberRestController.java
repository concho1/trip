package com.goott.trip.security.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.common.model.Image;
import com.goott.trip.common.service.ImageService;
import com.goott.trip.security.model.Member;
import com.goott.trip.security.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("member/con")
public class MemberRestController {
    private final ImageService imageService;
    private final MemberService memberService;

    // 이미지 갱신
    public void refreshImgUrl(HttpServletRequest request, String k){
        Optional<Image> imageOp = imageService.findImageByKey(k);
        if(imageOp.isPresent()) {
            request.getSession().setAttribute("userImgUrl", imageOp.get().getUrl());
        }else{
            System.out.println("이미지 키 없음");
        }
    }

    /*@GetMapping("test")
        public ModelAndView getTest(Principal principal){
            String memberId = principal.getName();          //getName 이 member Id 임
            System.out.println("memberId : " + memberId);
            ModelAndView modelAndView = new ModelAndView("security/member/member_page");
            modelAndView.addObject("memberId",memberId);
            return modelAndView;
        }*/
    @GetMapping("myPage")
    public ModelAndView getMyPage(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("security/member/member_page");

        if (principal != null) {
            String memberId = principal.getName(); // 로그인된 사용자의 ID getName 이 member Id 임
            System.out.println("memberId : " + memberId);
            modelAndView.addObject("memberId", memberId);
            modelAndView.addObject("logIn", true); // 로그인 상태 추가
        } else {
            modelAndView.addObject("logIn", false); // 로그인되지 않은 상태
        }

        return modelAndView;
    }
    @GetMapping("reservation")
    public ModelAndView getReservation(Principal principal, Member member) {
        /*ModelAndView modelAndView = new ModelAndView("security/member/member_reservation");*/

        /*if (principal != null) {
            String memberId = principal.getName(); // 로그인된 사용자의 ID getName 이 member Id 임
            System.out.println("memberId : " + memberId);
            modelAndView.addObject("memberId", memberId);
            modelAndView.addObject("logIn", true); // 로그인 상태 추가
        } else {
            modelAndView.addObject("logIn", false); // 로그인되지 않은 상태
        }*/
        /*String pwTm = member.getPw();
        modelAndView.addObject("id", member.getId());
        modelAndView.addObject("pw", pwTm);
        return modelAndView;*/
        String memberId = principal.getName();          //getName 이 member Id 임
        System.out.println("memberId : " + memberId);
        ModelAndView modelAndView = new ModelAndView("security/member/member_reservation");
        modelAndView.addObject("memberId",memberId);
        modelAndView.addObject("logIn", true); // 로그인 상태 추가
        return modelAndView;
    }

    /*@GetMapping("info")
    public ModelAndView getInfo(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("security/member/member_info");
        String memberId = principal.getName();          //getName 이 member Id 임
        System.out.println("memberId : " + memberId);

        String baseImgKey = "trip/4708729d-fd41-4966-bf67-8ece2b20f6bb";
        Optional<Image> imageOp = imageService.findImageByKey(baseImgKey);

        if(imageOp.isPresent()){
            modelAndView.addObject("baseImgUrl", imageOp.get().getUrl());
        }
        if (principal != null) {
            Member cont = this.memberService.getMemberById(memberId);
            modelAndView.addObject("dto", cont);
            modelAndView.addObject("memberId", memberId);
            modelAndView.addObject("logIn", true); // 로그인 상태 추가
        } else {
            modelAndView.addObject("logIn", false); // 로그인되지 않은 상태
        }
        return modelAndView;
    }
    @PostMapping("info")
    public ModelAndView postInfo(Principal principal, Model model,
                                 HttpServletRequest request,
                                 Member member, @RequestPart("file")MultipartFile file) {
        Alarm alarm = new Alarm(model);

        String memberId = principal.getName();          //getName 이 member Id 임

        System.out.println("memberId : " + memberId);
        String baseImgKey = "trip/4708729d-fd41-4966-bf67-8ece2b20f6bb";

        // 새 이미지 업로드 시도
        Optional<Image> uploadedImage = imageService.insertFile(file);

        if (uploadedImage.isPresent()) {
            // 새 이미지가 성공적으로 업로드된 경우
            member.setImgKey(uploadedImage.get().getImgKey());
        } else {// 새 이미지 업로드 실패 시
            // 기존 프로필 사진이 있는지 확인
            Member existingUser = memberService.getMemberById(memberId);
            String existingImgKey = (existingUser != null) ? existingUser.getImgKey() : null;

            if (existingImgKey != null && !existingImgKey.isEmpty()) {
                // 기존 프로필 사진이 있을 경우
                member.setImgKey(existingImgKey);
            } else {
                // 기존 프로필 사진이 없을 경우
                member.setImgKey(baseImgKey);
            }
        }

        int check = memberService.updateMem(member);
        if(check > 0){
            // 이미지 갱신 메서드 호출
            refreshImgUrl(request, member.getImgKey());
            *//*session.setAttribute("userImgKey", member.getImgKey());*//*
            alarm.setMessageAndRedirect("수정되었습니다.","info");
        }else {
            alarm.setMessageAndRedirect("수정에 실패하였습니다","");
        }

        return new ModelAndView(alarm.getMessagePage());
    }*/
    @GetMapping("info")
    public ModelAndView getInfo(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("security/member/member_info");
        String memberId = principal.getName();
        System.out.println("memberId : " + memberId);

        String baseImgKey = "trip/4708729d-fd41-4966-bf67-8ece2b20f6bb";
        imageService.findImageByKey(baseImgKey).ifPresent(image ->
                modelAndView.addObject("baseImgUrl", image.getUrl())
        );

        if (principal != null) {
            Member member = memberService.getMemberById(memberId);
            modelAndView.addObject("dto", member);
            modelAndView.addObject("memberId", memberId);
            modelAndView.addObject("logIn", true); // 로그인 상태 추가
        } else {
            modelAndView.addObject("logIn", false); // 로그인되지 않은 상태
        }

        return modelAndView;
    }

    @PostMapping("info")
    public ModelAndView postInfo(Principal principal, Model model,
                                 HttpServletRequest request,
                                 Member member, @RequestPart("file") MultipartFile file) {
        Alarm alarm = new Alarm(model);
        HttpSession session = request.getSession();
        String memberId = principal.getName();
        System.out.println("memberId : " + memberId);


        String newImgKey = handleImageUpload(memberId, file);
        member.setImgKey(newImgKey);
        member.setId(memberId);

        int check = memberService.updateMem(member);
        if (check > 0) {
            refreshImgUrl(request, member.getImgKey());
            session.setAttribute("userImgKey", member.getImgKey());
            alarm.setMessageAndRedirect("수정되었습니다.", "info");
        } else {
            alarm.setMessageAndRedirect("수정에 실패하였습니다", "");
        }

        return new ModelAndView(alarm.getMessagePage());
    }

    private String handleImageUpload(String memberId, MultipartFile file) {
        String baseImgKey = "trip/4708729d-fd41-4966-bf67-8ece2b20f6bb";
        Optional<Image> uploadedImage = imageService.insertFile(file);

        if (uploadedImage.isPresent()) {
            return uploadedImage.get().getImgKey();
        } else {
            Member existingUser = memberService.getMemberById(memberId);
            String existingImgKey = (existingUser != null) ? existingUser.getImgKey() : null;

            return (existingImgKey != null && !existingImgKey.isEmpty()) ? existingImgKey : baseImgKey;
        }
    }

    @GetMapping("vip")
    public ModelAndView getVip(Principal principal, Member member) {
        String memberId = principal.getName();          //getName 이 member Id 임
        System.out.println("memberId : " + memberId);
        ModelAndView modelAndView = new ModelAndView("security/member/member_vip");
        modelAndView.addObject("memberId",memberId);
        modelAndView.addObject("logIn", true); // 로그인 상태 추가
        return modelAndView;
    }
}
