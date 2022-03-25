package com.gateweb.charge.notice.component;

import com.gateweb.base.exception.NullPropertyException;
import com.gateweb.charge.notice.bean.PaymentRequestMailFreemarkerData;
import com.gateweb.utils.ObjectUtil;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class PaymentRequestFreemarkerUtil {
    Logger logger = LoggerFactory.getLogger(PaymentRequestFreemarkerUtil.class);
    private static final String PAYMENT_REQUEST_MAIL_TEMPLATE = "paymentRequestMail.ftl";
    @Autowired
    public Configuration freemarkerConfiguration;
    
    protected Optional<String> generatePaymentRequestMailContent(PaymentRequestMailFreemarkerData paymentRequestMailFreemarkerData) {
        try {
            ObjectUtil.objectPropertyNullCheck(paymentRequestMailFreemarkerData);
            Map<String, Object> freeMarkerTemplateMap = new HashMap<String, Object>();
            freeMarkerTemplateMap.put("paymentRequestMailFreemarkerData", paymentRequestMailFreemarkerData);
            String resultString = FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfiguration.getTemplate(PAYMENT_REQUEST_MAIL_TEMPLATE)
                    , freeMarkerTemplateMap
            );
            return Optional.of(resultString);
        } catch (NullPropertyException nullPropertyException) {
            logger.error("null property exception:{}", paymentRequestMailFreemarkerData);
        } catch (Exception ex) {
            logger.error(ex.getClass().getName() + "{}", ex.getMessage());
        }
        return Optional.empty();
    }
}
