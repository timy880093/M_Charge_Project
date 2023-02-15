package com.gateweb.charge.component.propertyProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:charge.advert.properties")
public class AdvertPropertyProvider {
    Logger logger = LoggerFactory.getLogger(AdvertPropertyProvider.class);
    @Autowired
    private Environment env;

    @Bean
    protected Boolean oBankPaymentNoticeAdvert() {
        try {
            String oBankPaymentNoticeAdvert = env.getProperty("obank.payment.notice");
            return Boolean.valueOf(oBankPaymentNoticeAdvert);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }
}
