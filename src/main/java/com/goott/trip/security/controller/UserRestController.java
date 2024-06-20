package com.goott.trip.security.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.goott.trip.common.model.Alarm;
import com.goott.trip.common.model.Image;
import com.goott.trip.common.service.ImageService;
import com.goott.trip.security.service.EmailService;
import com.goott.trip.security.service.KakaoApi;
import com.goott.trip.security.service.MemberService;
import com.goott.trip.security.model.Member;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
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
    private final KakaoApi kakaoApi;

    @GetMapping("test")
    public ModelAndView getTest(){
        return new ModelAndView("security/user/user_page");
    }

    // 회원가입
    @GetMapping("check")
    public ModelAndView getSingInCheck(){
        return new ModelAndView("security/user/user_signInCheck_page");
    }
    @GetMapping("sign-in")
    public ModelAndView getSignInPage(){
        ModelAndView modelAndView = new ModelAndView("security/user/user_signIn_page");

        String baseImgKey = "trip/4708729d-fd41-4966-bf67-8ece2b20f6bb";
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
            String baseImgKey = "trip/4708729d-fd41-4966-bf67-8ece2b20f6bb";
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

    @GetMapping("kakao")
    public RedirectView kakao(HttpServletRequest request){
        String fullURL = request.getRequestURL().toString();
        String extractedURL = fullURL.substring(0, fullURL.indexOf("/login"));
        System.out.println(extractedURL);

        String kakaoApiKey = kakaoApi.getKakaoApiKey();
        String redirectUri = extractedURL+kakaoApi.getKakaoRedirectUri();
        String authUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + kakaoApiKey + "&redirect_uri=" + redirectUri + "&response_type=code";

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(authUrl);
        return redirectView;
    }
    @RequestMapping("oauth2/kakao")
    public ModelAndView KaKaoLogin(@RequestParam(value = "code", required = false) String code, Principal principal,
                                   HttpServletRequest request, Model model) throws JsonProcessingException, MalformedURLException {

        Alarm alarm = new Alarm(model);
        if(code == null){
            alarm.setMessageAndRedirect("동의항목에 동의를 하셔야 카카오 로그인이 가능합니다.","user/con/log-in");
            return new ModelAndView(alarm.getMessagePage());
        }
        String fullURL = request.getRequestURL().toString();
        String baseURL = fullURL.substring(0, fullURL.indexOf("user/con/log-in"));
        System.out.println(baseURL);

        System.out.println("코드 : " + code);
        String Tok = this.kakaoApi.getAccessToken(code, baseURL);
        System.out.println("토큰 : " + Tok);
        Member dto = this.kakaoApi.getKakaoInfo(Tok);

        System.out.println(dto.getId());
        System.out.println(dto.getImgKey());

        // 이미 DB에 있는 이메일인지 확인
        // 이미 있으면
        // DB 에서 해당하는 이메일의 데이터를 꺼내와서 세션에 넣어주고 페이지 반환
        Boolean check = this.memberService.checkDupId(dto.getId());
        if (check) {
            // 기존에 저장되있는 정보 가져오기
            Member memberInstance = this.memberService.getMemberById(dto.getId());
            // 인증된 사용자 이름 설정
            model.addAttribute("memberId", memberInstance.getId());
            model.addAttribute("memberImgKey", memberInstance.getImgKey());
            model.addAttribute("memberImgUrl", imageService.findImageByKey(memberInstance.getImgKey()).get().getUrl());
            /*this.memberService.loginCnt();*/
            alarm.setMessageAndRedirect("","security/memeber/con/test");
            return new ModelAndView(alarm.getMessagePage());
        }

        MultipartFile multipartFile = null;
        URL url = new URL(dto.getImgKey());
        try (InputStream in = url.openStream()) {
            byte[] imageBytes = IOUtils.toByteArray(in);

            // MultipartFile로 변환
            multipartFile = new MockMultipartFile("file", "image.jpg", MediaType.IMAGE_JPEG_VALUE, imageBytes);

            String baseImgKey = "trip/4708729d-fd41-4966-bf67-8ece2b20f6bb";
            Optional<Image> imageOp = imageService.insertFile(multipartFile);
            if (imageOp.isPresent()) {
                dto.setImgKey(imageOp.get().getImgKey());
            } else {
                dto.setImgKey(baseImgKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // db 저장
        this.memberService.kakao(dto);

        // 인증된 사용자 정보 설정
        String memberId = principal.getName();
        model.addAttribute("memberId", dto.getId());
        model.addAttribute("memberImgKey", dto.getImgKey());
        model.addAttribute("memberImgUrl", imageService.findImageByKey(dto.getImgKey()).get().getUrl());

        /*this.memberService.joinCnt();*/
        alarm.setMessageAndRedirect("","security/memeber/con/test");
        return new ModelAndView(alarm.getMessagePage());
    }

    // 로그인
    @GetMapping("log-in")
    public ModelAndView getLogIn(){
        return new ModelAndView("security/user/user_login_page");
    }

    // 로그아웃
    @GetMapping("log-out")
    public ModelAndView logOut(){
        return new ModelAndView("security/user/user_login_page");
    }

}
