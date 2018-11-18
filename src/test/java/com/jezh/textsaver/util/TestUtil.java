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
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.repository.TextCommonDataRepository;
import com.jezh.textsaver.repository.TextPartRepository;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class TestUtil {

    public static <T> Object convertJSONStringToObject(String json, Class<T> objectClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); //??
//        JavaTimeModule module = new JavaTimeModule(); //??
//        mapper.registerModule(module);
//mapper.readValues()
        return mapper.readValue(json, objectClass);
    }
// -------------------------------------------------------------------------------------------------

    public static <T> Object convertArrayElementOfJSONStringArrayToObject(
            String jsonArray, Class<T[]> objectArrayClass, int elNumber) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); //??
//        JavaTimeModule module = new JavaTimeModule(); //??
//        mapper.registerModule(module);
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

// -----------------------------------------------------------------------------------------------
    /**
     *  choose random element from text_parts table and assign its id as firstItem in the chosen element
     *  in text_common_data table
     */
    public static Long assignFirstItem(
            TextCommonData textCommonData,
            List<TextPart> textParts) {
        Long randomTextPartId = ((TextPart) getRandomElementFromArray(textParts.toArray())).getId();
//                textParts.stream().findAny().ifPresent(textPart -> {textCommonData.setFirstItem(textPart.getId());});
        textCommonData.setFirstItem(randomTextPartId);
        return randomTextPartId;
    }

    /** Assign foreign key for each element in text_parts table */
    public static List<TextPart> assignTextPartsForeignKey(TextCommonData textCommonData, List<TextPart> textParts) {
        textParts.forEach(textPart -> textPart.setTextCommonData(textCommonData));
        return textParts;
    }

    /** Set an order of textParts by assigning of textPart.nextItem as "every second" order */
    public static List<TextPart> setTextPartsNextItemOrder(List<TextPart> textParts, Long first) {
        // find number of element in list, where textPart.id = first
        int elementNumber = 0;
        for (int i = 0; i < textParts.size(); i++) {
            if (textParts.get(i).getId().equals(first)) {
                elementNumber = i;
                break;
            }
        }
        System.out.println("/////////////////////////////////////////////// elementNumber " + elementNumber); // FIXME: 22.10.2018
        int currentTextPartNumber = 0;
    // set every second element has id = currentTextPart.nextItem, begin from the given elementNumber
        for (int i = elementNumber; i < textParts.size(); i += 2) {
            currentTextPartNumber = i;
            // until end of cycle:
            if (i + 2 < textParts.size()) {
                textParts.get(i).setNextItem(textParts.get(i + 2).getId());
            } 
        }
        // from the beginning
        // if elementNumber is even
        if (elementNumber > 0 && elementNumber % 2 == 0) {
            // first assign the remaining even elements from the beginning
            for (int j = 0; j < elementNumber; j += 2) {
                textParts.get(currentTextPartNumber).setNextItem(textParts.get(j).getId());
                currentTextPartNumber = j;
            }
            // and then all the odd elements from the beginning
            for (int k = 1; k < textParts.size(); k += 2) {
                textParts.get(currentTextPartNumber).setNextItem(textParts.get(k).getId());
                currentTextPartNumber = k;
            }
        }
        // if elementNumber is odd
        else if (elementNumber > 1 && elementNumber % 2 == 1) {
            // first assign the remaining odd elements from the beginning
            for (int l = 1; l < elementNumber; l += 2) {
                textParts.get(currentTextPartNumber).setNextItem(textParts.get(l).getId());
                currentTextPartNumber = l;
            }
            // and then all the even elements from the beginning
            for (int m = 0; m < textParts.size(); m += 2) {
                textParts.get(currentTextPartNumber).setNextItem(textParts.get(m).getId());
                currentTextPartNumber = m;
            }
        }
        return textParts;
    }

    /** assign body to transfer helpful data */
    public static void assignTextPartBodyToTransferHelpfulData(List<TextPart> textParts) {
        textParts.forEach(textPart -> textPart.setBody("id: " + textPart.getId() + " nextId: " + textPart.getNextItem()));
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
                "{\"name\":\"eighth\",\"creatingDate\":\"2018-09-13T06:43:22.345+0000\",\"updatingDate\":\"2018-09-13T17:10:42.199+0000\",\"id\":36}",
                TextCommonData.class));
        System.out.println(data);
        System.out.println(data.getId());
        System.out.println(data.getCreatingDate());
        System.out.println("-----------------------------------------------------------------------");

        String jsonArray = "[{\"name\":\"somename\",\"creatingDate\":\"000\",\"updatingDate\":\"" +
                "0000\",\"id\":37},{\"name\":\"eighth\",\"creatingDate\":\"000\",\"updatingDate\":\"000\",\"id\":36}]";
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
