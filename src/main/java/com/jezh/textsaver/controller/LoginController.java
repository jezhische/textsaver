package com.jezh.textsaver.controller;

import com.jezh.textsaver.entity.AppUser;
import com.jezh.textsaver.service.AppUserService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class LoginController {

    private final AppUserService appUserService;
    public Logger logger;

    public LoginController(AppUserService appUserService, Logger logger) {
        this.appUserService = appUserService;
//        logger = LogManager.getLogger(getClass());
    }

//    @GetMapping(value={"/login"})
//    public ModelAndView login(){
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("login");
//        return modelAndView;
//    }


    @GetMapping(value="/sign-up")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        AppUser appUser = new AppUser();
        // create a model attached to page
        modelAndView.addObject("user", appUser);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/sign-up")
    public ModelAndView createNewUser(@Valid @ModelAttribute(value="user")  AppUser user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        AppUser saved = appUserService.findByUsername(user.getUsername());
        if (saved != null) {
            bindingResult
                    .rejectValue("username", "error.user",
                            "There is already a user registered with the name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            appUserService.save(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new AppUser());
            modelAndView.setViewName("registration");
        }
        return modelAndView;
    }

    @GetMapping(value="/") // "/sign-in"
    public ModelAndView signin(){
        ModelAndView modelAndView = new ModelAndView();
        // NB the way to get access to user data
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = appUserService.findByUsername(auth.getName());
        modelAndView.addObject("user", user);
        modelAndView.addObject("welcomemessage", "Hi, " + user.getUsername() + "!");
        modelAndView.setViewName("index");
//        // for "/sign-in" mapping:
//        modelAndView.setViewName("redirect:/");
//        logger.info("******************************************** user authenticated: " + user +
//                ". Authentication successfull");
        return modelAndView;
    }

//    @RequestMapping(value = "/access-denied")
//    public ModelAndView setAccessDeniedView() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("access-denied");
//        return modelAndView;
//    }

}
