package com.goott.trip.common.model;

import lombok.Data;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@Data
public class Alarm {
    private String message;
    private String redirectTo;
    private String messagePage;
    private Model model;
    private ModelAndView modelAndView;
    public Alarm(Model model){
        this.model = model;
        this.messagePage = "common/message";
        this.model.addAttribute("alarm", this);
    }
    public Alarm(ModelAndView modelAndView){
        this.modelAndView = modelAndView;
        this.messagePage = "common/message";
        this.modelAndView.addObject("alarm", this);
    }
    public void setMessageAndRedirect(String message, String redirectTo){
        this.message = message;
        this.redirectTo = redirectTo;
        if(this.model != null){
            this.model.addAttribute("alarm", this);
        }else{
            this.modelAndView.addObject("alarm", this);
        }
    }
}
