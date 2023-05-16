package com.gateweb.charge.notice.component;

import com.gateweb.charge.notice.bean.PaymentRequestMailData;
import com.gateweb.charge.notice.bean.PaymentRequestMailFreemarkerData;
import com.gateweb.charge.notice.builder.MailMimeMessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Optional;

import static com.gateweb.utils.ObjectUtil.stringSetToArray;

@Component
public class PaymentOverdueRequestMailMimeMessageHelperGenerator implements NoticeMimeMessageHelperGenerator {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("billingSystemMailSender")
    JavaMailSender billingSystemMailSender;
    @Autowired
    PaymentRequestMailFreemarkerDataProvider paymentRequestMailFreemarkerDataProvider;
    @Autowired
    PaymentOverdueRequestFreemarkerUtil paymentOverdueRequestFreemarkerUtil;

    public Optional<MimeMessageHelper> genMimeMessageHelper(PaymentRequestMailData paymentRequestMailData) {
        try {
            PaymentRequestMailFreemarkerData paymentRequestMailFreemarkerData =
                    paymentRequestMailFreemarkerDataProvider.genPaymentRequestMailData(paymentRequestMailData);

            Optional<String> mailHtmlOpt = paymentOverdueRequestFreemarkerUtil.generateMailContent(paymentRequestMailFreemarkerData);

            if (!mailHtmlOpt.isPresent()) {
                return Optional.empty();
            }

            MailMimeMessageBuilder mailBuilder = new MailMimeMessageBuilder();
            MailMimeMessageBuilder mimeMessageBuilder = mailBuilder.initBuilder(billingSystemMailSender)
                    .withRecipientList(stringSetToArray(paymentRequestMailData.getRecipient()))
                    .withHtmlContent(mailHtmlOpt.get())
                    .withSubject(String.format(
                            "【催繳通知】%s__如未繳費，發票資訊將無法上傳財政部，請務必留意！",
                            paymentRequestMailFreemarkerData.getCompanyName()
                    ));

            if (paymentRequestMailData.getObankOpt().isPresent()
                    && paymentRequestMailData.getObankOpt().get().getoBankAdvert()) {
                addOBankAdvert(mimeMessageBuilder);
            }
            MimeMessageHelper mimeMessageHelper = mimeMessageBuilder.getMimeHelper();
            return Optional.of(mimeMessageHelper);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return Optional.empty();
        }
    }

    private void addOBankAdvert(MailMimeMessageBuilder mimeMessageBuilder) throws MessagingException {
        Resource res = new ClassPathResource("advert/20221230-O-Bank-Advert01.jpg");
        mimeMessageBuilder.withInlineImage("O_Bank_Advert01", res);
    }
}
