package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.dto.TextCommonDataResource;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.service.TextCommonDataService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class TextCommonDataResourceAssemblerTest extends BasePostgresConnectingTest {

    @Autowired
    private TextCommonDataResourceAssembler assembler;

    @Autowired
    private TextCommonDataService textCommonDataService;

    private TextCommonData textCommonData;

    @Before
    public void setUp() throws Exception {
//        textCommonData = textCommonDataService.findAll().stream().findAny().get();
        textCommonData = textCommonDataService.findTextCommonDataById(809L).get();
    }

    @After
    public void tearDown() throws Exception {
        textCommonData = null;
    }

    @Test
    public void getLinkedDocsData() {
    }

    @Ignore // почему-то из контроллера все работает правильно, а из теста не прописывается context-path
    @Test
    public void convertToLinkedRepresentation() throws UnknownHostException, NoHandlerFoundException {
        TextCommonDataResource resource = assembler.convertToLinkedRepresentation(textCommonData);
        System.out.println("************* " + resource);
    }
}