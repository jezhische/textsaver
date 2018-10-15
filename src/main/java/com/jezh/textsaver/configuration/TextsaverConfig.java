package com.jezh.textsaver.configuration;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = "com.jezh.textsaver.repository")
@EnableTransactionManagement
// https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html#boot-features-configure-datasource
// "By default, all packages below your main configuration class (the one annotated with @EnableAutoConfiguration or
// @SpringBootApplication) are searched." So there is no need in this annotation if the main class is located in root package.
//@EntityScan(basePackages = "com.jezh.textsaver.entity")

// To activate auditing in the classes marked @EntityListeners(AuditingEntityListener.class)
@EnableJpaAuditing
public class TextsaverConfig {
}
