package com.jezh.textsaver.dto;

import com.jezh.textsaver.businessLayer.TextPartPageRepresentationConverter;
import com.jezh.textsaver.businessLayer.TextPartLinkAssembler;
import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.service.TextCommonDataService;
import com.jezh.textsaver.service.TextPartService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

public class TextPartPageRepresentationConverterTest extends BasePostgresConnectingTest {

    @Autowired
    TextPartService textPartService;

    @Autowired
    TextCommonDataService textCommonDataService;

    @Autowired
    TextPartPageRepresentationConverter textPartPageRepresentationConverter;

    @Autowired
    TextPartLinkAssembler resourceAssembler;

    private TextPart textPart;
    private TextPartPagedLinkedRepresentation textPartPagedLinkedRepresentation;

    @Before
    public void setUp() throws Exception {
//        textPart = textPartService.findAll().stream().findAny().get();
        textPart = textPartService.findTextPartById(44L).get();
        textPartPagedLinkedRepresentation = TextPartPagedLinkedRepresentation
                .builder()
                .body("test")
                .lastUpdate(new Date())
//                .nextItem(2L)
//                .textCommonData(textCommonDataService.findTextCommonDataById(36L).get())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        textPart = null;
        textPartPagedLinkedRepresentation = null;
    }

    @Test
    public void forthMappingTest() throws Exception {
        TextPartPagedLinkedRepresentation destination = textPartPageRepresentationConverter.convertToLinkedPage(textPart);
        assertEquals(textPart.getBody(), destination.getBody());
//        assertEquals(textPart.getNextItem(), destination.getNextItem());
        assertEquals(textPart.getLastUpdate(), destination.getLastUpdate());
//        System.out.println("************************" + textPart.getTextCommonData());
//        System.out.println("************************" + textPart.getLastUpdate());
        System.out.println("************************************" + destination);
    }

    @Test
    public void backMappingTest() {
        TextPart source = textPartPageRepresentationConverter.convertToEntity(textPartPagedLinkedRepresentation);
        assertEquals(textPartPagedLinkedRepresentation.getBody(), source.getBody());
//        assertEquals(textPartPagedLinkedRepresentation.getNextItem(), source.getNextItem());
        assertEquals(textPartPagedLinkedRepresentation.getLastUpdate(), source.getLastUpdate());
//        System.out.println("*************************" + textPartPagedLinkedRepresentation.getTextCommonData());
    }

    @Test
    public void testTextPartResourceAssembler_toResource() {
//        TextPartPagedLinkedRepresentation resource = resourceAssembler.toResource(textPart);
//        System.out.println("**********************************************************" + resource);
    }
}
