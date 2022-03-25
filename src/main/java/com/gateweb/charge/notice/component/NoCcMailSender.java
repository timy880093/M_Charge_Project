package com.gateweb.charge.notice.component;

import com.gateweb.charge.component.propertyProvider.MailPropertyProvider;
import com.gateweb.charge.config.BillingSystemMailSender;
import com.gateweb.charge.exception.JavaMailSenderConfigurationException;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NoCcMailSender implements NoticeMimeMessageHelperSender {
    protected org.apache.log4j.Logger logger = LogManager.getLogger(BillingSystemMailSender.class);
    @Autowired
    BillingSystemMailSender billingSystemMailSender;
    @Autowired
    MailPropertyProvider mailPropertyProvider;

    @Override
    public boolean sendMimeMessageHelper(MimeMessageHelper mimeMessageHelper) {
        try {
            String[] forceMailTo = mailPropertyProvider.getMailForceTo();
            if (forceMailTo.length > 0
                    && mimeMessageHelper.getMimeMessage().getAllRecipients().length == 0) {
                mimeMessageHelper.setTo(forceMailTo);
            }
            Optional<String> mailFromOpt = mailPropertyProvider.getMailFrom();
            if (mailFromOpt.isPresent()) {
                mimeMessageHelper.setFrom(mailFromOpt.get());
            }
            if (billingSystemMailSender.javaMailSenderOpt.isPresent()) {
                billingSystemMailSender.javaMailSenderOpt.get().send(mimeMessageHelper.getMimeMessage());
            } else {
                throw new JavaMailSenderConfigurationException();
            }
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }
}
