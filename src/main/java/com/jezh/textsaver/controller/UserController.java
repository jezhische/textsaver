package com.jezh.textsaver.controller;

import com.jezh.textsaver.entity.AppUser;
import com.jezh.textsaver.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * view controllers are defined in {@link com.jezh.textsaver.configuration.WebMvcConfig}
 */
@Controller
public class UserController {

    private AppUserService appUserService;
    private BCryptPasswordEncoder encoder;

    public UserController(AppUserService appUserService, BCryptPasswordEncoder encoder) {
        this.appUserService = appUserService;
        this.encoder = encoder;
    }

//    @GetMapping("/")
//    public ModelAndView successAuth() {
//        // сюда можно добавить юзера, полученного из секьюрити, и использовать на странице его имя
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("index");
//        return modelAndView;
//    }

    /**
     * serves default url
     */
    @GetMapping("/")
    public ModelAndView getIndex() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = appUserService.findByUsername(authentication.getName());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/users/sign-up")
    public ModelAndView gotoRegistrationPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", new AppUser());
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping("/users/sign-up")
//    @ResponseBody
    public ModelAndView createNewUser(@Valid AppUser user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        AppUser byUsername = appUserService.findByUsername(user.getUsername());
        if (byUsername != null) {
            bindingResult.rejectValue("username", "error.appUser", "There is already a user registered with the login provided");
        } if (bindingResult.hasErrors()) modelAndView.setViewName("registration");
        else {
            appUserService.save(user);
            modelAndView.addObject("successMessage", "Customer has been registered successfully");
            modelAndView.addObject("user", new AppUser());
            modelAndView.setViewName("registration");
        }
        return modelAndView;
    }

// "Spring Security to process the submitted credentials when sent the specified path and, by default, redirect user back
// to the page user came from. It will not pass the request to Spring MVC and your controller."

//    @PostMapping("/login")
//    public @ResponseBody
//    ResponseEntity<AppUser> getJsonInputStream(@RequestBody AppUser user) {
//        System.out.println("******************************************************** user.username = " + user.getUsername());
//        System.out.println("appUserService.findByUsername(user.getUsername()) = " + appUserService.findByUsername(user.getUsername()));
//        AppUser byUsername = appUserService.findByUsername(user.getUsername());
//        if (byUsername == null) return null /*ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()*/;
//        else {
//            System.out.println("*********************************************************** UserController /users/submit: byUsername = " + byUsername);
//            return ResponseEntity.ok(byUsername);
////            return ResponseEntity.created(URI.create("")).body(byUsername);
////            return ResponseEntity.status(HttpStatus.CREATED).body(byUsername);
//        }
//    }


}
