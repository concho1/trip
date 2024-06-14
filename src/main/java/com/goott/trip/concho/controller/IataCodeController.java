package com.goott.trip.concho.controller;

import com.goott.trip.common.model.Alarm;
import com.goott.trip.concho.service.main.IataCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("user/concho")
public class IataCodeController {
    private final IataCodeService iataCodeService;

    @GetMapping("upload")
    public ModelAndView getUpload() {
        return new ModelAndView("concho/com/upload_csv");
    }
    @PostMapping("upload")
    public ModelAndView uploadCsv(@RequestParam("file") MultipartFile file, Model model) {
        Alarm alarm = new Alarm(model);
        if(iataCodeService.uploadCsv(file)){
            System.out.println("끝");
            alarm.setMessageAndRedirect("파일 업로드 성공", "/user/concho/upload");
        }else{
            alarm.setMessageAndRedirect("파일 업로드 실패", "");
        }
        return new ModelAndView(alarm.getMessagePage());
    }
}