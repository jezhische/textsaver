package com.jezh.textsaver.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DataSourceConfig.class)
@ComponentScan("com.jezh.textsaver")
public class TextsaverApplication {

	public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TextsaverApplication.class, args);
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            System.out.println("******************** " + beanName);
        }
    }
}
