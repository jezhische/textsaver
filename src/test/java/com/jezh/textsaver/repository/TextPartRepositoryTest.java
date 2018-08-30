package com.jezh.textsaver.repository;

import com.jezh.textsaver.configuration.TextsaverApplication;
import com.jezh.textsaver.entity.TextPart;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TextsaverApplication.class)
public class TextPartRepositoryTest {

    @Autowired
    TextPartRepository repository;
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreate() {
        TextPart textPart = TextPart.builder().id(444L).body("hururu").build();
        Assert.assertNotNull(repository.saveAndFlush(textPart));
    }

    @Test
    public void testRetrieveOne() {
        Assert.assertEquals("TUaIVJRQKa", repository.findById(226L).get().getBody());
    }

    @Test
    public void testUpdateOne() {
        TextPart textPart = repository.findById(226L).orElse(null);
        textPart.setBody("TUaIVJRQKa");
        repository.save(textPart);
    }
}