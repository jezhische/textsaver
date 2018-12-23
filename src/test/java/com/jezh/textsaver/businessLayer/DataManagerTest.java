package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class DataManagerTest extends BasePostgresConnectingTest {

    @Autowired
    DataManager dataManager;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getLastOpenedArrayItem() {
    }

    @Test
    public void createPageLink() throws UnknownHostException {
        System.out.println("**********************************************" + dataManager.createPageLink(333L, 1));
    }

    @Test
    public void createDocDataLink() {
    }
}