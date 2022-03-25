package com.gateweb.charge.notice.component;

import com.gateweb.charge.config.BillingSystemMailSender;
import com.gateweb.charge.notice.bean.PaymentRequestMailData;
import com.gateweb.charge.notice.bean.PaymentRequestMailFreemarkerData;
import com.gateweb.charge.notice.builder.MailMimeMessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentRequestCorrectionMailMimeMessageHelperGenerator implements NoticeMimeMessageHelperGenerator {
    Logger logger = LoggerFactory.getLogger(PaymentRequestCorrectionMailMimeMessageHelperGenerator.class);
    @Autowired
    PaymentRequestMailFreemarkerDataProvider paymentRequestMailFreemarkerDataProvider;
    @Autowired
    PaymentRequestFreemarkerUtil paymentRequestFreemarkerUtil;
    @Autowired
    BillingSystemMailSender billingSystemMailSender;

    private String paymentRequestCorrectionMailSubject(String companyName) {
        String subject = String.format("【帳務更正】__【關網電子發票繳款通知更正 %s_關網電子發票服務費用，請詳內文。", companyName);
        return subject;
    }

    @Override
    public Optional<MimeMessageHelper> genMimeMessageHelper(PaymentRequestMailData paymentRequestMailData) {
        Optional<MimeMessageHelper> mimeMessageHelperOptional = Optional.empty();
        try {
            PaymentRequestMailFreemarkerData paymentRequestMailFreemarkerData =
                    paymentRequestMailFreemarkerDataProvider.genPaymentRequestCorrectionMailData(paymentRequestMailData);
            Optional<String> mailHtmlOpt =
                    paymentRequestFreemarkerUtil.generatePaymentRequestMailContent(paymentRequestMailFreemarkerData);

            if (!mailHtmlOpt.isPresent()) {
                return mimeMessageHelperOptional;
            }
            if (!billingSystemMailSender.javaMailSenderOpt.isPresent()) {
                return mimeMessageHelperOptional;
            }

            MailMimeMessageBuilder mailBuilder = new MailMimeMessageBuilder();
            MimeMessageHelper mimeMessageHelper = mailBuilder.initBuilder(billingSystemMailSender.javaMailSenderOpt.get())
                    .withRecipient(paymentRequestMailData.getRecipient())
                    .withHtmlContent(mailHtmlOpt.get())
                    .withSubject(paymentRequestCorrectionMailSubject(paymentRequestMailFreemarkerData.getCompanyName()))
                    .getMimeHelper();
            mimeMessageHelperOptional = Optional.of(mimeMessageHelper);
            return mimeMessageHelperOptional;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return mimeMessageHelperOptional;
        }
    }

}
