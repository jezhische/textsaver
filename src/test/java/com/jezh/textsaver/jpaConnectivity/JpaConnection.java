package com.jezh.textsaver.jpaConnectivity;

import com.jezh.textsaver.configuration.TextsaverApplication;
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
public class JpaConnection {

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
}
