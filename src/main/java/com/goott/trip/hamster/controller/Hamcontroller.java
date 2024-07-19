package com.goott.trip.hamster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.goott.trip.common.model.Alarm;
import com.goott.trip.common.model.Image;
import com.goott.trip.common.service.ImageService;
import com.goott.trip.esh.service.ExchangeService;
import com.goott.trip.hamster.model.*;
import com.goott.trip.hamster.service.ConHotelCartService;
import com.goott.trip.hamster.service.airplaneService;
import com.goott.trip.hamster.service.paymentService;
import com.goott.trip.hamster.service.shoppingCartService;
import com.goott.trip.jhm.model.CartDuration;
import com.goott.trip.jhm.model.CartFlight;
import com.goott.trip.jhm.model.CartPricing;
import com.goott.trip.jhm.model.CartSegment;
import com.goott.trip.security.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.*;

@RequestMapping("member/hamster")
@RestController
public class Hamcontroller {

    @Autowired
    private shoppingCartService shoppingCartService;
    @Autowired
    private airplaneService airservice;
    @Autowired
    private EmailService emailService;
    @Autowired
    private paymentService paymentservice;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ConHotelCartService hotelCartService;
    @Autowired
    private ExchangeService exchangeService;
    @GetMapping("airplane/list")
    public ModelAndView list() {

        List<Testproduct> list =  this.airservice.airplaneList();

        return new ModelAndView("Hamster/testAirplaneList").addObject("list",list);
    }

    @GetMapping("airplane/table")
    public ModelAndView table(){
        return new ModelAndView("Hamster/table");
    }

    @GetMapping("airplane/airplaneInfoList")
    public ModelAndView airplaneInfoList(){

        List<AirplaneInfo> list = this.airservice.airplaneInfoList();

        for(int i = 0; i < list.size(); i ++){
            list.get(i).setLogo(imageService.findImageByKey(list.get(i).getLogo()).get().getUrl());
        }

        return new ModelAndView("Hamster/airplaneInfoList").addObject("list",list);
    }

    @GetMapping("airplane/ticketing")
    public ModelAndView airTicketing(@RequestParam("key")String Key,Principal principal){

        ModelAndView modelAndView = new ModelAndView("Hamster/airplaneReservation");
        String memId = principal.getName();

        List<CartDuration> DurationInfo = this.airservice.getDurationInfo(Key);
        List<CartDuration> DepDur = this.airservice.getDepDur(Key);
        List<CartDuration> CombDur = this.airservice.getCombDur(Key);
        List<CartSegment> airSeg = this.airservice.getSegment(Key);
        List<CartFlight> airInfo = this.airservice.getAirInfo(Key);
        List<String> country = this.airservice.getCountry();
        List<String> OnlyCountry = this.airservice.getOnlyCountry();
        List<CartSegment> segDep = this.airservice.getDep(Key);
        List<CartSegment> segComb = this.airservice.getComb(Key);
        List<CartPricing> price = this.airservice.getPricing(Key);

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
                    .addObject("country",country)
                    .addObject("AirKey",Key)
                    .addObject("airInfo",airInfo)
                    .addObject("airSeg",airSeg)
                    .addObject("segDep",segDep)
                    .addObject("segComb",segComb)
                    .addObject("duration", DurationInfo)
                    .addObject("DepDur",DepDur)
                    .addObject("CombDur",CombDur)
                    .addObject("price",price)
                    .addObject("OnlyCountry",OnlyCountry);


    }

