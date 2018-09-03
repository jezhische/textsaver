package com.jezh.textsaver.jpaConnectivity;

import com.jezh.textsaver.TextsaverApplication;
import com.jezh.textsaver.configuration.TestEntityManagerDataSourcePostgresConfig;
import com.jezh.textsaver.entity.TextPart;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@RunWith(SpringRunner.class)
// need either @ContextConfiguration(classes = TextsaverApplication.class),
// or @SpringBootTest(classes = {TextsaverApplication.class}), or @DataJpaTest

//@SpringBootTest(classes = {TextsaverApplication.class/*, TestEntityManagerDataSourcePostgresConfig.class*/})

// When I use this profile, I get "...'entityManagerFactory': Requested bean is currently in creation: Is there an
// unresolvable circular reference?". So I need to use @Profile and @ActiveProfiles also with the production configuration.
//@ActiveProfiles("testPostgres")

// https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html
//  "By default, @DataJpaTest configures an in-memory embedded database, scans for @Entity classes, and configures
// Spring Data JPA repositories".
@DataJpaTest
// "By default, data JPA tests are transactional and roll back at the end of each test.  If that is not what you want,
// you can disable transaction management for a test or for the whole class as follows:"
//@Transactional(propagation = Propagation.NOT_SUPPORTED) // transaction does not work - ???
@Transactional(propagation = Propagation.REQUIRED)
//@Transactional(propagation = Propagation.SUPPORTS)
// " If, however, you prefer to run tests against a real database...:"
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class JpaPostgresConnectionTest {

    @Autowired
    EntityManagerFactory entityManagerFactory;

//    @Autowired
    private EntityManager entityManager;

// an alternative to the standard JPA EntityManager that is specifically designed for tests:
    @Autowired
    private TestEntityManager testEntityManager;

    @Before
    public void setUp() throws Exception {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Test
    public void testGetConnection() {
        Assert.assertTrue(entityManager.isOpen());
    }

    @Test
    public void assertNotNullTestEntityManager() {
        Assert.assertNotNull(testEntityManager);
    }

    @Test
// https://www.baeldung.com/spring-test-programmatic-transactions
// to flag transaction for commit (to cancel automatic rollback after each test)...
// Note that these methods merely flag the transaction, as their names imply. That is, the transaction isn’t committed
// or rolled back immediately, but only just before it ends:
    @Commit
    public void testSaveTextPart() {
//        TestTransaction.start(); // Cannot start a new transaction without ending the existing transaction first
        TextPart textPart = new TextPart("JpaPostgresConnectionTest/ testSaveTextPart()/ entityManager.persist(textPart)");
        entityManager.persist(textPart);
        Assert.assertFalse(TestTransaction.isFlaggedForRollback());
        TestTransaction.end();
        TestTransaction.start();
        entityManager.persist(textPart);
    }
}
