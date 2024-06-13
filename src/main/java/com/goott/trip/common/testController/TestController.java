package com.goott.trip.common.testController;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.common.model.Image;
import com.goott.trip.common.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/image")
    public String sendImagePage(){
        return "common/imageTest";
    }
    @PostMapping("/image")
    public String imageUpload(
            @RequestPart("file") MultipartFile file,
            @RequestParam("nickname") String nickname,
            Model model){

        System.out.println("닉네임 : " + nickname);

        Alarm alarm = new Alarm(model);
        Optional<Image> image = imageService.insertFile(file);

        if(image.isEmpty()){
            alarm.setMessageAndRedirect("빈 이미지 or 오류 발생!", "");
            return alarm.getMessagePage();
        }
        alarm.setMessageAndRedirect("이미지가 업로드 되었습니다.", "image");
        return alarm.getMessagePage();
    }
}
