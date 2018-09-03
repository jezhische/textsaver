package com.jezh.textsaver.service;

import com.jezh.textsaver.TextsaverApplication;
import com.jezh.textsaver.entity.TextPart;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TextsaverApplication.class)
public class TextPartServiceImplTest {

    @Resource
    TextPartService service;

    private TextPart textPart;

    @Before
    public void setUp() throws Exception {
        textPart = TextPart.builder().body("TextPartServiceImplTest/ ").build();
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
    public void update() {
    }

    @Test
    public void getOne() {
    }

    @Test
    public void findTextPartById() {
    }

    @Test
    public void findAll() {
    }
}