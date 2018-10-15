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
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.math.BigInteger;
import java.util.List;

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
        TextPart textPart = TextPart.builder()
                .body("JpaPostgresConnectionTest/ testSaveTextPart()/ entityManager.persist(textPart)")
                .build();
        entityManager.persist(textPart);
        Assert.assertFalse(TestTransaction.isFlaggedForRollback());
        TestTransaction.end();
        TestTransaction.start();
        entityManager.persist(textPart);
    }


    @Test
    public void test_get_textparts_ordered_set_storedFunction() {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("public.get_textparts_ordered_set")
                .registerStoredProcedureParameter("start_id", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("size", int.class, ParameterMode.IN);
        query.setParameter("start_id", 28L).setParameter("size", 4);
        query.execute();
        List<Object[]> results = query.getResultList();
        results.forEach(result ->
        {for (Object o : result)  System.out.println(o);
        });
        Assert.assertTrue((long) (int) (results.get(0)[0]) == 28L);

        // because of currently (temporarily) I have id field in the text_parts table as SERIAL, not BIGSERIAL, so I get
        // this field value as Integer, not BigInteger:
        Assert.assertEquals(results.get(0)[3].getClass(), BigInteger.class);
        Assert.assertEquals(results.get(0)[0].getClass(), Integer.class);
        // so I need casting:
        Assert.assertEquals(results.get(0)[3], BigInteger.valueOf ((int)results.get(1)[0]));
    }

    @Test
    public void testFindTextPartById() {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("get_textpart_by_id")
                .registerStoredProcedureParameter("id", Long.class, ParameterMode.IN);
        query.setParameter("id", 28L);
        query.execute();

    }
}
