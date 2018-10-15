package com.jezh.textsaver.webTests.fullStackTests;

import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.service.TextCommonDataService;
import com.jezh.textsaver.service.TextPartService;
import com.jezh.textsaver.util.TestUtil;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.FlashMap;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;

// almost the full stack is used, but without the cost of starting the server. @SpringBootTest loads the full contexts,
// so I can use service requests to data base.
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

//        private TextCommonDataService textCommonDataService;
//    @Autowired
//    private TextPartService textPartService;

    private TextPart textPartTwo, textPartThree, textPartFour;
    private TextCommonData textCommonData;

    @Before
    public void setUp() throws Exception {
        textPartTwo = TextPart
                .builder()
                .body("test")
                .previousItem(1L)
                .nextItem(4L)
                .lastUpdate(new Date())
                .build();
        textPartTwo.setId(2L);
        textPartThree = TextPart.builder().build();
        textPartThree.setId(3L);
        textPartFour = TextPart.builder().nextItem(3L).build();
        textPartFour.setId(4L);

        textCommonData = TextCommonData
                .builder()
                .name("testTextCommonData")
                .build();
        textCommonData.setId(1L);
    }

    @After
    public void tearDown() throws Exception {
        textPartTwo = null;
        textPartThree = null;
        textPartFour = null;
        textCommonData = null;
    }

// =============================================================================================== textCommonData tests
    /** find all the textCommonData */
    @Test
    public void testGetTextCommonDataList() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/text-common-data"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]", Matchers.allOf(
                        Matchers.hasKey("id"), Matchers.hasKey("name"),
                        Matchers.hasKey("creatingDate"), Matchers.hasKey("updatingDate"))))
                .andDo(mvcResult -> {
                    String jsonArray = mvcResult.getResponse().getContentAsString();
                    System.out.println(jsonArray);
                });
    }


// ====================================================================================================== textPart tests

    /**
     * test controller method findSortedTextPartBunchByStartId(), where startId == null && size == null:
     * find all the textPart with the given textCommonData id in an order, where textPart.nextItem = nextTextPart.id
     */
    @Test
    public void testGetTextPartListByTextCommonDataId() throws Exception {
        long someTextCommonDataId = getSomeTextCommonDataIdFromDB(1);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/text-common-data/"
                        + someTextCommonDataId
                        + "/text-parts"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].previousItem", Matchers.is((int)someTextCommonDataId)))
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    Arrays.asList(json.split("}")).forEach(System.out::println);
                })
                .andReturn();

        /* get textPart array from mvcResult */
        TextPart[] textParts = ((TextPart[]) TestUtil.convertJSONStringArrayToObjectArray(result, TextPart[].class));

        /* check the array order as the main test method expectancy */
        for (int i = 0; i < textParts.length - 1; i++) {
            Assert.assertEquals(textParts[i].getNextItem(), textParts[i + 1].getId());
//            System.out.println("************************************************" + i + ": OK");
        }

    }
// -------------------------------------------------------

    /**
     * test controller method findSortedTextPartBunchByStartId(), where startId != null && size != null:
     * find list of the textPart in an order, where textPart.nextItem = nextTextPart.id,
     * start from textPart.id = startId and with the list.size() = size
     */
    @Test
    public void testFindSortedTextPartBunchByStartId() throws Exception {
        /* get some TextCommonData id from db to find set of TextPart with  */
        long someTextCommonDataId = getSomeTextCommonDataIdFromDB(1);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/text-common-data/"
                        + someTextCommonDataId
                        + "/text-parts"
                        + "?"
                        + "startId=" + someTextCommonDataId
                        + "&"
                        + "size=" + 10))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                /* assert equals of previous_result_element.previousItem and id of given TextCommonData */
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].previousItem", Matchers.is((int)someTextCommonDataId)))
                .andDo(mvcResult -> {
                    mvcResult.getRequest().getParameterMap().forEach((key, value) ->
                            System.out.println("******************************key: " + key + ", value: " + Arrays.toString(value)));
                    MockHttpServletResponse response = mvcResult.getResponse();
                    String json = response.getContentAsString();
                    Arrays.asList(json.split("}")).forEach(System.out::println);
                })
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is("$[0].nextItem")))
                .andReturn();

        /* get textPart array from mvcResult */
        TextPart[] textParts = ((TextPart[]) TestUtil.convertJSONStringArrayToObjectArray(result, TextPart[].class));

        /* check the array order as the main test method expectancy */
        for (int i = 0; i < textParts.length - 1; i++) {
            Assert.assertEquals(textParts[i].getNextItem(), textParts[i + 1].getId());
            System.out.println("************************************************" + i + ": OK");
        }
    }


// ====================================================================================================== TEST UTIL

    /** get list of textCommonData from db, choose one with the given element number and get its id */
    private long getSomeTextCommonDataIdFromDB(int elNumber) throws Exception {
        MvcResult textCommonDataResut = mockMvc
                .perform(MockMvcRequestBuilders.get("/text-common-data"))
                .andReturn();
// fixme: try to use something like jsonPath to get the result
        String json = textCommonDataResut.getResponse().getContentAsString();
        return ((TextCommonData) TestUtil.convertArrayElementOfJSONStringArrayToObject(
                json, TextCommonData[].class, elNumber)).getId();
    }

// --------------------------------------------------------------------------------



}
