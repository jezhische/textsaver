package com.jezh.textsaver.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index.html");
        registry.addViewController("/login").setViewName("login.html");
//        registry.addViewController("/sign-in").setViewName("redirect:/");
//        registry.addRedirectViewController("/sign-in", "/");
//        registry.addViewController("/sign-up").setViewName("registration.html");
        registry.addViewController("/access-denied").setViewName("access-denied.html");
    }
}
