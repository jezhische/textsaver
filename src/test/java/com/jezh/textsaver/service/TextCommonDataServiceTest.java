package com.jezh.textsaver.service;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.TextCommonData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.List;

public class TextCommonDataServiceTest extends BasePostgresConnectingTest {

    @Autowired
    private TextCommonDataService textCommonDataService;

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
        Assert.assertNotNull(dataList);
        dataList.forEach(System.out::println);
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
