package com.gateweb.charge.notice.component;

import org.springframework.mail.javamail.MimeMessageHelper;

public interface NoticeMimeMessageHelperSender {
    boolean sendMimeMessageHelper(MimeMessageHelper mimeMessageHelper);
}
