package com.jezh.textsaver.configuration;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
// need either @ContextConfiguration(classes = TextsaverApplication.class),
// or @SpringBootTest(classes = {TextsaverApplication.class}), or @DataJpaTest

// When I use this profile, I get "...'entityManagerFactory': Requested bean is currently in creation: Is there an
// unresolvable circular reference?". So I need to use @Profile and @ActiveProfiles also with the production configuration.
//@ActiveProfiles("testPostgres")

// https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html
//  "By default, @DataJpaTest configures an in-memory embedded database, scans for @Entity classes, and configures
// Spring Data JPA repositories".
@DataJpaTest
// "By default, data JPA tests are transactional and roll back at the end of each test.  If that is not what you want,
// you can disable transaction management for a test or for the whole class as follows:"
@Transactional/*(propagation = Propagation.NOT_SUPPORTED)*/
@Rollback(value = false)
// " If, however, you prefer to run tests against a real database...:"
@AutoConfigureTestDatabase(/*connection = EmbeddedDatabaseConnection.NONE, */replace = AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(value = false)
public class BasePostgresConnectingTest {
}
