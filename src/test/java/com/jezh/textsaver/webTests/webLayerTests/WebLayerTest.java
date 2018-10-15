package com.jezh.textsaver.webTests.webLayerTests;

import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.service.TextCommonDataService;
import com.jezh.textsaver.service.TextPartService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// test only the layer below that, where Spring handles the incoming HTTP request and hands it off to your controller.
//... your code will be called exactly the same way as if it was processing a real HTTP request,
// but without the cost of starting the server.
// @WebMvcTest means Spring Boot is only instantiating the web layer, not the whole context
@RunWith(SpringRunner.class)
@WebMvcTest
//@SpringBootTest
//@AutoConfigureMockMvc
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    // не могу, поскольку здесь не подгружается основной контекст - только веб-контекст
//    @Autowired
//    private TextCommonDataService textCommonDataService;
//    @Autowired
//    private TextPartService textPartService;

    private TextPart textPart;
    private TextCommonData textCommonData;

    @Before
    public void setUp() throws Exception {
//        textCommonData = textCommonDataService.findAll().get(0);
////        textPart = textPartService.findSortedTextPartBunchByStartId(textCommonData.getId(), 1).get(0);
//        textPart = textPartService.findByPreviousItem(textCommonData.getId()).get();

    }


    @Test
    public void testFindTextPartById() throws Exception {

    }
}
