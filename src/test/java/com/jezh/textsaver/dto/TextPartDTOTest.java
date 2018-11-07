package com.jezh.textsaver.dto;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.service.TextCommonDataService;
import com.jezh.textsaver.service.TextPartService;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

public class TextPartDTOTest extends BasePostgresConnectingTest {

    @Autowired
    private MapperFactory mapperFactory;

    @Autowired
    TextPartService textPartService;

    @Autowired
    TextCommonDataService textCommonDataService;

    private TextPart textPart;
    private TextPartDTO textPartDTO;
    private BoundMapperFacade<TextPart, TextPartDTO> boundMapper;

    @Before
    public void setUp() throws Exception {
        textPart = textPartService.findAll().stream().findAny().get();
        textPartDTO = TextPartDTO
                .builder()
                .id(1L)
                .body("test")
                .lastUpdate(new Date())
                .nextItem(2L)
                .textCommonData(textCommonDataService.findAll().stream().findAny().get())
                .build();
        boundMapper = mapperFactory.getMapperFacade(TextPart.class, TextPartDTO.class);
    }

    @After
    public void tearDown() throws Exception {
        textPart = null;
        textPartDTO = null;
        boundMapper = null;
    }

    @Test
    public void forthMappingTest() {
        TextPartDTO destination = boundMapper.map(textPart);
        assertEquals(textPart.getId(), destination.getId());
        assertEquals(textPart.getBody(), destination.getBody());
        assertEquals(textPart.getNextItem(), destination.getNextItem());
        assertEquals(textPart.getLastUpdate(), destination.getLastUpdate());
        assertEquals(textPart.getTextCommonData(), destination.getTextCommonData());
//        System.out.println("************************" + textPart.getTextCommonData());
//        System.out.println("************************" + textPart.getLastUpdate());
    }

    @Test
    public void backMappingTest() {
        TextPart source = boundMapper.mapReverse(textPartDTO);
        assertEquals(textPartDTO.getId(), source.getId());
        assertEquals(textPartDTO.getBody(), source.getBody());
        assertEquals(textPartDTO.getNextItem(), source.getNextItem());
        assertEquals(textPartDTO.getLastUpdate(), source.getLastUpdate());
        assertEquals(textPartDTO.getTextCommonData(), source.getTextCommonData());
//        System.out.println("*************************" + textPartDTO.getTextCommonData());
    }
}
