package com.gateweb.charge.notice.component;

import com.gateweb.charge.component.propertyProvider.MailProperty;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class GeneralMailSender implements NoticeMimeMessageHelperSender {
    protected org.apache.log4j.Logger logger = LogManager.getLogger(GeneralMailSender.class);

    @Autowired
    @Qualifier("billingSystemMailSender")
    JavaMailSender billingSystemMailSender;
    @Autowired
    MailProperty mailProperty;

    @Override
    public boolean sendMimeMessageHelper(MimeMessageHelper mimeMessageHelper) {
        try {
            mimeMessageHelper.setFrom(mailProperty.getMailFrom());
            String[] forceMailTo = mailProperty.getMailForceTo();
            if (forceMailTo.length > 0) {
                mimeMessageHelper.setTo(forceMailTo);
            }

            String[] ccSet = mailProperty.getMailCcTo();
            if (ccSet.length > 0) {
                mimeMessageHelper.setCc(ccSet);
            }
            billingSystemMailSender.send(mimeMessageHelper.getMimeMessage());
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }
}
