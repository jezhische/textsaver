package com.jezh.textsaver.configuration;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("com.jezh.textsaver.repository")
@EnableTransactionManagement
// without following I get "IllegalStateException: Failed to load ApplicationContext...Required key 'spring.datasource.url' not found"
@PropertySource("application.properties")
public class DataSourceConfig {

    private static final String PROP_DATABASE_DRIVER = "spring.datasource.driver-class-name";
    private static final String PROP_DATABASE_PASSWORD = "spring.datasource.password";
    private static final String PROP_DATABASE_URL = "spring.datasource.url";
    private static final String PROP_DATABASE_USERNAME = "spring.datasource.username";
    private static final String PROP_HIBERNATE_DIALECT = "spring.jpa.database-platform";
    private static final String PROP_HIBERNATE_SHOW_SQL = "spring.jpa.show-sql";
    private static final String PROP_ENTITYMANAGER_PACKAGES_TO_SCAN = "db.entitymanager.packages.to.scan";
    private static final String PROP_HIBERNATE_HBM2DDL_AUTO = "spring.jpa.hibernate.ddl-auto";



    @Resource
    private Environment environment;

// Since I don't need connection pool for multithread requests for this app running, I can use simple DataSource implementation:
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty(PROP_DATABASE_DRIVER));
        dataSource.setUrl(environment.getRequiredProperty(PROP_DATABASE_URL));
        dataSource.setUsername(environment.getRequiredProperty(PROP_DATABASE_USERNAME));
        dataSource.setPassword(environment.getRequiredProperty(PROP_DATABASE_PASSWORD));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan(environment.getRequiredProperty(PROP_ENTITYMANAGER_PACKAGES_TO_SCAN));
        entityManagerFactoryBean.setJpaProperties(getHibernateProperties());
        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put(PROP_HIBERNATE_DIALECT, environment.getRequiredProperty(PROP_HIBERNATE_DIALECT));
        properties.put(PROP_HIBERNATE_SHOW_SQL, environment.getRequiredProperty(PROP_HIBERNATE_SHOW_SQL));
        properties.put(PROP_HIBERNATE_HBM2DDL_AUTO, environment.getRequiredProperty(PROP_HIBERNATE_HBM2DDL_AUTO));
        return properties;
    }
}
