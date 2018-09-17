package com.jezh.textsaver.repository;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.TextPart;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .textCommonData(textCommonDataRepository.findAll().get(0))
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
        Assert.assertNotNull(textPartRepository.saveAndFlush(textPart));
        System.out.println("*****************************************************************" + textPart.getId());
    }

    @Test
    public void testCreateFiveItems() {
        for (int i = 0; i < 5; i++) {
            textPart = null;
            textPart = TextPart.builder()
                    .body("TextPartRepositoryPostgresTest/")
                    .textCommonData(textCommonDataRepository.findAll().get(0))
                    .nextItem((long)(Math.random() * 10000))
                    .previousItem((long)(Math.random() * 10000))
                    .build();
            textPartRepository.saveAndFlush(textPart);
        }
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
//        textPart = textPartRepository.findAll().get(0);
        TextPart textPart = textPartRepository.findById(5L).get();
//        textPart.setTextCommonData(null);
        textPart.setTextCommonData(textCommonDataRepository.findAll().get(0));
        textPartRepository.saveAndFlush(textPart);
    }

    @Test
    public void testFindByPreviousItem() {
        TextPart lastSavedTextPart = textPartRepository.findAll().get((int) textPartRepository.count() - 1);

        Long previous = lastSavedTextPart.getId();
        textPart.setPreviousItem(previous);
        textPartRepository.saveAndFlush(textPart);
        System.out.println("***********************************************" + textPart);
        Optional<TextPart> byPreviousItem = textPartRepository.findByPreviousItem(previous);
        TextPart part = byPreviousItem.get();
        Assert.assertNotNull(part);
        Assert.assertTrue(part.getPreviousItem() == previous);
        System.out.println("***********************************************" + part);
    }

    @Test
    @Transactional
    public void testFindByTextCommonDataName() {
        List<TextPart> textPartList = textPartRepository.findByTextCommonDataName("eighth");
        textPartList.forEach(textPart1 -> System.out.println("id: " + textPart1.getId() +
        ", foreignKey: " + textPart1.getTextCommonData().getId() + ": " + textPart1.getTextCommonData().getName()));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testPreviousItemUniqueConstraint() {
        TextPart lastSavedTextPart = textPartRepository.findAll().get((int) textPartRepository.count() - 1);
        textPart.setPreviousItem(lastSavedTextPart.getPreviousItem());
        textPartRepository.saveAndFlush(textPart);
    }

//    @Test
//    public void testFindBySequence() {
//        textPart = textPartRepository.findByTextCommonDataName("eighth").stream().findAny().get();
//        System.out.println("********************************** previous textPart: previousItem = " +
//        textPart.getPreviousItem() + ", nextItem = " + textPart.getNextItem());
//        Long nextItem = textPart.getNextItem();
//        textPart = textPartRepository.findNextByCurrentInSequence(nextItem).get();
//        System.out.println("********************************** next textPart: previousItem = " +
//                textPart.getPreviousItem() + ", nextItem = " + textPart.getNextItem());
//    }
@Test
public void testFindNextByCurrentInSequence() {
    textPart = textPartRepository.findByTextCommonDataName("eighth").stream().findAny().get();
    System.out.println("********************************** previous textPart: previousItem = " +
            textPart.getPreviousItem() + ", nextItem = " + textPart.getNextItem());
//    Long nextItem = textPart.getNextItem();
    TextPart nextTextPart = textPartRepository.findNextByCurrentInSequence(textPart).get();
    System.out.println("********************************** next textPart: previousItem = " +
            nextTextPart.getPreviousItem() + ", nextItem = " + nextTextPart.getNextItem());
}

//    @Test
//    public void testIndexPreviousItems() {
//        textPartRepository.indexPreviousItems();
//    }
//
//
//    @Test
//    public void testDropIndexPreviousItems() {
//        textPartRepository.dropIndexPreviousItems();
//    }
}