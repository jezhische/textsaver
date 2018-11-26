package com.jezh.textsaver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainMenuController {

//    /** when the application started, redirect to home page */
//    @GetMapping(value = "")
//    public String getHomePage() {
//        return "redirect:/documents";
//    }

    @ResponseBody
    @GetMapping(value = "/about")
    public String getAbout() {
        return "this is about info";
    }

    @GetMapping(value = "/swagger")
    public String getSwaggerPage() {
        return "redirect:/swagger-ui.html";
    }


}
