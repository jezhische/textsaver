package com.jezh.textsaver.webTests.webLayerTests;

import com.jezh.textsaver.TextsaverApplication;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.service.TextCommonDataService;
import com.jezh.textsaver.service.TextPartService;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

// test only the layer below that, where Spring handles the incoming HTTP request and hands it off to your controller.
//... your code will be called exactly the same way as if it was processing a real HTTP request,
// but without the cost of starting the server.
// @WebMvcTest means Spring Boot is only instantiating the web layer, not the whole context
@RunWith(SpringRunner.class)
// @WebMvcTest throws an error: IllegalArgumentException: At least one JPA metamodel must be present!, - see solution
// in comments to TextsaverApplication.class.
@WebMvcTest
//@SpringBootTest
//@AutoConfigureMockMvc
public class WebMockTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TextPartService textPartService;
    @MockBean
    private TextCommonDataService textCommonDataService;

    private TextPart textPart;
    private TextCommonData textCommonData;

    @Before
    public void setUp() throws Exception {
        textPart = TextPart
                .builder()
                .body("test")
                .nextItem(3L)
                .lastUpdate(new Date())
                .build();
        textPart.setId(2L);

        textCommonData = TextCommonData
                .builder()
                .name("testTextCommonData")
                .build();
        textCommonData.setId(1L);
    }

    @After
    public void tearDown() throws Exception {
        textPart = null;
        textCommonData = null;
    }

    @Test
    public void testFindTextPartById() throws Exception {
        Mockito.when(textPartService.findTextPartById(1L)).thenReturn(Optional.of(textPart));
        mockMvc
                .perform(MockMvcRequestBuilders.get("/text-common-data/26/text-parts/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body", Matchers.is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(2)));
    }

    @Test
    public void testGetTextCommonDataList() throws Exception {
        Mockito.when(textCommonDataService.findAll()).thenReturn(Arrays.asList(textCommonData));
        mockMvc
                .perform(MockMvcRequestBuilders.get("/text-common-data"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]", Matchers.allOf(
                        Matchers.hasKey("id"), Matchers.hasKey("name"),
                        Matchers.hasKey("creatingDate"), Matchers.hasKey("updatingDate"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]", Matchers.hasValue("testTextCommonData")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]", Matchers.hasValue(1)));
    }

}
