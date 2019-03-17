package com.jezh.textsaver.service;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.AppUser;
import com.jezh.textsaver.entity.Role;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class AppUserServiceImplTest extends BasePostgresConnectingTest {

    @Autowired
    AppUserService appUserService;

    private AppUser user;

    @Before
    public void setUp() throws Exception {
        user = AppUser.builder()
                .username("jezh")
                .password("password")
                .enabled(true)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    @Test
    public void findByName() {
        AppUser jezh = appUserService.findByUsername("jezh");
        System.out.println("*********************" + jezh);
        Set<Role> roles = jezh.getRoles();
        Iterator<Role> iterator = roles.iterator();
        boolean assertion = false;
        while (iterator.hasNext()) {
            Role next = iterator.next();
            if (next.getRole().equals("USER")) assertion = true;
        }
        assertTrue(assertion);
    }

    @Test
    public void save() {
        AppUser saved = appUserService.save(user);
        AppUser byUsername = appUserService.findByUsername(user.getUsername());
        assertEquals(saved, byUsername);
        assertThat(appUserService.findByUsername(user.getUsername()).isEnabled(), Matchers.comparesEqualTo(true));
        assertThat(appUserService.findByUsername(user.getUsername()).getRoles().stream().findFirst().get().getRole(),
                Matchers.is("USER"));
    }

//    @Test(expected = EntityExistsException.class)
//    public void doubleSave() {
//        AppUser saved = appUserService.save(user);
//    }
}