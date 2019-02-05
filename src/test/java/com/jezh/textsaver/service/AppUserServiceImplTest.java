package com.jezh.textsaver.service;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.AppUser;
import com.jezh.textsaver.entity.Role;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class AppUserServiceImplTest extends BasePostgresConnectingTest {

    @Autowired
    AppUserService appUserService;

    @Test
    public void findByName() {
        AppUser jezh = appUserService.findByName("jezh");
        System.out.println("*********************" + jezh);
        Set<Role> roles = jezh.getRoles();
        Iterator<Role> iterator = roles.iterator();
        boolean assertion = false;
        while (iterator.hasNext()) {
            Role next = iterator.next();
            if (next.getRole().equals("ROLE_USER")) assertion = true;
        }
        assertTrue(assertion);
    }

    @Test
    public void save() {
    }
}