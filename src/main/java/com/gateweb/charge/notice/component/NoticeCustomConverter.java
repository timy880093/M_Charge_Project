package com.gateweb.charge.notice.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateweb.charge.notice.bean.NoticeCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NoticeCustomConverter {
    Logger logger = LoggerFactory.getLogger(NoticeCustomConverter.class);
    ObjectMapper objectMapper = new ObjectMapper();

    public Optional<NoticeCustom> fromStr(String json) {
        try {
            return Optional.of(objectMapper.readValue(json, NoticeCustom.class));
        } catch (Exception e) {
            logger.info(e.getMessage());
            return Optional.empty();
        }
    }

    public String fromObj(NoticeCustom noticeCustom) {
        try {
            return objectMapper.writeValueAsString(noticeCustom);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "";
    }
}
