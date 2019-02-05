package com.jezh.textsaver.service;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.service.roleProperties.ExistingRoles;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class RoleServiceImplTest extends BasePostgresConnectingTest {

    @Autowired
    private RoleService service;

    @Test
    public void findByRole() {
        assertTrue(service.findByRole(ExistingRoles.ROLE_USER).getRole().equals("ROLE_USER"));
    }
}