package com.bank.star.recommendationservice.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.bank.star.recommendationservice.repository",
        entityManagerFactoryRef = "dynamicEntityManagerFactory",
        transactionManagerRef = "dynamicTransactionManager"
)
public class DynamicRulesDataSourceConfiguration {

    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource(
            @Value("${spring.dynamic-datasource.url}") String url,
            @Value("${spring.dynamic-datasource.username}") String username,
            @Value("${spring.dynamic-datasource.password}") String password
    ) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
            JpaProperties jpaProperties,
            ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(),
                jpaProperties.getProperties(),
                persistenceUnitManager.getIfAvailable());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean dynamicEntityManagerFactory(
            @Qualifier("dynamicDataSource") DataSource dynamicDataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dynamicDataSource)
                .packages("com.bank.star.recommendationservice.entity")
                .persistenceUnit("dynamic")
                .build();
    }

    @Bean
    public PlatformTransactionManager dynamicTransactionManager(
            @Qualifier("dynamicEntityManagerFactory") EntityManagerFactory dynamicEntityManagerFactory) {
        return new JpaTransactionManager(dynamicEntityManagerFactory);
    }


}