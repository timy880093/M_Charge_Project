package com.gateweb.charge.notice.component;

import com.gateweb.charge.notice.bean.PaymentRequestMailData;
import com.gateweb.charge.notice.bean.PaymentRequestMailFreemarkerData;
import com.gateweb.charge.notice.builder.MailMimeMessageBuilder;
import com.gateweb.orm.charge.repository.BillRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
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
import javax.servlet.ServletContext;
import java.util.Optional;

import static com.gateweb.utils.ObjectUtil.stringSetToArray;

@Component
public class PaymentRequestMailMimeMessageHelperGenerator implements NoticeMimeMessageHelperGenerator {
    Logger logger = LoggerFactory.getLogger(PaymentRequestMailMimeMessageHelperGenerator.class);

    @Autowired
    BillRepository billRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired(required = false)
    ServletContext servletContext;
    @Autowired
    @Qualifier("billingSystemMailSender")
    JavaMailSender billingSystemMailSender;
    @Autowired
    PaymentRequestMailFreemarkerDataProvider paymentRequestMailFreemarkerDataProvider;
    @Autowired
    NoticeCustomConverter noticeCustomConverter;
    @Autowired
    PaymentRequestFreemarkerUtil paymentRequestFreemarkerUtil;
    @Autowired
    Boolean oBankPaymentNoticeAdvert;

    private String paymentRequestMailSubject(String companyName) {
        String subject = String.format("【關網電子發票繳款通知】 %s_關網電子發票服務費用，請詳內文。", companyName);
        return subject;
    }

    @Override
    public Optional<MimeMessageHelper> genMimeMessageHelper(PaymentRequestMailData paymentRequestMailData) {
        Optional<MimeMessageHelper> mimeMessageHelperOptional = Optional.empty();
        try {
            PaymentRequestMailFreemarkerData paymentRequestMailFreemarkerData =
                    paymentRequestMailFreemarkerDataProvider.genPaymentRequestMailData(paymentRequestMailData);

            Optional<String> mailHtmlOpt = paymentRequestFreemarkerUtil.generatePaymentRequestMailContent(paymentRequestMailFreemarkerData);

            if (!mailHtmlOpt.isPresent()) {
                return mimeMessageHelperOptional;
            }

            MailMimeMessageBuilder mailBuilder = new MailMimeMessageBuilder();
            MailMimeMessageBuilder mimeMessageBuilder = mailBuilder.initBuilder(billingSystemMailSender)
                    .withRecipientList(stringSetToArray(paymentRequestMailData.getRecipient()))
                    .withHtmlContent(mailHtmlOpt.get())
                    .withSubject(paymentRequestMailSubject(paymentRequestMailFreemarkerData.getCompanyName()));

            if (oBankPaymentNoticeAdvert) {
                addOBankAdvert(mimeMessageBuilder);
            }
            MimeMessageHelper mimeMessageHelper = mimeMessageBuilder.getMimeHelper();
            mimeMessageHelperOptional = Optional.of(mimeMessageHelper);
            return mimeMessageHelperOptional;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return mimeMessageHelperOptional;
        }
    }

    private void addOBankAdvert(MailMimeMessageBuilder mimeMessageBuilder) throws MessagingException {
        Resource res = new ClassPathResource("advert/20221230-O-Bank-Advert01.jpg");
        mimeMessageBuilder.withInlineImage("O_Bank_Advert01", res);
    }

}
