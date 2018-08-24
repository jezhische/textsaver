package com.jezh.textsaver.jdbsConnection;

import com.jezh.textsaver.configuration.DataSourceConfig;
import com.jezh.textsaver.configuration.TextsaverApplication;
import com.sun.org.apache.bcel.internal.generic.DMUL;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.validation.constraints.AssertTrue;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {TextsaverApplication.class, DataSourceConfig.class})
public class JdbsConnectionTest {

//    @Resource
//    EntityManagerFactory entityManagerFactory;
//    @Resource
//    private Environment environment;

    private Connection connection;

    private static final String PROP_DATABASE_DRIVER = "org.postgresql.Driver";
    private static final String PROP_DATABASE_PASSWORD = "password";
    private static final String PROP_DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String PROP_DATABASE_USERNAME = "postgres";

    private DriverManager driverManager;

    @Before
    public void setUp() throws Exception {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) connection.close();
    }

    @Test
    public void getAvailableDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) System.out.println(drivers.nextElement().getClass().getName());
    }

    @Test
    public void getConnection() throws SQLException {
        connection = DriverManager.getConnection(
                PROP_DATABASE_URL,
                PROP_DATABASE_USERNAME,
                PROP_DATABASE_PASSWORD
        );
        Assert.assertNotNull(connection);
        Assert.assertTrue(connection.isValid(1000));
    }
}
