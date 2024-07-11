package com.goott.trip.concho.service.component;

import com.goott.trip.concho.model.hotel.ConHotel;
import com.goott.trip.concho.model.hotel.ConImage64;
import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class GoogleCrawlingComponent {
    public List<ConImage64> findImage64ListByGoogleCrawlingWithConHotel(ConHotel conHotel){
        WebDriver driver = WebDriverSingleton.getDriver();
        var conImage64List = new ArrayList<ConImage64>();
        try {
            String baseUrl = "https://www.google.com/search?q=";
            String query = "&tbm=isch";
            String url = baseUrl + conHotel.getHotelName() + query;

            // Selenium으로 페이지 로드
            driver.get(url);
            Thread.sleep(500); // 페이지가 로드될 시간을 기다립니다.

            System.out.println(url);

            // class 'YQ4gaf' 에서 base64 이미지 찾기
            List<WebElement> imageElements = driver.findElements(By.className("YQ4gaf"));
            int imgCnt = 0;
            boolean isFirstImage = true;
            for (WebElement imgElement : imageElements.subList(imageElements.size() < 5 ? 0 : 5, imageElements.size())) {
                String base64Image = imgElement.getAttribute("src");
                System.out.println(base64Image.substring(11,22));
                // jpeg 타입이 아니면 광고 이미지나 로고임
                if(base64Image.startsWith("jpeg;base64", 11)){
                    ConImage64 conImage64 = new ConImage64();
                    if(isFirstImage){
                        isFirstImage = false;
                        conHotel.setSampleImage(base64Image);
                    }
                    conImage64.setHotelUuid(conHotel.getUuid());
                    conImage64.setBase64(base64Image);
                    conImage64List.add(conImage64);
                    imgCnt++;
                    if(imgCnt >= 15) break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conImage64List;
    }
}
