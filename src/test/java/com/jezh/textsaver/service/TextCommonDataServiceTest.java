package com.jezh.textsaver.service;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.Assert.*;

public class TextCommonDataServiceTest extends BasePostgresConnectingTest {

    @Autowired
    private TextCommonDataService textCommonDataService;
    @Autowired
    private BookmarkService bookmarkService;
    @Autowired
    private TextPartService textPartService;

    private TextCommonData textCommonData;



    @Before
    public void setUp() throws Exception {
        textCommonData = TextCommonData
                .builder()
                .name("TextCommonDataServiceTest")
                .build();
    }

    @After
    public void tearDown() throws Exception {
        textCommonData = null;
    }

    @Test
//    @Rollback(value = false)
    public void testFindAll() {
        List<TextCommonData> dataList = textCommonDataService.findAll();
        assertNotNull(dataList);
        dataList.forEach(System.out::println);
    }

    public void testCreate() {
        TextCommonData textCommonData = textCommonDataService.create("TextCommonDataServiceTest");
        Long id = textCommonData.getId();
        System.out.println("*****************************************************************" + bookmarkService.findById(id).get());
        System.out.println("******************************textPartService.findSortedTextPartIdByTextCommonDataId(id) = "
                + textPartService.findSortedTextPartIdByTextCommonDataId(id));
        assertNotNull(textCommonDataService.findTextCommonDataById(id));
        assertNotNull(bookmarkService.findById(id));
        assertNotNull(textPartService.findSortedTextPartIdByTextCommonDataId(id).get(0));
        assertEquals(textCommonData, bookmarkService.findById(id).get().getTextCommonData());
        assertEquals(textCommonData.getFirstItem(), textPartService.findSortedTextPartIdByTextCommonDataId(id).get(0));
        System.out.println("*****************************************************************" + textCommonData);
    }

    // ======================================================================================= TEST UTIL

    public Long getExistingTextCommonDataId() {
        List<TextCommonData> dataList = textCommonDataService.findAll();
        return dataList.size() == 0? null : dataList.get(0).getId();
    }

    @Test
    public void testGetExistingTextCommonDataId() {
        System.out.println(getExistingTextCommonDataId());
    }
}
