package com.gateweb.charge.config;

import com.gateweb.charge.infrastructure.propertyProvider.MailPropertyProvider;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.io.IOException;
import java.util.Optional;

@Component
public class FreeMarkerConfig {
    protected org.apache.log4j.Logger logger = LogManager.getLogger(MailPropertyProvider.class);

    @Autowired
    private Environment env;

    public Optional<String> getFreeMarkerProperty(String propertyName) {
        Optional result = Optional.empty();
        try {
            result = Optional.of(env.getProperty(propertyName));
        } catch (Exception ex) {
            logger.error(propertyName + ":" + ex.getMessage());
        }
        return result;
    }

    @Bean
    public Configuration freemarkerJavaConfiguration() throws TemplateException, IOException {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        Optional<String> templateLoaderPathOpt = getFreeMarkerProperty("spring.freemarker.template-loader-path");
        Optional<String> defaultEncodingOpt = getFreeMarkerProperty("spring.freemarker.settings.default_encoding");
        Optional<String> preferFileSystemAccessOpt = getFreeMarkerProperty("spring.freemarker.prefer-file-system-access");
        bean.setTemplateLoaderPath(templateLoaderPathOpt.get());
        bean.setDefaultEncoding(defaultEncodingOpt.get());
        bean.setPreferFileSystemAccess(Boolean.valueOf(preferFileSystemAccessOpt.get()));
        return bean.createConfiguration();
    }
}
