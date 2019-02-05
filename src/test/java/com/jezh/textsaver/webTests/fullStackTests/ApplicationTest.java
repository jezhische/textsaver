package com.jezh.textsaver.webTests.fullStackTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jezh.textsaver.businessLayer.DataManager;
import com.jezh.textsaver.controller.TextPartController;
import com.jezh.textsaver.dto.BookmarksData;
import com.jezh.textsaver.dto.TextPartResource;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
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
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private DataManager dataManager;

    private TextPart textPartTwo, textPartThree, textPartFour;
    private TextCommonData textCommonData;
    private Long existingTextCommonDataId;
    private TextPartResource textPartResource;
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

        existingTextCommonDataId = /*getSomeTextCommonDataIdFromDB(0)*/ 36L; // FIXME: 15.11.2018 method returns null - why?

        textCommonData = TextCommonData
                .builder()
                .name("testTextCommonData")
                .build();
        textCommonData.setId(existingTextCommonDataId);

        textPartResource = TextPartResource.builder()
                .body("textPartResource application test")
                .lastUpdate(new Date())
//                .nextItem(new Random().nextLong())
//                .textCommonData(textCommonData)
                .build();

        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() throws Exception {
        textPartTwo = null;
        textPartThree = null;
        textPartFour = null;
        textCommonData = null;
        textPartResource = null;
    }

// =============================================================================================== textCommonData tests


    @Test
    public void testInetAddress() throws UnknownHostException {
        String port = env.getProperty("server.port");
        String lPort = env.getProperty("local.server.port");
        String hostName = InetAddress.getLocalHost().getHostName();
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        System.out.println("*****************" + port + " " + hostName + " " + hostAddress);
        System.out.println("**********************************" + lPort);
    }

    @Test
    public void testCreate_UriComponentBuilder_build() throws UnknownHostException, NoHandlerFoundException {
        String id = "25";
        String port = env.getRequiredProperty("local.server.port"); // with unknown reason property "server.port" in
        // spring boot 2 returns "-1", so this is a crutch
        String contextPath = env.getRequiredProperty("server.servlet.context-path");
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        String uri = UriComponentsBuilder.newInstance().scheme("http")
                .host(hostAddress).port(port).path(contextPath).path("/doc-data/{commonDataId}/pages")
                .query("page={currentPageNumber}").buildAndExpand(id, "1").toString();
        assertEquals("http://192.168.1.5:8074/textsaver/doc-data/25/pages?page=1", uri);

        String pageLink = dataManager.createPageLink( 25L, 1);
        assertEquals("http://192.168.1.5:8074/textsaver/doc-data/25/pages?page=1", pageLink);

        System.out.println("********************************" + uri);
        System.out.println("*******************************************" + pageLink);
        System.out.println("*****" + linkTo(methodOn(TextPartController.class)
                .findPage(55L, 4))
                .toUriComponentsBuilder().port(port).toUriString());

    }

    @Test
    @WithMockUser
    public void getBookmarks() throws Exception {
        mockMvc
                .perform(post("/doc-data/1/bookmarks")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(new ObjectMapper().writeValueAsString(
                        BookmarksData.builder().currentPageNumber(5).totalPages(10).build())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /** find all the textCommonData */
    @Test
    public void testGetTextCommonDataList() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/doc-data"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]", Matchers.allOf(
                        Matchers.hasKey("id"), Matchers.hasKey("name"),
                        Matchers.hasKey("createdDate"), Matchers.hasKey("updatedDate"))))
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
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
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
                .andDo(print())
                .andExpect(status().isOk())
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

//        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
//        String json = objectWriter.writeValueAsString(textPartResource);
        String json = TestUtil.convertObjectToJSONString(textPartResource);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/text-common-data/" + existingTextCommonDataId + "/text-parts")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        // have got Status = 415 without following:
                        .contentType(MediaTypes.HAL_JSON)
                        .content(json))
                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        System.out.println("******************************************" + result.getResponse().getContentAsString());
        System.out.println("****************************" + env.getRequiredProperty("local.server.port", Integer.class));
    }

// ------------------------------------------------------------------------------------------------------

    @Test
    public void test_unique_next_item_for_null_whenCreateTextPart() throws Exception {
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(TextPartResource.builder()
                .body("test_unique_next_item_for_null_whenCreateTextPart")
                .lastUpdate(new Date())
//                .nextItem(null)
                .build());
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/text-common-data/" + existingTextCommonDataId + "/text-parts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        System.out.println("******************************************" + result.getResponse().getContentAsString());
        System.out.println("****************************" + env.getRequiredProperty("local.server.port", Integer.class));
    }

// ---------------------------------------------------------------------------------------------------------------

    @Test
    public void testUpdatePage() throws Exception {
        mockMvc.perform(put("/text-common-data/36/text-parts/pages?page=49")
//                .contentType(MediaTypes.HAL_JSON_VALUE)
                .content("{\"body\":\"new body - updated: id: 41 nextId: 93\"}")
        )
                .andDo(print());
    }

// ---------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetLinkedTextCommonDataList() throws Exception {
        mockMvc.perform(get("/documents"))
                .andDo(print());
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
                .andDo(print())
                .andExpect(status().isBadRequest())
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
        MvcResult textCommonDataResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/text-common-data"))
                .andReturn();
// FIXME: 24.10.2018 try to use something like jsonPath to get the result
        String json = textCommonDataResult.getResponse().getContentAsString();
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
