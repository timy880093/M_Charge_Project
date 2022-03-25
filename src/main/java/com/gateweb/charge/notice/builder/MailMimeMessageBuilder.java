package com.gateweb.charge.notice.builder;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

public class MailMimeMessageBuilder {
    MimeMessageHelper helper;
    String charset = "UTF-8";

    public MailMimeMessageBuilder initBuilder(JavaMailSender javaMailSender) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        this.helper = new MimeMessageHelper(mimeMessage, true, charset);
        return this;
    }

    public MailMimeMessageBuilder withRecipient(String recipient) throws MessagingException {
        helper.addTo(recipient);
        return this;
    }

    public MailMimeMessageBuilder withRecipientAndName(String recipient, String name) throws UnsupportedEncodingException, MessagingException {
        helper.addTo(recipient, name);
        return this;
    }

    public MailMimeMessageBuilder withRecipientList(String[] recipientList) throws MessagingException {
        for (String recipient : recipientList) {
            helper.addTo(recipient);
        }
        return this;
    }

    public MailMimeMessageBuilder withSubject(String subject) throws MessagingException {
        helper.setSubject(subject);
        return this;
    }

    public MailMimeMessageBuilder withAttachment(String fileName, DataSource dataSource) throws MessagingException, UnsupportedEncodingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setDisposition("attachment");
        mimeBodyPart.setFileName(MimeUtility.encodeText(fileName, charset, null));
        mimeBodyPart.setDataHandler(new DataHandler(dataSource));
        helper.getRootMimeMultipart().addBodyPart(mimeBodyPart);
        return this;
    }

    public MailMimeMessageBuilder withHtmlContent(String content) throws MessagingException {
        helper.setText(content, true);
        return this;
    }

    public MimeMessageHelper getMimeHelper() {
        return helper;
    }

}
