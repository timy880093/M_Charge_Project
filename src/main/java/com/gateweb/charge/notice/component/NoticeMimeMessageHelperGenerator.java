package com.gateweb.charge.notice.component;

import com.gateweb.charge.notice.bean.PaymentRequestMailData;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Optional;

public interface NoticeMimeMessageHelperGenerator {
    Optional<MimeMessageHelper> genMimeMessageHelper(PaymentRequestMailData paymentRequestMailData);
}
