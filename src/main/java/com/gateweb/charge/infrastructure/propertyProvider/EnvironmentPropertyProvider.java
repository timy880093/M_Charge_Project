package com.gateweb.charge.infrastructure.propertyProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import java.util.Optional;

@Configuration
@PropertySources({
        @PropertySource("classpath:application.properties")
})
public class EnvironmentPropertyProvider {
    protected Logger logger = LoggerFactory.getLogger(EnvironmentPropertyProvider.class);

    @Autowired
    private Environment env;

    public Optional<String> getEnvProperty() {
        Optional result = Optional.empty();
        try {
            String databaseTarget = env.getProperty("charge.database.source");
            if (!databaseTarget.isEmpty()) {
                if (databaseTarget.contains("uat")) {
                    result = Optional.of("uat");
                } else if (databaseTarget.contains("production")) {
                    result = Optional.of("Production");
                }
            }
        } catch (Exception ex) {
            logger.error("charge.database.source" + ":" + ex.getMessage());
        }
        return result;
    }
}
