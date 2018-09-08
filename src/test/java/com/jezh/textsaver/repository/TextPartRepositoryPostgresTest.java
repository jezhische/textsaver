package com.jezh.textsaver.repository;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.TextPart;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


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
                .body("TextPartRepositoryTest/ testCreate()/ textPartRepository.saveAndFlush(textPart)")
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
        TextPart textPart = textPartRepository.findAll().get(0);
        textPart.setBody("updated");
        textPartRepository.saveAndFlush(textPart);
    }

    @Test
    public void testUpdateForeignKey() {
        TextPart textPart = textPartRepository.findAll().get(0);
        textPart.setTextCommonData(textCommonDataRepository.findAll().get(0));
        textPartRepository.saveAndFlush(textPart);
    }
}