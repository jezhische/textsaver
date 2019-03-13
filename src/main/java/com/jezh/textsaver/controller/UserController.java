package com.jezh.textsaver.controller;

import com.jezh.textsaver.entity.AppUser;
import com.jezh.textsaver.service.AppUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * view controllers are defined in {@link com.jezh.textsaver.configuration.MvcConfig}
 */
@RestController
@RequestMapping("/login")
public class UserController {

    private AppUserService appUserService;
    private BCryptPasswordEncoder encoder;

    public UserController(AppUserService appUserService, BCryptPasswordEncoder encoder) {
        this.appUserService = appUserService;
        this.encoder = encoder;
    }

    @PostMapping("/sign-up")
    public void registration(@RequestBody AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        appUserService.save(user);
    }




//    @GetMapping("/")
//    public String root() {
//        return "index";
//    }
//
//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }

//    @PostMapping("/sign-up")
//    @ResponseBody  public AppUser signUp (@RequestBody AppUser user) {
//        // password encoding will be implemented in service save() method
//        return appUserService.save(user);
//    }

//    @RequestMapping("/login")
//    public String login() {
//        return "login";
//    }

    //    @Autowired
//    private AppUserService userService;
//
//    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
//    public ModelAndView login(){
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("login");
//        return modelAndView;
//    }
//
//
//    @RequestMapping(value="/registration", method = RequestMethod.GET)
//    public ModelAndView registration(){
//        ModelAndView modelAndView = new ModelAndView();
//        AppUser user = new AppUser();
//        modelAndView.addObject("user", user);
//        modelAndView.setViewName("registration");
//        return modelAndView;
//    }
//
//    @PostMapping(value = "/userAuthenticate")
//    public ModelAndView authenticate(/*@RequestBody AppUser appUser*/) {
//        System.out.println("*****************************************************************************************************");
////        ModelAndView modelAndView = new ModelAndView();
////        modelAndView.setViewName("index");
////        return modelAndView;
//        System.out.println("****************************************************************** AppUser name = "
//                + SecurityContextHolder.getContext().getAuthentication().getName());
////        System.out.println("****************************************************************** AppUser name = "
////                + appUser.getUsername());
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        AppUser user = appUserService.findByUsername(auth.getName());
//        modelAndView.addObject("newuser", user);
////        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
////        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
//        modelAndView.setViewName("forward:/doc-data");
//        return modelAndView;
//    }
//
////    @RequestMapping(value = "/testlogin")
////    public ModelAndView authenticate() {
////        ModelAndView modelAndView = new ModelAndView();
////        modelAndView.setViewName("testlogin");
////        return modelAndView;
////    }
//
////        @RequestMapping(value = "/index", method = RequestMethod.POST)
////    public ModelAndView authenticate() {
////        ModelAndView modelAndView = new ModelAndView();
////        modelAndView.setViewName("index");
////        return modelAndView;
////    }
//
//    @RequestMapping(value = "/registration", method = RequestMethod.POST)
//    public ModelAndView createNewUser(@Valid AppUser user, BindingResult bindingResult) {
//        ModelAndView modelAndView = new ModelAndView();
//        AppUser userExists = userService.findByUsername(user.getName());
//        if (userExists != null) {
//            bindingResult
//                    .rejectValue("email", "error.user",
//                            "There is already a user registered with the email provided");
//        }
//        if (bindingResult.hasErrors()) {
//            modelAndView.setViewName("registration");
//        } else {
//            userService.save(user);
//            modelAndView.addObject("successMessage", "User has been registered successfully");
//            modelAndView.addObject("user", new AppUser());
//            modelAndView.setViewName("registration");
//
//        }
//        return modelAndView;
//    }
////
//    @RequestMapping(value="/login", method = RequestMethod.POST)
//    public ModelAndView home(){
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        AppUser user = userService.findByUsername(auth.getName());
//        modelAndView.addObject("newuser", user);
////        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
////        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
//        modelAndView.setViewName("index");
//        return modelAndView;
//    }


}
