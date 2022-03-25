package com.gateweb.charge.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Component
public class EnvironmentPropertiesUtils {
    private Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private Environment env;

    public Optional<Map<String, Object>> getByPropertiesFileName(String propertiesFileName) {
        return getPropertiesMap(String.valueOf(env.getProperty(propertiesFileName)));
    }

    public Optional<Map<String, Object>> getPropertiesMap(String categoryName) {
        Optional<MapPropertySource> mapPropertySourceOptional = getPropertiesSource(categoryName);
        if (mapPropertySourceOptional.isPresent()) {
            return Optional.of(mapPropertySourceOptional.get().getSource());
        } else {
            return Optional.empty();
        }
    }

    public Optional<MapPropertySource> getPropertiesSource(String categoryName) {
        for (Iterator<?> it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
            Object propertySource = it.next();
            if (propertySource instanceof MapPropertySource
                    && ((MapPropertySource) propertySource).getName().contains(categoryName)) {
                return Optional.ofNullable((MapPropertySource) propertySource);
            }
        }
        return Optional.ofNullable(null);
    }

    public Optional<Object> getProperties(String category, String name) {
        Optional<MapPropertySource> mapPropertySourceOptional = getPropertiesSource(category);
        if (mapPropertySourceOptional.isPresent()) {
            return Optional.ofNullable(mapPropertySourceOptional.get().getProperty(name));
        } else {
            return Optional.empty();
        }
    }

    public void propertiesLogger(Optional<MapPropertySource> propertyMap) {
        if (propertyMap.isPresent()) {
            Arrays.asList(propertyMap.get().getPropertyNames()).stream().forEach(key -> {
                logger.info(key + ":" + propertyMap.get().getProperty(key));
            });
        }
    }
}
