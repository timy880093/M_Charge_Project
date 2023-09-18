package com.gateweb.charge.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Optional;

@Configuration
@PropertySources({
        @PropertySource("classpath:einv.production.replication.jdbc.properties"),
        @PropertySource("classpath:einv.test.jdbc.properties")
})
@EnableJpaRepositories(
        basePackages = {"com.gateweb.orm.einv.repository"},
        entityManagerFactoryRef = "einvEntityManager",
        transactionManagerRef = "einvTransactionManager"
)
@ComponentScan
public class EinvDatabaseConfig {
    @Autowired
    private Environment env;

    @Autowired
    EnvironmentPropertiesUtils environmentPropertiesUtils;

    public Optional<MapPropertySource> getEinvDatabaseProperty() {
        String einvDataSourcePropertiesFileName = env.getProperty("einv.database.source");
        Optional<MapPropertySource> einvDataBaseProperties = environmentPropertiesUtils.getPropertiesSource(einvDataSourcePropertiesFileName);
        return einvDataBaseProperties;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean einvEntityManager() {
        Optional<MapPropertySource> einvDatabaseProperty = getEinvDatabaseProperty();
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(einvDataSource());
        em.setPackagesToScan(
                new String[]{
                        "com.gateweb.orm.einv.entity"
                }
        );

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                einvDatabaseProperty.get().getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
                einvDatabaseProperty.get().getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public DataSource einvDataSource() {
        Optional<MapPropertySource> einvDatabaseProperty = getEinvDatabaseProperty();
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName((String) einvDatabaseProperty.get().getProperty("jdbc.driverClassName"));
        dataSource.setJdbcUrl((String) einvDatabaseProperty.get().getProperty("jdbc.url"));
        dataSource.setUsername((String) einvDatabaseProperty.get().getProperty("jdbc.username"));
        dataSource.setPassword((String) einvDatabaseProperty.get().getProperty("jdbc.password"));
        dataSource.setConnectionTimeout(10000);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager einvTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(einvEntityManager().getObject());
        transactionManager.setDataSource(einvDataSource());
        return transactionManager;
    }
}
