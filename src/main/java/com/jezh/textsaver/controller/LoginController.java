package com.jezh.textsaver.controller;

import com.jezh.textsaver.entity.AppUser;
import com.jezh.textsaver.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private AppUserService userService;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        AppUser user = new AppUser();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

//    @PostMapping(value = "/login")
//    public ModelAndView authenticate() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("index");
//        return modelAndView;
//    }

//    @RequestMapping(value = "/testlogin")
//    public ModelAndView authenticate() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("testlogin");
//        return modelAndView;
//    }

//        @RequestMapping(value = "/index", method = RequestMethod.POST)
//    public ModelAndView authenticate() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("index");
//        return modelAndView;
//    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid AppUser user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        AppUser userExists = userService.findByName(user.getUsername());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.save(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new AppUser());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }
//
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = userService.findByName(auth.getName());
        modelAndView.addObject("newuser", user);
//        modelAndView.addObject("userName", "Welcome " + user.getUsername() + " " + user.getLastName() + " (" + user.getEmail() + ")");
//        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("index");
        return modelAndView;
    }


}
