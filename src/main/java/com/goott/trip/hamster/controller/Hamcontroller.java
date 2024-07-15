package com.goott.trip.hamster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.goott.trip.common.model.Alarm;
import com.goott.trip.hamster.model.HotelShoppingCartDTO;
import com.goott.trip.hamster.model.Testproduct;
import com.goott.trip.hamster.model.AirplaneInfo;
import com.goott.trip.hamster.model.Payment;
import com.goott.trip.hamster.model.ShoppingCart;
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

        return new ModelAndView("Hamster/airplaneInfoList").addObject("list",list);
    }

    @PostMapping("airplane/ticketing")
    public ModelAndView airticketing(@RequestParam(name = "key", required = false)List<String> key,Principal principal){

        List<String> country = this.airservice.getCountry();

        if(key.size() == 1){
            Testproduct cont = this.airservice.airplaneCont(key.get(0));
            return new ModelAndView("Hamster/PlaneReservation").addObject("cont",cont).
                    addObject("country",country);
        }else {
            ModelAndView modelAndView = new ModelAndView("Hamster/PlaneReservation");
            for(int i = 0; i < key.size(); i++){

                Testproduct cont = this.airservice.airplaneCont(key.get(i));
                modelAndView.addObject("cont",cont).addObject("country",country);
            }
            return modelAndView;
        }

    }

    @GetMapping("airplane/ticketing")
    public ModelAndView airticketingaa(@RequestParam(name = "key", required = false)List<String> key,Principal principal){

        ModelAndView modelAndView = new ModelAndView("Hamster/airplaneReservation");
        String memId = principal.getName();
        List<Integer> count = new ArrayList<>();
        String AirKey = this.shoppingCartService.getAirKey(memId);
        List<CartDuration> DurationInfo = this.airservice.getDurationInfo(AirKey);
        List<CartDuration> DepDur = this.airservice.getDepDur(AirKey);
        List<CartDuration> CombDur = this.airservice.getCombDur(AirKey);
        List<CartSegment> airSeg = this.airservice.getSegment(AirKey);
        List<CartFlight> airInfo = this.airservice.getAirInfo(AirKey);
        List<String> country = this.airservice.getCountry();
        List<String> OnlyCountry = this.airservice.getOnlyCountry();
        List<CartSegment> segDep = this.airservice.getDep(AirKey);
        List<CartSegment> segComb = this.airservice.getComb(AirKey);
        List<CartPricing> price = this.airservice.getPricing(AirKey);


        if(key.size() == 1){
            Testproduct cont = this.airservice.airplaneCont(key.get(0));
            return modelAndView
                    .addObject("cont",cont)
                    .addObject("count",count)
                    .addObject("country",country)
                    .addObject("AirKey",AirKey)
                    .addObject("airInfo",airInfo)
                    .addObject("airSeg",airSeg)
                    .addObject("segDep",segDep)
                    .addObject("segComb",segComb)
                    .addObject("duration", DurationInfo)
                    .addObject("DepDur",DepDur)
                    .addObject("CombDur",CombDur)
                    .addObject("price",price)
                    .addObject("OnlyCountry",OnlyCountry);
        }else {
            ModelAndView modelAndViewE = new ModelAndView("Hamster/airplaneReservation");
            for(int i = 0; i < key.size(); i++){
                Testproduct cont = this.airservice.airplaneCont(key.get(i));
                modelAndView.addObject("cont",cont);
            }
            return modelAndViewE;
        }

    }

    @RequestMapping("hotel/shoppingCart-put")
    public Map<String, String> hotelShoppingCart(
            @ModelAttribute HotelShoppingCartDTO hotelShoppingCart){
        System.out.println(hotelShoppingCart.toString());
        // DB 저장하는 코드 작성


        // 호텔 쇼핑카트 url 넘겨주기

        return new HashMap<>(Map.of(
                "result", "ok",
                "url", "/member/hamster/ 넘겨줄 url 작성"));
    }

    @GetMapping("airplane/shoppingCart")
    public ModelAndView airShoppingCart(@RequestParam("key")String key, Principal principal, Model model){
        Alarm alarm = new Alarm(model);
        String memberId = principal.getName();
        List<ShoppingCart> dbVal = this.shoppingCartService.checkDup(memberId);
        List<Testproduct> plist = new ArrayList<>();

        System.out.println(key);

        boolean isAlreadyInCart = dbVal.stream().anyMatch(cartItem -> cartItem.getAirKey().equals(key));

        if (!isAlreadyInCart) {
            int check = this.shoppingCartService.insertCart(memberId, key);
            if (check > 0) {
                plist.add(this.airservice.airplaneCont(key));
            } else {
                alarm.setMessageAndRedirect("장바구니 넣기 중 오류가 발생했습니다.", "");
                return new ModelAndView(alarm.getMessagePage());
            }
        }

        for (ShoppingCart cartItem : dbVal) {
            plist.add(this.airservice.airplaneCont(cartItem.getAirKey()));

        }

        return new ModelAndView("Hamster/shoppingCart").addObject("plist", plist).addObject("dbVal",dbVal);
    }

    @PostMapping("airplane/categoryDelete")
    @ResponseBody
    public Map<String,Object> categoryDelete(@RequestParam("hotelKeyVal")String hotelKeyVal, Principal principal){

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

    @PostMapping("airplane/payment")
    public ModelAndView airplanePayment(@ModelAttribute Payment payment, @RequestParam("key")String AirKey,Principal principal){

        String memberId = principal.getName();
        List<CartFlight> airInfo = this.airservice.getAirInfo(AirKey);
        List<CartDuration> DepDur = this.airservice.getDepDur(AirKey);
        List<CartDuration> CombDur = this.airservice.getCombDur(AirKey);
        List<CartSegment> segDep = this.airservice.getDep(AirKey);
        List<CartSegment> segComb = this.airservice.getComb(AirKey);

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
                                       @RequestParam String paymentKey, @RequestParam("paymentInfo") String paymentInfo) throws UnsupportedEncodingException, MessagingException {

        String newAirKey = airKey.replaceAll("[{}]","");
        String newEmail = email.replaceAll("[{}]","");
        String newUUID = UUID.replaceAll("[{}]","");

        String decodedPaymentInfo = URLDecoder.decode(paymentInfo, "UTF-8");

        Gson gson = new Gson();
        Payment payment = gson.fromJson(decodedPaymentInfo, Payment.class);

        System.out.println(payment);

        List<CartFlight> airInfo = this.airservice.getAirInfo(newAirKey);
        List<CartDuration> DepDur = this.airservice.getDepDur(newAirKey);
        List<CartDuration> CombDur = this.airservice.getCombDur(newAirKey);
        List<CartSegment> segDep = this.airservice.getDep(newAirKey);
        List<CartSegment> segComb = this.airservice.getComb(newAirKey);
        List<CartPricing> price = this.airservice.getPricing(newAirKey);

        this.emailService.sendAirplaneEmail(newEmail,newUUID,payment,airInfo,DepDur,CombDur,segDep,segComb,price);

        ModelAndView modelAndView = new ModelAndView("Hamster/airplanePaymentSuccess");
        modelAndView.addObject("paymentKey", paymentKey)
                .addObject("orderId", orderId)
                .addObject("email", newEmail);

        return modelAndView;
    }

    @GetMapping("/shoppingCart")
    public ModelAndView shoppingCart() {return new ModelAndView("Hamster/shoppingCart");}
}
