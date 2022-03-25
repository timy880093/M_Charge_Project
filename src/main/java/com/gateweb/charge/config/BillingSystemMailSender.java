package com.gateweb.charge.config;

import com.gateweb.charge.component.propertyProvider.MailPropertyProvider;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.util.Optional;
import java.util.Properties;

@Component
public class BillingSystemMailSender {
    protected Logger logger = LoggerFactory.getLogger(BillingSystemMailSender.class);

    @Autowired
    MailPropertyProvider mailPropertyProvider;

    public Optional<JavaMailSender> javaMailSenderOpt = Optional.empty();

    @PostConstruct
    public void BillingSystemMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        //Printing Properties
        Optional<String> mailServerHostOpt = mailPropertyProvider.getMailProperty("mail.host");
        Optional<String> mailServerPortOpt = mailPropertyProvider.getMailProperty("mail.port");
        Optional<String> mailServerUsernameOpt = mailPropertyProvider.getMailProperty("mail.username");
        Optional<String> mailServerPasswordOpt = mailPropertyProvider.getMailProperty("mail.password");
        Optional<String> mailTransportProtocolOpt = mailPropertyProvider.getMailProperty("mail.protocol");
        Optional<String> mailSmtpAuthOpt = mailPropertyProvider.getMailProperty("mail.smtp.auth");
        Optional<String> mailSmtpStarttlsEnableOpt = mailPropertyProvider.getMailProperty("mail.smtp.starttls.enable");

        if (mailServerHostOpt.isPresent()) {
            mailSender.setHost(mailServerHostOpt.get());
            logger.info("mail.host:" + mailServerHostOpt.get());
        } else {
            return;
        }

        if (mailServerPortOpt.isPresent()) {
            try {
                Integer portNumber = Integer.parseInt(mailServerPortOpt.get());
                logger.info("mail.port:" + portNumber);
                mailSender.setPort(portNumber);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                return;
            }
        } else {
            return;
        }

        if (mailServerUsernameOpt.isPresent()) {
            logger.info("mail.username:" + mailServerUsernameOpt.get());
            mailSender.setUsername(mailServerUsernameOpt.get());
        } else {
            return;
        }

        if (mailServerPasswordOpt.isPresent()) {
            mailSender.setPassword(mailServerPasswordOpt.get());
        } else {
            return;
        }

        Properties props = mailSender.getJavaMailProperties();

        //default is smtp
        if (mailTransportProtocolOpt.isPresent()) {
            props.put("mail.transport.protocol", mailTransportProtocolOpt.get());
        } else {
            props.put("mail.transport.protocol", "smtp");
        }
        logger.info("mail.properties.mail.transport.protocol:" + props.get("mail.transport.protocol"));

        if (mailSmtpAuthOpt.isPresent()) {
            props.put("mail.smtp.auth", mailSmtpAuthOpt.get());
            logger.info("mail.properties.mail.smtp.auth:" + mailSmtpAuthOpt.get());
        } else {
            return;
        }

        if (mailSmtpStarttlsEnableOpt.isPresent()) {
            props.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnableOpt.get());
            logger.info("mail.properties.mail.smtp.starttls.enable:" + mailSmtpStarttlsEnableOpt.get());
        } else {
            return;
        }

        try {
            mailSender.testConnection();
        } catch (MessagingException e) {
            logger.error(e.getMessage());
            return;
        }
        this.javaMailSenderOpt = Optional.of(mailSender);
    }
}
