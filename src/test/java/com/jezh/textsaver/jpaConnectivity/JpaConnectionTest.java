package com.jezh.textsaver.jpaConnectivity;

import com.jezh.textsaver.TextsaverApplication;
import com.jezh.textsaver.entity.TextPart;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@RunWith(SpringRunner.class)
//@SpringBootTest()
// without following I couldn't autowire EntityManagerFactory:
@ContextConfiguration(classes = TextsaverApplication.class)
public class JpaConnectionTest {

//    SpringBootTest.WebEnvironment webEnvironment = SpringBootTest.WebEnvironment.MOCK;
    @Autowired
    EntityManagerFactory entityManagerFactory;

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
        TextPart textPart = new TextPart("JpaConnectionTest/ testSaveTextPart()/ entityManager.persist(textPart)");
        entityManager.persist(textPart);
    }
}


