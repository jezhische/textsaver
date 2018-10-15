package com.jezh.textsaver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@Import(DataSourceConfig.class)
@ComponentScan("com.jezh.textsaver")
// Since @WebMvcTest throws an error: IllegalArgumentException: At least one JPA metamodel must be present!,
// I moved all the @Enable* annotations to a dedicated @Configuration class  TextsaverConfig.
public class TextsaverApplication {

	public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TextsaverApplication.class, args);
//        for (String beanName : applicationContext.getBeanDefinitionNames()) {
//            System.out.println("******************** " + beanName);
//        }
    }
}
