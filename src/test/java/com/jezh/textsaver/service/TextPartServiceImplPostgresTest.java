package com.jezh.textsaver.service;

import com.jezh.textsaver.TextsaverApplication;
import com.jezh.textsaver.entity.TextPart;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * need to create test only on the 'real' db, cause on h2 db I get error: "Error creating bean with name
 * 'inMemoryDatabaseShutdownExecutor'... Syntax error in SQL statement "CREATE INDEX IF NOT EXISTS IDX_NEXT_IT ON
 * PUBLIC.TEXT_PARTS USING[*] HASH (NEXT_ITEM)"; expected "COMMENT, ("
 * */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class TextPartServiceImplPostgresTest {

    @Resource
    private TextPartService service;

    @Resource
    private TextCommonDataService textCommonDataService;

    private TextPart textPart;

    @Before
    public void setUp() throws Exception {
        textPart = TextPart.builder()
                .body("TextPartServiceImplPostgresTest/")
                .textCommonData(textCommonDataService.findAll().get(0))
                .nextItem((long)(Math.random() * 10000))
                .previousItem((long)(Math.random() * 10000))
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
        service.findByTextCommonDataId(27L).forEach(System.out::println);
    }

    @Test
    public void testFindPreviousByCurrentInSequence() {
        textPart = service.findByTextCommonDataId(26L).stream().findAny().get();
        System.out.println("********************************** current textPart: previousItem = " +
                textPart.getPreviousItem() + ", nextItem = " + textPart.getNextItem());
        if (textPart.getPreviousItem() == null) {
            System.out.println("there is no previous item");
            return;
        } else {
        TextPart previousTextPart = service.findPreviousByCurrentInSequence(textPart).get();
        System.out.println("********************************** previous textPart: previousItem = " +
                previousTextPart.getPreviousItem() + ", nextItem = " + previousTextPart.getNextItem());
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
    }
}