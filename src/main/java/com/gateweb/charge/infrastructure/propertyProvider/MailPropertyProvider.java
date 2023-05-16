package com.gateweb.charge.infrastructure.propertyProvider;

import com.gateweb.charge.config.EnvironmentPropertiesUtils;
import com.gateweb.charge.exception.InitializeMailPropertyFailed;
import com.gateweb.utils.MapMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class MailPropertyProvider {
    protected Logger logger = LoggerFactory.getLogger(MailPropertyProvider.class);

    @Autowired
    EnvironmentPropertiesUtils environmentPropertiesUtils;

    @Bean
    public MailProperty mailProperty() throws InitializeMailPropertyFailed {
        Optional<Map<String, Object>> mailPropertyMapOpt = environmentPropertiesUtils.getByPropertiesFileName(
                "mail.config.source"
        );
        if (mailPropertyMapOpt.isPresent()) {
            MapMapper mapMapper = new MapMapper(mailPropertyMapOpt.get());
            MailProperty mailProperty = new MailProperty();
            mapMapper.toCharSplitStrArray("mail.force.to").ifPresent(mailProperty::setMailForceTo);
            mapMapper.toStringOptional("mail.from").ifPresent(mailProperty::setMailFrom);
            mapMapper.toCharSplitStrArray("mail.cc.to").ifPresent(mailProperty::setMailCcTo);
            mapMapper.toStringOptional("mail.host").ifPresent(mailProperty::setMailServerHost);
            mapMapper.toIntegerOptional("mail.port").ifPresent(mailProperty::setMailServerPort);
            mapMapper.toStringOptional("mail.username").ifPresent(mailProperty::setMailServerUsername);
            mapMapper.toStringOptional("mail.password").ifPresent(mailProperty::setMailServerPassword);
            mapMapper.toStringOptional("mail.smtp.auth").ifPresent(mailProperty::setSmtpAuth);
            mapMapper.toStringOptional("mail.smtp.starttls.enable").ifPresent(mailProperty::setSmtpStarttlsEnable);
            mapMapper.toStringOptional("mail.transport.protocol").ifPresent(mailProperty::setMailTransportProtocol);
            mapMapper.toCharSplitStrArray("mail.due.bcc.to").ifPresent(mailProperty::setMailDueBccTo);
            mapMapper.toCharSplitStrArray("mail.overdue.bcc.to").ifPresent(mailProperty::setMailOverdueBccTo);
            return mailProperty;
        } else {
            throw new InitializeMailPropertyFailed();
        }
    }

}
