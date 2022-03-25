package com.gateweb.charge.notice.component;

import com.gateweb.charge.config.BillingSystemMailSender;
import com.gateweb.charge.enumeration.NoticeType;
import com.gateweb.charge.notice.bean.PaymentRequestMailData;
import com.gateweb.orm.charge.entity.Notice;
import com.gateweb.orm.charge.repository.BillRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import freemarker.template.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentRequestMimeMessageHelperProvider implements NoticeMimeMessageHelperProvider {
    protected final Logger logger = LogManager.getLogger(getClass());
    private static final String SCSB_PAY_TUTORIAL = "WEB-INF/classes/template/scsbPayBillTutorial.pdf";
    private static final String PAYMENT_REQUEST_MAIL_TEMPLATE = "paymentRequestMail.ftl";

    @Autowired
    BillingSystemMailSender billingSystemMailSender;
    @Autowired
    BillRepository billRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    public Configuration freemarkerConfiguration;
    @Autowired
    PaymentRequestMailFreemarkerDataProvider paymentRequestMailFreemarkerDataProvider;
    @Autowired
    PaymentRequestMailDataProvider paymentRequestMailDataProvider;
    @Autowired
    PaymentRequestMailMimeMessageHelperGenerator paymentRequestMailMimeMessageHelperGenerator;
    @Autowired
    PaymentRequestCorrectionMailMimeMessageHelperGenerator paymentRequestCorrectionMailMimeMessageHelperGenerator;

    @Override
    public Optional<MimeMessageHelper> createHelper(Notice notice) {
        return paymentRequestMimeMessageHelperCreator(notice);
    }

    /**
     * 寄帳單明細
     * 需先出帳，才可以寄帳單。
     */
    public Optional<MimeMessageHelper> paymentRequestMimeMessageHelperCreator(Notice notice) {
        Optional<MimeMessageHelper> mimeMessageHelperOptional = Optional.empty();
        try {
            Optional<PaymentRequestMailData> paymentRequestMailDataOptional
                    = paymentRequestMailDataProvider.getPaymentRequestMailData(notice);
            if (paymentRequestMailDataOptional.isPresent()) {
                if (notice.getNoticeType().equals(NoticeType.PAYMENT_REQUEST_MAIL)
                        || notice.getNoticeType().equals(NoticeType.PAYMENT_REQUEST_MAIL_NO_CC)) {
                    mimeMessageHelperOptional = paymentRequestMailMimeMessageHelperGenerator.genMimeMessageHelper(
                            paymentRequestMailDataOptional.get()
                    );
                }
                if (notice.getNoticeType().equals(NoticeType.PAYMENT_REQUEST_CORRECTION_MAIL)
                        || notice.getNoticeType().equals(NoticeType.PAYMENT_REQUEST_CORRECTION_MAIL_NO_CC)) {
                    mimeMessageHelperOptional =
                            paymentRequestCorrectionMailMimeMessageHelperGenerator.genMimeMessageHelper(
                                    paymentRequestMailDataOptional.get()
                            );
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return mimeMessageHelperOptional;
    }
}
