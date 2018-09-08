package com.jezh.textsaver.repository;


import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class TextCommonDataRepositoryPostgresTest extends BasePostgresConnectingTest {

    private TextCommonData textCommonData;

    private TextPart textPart;

    @Autowired
    private TextCommonDataRepository textCommonDataRepository;

    @Autowired
    private TextPartRepository textPartRepository;

    @Before
    public void setUp() throws Exception {
        textCommonData = TextCommonData.builder().name("TextCommonDataRepositoryPostgresTest").build();
        textPart = TextPart.builder()
                .body("TextPartRepositoryTest/ testCreate()/ repository.saveAndFlush(textPart)")
                .build();
    }

    @After
    public void tearDown() throws Exception {
        textCommonData = null;
        textPart = null;
    }

    @Test
    public void testCreate() {
        textCommonData.setName(textCommonData.getName() + "/ testCreate()/ textCommonDataRepository.saveAndFlush(textCommonData)");
//        textPartRepository.saveAndFlush(textPart);
//        textCommonData.addTextParts(textPart);
        Assert.assertNotNull(textCommonDataRepository.saveAndFlush(textCommonData));
        System.out.println("*****************************************************************" + textCommonData.getId());
    }

    @Test
    @Transactional
    public void testRetrieve() {
        System.out.println("***********************************************" + textCommonDataRepository.findAll().get(0));
    }

    @Test
    public void testUpdateName() {
        textCommonData = textCommonDataRepository.findAll().get(0);
        textCommonData.setName("updated");
        textCommonDataRepository.saveAndFlush(textCommonData);
    }

    @Test
    public void testSetTextParts_thenTextPartForeignKeysBeUpdated() {
        textCommonData = textCommonDataRepository.findAll().get(0);
        List<TextPart> textPartList = textPartRepository.findAll();
//        int size = textPartList.size();
//        textCommonData.addTextParts(textPartList.toArray(new TextPart[size]));
        textCommonData.setTextParts(new HashSet<>(textPartList));
        textCommonDataRepository.saveAndFlush(textCommonData);
    }

    @Test
    @Transactional
    @Rollback(value = false)// see comments to Set<TextPart> textParts in TextCommonData (to avoid LazyInitializationException)
    public void testAddTextParts() {
        textCommonData = textCommonDataRepository.findAll().get(0);
//        textCommonData.addTextParts(textPartRepository.findById(1L).get());
        textCommonData.addTextParts(TextPart.builder().body("TextCommonDataRepositoryPostgresTest/ testAddTextParts").build());
        textCommonDataRepository.saveAndFlush(textCommonData);
    }

    @Test
    @Transactional
    public void getTextPartsCollection() {
        textCommonDataRepository.findAll().get(0).getTextParts().forEach(System.out::println);
    }
}
