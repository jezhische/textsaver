package com.jezh.textsaver.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = "com.jezh.textsaver.repository")
@EnableTransactionManagement
// To activate auditing in the classes marked @EntityListeners(AuditingEntityListener.class), see TextPart.class
@EnableJpaAuditing
public class DataSourceConfig {
}
