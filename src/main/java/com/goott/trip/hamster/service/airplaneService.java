package com.goott.trip.hamster.service;

import com.goott.trip.hamster.mapper.AirplaneMapper;
import com.goott.trip.hamster.model.Testproduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class airplaneService {

    @Autowired
    private AirplaneMapper mapper;

    public List<Testproduct> airplaneList(){return this.mapper.airplaneList(); };

    public Testproduct airplaneCont(String key){return this.mapper.airplaneCont(key); };

    public List<String> getCountry(){
        List<String> countries = new ArrayList<>();
        countries.add("가나 (233)");
        countries.add("가봉 (241)");
        countries.add("감비아 (220)");
        countries.add("과테말라 (502)");
        countries.add("그리스 (30)");
        countries.add("나미비아 (264)");
        countries.add("나이지리아 (234)");
        countries.add("네덜란드 (31)");
        countries.add("노르웨이 (47)");
        countries.add("뉴질랜드 (64)");
        countries.add("니카라과 (505)");
        countries.add("대만 (886)");
        countries.add("덴마크 (45)");
        countries.add("대한민국 (82)");
        countries.add("독일 (49)");
        countries.add("러시아 (7)");
        countries.add("루마니아 (40)");
        countries.add("룩셈부르크 (352)");
        countries.add("르완다 (250)");
        countries.add("리비아 (218)");
        countries.add("리투아니아 (370)");
        countries.add("리히텐슈타인 (423)");
        countries.add("마다가스카르 (261)");
        countries.add("말레이시아 (60)");
        countries.add("멕시코 (52)");
        countries.add("모로코 (212)");
        countries.add("몽골 (976)");
        countries.add("미국 (1)");
        countries.add("베트남 (84)");
        countries.add("벨기에 (32)");
        countries.add("보츠와나 (267)");
        countries.add("브라질 (55)");
        countries.add("사우디아라비아 (966)");
        countries.add("스웨덴 (46)");
        countries.add("스위스 (41)");
        countries.add("스페인 (34)");
        countries.add("싱가포르 (65)");
        countries.add("아르헨티나 (54)");
        countries.add("아랍에미리트 (971)");
        countries.add("아이슬란드 (354)");
        countries.add("아일랜드 (353)");
        countries.add("아프가니스탄 (93)");
        countries.add("알제리 (213)");
        countries.add("영국 (44)");
        countries.add("오스트리아 (43)");
        countries.add("우간다 (256)");
        countries.add("우루과이 (598)");
        countries.add("우크라이나 (380)");
        countries.add("이란 (98)");
        countries.add("이스라엘 (972)");
        countries.add("이집트 (20)");
        countries.add("이탈리아 (39)");
        countries.add("인도 (91)");
        countries.add("인도네시아 (62)");
        countries.add("일본 (81)");
        countries.add("중국 (86)");
        countries.add("칠레 (56)");
        countries.add("카자흐스탄 (7)");
        countries.add("캐나다 (1)");
        countries.add("케냐 (254)");
        countries.add("코스타리카 (506)");
        countries.add("콜롬비아 (57)");
        countries.add("쿠바 (53)");
        countries.add("태국 (66)");
        countries.add("튀니지 (216)");
        countries.add("파나마 (507)");
        countries.add("파키스탄 (92)");
        countries.add("페루 (51)");
        countries.add("폴란드 (48)");
        countries.add("프랑스 (33)");
        countries.add("필리핀 (63)");
        countries.add("헝가리 (36)");
        countries.add("호주 (61)");
        countries.add("홍콩 (852)");

        return countries;
    }

}
