package com.gateweb.charge.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = {
                "com.gateweb.orm.charge.repository"
        },
        entityManagerFactoryRef = "chargeEntityManager",
        transactionManagerRef = "chargeTransactionManager"
)
@ComponentScan
@EnableTransactionManagement
public class ChargeDatabaseConfig {
    //JavaConfiguration無法完全取代舊的EntityManager，使用xml設定的java persistence entityManager可以正常產生java persistence query轉成hibernate query
    //使用java configuration會造成connection leak因此先並存
    @Autowired
    Environment env;
    @Autowired
    EnvironmentPropertiesUtils environmentPropertiesUtils;

    private static final String PROPAGATION_REQUIRED = "PROPAGATION_REQUIRED";

    public Optional<MapPropertySource> getChargeDatabaseProperty() {
        String chargeDataSourcePropertiesFileName = env.getProperty("charge.database.source");
        Optional<MapPropertySource> chargeDataBaseProperties = environmentPropertiesUtils.getPropertiesSource(chargeDataSourcePropertiesFileName);
        return chargeDataBaseProperties;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean chargeEntityManager() {
        Optional<MapPropertySource> chargeDatabaseProperty = getChargeDatabaseProperty();
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(basicDataSource());
        em.setPackagesToScan(
                new String[]{
                        "com.gateweb.orm.charge.entity"
                        , "com.gateweb.charge.fetchModel"
                        , "com.gateweb.charge.fetchView"
                }
        );

        em.setJtaDataSource(basicDataSource());
        em.setPersistenceUnitName("chargeFacade");
        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        if (chargeDatabaseProperty.isPresent()) {
            properties.put("hibernate.hbm2ddl.auto",
                    chargeDatabaseProperty.get().getProperty("hibernate.hbm2ddl.auto"));
            properties.put("hibernate.show_sql",
                    chargeDatabaseProperty.get().getProperty("hibernate.show_sql"));
            properties.put("hibernate.default_schema",
                    chargeDatabaseProperty.get().getProperty("hibernate.default_schema"));
            properties.put("hibernate.jdbc.batch_size", "5");
            properties.put("hibernate.databasePlatform", "org.hibernate.dialect.PostgreSQLDialect");
            em.setJpaPropertyMap(properties);
        }
        return em;
    }

    @Primary
    @Bean
    public DataSource basicDataSource() {
        Optional<MapPropertySource> chargeDatabaseProperty = getChargeDatabaseProperty();
        BasicDataSource dataSource = new BasicDataSource();
        if (chargeDatabaseProperty.isPresent()) {
            dataSource.setDriverClassName(
                    (String) chargeDatabaseProperty.get().getProperty("jdbc.driverClassName"));
            dataSource.setUrl((String) chargeDatabaseProperty.get().getProperty("jdbc.url"));
            dataSource.setUsername((String) chargeDatabaseProperty.get().getProperty("jdbc.username"));
            dataSource.setPassword((String) chargeDatabaseProperty.get().getProperty("jdbc.password"));
            if (chargeDatabaseProperty.get().getProperty("jdbc.maxTotal") != null) {
                dataSource.setMaxTotal(Integer.parseInt((String) chargeDatabaseProperty.get().getProperty("jdbc.maxTotal")));
            }
            dataSource.setDefaultAutoCommit(true);
            dataSource.setRemoveAbandonedOnBorrow(true);
            dataSource.setRemoveAbandonedOnMaintenance(true);
            dataSource.setRemoveAbandonedTimeout(60);
            dataSource.setPoolPreparedStatements(true);
            dataSource.setValidationQuery("SELECT 1");
            dataSource.setLogAbandoned(true);
            dataSource.setMaxOpenPreparedStatements(-1);
            dataSource.setMaxIdle(2);
        }
        return dataSource;
    }

    @Primary
    @Bean
    public PlatformTransactionManager chargeTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(chargeEntityManager().getObject());
        transactionManager.setDataSource(basicDataSource());
        return transactionManager;
    }

    @Bean(name = "chargeTransactionInterceptor")
    public TransactionInterceptor chargeTransactionInterceptor() {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(chargeTransactionManager());
        Properties transactionAttributes = new Properties();

        transactionAttributes.setProperty("transaction*", "PROPAGATION_REQUIRED,-Exception");
        transactionAttributes.setProperty("create*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("upload*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("update*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("receive*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("insert*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("import*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("open*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("cancel*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("batchUp*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("delete*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("revert*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("save*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("send*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("clean*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("query*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("get*", PROPAGATION_REQUIRED);
        transactionAttributes.setProperty("*", "PROPAGATION_SUPPORTS,-Throwable,readOnly");
        transactionInterceptor.setTransactionAttributes(transactionAttributes);
        return transactionInterceptor;
    }

    /**
     * 因為用aop point cut的結果跑起來怪怪的，所以新的方法都是手動寫@Transactional
     *
     * @return
     */
    @Bean
    public AspectJExpressionPointcut chargeAspectJExpressionPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        StringBuffer transactionExecutionSb = new StringBuffer();
        transactionExecutionSb
                .append("  execution( * com.gate.web.facades.*Imp.*(..) )")
                .append(" || execution( * dao.*DAO.*(..) )")
                .append(" || execution( * dao.BaseDAO.*(..) )")
                .append(" || execution( * com.gateweb.bridge.service.*impl.*.*(..) )");
        pointcut.setExpression(transactionExecutionSb.toString());

        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor einvPointcutAdvisor() {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(); //設切點
        advisor.setPointcut(chargeAspectJExpressionPointcut()); //路徑限制
        advisor.setAdvice(chargeTransactionInterceptor()); //方法名限制
        return advisor;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
