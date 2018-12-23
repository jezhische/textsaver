package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.dto.TextCommonDataResource;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.service.TextCommonDataService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

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

    @Test
    public void convertToLinkedRepresentation() throws UnknownHostException {
        TextCommonDataResource resource = assembler.convertToLinkedRepresentation(textCommonData);
        System.out.println(resource);
    }
}