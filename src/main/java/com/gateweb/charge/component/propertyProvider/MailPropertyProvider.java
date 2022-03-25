package com.gateweb.charge.component.propertyProvider;

import com.gateweb.charge.config.EnvironmentPropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class MailPropertyProvider {
    protected Logger logger = LoggerFactory.getLogger(MailPropertyProvider.class);

    @Autowired
    EnvironmentPropertiesUtils environmentPropertiesUtils;

    Map<String, Object> mailPropertyMap;

    @PostConstruct
    public void initMailPropertyMap() {
        mailPropertyMap = environmentPropertiesUtils.getByPropertiesFileName("mail.config.source")
                .orElse(new HashMap<>());
    }

    public Optional<String> getMailProperty(String propertyName) {
        Optional result = Optional.empty();
        try {
            result = Optional.of(mailPropertyMap.get(propertyName));
        } catch (Exception ex) {
            logger.error(propertyName + ":{}", ex.getMessage());
        }
        return result;
    }

    public String[] getMailForceTo() {
        List<String> forceMailToList = new ArrayList<>();
        Optional<String> forceMailToOpt = getMailProperty("mail.force.to");
        if (forceMailToOpt.isPresent()) {
            if (forceMailToOpt.get().indexOf(",") == -1) {
                forceMailToList.add(forceMailToOpt.get());
            } else {
                String[] recipientSet = forceMailToOpt.get().split(",");
                forceMailToList = Arrays.asList(recipientSet);
            }
        }
        return forceMailToList.toArray(new String[]{});
    }

    public Optional<String> getMailFrom() {
        return getMailProperty("mail.from");
    }

    public String[] getCcSet() {
        String[] resultSet = new String[]{};
        Optional<String> forceCcTo = getMailProperty("mail.cc.to");
        try {
            if (forceCcTo.isPresent()) {
                if (forceCcTo.get().indexOf(",") == -1) {
                    resultSet = new String[]{forceCcTo.get()};
                } else {
                    resultSet = forceCcTo.get().split(",");
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return resultSet;
    }


}
