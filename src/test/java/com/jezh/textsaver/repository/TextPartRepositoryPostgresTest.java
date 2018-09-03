package com.jezh.textsaver.repository;

import com.jezh.textsaver.TextsaverApplication;
import com.jezh.textsaver.entity.TextPart;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
// need either @ContextConfiguration(classes = TextsaverApplication.class),
// or @SpringBootTest(classes = {TextsaverApplication.class}), or @DataJpaTest

// When I use this profile, I get "...'entityManagerFactory': Requested bean is currently in creation: Is there an
// unresolvable circular reference?". So I need to use @Profile and @ActiveProfiles also with the production configuration.
//@ActiveProfiles("testPostgres")

// todo: https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html
//  "By default, @DataJpaTest configures an in-memory embedded database, scans for @Entity classes, and configures
// Spring Data JPA repositories".
@DataJpaTest
// "By default, data JPA tests are transactional and roll back at the end of each test.  If that is not what you want,
// you can disable transaction management for a test or for the whole class as follows:"
@Transactional(propagation = Propagation.NOT_SUPPORTED)
// " If, however, you prefer to run tests against a real database...:"
@AutoConfigureTestDatabase(/*connection = EmbeddedDatabaseConnection.NONE, */replace = AutoConfigureTestDatabase.Replace.NONE)
public class TextPartRepositoryPostgresTest {

    private TextPart textPart;
    @Autowired
    private TextPartRepository repository;
    // an alternative to the standard JPA EntityManager that is specifically designed for tests:
    @Autowired
    private TestEntityManager testEntityManager;
    
    @Before
    public void setUp() throws Exception {
        textPart = TextPart.builder()
                .body("TextPartRepositoryTest/ testCreate()/ repository.saveAndFlush(textPart)")
                .build();
    }

    @After
    public void tearDown() throws Exception {
        textPart = null;
    }

    @Test
    public void testCreate() {
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