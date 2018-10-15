package com.jezh.textsaver.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jezh.textsaver.entity.TextCommonData;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class TestUtil {

    public static <T> Object convertJSONStringToObject(String json, Class<T> objectClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); //??
        JavaTimeModule module = new JavaTimeModule(); //??
        mapper.registerModule(module);
//mapper.readValues()
        return mapper.readValue(json, objectClass);
    }
// -------------------------------------------------------------------------------------------------

    public static <T> Object convertArrayElementOfJSONStringArrayToObject(
            String jsonArray, Class<T[]> objectArrayClass, int elNumber) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); //??
        JavaTimeModule module = new JavaTimeModule(); //??
        mapper.registerModule(module);
        T[] tcdArray = mapper.readValue(jsonArray, objectArrayClass);
        return elNumber < tcdArray.length? tcdArray[elNumber] : null;
    }
// ------------------------------------------------------------------------------
    // if return T[], get "Cannot select from a type variable" in the last line.
    public static <T> Object[] convertJSONStringArrayToObjectArray(MvcResult mvcResult, Class<T[]> objectArrayClass)
    {
        String json;
        try {
            json = mvcResult.getResponse().getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, objectArrayClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

// fixme: com.fasterxml.jackson.databind.exc.MismatchedInputException: No content to map due to end-of-input
// at [Source: (String)""; line: 1, column: 0]
//    public static <T> Object convertElementOfJSONStringArrayToObject(
//            String jsonArray, Class<T> objectArrayClass, int elNumber) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); //??
//        JavaTimeModule module = new JavaTimeModule(); //??
//        mapper.registerModule(module);
//// -------------------------------------------------------------------------------------
//        JsonParser parser = mapper.getFactory().createParser(jsonArray);
//        if(parser.nextToken() != JsonToken.START_ARRAY) {
//            throw new IllegalStateException("Expected an array");
//        }
////        JsonToken token = parser.nextToken();
////        if (token == null) return null;
////        // START_ARRAY is returned when encountering '[' which signals starting of an Array value
////        if (!JsonToken.START_ARRAY.equals(token)) return null;
//
//        int i = 0;
//        String searchResult = null;
//        while (parser.nextToken() == JsonToken.START_OBJECT) {
//            ObjectNode node = mapper.readTree(parser);
////            token = parser.nextToken();
////            // START_OBJECT is returned when encountering '{' which signals starting of an Object value
////            if (!JsonToken.START_OBJECT.equals(token)) {
////                return null;
////            }
////            if (token == null) {
////                return null;
////            }
////            if (i == elNumber) {
////                searchResult = token.asString();
////                i++;
////                break;
////            }
//            if (i == elNumber) {
//                searchResult = node.size() == 0? null : node.asText();
//                break;
//            }
//            i++;
//        }
//        return searchResult == null? null : mapper.readValue(searchResult, TextCommonData.class);
//// -------------------------------------------------------------------------------------------------------
//    }

// ------------------------------------------------------------------------------------
    public static String convertObjectToJSONString(Object o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }

// --------------------------------------------------------------------------

//    private static HttpMessageConverter mappingJackson2HttpMessageConverter;
//    public static String json(Object o) throws IOException {
//        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
//        mappingJackson2HttpMessageConverter.write(
//                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
//        return mockHttpOutputMessage.getBodyAsString();
//    }

// -----------------------------------------------------------------------------------------------
    /** get a random element from array */
    public static <T> T getRandomElementFromArray(T[] array) {
        Random random = new Random();
        int randomElementNumber = random.nextInt(array.length);
        /* for test purpose, chosen element must not be last in array */
        if (randomElementNumber != array.length - 1)
        return array[randomElementNumber];
        else return getRandomElementFromArray(array);
    }

    /** get a random element number from array */
    public static <T> int getRandomElementNumberFromArray(T[] array) {
        Random random = new Random();
        int randomElementNumber = random.nextInt(array.length);
        /* for test purpose, chosen number must not be last in array */
        if (randomElementNumber != array.length - 1)
            return randomElementNumber;
        else return getRandomElementNumberFromArray(array);
    }

// ============================================================================================================ TESTS
String jsonArray = "[{\"name\":\"TextCommonDataRepositoryPostgresTest/ testCreate()/ textCommonDataRepository." +
        "saveAndFlush(textCommonData)\",\"creatingDate\":\"2018-09-13T06:43:34.656+0000\",\"updatingDate\":\"" +
        "2018-09-13T06:43:34.656+0000\",\"id\":27},{\"name\":\"eighth\",\"creatingDate\":\"2018-09-13T06:43:22." +
        "345+0000\",\"updatingDate\":\"2018-09-13T17:10:42.199+0000\",\"id\":26}]\n";

    @Test
    public void testConvertJSONStringToObject() throws Exception {
        TextCommonData data = ((TextCommonData) convertJSONStringToObject(
                "{\"name\":\"eighth\",\"creatingDate\":\"2018-09-13T06:43:22.345+0000\",\"updatingDate\":\"2018-09-13T17:10:42.199+0000\",\"id\":26}",
                TextCommonData.class));
        System.out.println(data);
        System.out.println(data.getId());
        System.out.println(data.getCreatingDate());
    }

    @Test
    public void testGetRandomElementFromArray() {
        Integer[] array = new Integer[3];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        System.out.println(getRandomElementFromArray(array));
    }

    @Test
    public void testConvertJSONStringArrayToObjectArray() {

    }

    public static void main(String[] args) throws Exception {
        TextCommonData data = ((TextCommonData) convertJSONStringToObject(
                "{\"name\":\"eighth\",\"creatingDate\":\"2018-09-13T06:43:22.345+0000\",\"updatingDate\":\"2018-09-13T17:10:42.199+0000\",\"id\":26}",
                TextCommonData.class));
        System.out.println(data);
        System.out.println(data.getId());
        System.out.println(data.getCreatingDate());
        System.out.println("-----------------------------------------------------------------------");

        String jsonArray = "[{\"name\":\"TextCommonDataRepositoryPostgresTest/ testCreate()/ textCommonDataRepository." +
                "saveAndFlush(textCommonData)\",\"creatingDate\":\"2018-09-13T06:43:34.656+0000\",\"updatingDate\":\"" +
                "2018-09-13T06:43:34.656+0000\",\"id\":27},{\"name\":\"eighth\",\"creatingDate\":\"2018-09-13T06:43:22." +
                "345+0000\",\"updatingDate\":\"2018-09-13T17:10:42.199+0000\",\"id\":26}]\n";
        TextCommonData tcd = ((TextCommonData) convertArrayElementOfJSONStringArrayToObject(jsonArray,
                TextCommonData[].class, 1));
        System.out.println(tcd);
        System.out.println(tcd.getId());

//        tcd = ((TextCommonData) convertElementOfJSONStringArrayToObject(jsonArray, TextCommonData.class, 1));
        System.out.println("-----------------------------------------------------------------------");

        String dataToJsonString = convertObjectToJSONString(data);
        System.out.println(dataToJsonString);
    }
}
