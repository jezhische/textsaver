package com.jezh.textsaver;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@SpringBootApplication
/*(exclude = ErrorMvcAutoConfiguration.class)*/
//@Import(DataSourceConfig.class)
@ComponentScan("com.jezh.textsaver")
// Since @WebMvcTest throws an error: IllegalArgumentException: At least one JPA metamodel must be present!,
// I moved all the @Enable* annotations to a dedicated @Configuration class  DataSourceConfig.
public class TextsaverApplication {

	public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TextsaverApplication.class, args);
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            System.out.println("******************** " + beanName);
        }

        // to handle NoHandlerFoundException
        DispatcherServlet dispatcherServlet = (DispatcherServlet)applicationContext.getBean("dispatcherServlet");
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
//        dispatcherServlet.getHandlerMappings()
//                .forEach(handlerMapping -> System.out.println("==========================" + handlerMapping));
//        dispatcherServlet.setDetectAllHandlerExceptionResolvers(false);
    }

    @Bean
    public MapperFactory mapperFactory() {
	    return new DefaultMapperFactory.Builder().build();
    }
}
