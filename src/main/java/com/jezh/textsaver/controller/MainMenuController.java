package com.jezh.textsaver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainMenuController {

//    /** when the application started, redirect to home page */
//    @GetMapping(value = "")
//    public String getHomePage() {
//        return "forward:/documents";
//    }

//    @GetMapping(value = "/")
//    public ModelAndView getHome() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("index");
////        return new ModelAndView("index");
//        return modelAndView;
//    }

    @ResponseBody
    @GetMapping(value = "/about")
    public String getAbout() {
        return "for more information about controllers:      http://localhost:8074/textsaver/v2/api-docs" +
                "      http://localhost:8074/textsaver/swagger-ui.html";
    }

    @GetMapping(value = "/swagger")
    public String getSwaggerPage() {
        return "redirect:/swagger-ui.html";
    }


}
