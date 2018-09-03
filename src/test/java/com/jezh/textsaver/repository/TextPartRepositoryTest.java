package com.jezh.textsaver.repository;

import com.jezh.textsaver.TextsaverApplication;
import com.jezh.textsaver.entity.TextPart;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TextsaverApplication.class)
// если  использовать вместо @ContextConfiguration аннотацию @DataJpaTest, получим "Последовательность "TEXT_PARTS_ID_SEQ"
// не найдена", поскольку для TextPart id установлена @GeneratedValue(strategy = GenerationType.IDENTITY), а не AUTO.
// Эта последовательность работает для postgresql, но не работет для h2db, которую spring boot использует в тестах
//@DataJpaTest
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
        TextPart textPart = TextPart.builder()/*.id(444L)*/
                .body("TextPartRepositoryTest/ testCreate()/ repository.saveAndFlush(textPart)").build();
        Assert.assertNotNull(repository.saveAndFlush(textPart));
        System.out.println("*****************************************************************" + textPart.getId());
    }

    @Test
    public void testRetrieveOne() {
        Assert.assertEquals("TUaIVJRQKa", repository.findById(226L).orElse(new TextPart()).getBody());
    }

    @Test
    public void testUpdateOne() {
        TextPart textPart = repository.findById(226L).orElseThrow(() -> new RuntimeException("the textPart item with this id is not found"));
        textPart.setBody("TUaIVJRQKa");
        repository.save(textPart);
    }
}