    @RequestMapping("hotel/ticketing")
    public ModelAndView hotelTicketing(@RequestParam("uuid")List<String> CartUuid,Principal principal){

        System.out.println(CartUuid);

        String memberId = principal.getName();
        UUID uuid = UUID.randomUUID();

        List<ConHotelCartAll> hotelAllCont = new ArrayList<>();
        List<String> country = this.airservice.getCountry();
        double totalPrice = 0;

        for(int i = 0; i < CartUuid.size(); i ++){
            ConHotelCartAll hotelCartAll = hotelCartService.getConHotelContListByUuid(CartUuid.get(i));
            hotelCartAll.getOfferObj().setTotalCost(
                    exchangeService.convertCurrency(
                            hotelCartAll.getOfferObj().getCurrency(),
                            "KRW",
                            hotelCartAll.getOfferObj().getTotalCost()
                    )
            );
            hotelCartAll.getOfferObj().setCurrency("KRW");
            hotelAllCont.add(hotelCartAll);

            totalPrice += hotelAllCont.get(i).getOfferObj().getTotalCost();
        }


        return new ModelAndView("Hamster/hotelReservation")
                .addObject("hotelAllCont",hotelAllCont)
                .addObject("totalPrice",totalPrice)
                .addObject("country",country)
                .addObject("uuid",uuid)
                .addObject("CartUuid",CartUuid)
                .addObject("memberId",memberId);
    }

    @GetMapping("/shoppingCart")
    public ModelAndView ShoppingCart(Principal principal, Model model){

        String memberId = principal.getName();
        int check = 0;

        List<String> airKey = this.shoppingCartService.shoppingCartAirplane(memberId);


        List<CartFlight> airInfo = new ArrayList<>();
        List<CartSegment> segDep = new ArrayList<>();
        List<CartSegment> segComb = new ArrayList<>();
        List<CartDuration> DepDur = new ArrayList<>();
        List<CartDuration> CombDur = new ArrayList<>();

        for(int i = 0; i < airKey.size(); i ++){
            airInfo.addAll(this.airservice.getAirInfo(airKey.get(i)));
            segDep.addAll(this.airservice.getDep(airKey.get(i)));
            segComb.addAll(this.airservice.getComb(airKey.get(i)));
            DepDur.addAll(this.airservice.getDepDur(airKey.get(i)));
            CombDur.addAll(this.airservice.getCombDur(airKey.get(i)));
        }

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
        for(int i = 0; i < hotelCartAllList.size(); i++){
            if(hotelCartAllList.get(i).getPaymentObj() == null){
                check += 1;
            }
        }

        System.out.println("에어키 사이즈 : "+airKey.size());

        return new ModelAndView("Hamster/shoppingCart")
                .addObject("airInfo",airInfo)
                .addObject("segDep",segDep)
                .addObject("segComb",segComb)
                .addObject("DepDur",DepDur)
                .addObject("CombDur",CombDur)
                .addObject("hotelList",hotelCartAllList)
                .addObject("hotelCheck",check);
    }

