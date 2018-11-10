package com.jezh.textsaver.webTests.fullStackTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jezh.textsaver.controller.TextPartController;
import com.jezh.textsaver.dto.TextPartDTO;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.exception.ApiExceptionDetails;
import com.jezh.textsaver.service.TextCommonDataService;
import com.jezh.textsaver.service.TextCommonDataServiceTest;
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
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.xml.ws.Response;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Random;

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

    @Autowired
    private Environment env;

    private TextPart textPartTwo, textPartThree, textPartFour;
    private TextCommonData textCommonData;
    private Long existingTextCommonDataId;
    private TextPartDTO textPartDTO;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        textPartTwo = TextPart
                .builder()
                .body("test")
                .nextItem(4L)
                .lastUpdate(new Date())
                .build();
        textPartTwo.setId(2L);
        textPartThree = TextPart.builder().build();
        textPartThree.setId(3L);
        textPartFour = TextPart.builder().nextItem(3L).build();
        textPartFour.setId(4L);

        existingTextCommonDataId = getSomeTextCommonDataIdFromDB(1);

        textCommonData = TextCommonData
                .builder()
                .name("testTextCommonData")
                .build();
        textCommonData.setId(existingTextCommonDataId);

        textPartDTO = TextPartDTO.builder()
                .body("textPartDTO application test")
                .lastUpdate(new Date())
                .nextItem(new Random().nextLong())
                .textCommonData(textCommonData)
                .build();

        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() throws Exception {
        textPartTwo = null;
        textPartThree = null;
        textPartFour = null;
        textCommonData = null;
        textPartDTO = null;
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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id",
                        Matchers.is((int)getFirstItemFromSomeTextCommonData(someTextCommonDataId))))
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

// -----------------------------------------------------------------------------------------

    @Test
    public void testCreateTextPart() throws Exception {
//        existingTextCommonDataId = getSomeTextCommonDataIdFromDB(0);

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(textPartDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/text-common-data/" + existingTextCommonDataId + "/text-parts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        System.out.println("******************************************" + result.getResponse().getContentAsString());
        System.out.println("****************************" + env.getRequiredProperty("local.server.port", Integer.class));
    }

// ------------------------------------------------------------------------------------------------------

    @Test
    public void test_unique_next_item_for_null_whenCreateTextPart() throws Exception {
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(TextPartDTO.builder()
                .body("test_unique_next_item_for_null_whenCreateTextPart")
                .lastUpdate(new Date())
                .nextItem(null)
                .textCommonData(textCommonData)
                .build());
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/text-common-data/" + existingTextCommonDataId + "/text-parts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        System.out.println("******************************************" + result.getResponse().getContentAsString());
        System.out.println("****************************" + env.getRequiredProperty("local.server.port", Integer.class));
    }


// ==================================================================================== TEST CustomRestExceptionHandler

// ------------------------------------------------------------------------------------------------------- 400
    @Test
    public void testHandleMethodArgumentTypeMismatch_whenStringInsteadOfLong_thenBadRequest() throws Exception {
        long someTextCommonDataId = getSomeTextCommonDataIdFromDB(1);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/text-common-data/"
                        + someTextCommonDataId
                        + "/text-parts"
                        // here String instead of Long
                        + "/ccc"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("should be of type")))
                .andReturn();
        Assert.assertEquals(TextPartController.class, ((HandlerMethod) result.getHandler()).getBeanType());
        Assert.assertEquals(MethodArgumentTypeMismatchException.class, result.getResolvedException().getClass());
    }

// ----------------------------------------------------------------------------------------------------- 404
//    @Test
//    public void testHandleNoHandlerFoundException() throws Exception {
//        long someTextCommonDataId = getSomeTextCommonDataIdFromDB(1);
//        MvcResult result = mockMvc
//                .perform(MockMvcRequestBuilders.get("/text-common-data/"
//                        + someTextCommonDataId
//                        + "/text-partsyy"
//                        + "/1"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
////                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("No handler found for")))
//                .andReturn();
////        Assert.assertEquals(NoHandlerFoundException.class, result.getResolvedException().getClass());
//    }

// ----------------------------------------------------------------------------------------------------- 404
//    @Test/*(expected = NoHandlerFoundException.class)*/
//    public void whenFindTextPartByIdNotFound_thenNoHandlerFoundException() throws Exception {
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("textsaver/text-common-data/36/text-parts/1"))
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//        MockHttpServletResponse response = result.getResponse();
//        System.out.println("***********************" + response.getContentAsString());
////        ApiExceptionDetails details = ((ApiExceptionDetails) TestUtil.convertJSONStringToObject(response.getContentAsString(), ApiExceptionDetails.class));
//    }


// --------------------------------------------------------------------------------

// ====================================================================================================== TEST UTIL

    /** get list of textCommonData from db, choose one with the given element number and get its id */
    private long getSomeTextCommonDataIdFromDB(int elNumber) throws Exception {
        MvcResult textCommonDataResut = mockMvc
                .perform(MockMvcRequestBuilders.get("/text-common-data"))
                .andReturn();
// FIXME: 24.10.2018 try to use something like jsonPath to get the result
        String json = textCommonDataResut.getResponse().getContentAsString();
        return ((TextCommonData) TestUtil.convertArrayElementOfJSONStringArrayToObject(
                json, TextCommonData[].class, elNumber)).getId();
    }

// --------------------------------------------------------------------------------

    /** get textCommonData.firstItem */
    private long getFirstItemFromSomeTextCommonData(long someTextCommonDataId) throws Exception {
        MvcResult textCommonDataResut = mockMvc
                .perform(MockMvcRequestBuilders.get("/text-common-data/" + someTextCommonDataId))
                .andReturn();
        String json = textCommonDataResut.getResponse().getContentAsString();
        return ((TextCommonData) TestUtil.convertJSONStringToObject(json, TextCommonData.class)).getFirstItem();
    }
// --------------------------------------------------------------------------------





}
