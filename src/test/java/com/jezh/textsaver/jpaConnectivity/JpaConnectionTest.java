package com.jezh.textsaver.jpaConnectivity;

import com.jezh.textsaver.TextsaverApplication;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.jpaTestUtils.JpaTestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

@RunWith(SpringRunner.class)
//@SpringBootTest()
// without following I couldn't autowire EntityManagerFactory:
@ContextConfiguration(classes = TextsaverApplication.class)
public class JpaConnectionTest {

//    SpringBootTest.WebEnvironment webEnvironment = SpringBootTest.WebEnvironment.MOCK;
    @Autowired
    EntityManagerFactory entityManagerFactory;

//    @PersistenceContext
    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Test
    public void testGetConnection() {
        Assert.assertTrue(entityManager.isOpen());
    }

    @Test
    public void testSaveTextPart() {
        TextPart textPart = TextPart.builder()
                .body("JpaConnectionTest/ testSaveTextPart()/ entityManager.persist(textPart)")
                .build();
        entityManager.persist(textPart);
    }
}


