package com.goott.trip.concho.controller;

import com.goott.trip.concho.service.main.HotelCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("concho")
public class ConchoCrawlingRestController {
    private final HotelCrawlingService hotelCrawlingService;
    @PostMapping("crawling")
    public Map<String, String> crawlingHotelInfoByHotelId(@RequestParam Map<String, String> paramMap){
        String hotelName = paramMap.get("hotelName");
        String iataCode = paramMap.get("iataCode");
        String hotelId = paramMap.get("hotelId");
        var resultMap = new HashMap<String, String>();
        boolean result = hotelCrawlingService.CrawlingHotelInfo(hotelName, iataCode, hotelId);
        if(result){
            String imgUrl = hotelCrawlingService.getHotelExampleImgUrl(hotelId);
            resultMap.put("hotelId", hotelId);
            resultMap.put("imgUrl", imgUrl);
            resultMap.put("result", "ok");
        }else{
            resultMap.put("hotelId", hotelId);
            resultMap.put("result", "no");
        }
        return resultMap;
    }
}