    @PostMapping("airplane/cartHotelDelete")
    @ResponseBody
    public Map<String,Object> HotelDelete(@RequestParam("hotelKeyVal")String hotelKeyVal, Principal principal){

        System.out.println(hotelKeyVal);

        Map<String, Object> response = new HashMap<>();
        try{
            String memberId = principal.getName();
            int check = this.shoppingCartService.deleteHotel(hotelKeyVal,memberId);
            response.put("success", check);
        }catch (Exception e) {
            response.put("success", -1);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("airplane/cartAirDelete")
    @ResponseBody
    public Map<String,Object> AirDelete(@RequestParam("AirKeyVal")String AirKeyVal, Principal principal){

        System.out.println(AirKeyVal);

        Map<String, Object> response = new HashMap<>();
        try{
            String memberId = principal.getName();
            int check = this.shoppingCartService.deleteAir(AirKeyVal,memberId);
            response.put("success", check);
        }catch (Exception e) {
            response.put("success", -1);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("airplane/payment")
    public ModelAndView airplanePayment(@ModelAttribute Payment payment, @RequestParam("key")String AirKey,Principal principal){

        String memberId = principal.getName();
        List<CartFlight> airInfo = this.airservice.getAirInfo(AirKey);
        List<CartDuration> DepDur = this.airservice.getDepDur(AirKey);
        List<CartDuration> CombDur = this.airservice.getCombDur(AirKey);
        List<CartSegment> segDep = this.airservice.getDep(AirKey);
        List<CartSegment> segComb = this.airservice.getComb(AirKey);

        for(int i = 0; i < DepDur.size(); i ++){
            DepDur.get(i).setAirlineImg(imageService.findImageByKey(DepDur.get(i).getAirlineImg()).get().getUrl());
            CombDur.get(i).setAirlineImg(imageService.findImageByKey(CombDur.get(i).getAirlineImg()).get().getUrl());
        }

        UUID uuid = UUID.randomUUID();


        return new ModelAndView("Hamster/airplanePayment")
                .addObject("airInfo",airInfo)
                .addObject("memberId",memberId)
                .addObject("DepDur",DepDur)
                .addObject("CombDur",CombDur)
                .addObject("segDep",segDep)
                .addObject("segComb",segComb)
                .addObject("payment",payment)
                .addObject("key",AirKey)
                .addObject("UUID",uuid);
    }

    @GetMapping("/success")
    public ModelAndView PaymentSuccess(@RequestParam String airKey, @RequestParam String email,
                                       @RequestParam String UUID, @RequestParam String paymentType, @RequestParam String orderId,
                                       @RequestParam String paymentKey, @RequestParam("paymentInfo") String paymentInfo,Principal principal) throws UnsupportedEncodingException, MessagingException {

        String newAirKey = airKey.replaceAll("[{}]","");
        String newEmail = email.replaceAll("[{}]","");
        String newUUID = UUID.replaceAll("[{}]","");
        String memberId = principal.getName();

        String decodedPaymentInfo = URLDecoder.decode(paymentInfo, "UTF-8");
        Gson gson = new Gson();
        Payment payment = gson.fromJson(decodedPaymentInfo, Payment.class);

        payment.setMemberId(memberId);
        payment.setOrderUuid(newUUID);
        payment.setPaymentKey(paymentKey);
        payment.setAirKey(airKey);


        List<CartFlight> airInfo = this.airservice.getAirInfo(newAirKey);
        List<CartDuration> DepDur = this.airservice.getDepDur(newAirKey);
        List<CartDuration> CombDur = this.airservice.getCombDur(newAirKey);
        List<CartSegment> segDep = this.airservice.getDep(newAirKey);
        List<CartSegment> segComb = this.airservice.getComb(newAirKey);
        List<CartPricing> price = this.airservice.getPricing(newAirKey);

        this.emailService.sendAirplaneEmail(newEmail,newUUID,payment,airInfo,DepDur,CombDur,segDep,segComb,price);
        int check = this.paymentservice.airplanePay(payment);


        ModelAndView modelAndView = new ModelAndView("Hamster/paymentSuccess");

        if(check > 0){
            this.shoppingCartService.deleteAir(newAirKey,memberId);
            modelAndView.addObject("paymentKey", paymentKey)
                    .addObject("orderId", orderId)
                    .addObject("email", newEmail);

            return modelAndView;
        }else{
            return modelAndView;
        }

    }

    @GetMapping("hotel/success")
    public ModelAndView HotelPaymentSuccess(@RequestParam String UUID, @RequestParam String firstName,
                                            @RequestParam String lastName, @RequestParam String email,
                                            @RequestParam String country,@RequestParam List<String> cartUUID,
                                            @RequestParam String paymentType, @RequestParam String orderId,
                                            @RequestParam String paymentKey,Principal principal) throws UnsupportedEncodingException, MessagingException {


        List<ConHotelCartAll> hotelList = new ArrayList<>();
        String memId = principal.getName();
        double totalPrice = 0;


           int check = this.paymentservice.hotelPay(UUID,memId,firstName,lastName,country,email,paymentKey);

           if(check >0 ){
               for(int i =0; i < cartUUID.size(); i++){
                   hotelList.add(this.hotelCartService.getConHotelContListByUuid(cartUUID.get(i).replaceAll("\\[","").replaceAll("\\]","")));
                   this.paymentservice.insertHotel(UUID,memId,cartUUID.get(i).replaceAll("\\[","").replaceAll("\\]",""));
                   totalPrice += hotelList.get(i).getOfferObj().getTotalCost();
           }
               this.emailService.sendHotelEmail(UUID,firstName,lastName,email,hotelList,totalPrice);
        }

        System.out.println(UUID);
        System.out.println(firstName);
        System.out.println(lastName);
        System.out.println(email);
        System.out.println(country);




        return new ModelAndView("Hamster/paymentSuccess").addObject("email",email);

    }
}
