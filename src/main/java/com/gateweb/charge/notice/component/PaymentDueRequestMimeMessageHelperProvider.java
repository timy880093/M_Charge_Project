package com.gateweb.charge.notice.component;

import com.gateweb.charge.notice.bean.PaymentRequestMailData;
import com.gateweb.orm.charge.entity.Notice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentDueRequestMimeMessageHelperProvider implements NoticeMimeMessageHelperProvider {
    protected final Logger logger = LogManager.getLogger(getClass());
    @Autowired
    PaymentRequestMailDataProvider paymentRequestMailDataProvider;
    @Autowired
    PaymentDueRequestMailMimeMessageHelperGenerator paymentDueRequestMailMimeMessageHelperGenerator;

    @Override
    public Optional<MimeMessageHelper> createHelper(Notice notice) {
        try {
            Optional<PaymentRequestMailData> paymentRequestMailDataOptional
                    = paymentRequestMailDataProvider.getPaymentRequestMailData(notice);
            if (!paymentRequestMailDataOptional.isPresent()) {
                return Optional.empty();
            }
            return paymentDueRequestMailMimeMessageHelperGenerator.genMimeMessageHelper(
                    paymentRequestMailDataOptional.get()
            );
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return Optional.empty();
    }
}
