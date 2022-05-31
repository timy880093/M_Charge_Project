package com.gateweb.charge.config;

import com.gateweb.charge.component.propertyProvider.MailProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Properties;

@Component
public class BillingSystemMailSenderInitializer {
    protected Logger logger = LoggerFactory.getLogger(BillingSystemMailSenderInitializer.class);

    @Autowired
    MailProperty mailProperty;

    @Bean
    public JavaMailSender billingSystemMailSender() throws MessagingException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperty.getMailServerHost());
        mailSender.setPort(mailProperty.getMailServerPort());
        mailSender.setUsername(mailProperty.getMailServerUsername());
        mailSender.setPassword(mailProperty.getMailServerPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mailProperty.getMailTransportProtocol());
        props.put("mail.smtp.auth", mailProperty.getSmtpAuth());
        props.put("mail.smtp.starttls.enable", mailProperty.getSmtpStarttlsEnable());
        validation(mailSender);
        return mailSender;
    }

    public void validation(JavaMailSenderImpl javaMailSender) throws MessagingException {
        javaMailSender.testConnection();
    }
}
