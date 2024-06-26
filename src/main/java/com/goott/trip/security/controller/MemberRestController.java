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
    // 이미지
    private String handleImageUpload(String memberId, MultipartFile file) {

        String baseImgKey = "trip/f0fcf1b5-42c6-4a49-a9d1-7dadf703c35a";
        Optional<Image> uploadedImage = imageService.insertFile(file);

        if (uploadedImage.isPresent()) {
            return uploadedImage.get().getImgKey();
        } else {
            Member existingUser = memberService.getMemberById(memberId);
            String existingImgKey = (existingUser != null) ? existingUser.getImgKey() : null;

            return (existingImgKey != null && !existingImgKey.isEmpty()) ? existingImgKey : baseImgKey;
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
        String memberId = principal.getName(); // 로그인된 사용자의 ID getName 이 member Id 임
        System.out.println("memberId : " + memberId);
        modelAndView.addObject("memberId", memberId);

        return modelAndView;
    }
    @GetMapping("reservation")
    public ModelAndView getReservation(Principal principal, Member member) {
        /*ModelAndView modelAndView = new ModelAndView("security/member/member_reservation");
        setCommonAttributes(principal, modelAndView);
        return modelAndView;*/
        /*String memberId = principal.getName();          //getName 이 member Id 임
        System.out.println("memberId : " + memberId);*/
        ModelAndView modelAndView = new ModelAndView("security/member/member_reservation");
        /*modelAndView.addObject("memberId",memberId);*/
        return modelAndView;
    }

    @GetMapping("info")
    public ModelAndView getInfo(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("security/member/member_info");
        String memberId = principal.getName();
        /*System.out.println("memberId : " + memberId);*/

        String baseImgKey = "trip/f0fcf1b5-42c6-4a49-a9d1-7dadf703c35a";
        imageService.findImageByKey(baseImgKey).ifPresent(image ->
                modelAndView.addObject("baseImgUrl", image.getUrl())
        );

        if (memberId != null) {
            Member member = memberService.getMemberById(memberId);
            modelAndView.addObject("dto", member);
            modelAndView.addObject("memberId", memberId);
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

    @GetMapping("vip")
    public ModelAndView getVip(Principal principal, Member member) {
        /*ModelAndView modelAndView = new ModelAndView("security/member/member_vip");
        setCommonAttributes(principal, modelAndView);
        return modelAndView;*/
        /*String memberId = principal.getName();          //getName 이 member Id 임*/
        ModelAndView modelAndView = new ModelAndView("security/member/member_vip");/*
        modelAndView.addObject("memberId",memberId);*/
        return modelAndView;
    }


}
