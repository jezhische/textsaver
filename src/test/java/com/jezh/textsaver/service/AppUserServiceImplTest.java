package com.jezh.textsaver.service;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.AppUser;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class AppUserServiceImplTest extends BasePostgresConnectingTest {

    @Autowired
    private AppUserService appUserService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void findByUsername() {
        AppUser jezh = appUserService.findByUsername("jezh");
        System.out.println("********************************************* " + jezh);
        AppUser kk = appUserService.findByUsername("kk");
        System.out.println("***************************************** " + kk);
    }

    @Test
    public void save() {
    }
}