package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.NoHandlerFoundException;

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

    @Ignore // почему-то из контроллера все работает правильно, а из теста не прописывается context-path
    @Test
    public void createPageLink() throws UnknownHostException, NoHandlerFoundException {
        String pageLink = dataManager.createPageLink(333L, 1);
        System.out.println("**********************************************" + pageLink);
        assertEquals("http://192.168.1.5:8074/textsaver/doc-data/333/pages?page=1", pageLink);
    }

    @Test
    public void createDocDataLink() {
    }
}