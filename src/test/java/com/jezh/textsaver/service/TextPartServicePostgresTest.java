package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.TextPart;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

/**
 * need to create test only on the 'real' db, cause on h2 db I get error: "Error creating bean with username
 * 'inMemoryDatabaseShutdownExecutor'... Syntax error in SQL statement "CREATE INDEX IF NOT EXISTS IDX_NEXT_IT ON
 * PUBLIC.TEXT_PARTS USING[*] HASH (NEXT_ITEM)"; expected "COMMENT, ("
 * */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.REQUIRED)
@Rollback(value = false)
public class TextPartServicePostgresTest {

    @Resource
    private TextPartService service;

    @Resource
    private TextCommonDataService textCommonDataService;

    private TextPart textPart;

    @Before
    public void setUp() throws Exception {
        textPart = TextPart.builder()
                .body("TextPartServicePostgresTest/")
                .textCommonData(textCommonDataService.findAll().get(0))
                .nextItem((long)(Math.random() * 10000))
                .build();
    }

    @After
    public void tearDown() throws Exception {
        textPart = null;
    }

    @Test
    public void testCreate() {
        textPart.setBody(textPart.getBody() + "testCreate()/ service.create(textPart)");
        Assert.assertNotNull(service.create(textPart));
        System.out.println("*****************************************************************" + textPart.getId());
        Assert.assertNotNull(service.getOne(textPart.getId()));
    }

    @Test
    public void testFindByTextCommonDataId() {
        service.findSortedSetByTextCommonDataId(27L).forEach(System.out::println);
    }

//    @Test
//    public void testFindPreviousByCurrentInSequence() {
//        textPart = service.findSortedSetByTextCommonDataId(26L).stream().findAny().get();
//        System.out.println("********************************** current textPart: previousItem = " +
//                textPart.getPreviousItem() + ", nextItem = " + textPart.getNextItem());
//        if (textPart.getPreviousItem() == null) {
//            System.out.println("there is no previous item");
//            return;
//        } else {
//        TextPart previousTextPart = service.findPreviousByCurrentInSequence(textPart).get();
//        System.out.println("********************************** previous textPart: previousItem = " +
//                previousTextPart.getPreviousItem() + ", nextItem = " + previousTextPart.getNextItem());
//        }
//    }

    @Test
    public void testFindSortedTextPartBunchByStartId() {
        List<TextPart> result = service.findSortedTextPartBunchByStartId(28L, 10);
        result.forEach(System.out::println);
        for (int i = 0; i < result.size() - 1; i++) {
            Assert.assertEquals(result.get(i).getNextItem(), result.get(i + 1).getId());
        }
    }

    @Test
    public void testFindRemainingSortedTextPartBunchByStartId() {
        List<TextPart> result = service.findRemainingSortedTextPartBunchByStartId(50L);
        result.forEach(System.out::println);
        for (int i = 0; i < result.size() - 1; i++) {
            Assert.assertEquals(result.get(i).getNextItem(), result.get(i + 1).getId());
        }
    }

    @Test
    public void testUpdate() {
    }
    @Test
    public void testFindAll() {
        service.findAll().forEach(System.out::println);
    }

    @Test
    public void testGetOne() {
        Long id = service.findAll().get(0).getId();
        System.out.println(service.getOne(id));
    }

    @Test
    public void testFindTextPartById() {
        service.create(textPart);
        Assert.assertEquals(textPart, service.findTextPartById(textPart.getId()).get());
    }

// ====================================================================================================== page tests


    @Test
    public void updateById() {
        service.updateById(1736L, "updated from test", new Date());
    }

    @Test
    public void createPage() throws NoHandlerFoundException, UnknownHostException {
        System.out.println("******************************************************************" +
                service.createPage(2, 1469L).getContent().get(0));
    }

    @Test
    public void findPageByDocDataIdAndPageNumber() {
        System.out.println("******************************************************************" +
                service.findPageByDocDataIdAndPageNumber(1456L, 1).getContent().get(0));
    }

    @Test
    public void delete() throws UnknownHostException, NoHandlerFoundException {
        service.delete(2101L, 9);
    }
}