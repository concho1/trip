package com.goott.trip.security.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.common.model.Image;
import com.goott.trip.common.service.ImageService;
import com.goott.trip.esh.service.ExchangeService;
import com.goott.trip.hamster.model.ConHotelCartAll;
import com.goott.trip.hamster.model.Payment;
import com.goott.trip.hamster.service.ConHotelCartService;
import com.goott.trip.hamster.service.airplaneService;
import com.goott.trip.jhm.model.CartDuration;
import com.goott.trip.jhm.model.CartFlight;
import com.goott.trip.jhm.model.CartPricing;
import com.goott.trip.jhm.model.CartSegment;
import com.goott.trip.security.model.Member;
import com.goott.trip.security.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("member/ming")
public class MemberRestController {
    private final ImageService imageService;
    private final MemberService memberService;
    private final airplaneService airService;
    private final ConHotelCartService hotelCartService;
    private final ExchangeService exchangeService;

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

        String baseImgKey = "trip/4c4a3bf6-615b-414a-8273-c91f42334fdc";
        Optional<Image> uploadedImage = imageService.insertFile(file);

        if (uploadedImage.isPresent()) {
            return uploadedImage.get().getImgKey();
        } else {
            Member existingUser = memberService.getMemberById(memberId);
            String existingImgKey = (existingUser != null) ? existingUser.getImgKey() : null;

            return (existingImgKey != null && !existingImgKey.isEmpty()) ? existingImgKey : baseImgKey;
        }
    }

    @GetMapping("info")
    public ModelAndView getInfo(Principal principal) {
        /*HttpSession session = request.getSession();*/
        ModelAndView modelAndView = new ModelAndView("security/member/member_info");

        String memberId = principal.getName();
        System.out.println("memberId : " + memberId);


        /*String baseImgKey = "trip/4c4a3bf6-615b-414a-8273-c91f42334fdc";
        Optional<Image> imageOp = imageService.findImageByKey(baseImgKey);*/
        Member member = memberService.getMemberById(memberId);
        // 회원 이미지 있으면 쓰고 없으면 기본 이미지로 바꾸기
        imageService.findImageByKey(member.getImgKey()).ifPresentOrElse(
                image -> modelAndView.addObject(
                        "baseImgUrl", image.getUrl()),
                () -> modelAndView.addObject(
                        "baseImgUrl", "/common/images/member_base.png")
        );
        modelAndView.addObject("dto", member);
        modelAndView.addObject("memberId", memberId);
        /*session.setAttribute("userImgUrl", member.getImgKey());*/
        /*modelAndView.addObject("baseUrl", imageOp.get().getUrl());*/

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

        String baseImgKey = "trip/4c4a3bf6-615b-414a-8273-c91f42334fdc";

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

    @PostMapping("updatePwd")
    public ModelAndView postPwd(Principal principal, Model model,
                                @RequestParam("pw") String pw, @RequestParam("newPw") String newPw) {
        Alarm alarm = new Alarm(model);
        String memberId = principal.getName();

        // 사용자가 입력한 기존 비밀번호와 DB에 저장된 비밀번호가 일치하는지 확인
        if (memberService.checkPwd(memberId, pw)) {
            // 비밀번호 업데이트 시도
            int check = memberService.updatePwd(memberId, pw, newPw);
            if (check > 0) {
                alarm.setMessageAndRedirect("비밀번호가 변경되었습니다.", "info");
            } else if (check == -1) {
                alarm.setMessageAndRedirect("기존 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다.", "");
            } else {
                alarm.setMessageAndRedirect("비밀번호 변경 실패", "");
            }
        } else {
            alarm.setMessageAndRedirect("기존 비밀번호가 일치하지 않습니다.", "");
        }

        return new ModelAndView(alarm.getMessagePage());
    }

    // 회원 탈퇴
    @PostMapping("delete")
    public ModelAndView delOk(Principal principal, @RequestParam("pw") String pw, Model model) {
        Alarm alarm = new Alarm(model);
        String memberId = principal.getName();

        // 사용자가 입력한 비밀번호와 DB에 저장된 비밀번호가 일치하는지 확인
        if (memberService.checkPwd(memberId, pw)) {
            // 회원 탈퇴 시도
            int check = memberService.deleteMem(memberId, pw);
            if (check > 0) {
                alarm.setMessageAndRedirect("탈퇴 성공했습니다.", "log-out");
            } else {
                alarm.setMessageAndRedirect("탈퇴 실패, 다시 시도해주세요.", "");
            }
        } else {
            alarm.setMessageAndRedirect("비밀번호가 틀렸습니다.", "");
        }

        return new ModelAndView(alarm.getMessagePage());
    }
    /*

    // 카카오 회원 탈퇴
    @PostMapping("deleteNull")
    public ModelAndView delOkNull(HttpServletRequest request, Model model){
        Alarm alarm = new Alarm(model);
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if (userId == null) {
            alarm.setRedirectTo("/login");
            return new ModelAndView(alarm.getRedirectTo());
        }
        User cont = this.myPageService.contUser(userId);
        model.addAttribute("dto", cont);
        int check = this.myPageService.deleteUser(userId);
        if(check > 0){
            alarm.setMessageAndRedirect("탈퇴 성공했습니다.","/logout");
        }else{
            alarm.setMessageAndRedirect("탈퇴 실패, 다시 시도해주세요.","");
        }
        return new ModelAndView(alarm.getMessagePage());
    }*/

    @GetMapping("vip")
    public ModelAndView getVip(Principal principal) {
        String memberId = principal.getName();

        // 총 소비 금액 업데이트
        memberService.updateTotalSpentByMember(memberId);

        // VIP 등급 업데이트
        memberService.updateVIPStatus(memberId);

        // 회원 정보 조회
        Member member = memberService.getMemberById(memberId);

        // ModelAndView 설정
        ModelAndView modelAndView = new ModelAndView("security/member/member_vip");
        modelAndView.addObject("memberId", memberId);
        modelAndView.addObject("totalSpent", member.getTotal()); // 총 소비 금액은 Member 객체에서 가져옵니다
        modelAndView.addObject("vipLevel", member.getVip());

        return modelAndView;
    }

    @GetMapping("reservation")
    public ModelAndView getReservation(Principal principal) {
        String memberId = principal.getName();

        List<String> airKey = this.memberService.getPaymentAirKey(memberId);
        List<Payment> air = this.memberService.payAir(memberId);
        List<Payment> hotel = this.memberService.payHotel(memberId);

        List<CartFlight> airInfo = new ArrayList<>();
        List<CartSegment> segDep = new ArrayList<>();
        List<CartSegment> segComb = new ArrayList<>();
        List<CartDuration> DepDur = new ArrayList<>();
        List<CartDuration> CombDur = new ArrayList<>();

        for(int i = 0; i < airKey.size(); i ++){
            airInfo.addAll(this.airService.getAirInfo(airKey.get(i)));
            segDep.addAll(this.airService.getDep(airKey.get(i)));
            segComb.addAll(this.airService.getComb(airKey.get(i)));
            DepDur.addAll(this.airService.getDepDur(airKey.get(i)));
            CombDur.addAll(this.airService.getCombDur(airKey.get(i)));
        }

        // DepDur 리스트 처리 Optional 이용 null 처리
        for (CartDuration depDur : DepDur) {
            depDur.setAirlineImg(
                    imageService.findImageByKey(depDur.getAirlineImg())
                            .map(Image::getUrl)
                            .orElse("/common/images/air.png")
            );
        }

        // CombDur 리스트 처리
        for (CartDuration combDur : CombDur) {
            combDur.setAirlineImg(
                    imageService.findImageByKey(combDur.getAirlineImg())
                            .map(Image::getUrl)
                            .orElse("/common/images/air.png")
            );
        }

        System.out.println("memberId : " + memberId);
        // 여기서 호텔 리스트 받아오는거 추가
        List<ConHotelCartAll> hotelCartAllList
                = hotelCartService.getConHotelCartAllListByMemberId(memberId);
        // 호텔 가격 원화로 환전하기
        hotelCartAllList.forEach(hotelCartAll -> {
            hotelCartAll.getOfferObj().setTotalCost(
                    exchangeService.convertCurrency(
                            hotelCartAll.getOfferObj().getCurrency(),
                            "KRW",
                            hotelCartAll.getOfferObj().getTotalCost()
                    )
            );
            hotelCartAll.getOfferObj().setCurrency("KRW");
        });
        // hotel 인덱스 오류 remove 하면 인덱스 한칸씩 땅겨지는거 고려 안해서 에러 => removeIf 로 수정 완료
        hotelCartAllList.removeIf(hotelCartAll -> hotelCartAll.getPaymentObj() == null);

        // 같은 hotelCartAll 의 payment 가 같은 orderUuid 를 가지고 있으면 묶인 예약
        // map 으로 묶어주자
        var CartMapByPayment = new HashMap<String, List<ConHotelCartAll>>();
        for(ConHotelCartAll conHotelCartAll : hotelCartAllList){
            String orderUuid = conHotelCartAll.getPaymentObj().getOrderUuid();
            if(CartMapByPayment.containsKey(orderUuid)){
                CartMapByPayment.get(orderUuid).add(conHotelCartAll);
            }else{
                CartMapByPayment.put(orderUuid, new ArrayList<>(List.of(conHotelCartAll)));
            }
        }

        ModelAndView modelAndView = new ModelAndView("security/member/member_reservation");
        modelAndView.addObject("memberId",memberId)
                .addObject("airInfo",airInfo)
                .addObject("segDep",segDep)
                .addObject("segComb",segComb)
                .addObject("DepDur",DepDur)
                .addObject("CombDur",CombDur)
                .addObject("CartMapByPayment", CartMapByPayment)
                .addObject("airStatus", air)
                .addObject("hotelStatus",hotel);
        return modelAndView;
    }
    @GetMapping("airplane")
    public ModelAndView airRes(@RequestParam("key")String Key){

        ModelAndView modelAndView = new ModelAndView("security/member/member_airResInfo");

        List<CartDuration> DurationInfo = this.airService.getDurationInfo(Key);
        List<CartDuration> DepDur = this.airService.getDepDur(Key);
        List<CartDuration> CombDur = this.airService.getCombDur(Key);
        List<CartSegment> airSeg = this.airService.getSegment(Key);
        List<CartFlight> airInfo = this.airService.getAirInfo(Key);
        List<CartSegment> segDep = this.airService.getDep(Key);
        List<CartSegment> segComb = this.airService.getComb(Key);
        List<CartPricing> price = this.airService.getPricing(Key);
        List<Payment> allPayment = this.memberService.getPayment(Key);

        // DepDur 리스트 처리 Optional 이용 null 처리했습니당
        for (CartDuration depDur : DepDur) {
            depDur.setAirlineImg(
                    imageService.findImageByKey(depDur.getAirlineImg())
                            .map(Image::getUrl)
                            .orElse("/common/images/air.png")
            );
        }
        // CombDur 리스트 처리
        for (CartDuration combDur : CombDur) {
            combDur.setAirlineImg(
                    imageService.findImageByKey(combDur.getAirlineImg())
                            .map(Image::getUrl)
                            .orElse("/common/images/air.png")
            );
        }

        return modelAndView
                .addObject("AirKey",Key)
                .addObject("airInfo",airInfo)
                .addObject("airSeg",airSeg)
                .addObject("segDep",segDep)
                .addObject("segComb",segComb)
                .addObject("duration", DurationInfo)
                .addObject("DepDur",DepDur)
                .addObject("CombDur",CombDur)
                .addObject("price",price)
                .addObject("allPayment",allPayment);

    }

    @RequestMapping("hotel")
    public ModelAndView hotelRes(@RequestParam("uuid")List<String> HotelUuid,Principal principal){

        System.out.println(HotelUuid);

        String memberId = principal.getName();
        UUID uuid = UUID.randomUUID();

        List<ConHotelCartAll> hotelAllCont = new ArrayList<>();
        List<String> country = this.airService.getCountry();
        double totalPrice = 0;

        for(int i = 0; i < HotelUuid.size(); i ++){
            hotelAllCont.add(hotelCartService.getConHotelContListByUuid(HotelUuid.get(i)));
            totalPrice += hotelAllCont.get(i).getOfferObj().getTotalCost();
        }


        return new ModelAndView("security/member/member_hotelResInfo")
                .addObject("hotelAllCont",hotelAllCont)
                .addObject("totalPrice",totalPrice)
                .addObject("country",country)
                .addObject("uuid",uuid)
                .addObject("CartUuid",HotelUuid)
                .addObject("memberId",memberId);
    }

    // 로그아웃
    @GetMapping("log-out")
    public ModelAndView logOut(){return new ModelAndView("security/user/user_login_page");}


}
