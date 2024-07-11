package com.goott.trip.concho.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.concho.model.*;
import com.goott.trip.concho.service.main.HotelCrawlingService;
import com.goott.trip.concho.service.main.HotelSearchService;
import com.goott.trip.concho.service.main.IataCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("user/concho/service")
public class ConchoRestController {
    private final HotelSearchService hotelSearchService;
    private final IataCodeService iataCodeService;
    private final HotelCrawlingService hotelCrawlingService;

    @GetMapping("search")
    public ModelAndView getSearchPages(){
        ModelAndView mv = new ModelAndView("concho/user/hotel_search");
        return mv;
    }
    @GetMapping("hotel-list-iata")
    public ModelAndView getServicePages(
            Model model,
            Principal principal,
            @RequestParam(required = false) Map<String, String> paramMap){

        ModelAndView mv = new ModelAndView();
        Alarm alarm = new Alarm(model);
        for(String param : paramMap.values()){
            if(param == null || param.isEmpty()){
                alarm.setMessageAndRedirect("검색 정보를 정확하게 입력해주세요.", "");
                mv.setViewName(alarm.getMessagePage());
                return mv;
            }
        }

        String memberId = principal != null ? principal.getName() : null;
        String iataCode = paramMap.get("iataCode");
        String startDate = paramMap.get("startDate");
        String endDate = paramMap.get("endDate");
        String personCnt = paramMap.get("personCnt");
        
        
        Optional<List<ConchoHotel>> hotelListOp = hotelSearchService.getHotelListByIataCode(iataCode, memberId);
        if(hotelListOp.isEmpty() || hotelListOp.get().isEmpty()){
            alarm.setMessageAndRedirect("해당 장소의 호텔 정보가 없습니다.", "");
            mv.setViewName(alarm.getMessagePage());
            return mv;
        }

        List<ConchoHotel> hotelList = hotelListOp.get();
        System.out.println("총 호텔 수 : " + hotelList.size());
        iataCodeService.updateHotelListToKoByIataCode(hotelList);
        mv.addObject("hotelList", hotelList);
        mv.addObject("iataCode", iataCode);
        mv.addObject("startDate", startDate);
        mv.addObject("endDate", endDate);
        mv.addObject("personCnt", personCnt);
        mv.setViewName("concho/user/hotel_list");
        return mv;
    }

    @PostMapping("hotel-info")
    public ModelAndView postService(
            Model model,
            Principal principal,
            @RequestParam(required = false) Map<String, String> paramMap){
        ModelAndView mv = new ModelAndView();
        Alarm alarm = new Alarm(model);
        String memberId = principal != null ? principal.getName() : null;
        String hotelIdKey = paramMap.get("hotelIdKey");
        String hotelId = paramMap.get("hotelId");
        String startDate = paramMap.get("startDate");
        String endDate = paramMap.get("endDate");
        int personCnt = Integer.parseInt(paramMap.get("personCnt"));
        for(String param : paramMap.values()){
            if(param == null || param.isEmpty()){
                alarm.setMessageAndRedirect("검색 오류!", "");
                mv.setViewName(alarm.getMessagePage());
                return mv;
            }
        }

        ConchoHotel conchoHotel = hotelSearchService.findHotelByIdKey(hotelIdKey);
        mv.addObject("hotel", conchoHotel);

        Optional<List<ConchoHotelOffer>> conchoHotelOffersOp
                = hotelSearchService.getHotelRoomsById(hotelId,personCnt,startDate,endDate, memberId);
        if(conchoHotelOffersOp.isEmpty() || conchoHotelOffersOp.get().isEmpty() || !hotelCrawlingService.isCrawled(hotelId)){
            alarm.setMessageAndRedirect("해당 날짜 객실 정보 없음", "");
            mv.setViewName(alarm.getMessagePage());
            return mv;
        }

        List<ConchoHotelOffer> conchoHotelOffers = conchoHotelOffersOp.get();
        conchoHotelOffers.sort(Comparator.comparingDouble(o -> Double.parseDouble(o.getTotalPrice())));
        mv.addObject("hotelOfferList",conchoHotelOffers);
        if(conchoHotelOffers.get(0).getTotalPrice() == null && conchoHotelOffers.get(0).getTotalPrice().isEmpty()){
            mv.addObject("fakePrice", 0.0);
        }else{
            mv.addObject("fakePrice",
                    Double.parseDouble(conchoHotelOffers.get(0).getTotalPrice()) * 1.43);
        }
        // 크롤링했던 데이터들 가져오기
        List<String> imgUrlList = hotelCrawlingService.getHotelCrawledImgUrlList(hotelId);
        List<String> imgKeyList = hotelCrawlingService.getHotelCrawledImgKeyList(hotelId);
        List<HotelCrawledRoom> hotelCrawledRoomList = hotelCrawlingService.getHotelCrawledRoomList(hotelId);
        List<HotelCrawledInfo> hotelCrawledInfoList = hotelCrawlingService.getHotelCrawledInfo(hotelId);
        mv.addObject("imgKeyList", imgKeyList);
        mv.addObject("imgUrlList", imgUrlList);
        mv.addObject("personCnt", personCnt);
        mv.addObject("hotelCrawledRoomList", hotelCrawledRoomList);
        mv.addObject("hotelCrawledInfoList", hotelCrawledInfoList);
        mv.addObject("memberId", principal != null ? principal.getName() : null);
        mv.setViewName("concho/user/hotel_offer_list");
        return mv;
    }


}
