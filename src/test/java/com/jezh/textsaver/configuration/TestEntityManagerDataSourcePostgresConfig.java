package com.jezh.textsaver.configuration;

import com.jezh.textsaver.repository.TextPartRepository;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("com.jezh.textsaver.repository")
@EnableTransactionManagement
@ComponentScan(basePackages = "com.jezh.textsaver")
//@EntityScan(basePackages = "com.jezh.textsaver.entity")
@Profile("testPostgres")
public class TestEntityManagerDataSourcePostgresConfig {

    @Autowired
    private TextPartRepository textPartRepository;

//    @Profile("testPostgres")
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/textsaver");
        dataSource.setUsername("postgres");
        dataSource.setPassword("password");
        return dataSource;
    }

//    @Profile("testPostgres")
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan("com.jezh.textsaver.entity");
        entityManagerFactoryBean.setJpaProperties(getHibernateProperties());
        return entityManagerFactoryBean;
    }

//    @Profile("testPostgres")
    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQL9Dialect");
        properties.put("spring.jpa.show-sql", "true");
        properties.put("spring.jpa.hibernate.ddl-auto", "none");
        return properties;
    }
}
