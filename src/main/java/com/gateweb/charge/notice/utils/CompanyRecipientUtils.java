package com.gateweb.charge.notice.utils;

import com.gateweb.orm.charge.entity.Company;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Address;
import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CompanyRecipientUtils {
    private static final String COMMA_SEPARATOR = ",";
    private static final String SEMICOLON_SEPARATOR = ";";

    public static String noticeCompanyRecipientEncode(Company company) {
        Set<String> emailSet = new HashSet<>();
        emailSet.addAll(noticeCompanyRecipientDecode(company.getEmail1()));
        emailSet.addAll(noticeCompanyRecipientDecode(company.getEmail2()));
        return String.join(COMMA_SEPARATOR, emailSet);
    }

    public static Set<String> noticeCompanyRecipientDecode(String noticeRecipient) {
        if (noticeRecipient.contains(COMMA_SEPARATOR)) {
            return new HashSet<>(Arrays.asList(noticeRecipient.split(COMMA_SEPARATOR)));
        }
        if (noticeRecipient.contains(SEMICOLON_SEPARATOR)) {
            return new HashSet<>(Arrays.asList(noticeRecipient.split(SEMICOLON_SEPARATOR)));
        }
        HashSet<String> resultSet = new HashSet<>();
        resultSet.add(noticeRecipient);
        return resultSet;
    }

    public static String getRecipientListStr(MimeMessageHelper mimeMessageHelper) throws MessagingException {
        Set<String> recipientSet = new HashSet<>();
        for (Address address : mimeMessageHelper.getMimeMessage().getAllRecipients()) {
            recipientSet.add(address.toString());
        }
        return String.join(COMMA_SEPARATOR, recipientSet);
    }

}
