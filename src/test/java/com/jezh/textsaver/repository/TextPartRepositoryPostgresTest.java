package com.jezh.textsaver.repository;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.TextPart;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;


public class TextPartRepositoryPostgresTest extends BasePostgresConnectingTest {

    private TextPart textPart;
    @Autowired
    private TextPartRepository textPartRepository;
    @Autowired
    private TextCommonDataRepository textCommonDataRepository;
    // an alternative to the standard JPA EntityManager that is specifically designed for tests:
    @Autowired
    private TestEntityManager testEntityManager;
    
    @Before
    public void setUp() throws Exception {
        textPart = TextPart.builder()
                .body("TextPartRepositoryPostgresTest/")
                .build();
    }

    @After
    public void tearDown() throws Exception {
        textPart = null;
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(textPartRepository.saveAndFlush(textPart));
        System.out.println("*****************************************************************" + textPart.getId());
    }

    @Test
    public void testRetrieveOne() {
        textPart = textPartRepository.findAll().get(0);
        Assert.assertEquals(textPart.getBody(), textPartRepository.findById(textPart.getId()).orElse(new TextPart()).getBody());
    }

    @Test
    public void testUpdateOne() {
        textPart = textPartRepository.findAll().get(0);
        textPart.setBody("updated");
        textPartRepository.saveAndFlush(textPart);
    }

    @Test
    public void testUpdateForeignKey() {
        textPart = textPartRepository.findAll().get(0);
//        TextPart textPart = textPartRepository.findById(5L).get();
//        textPart.setTextCommonData(null);
        textPart.setTextCommonData(textCommonDataRepository.findAll().get(0));
        textPartRepository.saveAndFlush(textPart);
    }

    @Test
    public void testFindByPreviousItem() {
        TextPart lastSavedTextPart = textPartRepository.findAll().get((int) textPartRepository.count() - 1);
// to avoid NullPointerException in lastSavedTextPart.toString(), since some properties can be equals null -----------:
        lastSavedTextPart.setPreviousItem(1L);
        lastSavedTextPart.setNextItem(2L);
        lastSavedTextPart.setTextCommonData(textCommonDataRepository.findAll().get(0));
        System.out.println("***********************************************" + lastSavedTextPart);
// --------------------------------------------------------------------------------------------------------------------
        Long previous = lastSavedTextPart.getId();
        textPart = TextPart.builder().previousItem(previous).nextItem(3L).body("TextPartRepositoryPostgresTest/")
                .textCommonData(textCommonDataRepository.findAll().get(0)).build();
        textPartRepository.saveAndFlush(this.textPart);
        System.out.println("***********************************************" + textPart);
        Optional<TextPart> byPreviousItem = textPartRepository.findByPreviousItem(previous.intValue());
        TextPart part = byPreviousItem.get();
        Assert.assertNotNull(part);
        Assert.assertTrue(part.getPreviousItem() == previous);
        System.out.println("***********************************************" + part);
    }

    @Test
    public void testOptional() {
        textPart.setPreviousItem(27L);
        textPartRepository.saveAndFlush(textPart);
        System.out.println(textPartRepository.findByPreviousItem(27).get().getId());
    }
}