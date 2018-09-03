package com.jezh.textsaver.hibernateConnection;

import com.jezh.textsaver.TextsaverApplication;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TextsaverApplication.class)
public class HibernateConnectionTest {

//    @Autowired
    SessionFactory sessionFactory;
}
