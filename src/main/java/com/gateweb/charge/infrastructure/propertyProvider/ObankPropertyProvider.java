package com.gateweb.charge.infrastructure.propertyProvider;

import com.gateweb.charge.notice.bean.OBank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@PropertySource("classpath:charge.advert.properties")
public class ObankPropertyProvider {
    Logger logger = LoggerFactory.getLogger(ObankPropertyProvider.class);
    private Optional<OBank> obankPropertyOptional = Optional.empty();
    @Autowired
    private Environment env;

    public Optional<OBank> getProperty() {
        return obankPropertyOptional;
    }

    @PostConstruct
    protected void oBankPropertyInitial() {
        try {
            Boolean oBankPaymentNoticeAdvert = Boolean.valueOf(env.getProperty("obank.payment.notice"));
            String oBankAdvertImgUrl = String.valueOf(env.getProperty("obank.advert.img.url"));
            OBank oBank = new OBank(
                    oBankPaymentNoticeAdvert,
                    oBankAdvertImgUrl
            );
            obankPropertyOptional = Optional.of(oBank);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
