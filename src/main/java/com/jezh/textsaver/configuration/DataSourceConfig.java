package com.jezh.textsaver.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = "com.jezh.textsaver.repository")
@EnableTransactionManagement
// To activate auditing in the classes marked @EntityListeners(AuditingEntityListener.class)
@EnableJpaAuditing
// https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html#boot-features-configure-datasource
// Since AbstractIdentifier class is located in the directory "extension", different from "entity" directory,
// there is need in this scan:
@EntityScan("com.jezh.textsaver.entity.extension")
public class DataSourceConfig {
}
