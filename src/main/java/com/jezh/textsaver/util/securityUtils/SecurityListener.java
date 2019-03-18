package com.jezh.textsaver.util.securityUtils;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class SecurityListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        Object userName = event.getAuthentication().getName(); // .getPrincipal();
        Object credentials = event.getAuthentication().getCredentials();
        System.out.println("************************************************* Failed login using USERNAME [" + userName + "]");
        System.out.println("************************************************* Failed login using PASSWORD [" + credentials + "]");
    }
}
