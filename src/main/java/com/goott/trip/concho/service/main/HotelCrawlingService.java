package com.goott.trip.concho.service.main;

import com.goott.trip.common.model.Image;
import com.goott.trip.common.service.ImageService;
import com.goott.trip.concho.mapper.HotelCrawledMapper;
import com.goott.trip.concho.mapper.HotelMapper;
import com.goott.trip.concho.mapper.IataCodeMapper;
import com.goott.trip.concho.model.HotelCrawledImg;
import com.goott.trip.concho.model.HotelCrawledInfo;
import com.goott.trip.concho.model.HotelCrawledRoom;
import com.goott.trip.concho.model.IataCode;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelCrawlingService {
    private final IataCodeMapper iataCodeMapper;
    private final HotelCrawledMapper hotelCrawledMapper;
    private final HotelMapper hotelMapper;
    private final ImageService imageService;

    public boolean isCrawled(String hotelId){
        return hotelCrawledMapper.existsHotelInAllTables(hotelId);
    }
    
    // 동시성 오류 방지용
    private synchronized void saveHotelData(String hotelId, HotelCrawledInfo hotelCrawledInfo, List<HotelCrawledImg> hotelCrawledImgList) {
        // 저장이 안되있을때만 저장
        if (!hotelCrawledMapper.existsHotelInAllTables(hotelId)) {
            for (HotelCrawledImg img : hotelCrawledImgList) {
                hotelCrawledMapper.insertHotelCrawledImg(img);
            }
            hotelCrawledMapper.insertHotelCrawledInfo(hotelCrawledInfo);
        }
    }
    private void hotelDetailsByUrl(String url, String hotelId) throws Exception {
        Document doc = Jsoup.connect(url).get();

        // hotelchars 클래스 내부의 요소들을 선택
        Elements hotelCharsElements = doc.select(".hotelchars");

        // 수집한 데이터 저장을 위한 리스트
        List<HotelCrawledImg> hotelCrawledImgList = new ArrayList<>();
        List<HotelCrawledRoom> hotelCrawledRoomList = new ArrayList<>();
        HotelCrawledInfo hotelCrawledInfo = new HotelCrawledInfo();

        // 1. <img> 태그의 모든 src 출력 - 여러개
        // clearfix bh-photo-grid bh-photo-grid--space-down fix-score-hover-opacity
        // clearfix bh-photo-grid bh-photo-grid--space-down fix-score-hover-opacity
        Elements imgElements = hotelCharsElements.select(".k2-hp--gallery-header.bui-grid__column.bui-grid__column-9 .gallery-side-reviews-wrapper.js-no-close img");
        int imgCnt = 0;
        for (Element img : imgElements) {
            // facebook 은 현제 이미지 크롤링을 막고있음
            if(img.attr("src").contains("hotel")){
                //System.out.println(img.attr("src"));
                // 이미지 s3 저장 + DB 저장
                Image image = imageService.insertImageUrl(img.attr("src")).get();
                hotelCrawledImgList.add(new HotelCrawledImg(image.getImgKey(), hotelId));
                imgCnt++;
            }
            if(imgCnt > 10) break;
        }


        // eb2c6a4f4b bbafac9fc9 호텔 설명 - TEXT


        // data-testid="property-description-location-score-trans" - 호텔 간략 설명 + 점수

        // 2. 편의시설 블록 - 1개
        Elements blockElements1 = hotelCharsElements.select(".hp--bh_stripe-container-fix.js-k2-hp--block .da2b81213f.f598d65660.ba88e720cd.d8bb4e22d2.c6381d692a.c202c6890d");
        for (Element block : blockElements1) {
            hotelCrawledInfo.setCrFacility(block.outerHtml());
        }

        // 4. 편의시설 블록 2 - 1개
        Elements blockElements2 = hotelCharsElements.select(".hp--popular_facilities.js-k2-hp--block .c01d7b59ed.ad671605ef.ee250051de");
        for (Element block : blockElements2) {
            hotelCrawledInfo.setCrFacilityInfo(block.outerHtml());
        }

        // 3. 호텔 설명 블록 - 1개
        Elements textElements = hotelCharsElements.select(".page-section.hp--desc_highlights.js-k2-hp--block .eb2c6a4f4b.bbafac9fc9");
        for (Element textElement : textElements) {
            hotelCrawledInfo.setCrContent(textElement.text());
        }
        //page-section js-k2-hp--block k2-hp--rt
        // 5. 객실별 정보 - 한개
        Elements blockElements3 = hotelCharsElements.select("#maxotelRoomArea");
        for (Element block : blockElements3) {
            hotelCrawledInfo.setCrRoomInfo(block.outerHtml());
            break;
        }

        // 6. 후기
        Elements blockElement4 = hotelCharsElements.select("[data-testid=PropertyReviewsRegionBlock]");
        for (Element block : blockElement4) {
            hotelCrawledInfo.setCrReview(block.outerHtml());
            break;
        }

        // aceeb7ecbc pp-header__title => 호텔 이름
        Elements blockElements5 = hotelCharsElements.select(".aceeb7ecbc.pp-header__title");
        for (Element block : blockElements5) {
            hotelCrawledInfo.setCrHotelNameKo(block.outerHtml());
            break;
        }
        //f5113518a6 b49dbf078f 별점
        Elements blockElements6 = hotelCharsElements.select(".f5113518a6.b49dbf078f");
        for (Element block : blockElements6) {
            hotelCrawledInfo.setCrHotelStars(block.outerHtml());
            break;
        }
        // page-section js-k2-hp--block k2-hp--facilities_and_policies => 이용수칙
        Elements blockElements7 = hotelCharsElements.select(".page-section.js-k2-hp--block.k2-hp--facilities_and_policies");
        for (Element block : blockElements7) {
            hotelCrawledInfo.setCrRule(block.outerHtml());
            break;
        }
        // 호텔 ID 설정
        hotelCrawledInfo.setCrHotelId(hotelId);

        // 데이터베이스에 저장
        saveHotelData(hotelId, hotelCrawledInfo, hotelCrawledImgList);
    }


    public boolean CrawlingHotelInfo(String hotelName, String iataCode, String hotelId){
        boolean result = false;
        // 이미 크롤링 한 호텔이면 return ( 모든 정보가 잘 들어가 있는 경우)
        if(hotelCrawledMapper.existsHotelInAllTables(hotelId)){
            //System.out.println("이미 크롤링 한 호텔");
            return true;
        }
        try {
            String baseUrl = "https://www.booking.com/searchresults.html";
            // 앞에만 대문자로
            String ss = Arrays.stream(hotelName.toLowerCase().split(" "))
                    .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                    .collect(Collectors.joining(" "));
            Optional<IataCode> iataCodeOp = iataCodeMapper.findCityByIataCode(iataCode);
            if(iataCodeOp.isEmpty()){
                // 등록되지 않은 iataCode 로 검색함
                return false;
            } else if (iataCodeOp.get().getDestId() == null) {
                // 나라 이름으로 재검색
                iataCodeOp = iataCodeMapper.findIataCodeBySearchStrLimit(iataCodeOp.get().getCountry());

                // 부킹닷컴에 없는 지역, 크롤링 불가
                if(iataCodeOp.isEmpty()){
                    System.out.println("부킹닷컴에 없는 지역 : "+iataCodeOp.get().getCountry());
                    return false;
                }
            }
            IataCode iataCodeObj = iataCodeOp.get();
            String dest_id = String.valueOf(iataCodeObj.getDestId());
            //String otherParams = "&dest_type=hotel&lang=en-us&sb=1&src_elem=sb&src=searchresults&ac_position=0&ac_click_type=b&ac_langcode=en&ac_suggestion_list_length=5&search_selected=false";
            String otherParams = "&lang=en-us&soz=1&lang_changed=1";

            StringBuilder url = new StringBuilder(baseUrl);
            url.append("?ss=").append(URLEncoder.encode(ss, StandardCharsets.UTF_8));
            url.append("&dest_id=").append(URLEncoder.encode(dest_id, StandardCharsets.UTF_8));
            url.append(otherParams);

            String[] hotelStrArr = hotelName.toUpperCase().split(" ");

            Document doc = Jsoup.connect(url.toString()).get();
            Elements hotelElements = doc.select(".fa298e29e2.b74446e476.e40c0c68b1.ea1d0cfcb7.d8991ab7ae.e8b7755ec7.ad0e783e41");
            String crawlingHotelName = null;
            String crawlingHotelInfoLink = null;
            int maxNum = hotelStrArr.length/2 + 1;
            for (Element hotelElement : hotelElements) {
                Elements names = hotelElement.select(".fa4a3a8221.b121bc708f");
                Elements links = hotelElement.select(".eba3d3a8df");

                if (!names.isEmpty() && !links.isEmpty()) {
                    String hotelNameText = (names.first() != null) ? names.first().text() : null;
                    String hotelLinkHref = (links.first() != null) ? links.first().attr("href") : null;
                    int cnt = 0;
                    if(hotelNameText != null){
                        for(String str : hotelStrArr){
                            if(hotelNameText.toUpperCase().contains(str)){
                                cnt++;
                            }
                        }
                    }

                    if(maxNum < cnt){
                        maxNum = cnt;
                        crawlingHotelName = hotelNameText;
                        crawlingHotelInfoLink = hotelLinkHref;
                    }
                    // 모두 일치하면 크롤링 중단
                    if(hotelStrArr.length == cnt) break;
                }
            }
            if(maxNum == 0){
                //System.out.println("크롤링 일치하는 결과 없음 ....");
            }else{
                if(crawlingHotelName == null || crawlingHotelInfoLink == null){
                    //System.out.println("크롤링 일치하는 결과 없음 ....");
                }else{
                    crawlingHotelInfoLink = crawlingHotelInfoLink.replaceAll(".html", ".ko.html");
                    // 이때만 결과가 있음
                    System.out.println(crawlingHotelName+ " 호텔 크롤링 완료 : ");
                    System.out.println(crawlingHotelInfoLink);
                    hotelDetailsByUrl(crawlingHotelInfoLink, hotelId);
                    result = true;
                }
            }

        } catch (Exception e) {
            //e.printStackTrace();
            // 뭔가 오류가 나도 없는거로 처리
            //System.out.println("크롤링 일치하는 결과 없음 ....");
        }
        return result;
    }

    public String getHotelExampleImgUrl(String hotelId){
        Optional<HotelCrawledImg> crImgOp = hotelCrawledMapper.findOneHotelCrawledImgByHotelId(hotelId);
        if(crImgOp.isEmpty()){
            return null;
        }else{
            HotelCrawledImg crawledImg = crImgOp.get();
            Optional<Image> imageOp = imageService.findImageByKey(crawledImg.getCrImgKey());
            if(imageOp.isEmpty()){
                System.out.println("S3 문제 확인 바람");
                return null;
            }else{
                return imageOp.get().getUrl();
            }
        }
    }

    public List<String> getHotelCrawledImgUrlList(String hotelId){
        List<String> result = new ArrayList<>();
        List<HotelCrawledImg> hotelCrawledImgList = hotelCrawledMapper.findHotelCrawledImgByHotelId(hotelId);
        for(HotelCrawledImg hotelCrawledImg : hotelCrawledImgList){
            Optional<Image> imageOp = imageService.findImageByKey(hotelCrawledImg.getCrImgKey());
            if(imageOp.isPresent()){
                result.add(imageOp.get().getUrl());
            }
        }
        return result;
    }

    public List<HotelCrawledRoom> getHotelCrawledRoomList(String hotelId){
        return hotelCrawledMapper.findHotelCrawledRoomByHotelId(hotelId);
    }

    public List<HotelCrawledInfo> getHotelCrawledInfo(String hotelId){
        return hotelCrawledMapper.findHotelCrawledInfoByHotelId(hotelId);
    }
}
