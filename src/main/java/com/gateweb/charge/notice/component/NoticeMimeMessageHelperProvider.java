package com.gateweb.charge.notice.component;

import com.gateweb.orm.charge.entity.Notice;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Optional;

public interface NoticeMimeMessageHelperProvider {
    public static final String SCSB_PAY_TUTORIAL = "WEB-INF/classes/template/scsbPayBillTutorial.pdf";
    Optional<MimeMessageHelper> createHelper(Notice notice);
}
