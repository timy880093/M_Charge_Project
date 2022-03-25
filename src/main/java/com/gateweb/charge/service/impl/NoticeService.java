package com.gateweb.charge.service.impl;

import com.gateweb.charge.enumeration.NoticeStatus;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.charge.exception.InvalidNoticeRequestException;
import com.gateweb.charge.frontEndIntegration.bean.OperationObject;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.notice.component.NoticeHandler;
import com.gateweb.charge.notice.component.NoticeMimeMessageHelperProvider;
import com.gateweb.charge.notice.component.NoticeMimeMessageHelperSender;
import com.gateweb.charge.notice.component.NoticeRequestGenerator;
import com.gateweb.charge.service.dataGateway.BillDataGateway;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.Notice;
import com.gateweb.orm.charge.repository.NoticeRepository;
import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class NoticeService {
    protected final Logger logger = LogManager.getLogger(getClass());
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    @Qualifier("chargeSystemEventBus")
    EventBus chargeSystemEventBus;
    @Autowired
    NoticeHandler noticeHandler;
    @Autowired
    BillDataGateway billDataGateway;
    @Autowired
    NoticeRequestGenerator noticeRequestGenerator;

    public void sendNotice(Notice notice) {
        try {
            Optional<NoticeMimeMessageHelperProvider> noticeMimeMessageHelperProviderOpt
                    = noticeHandler.getMimeMessageHelperProvider(notice);
            Optional<NoticeMimeMessageHelperSender> noticeMimeMessageHelperSenderOpt
                    = noticeHandler.getMimeMessageHelperSender(notice);
            if (noticeMimeMessageHelperProviderOpt.isPresent() && noticeMimeMessageHelperSenderOpt.isPresent()) {
                Optional<MimeMessageHelper> mimeMessageHelperOptional
                        = noticeMimeMessageHelperProviderOpt.get().createHelper(notice);
                if (mimeMessageHelperOptional.isPresent()) {
                    noticeMimeMessageHelperSenderOpt.get().sendMimeMessageHelper(mimeMessageHelperOptional.get());
                    //記錄最後寄給誰
                    notice.setRecipient(getRecipientListStr(mimeMessageHelperOptional.get()));
                    notice.setModifyDate(LocalDateTime.now());
                    notice.setModifierId(notice.getCreatorId());
                    ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                            EventSource.NOTICE,
                            EventAction.SEND_NOTICE,
                            notice.getNoticeId(),
                            notice.getCreatorId()
                    );
                    chargeSystemEventBus.post(chargeSystemEvent);
                } else {
                    logger.error("invalidMailData:{}", notice.toString());
                }
            } else {
                logger.error("invalidMailType:{}", notice.toString());
            }
            notice.setNoticeStatus(NoticeStatus.FINISH);
            noticeRepository.save(notice);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public Map sendPaymentRequestEmail(OperationObject operationObject, CallerInfo callerInfo) {
        Set<Bill> successList = new HashSet<>();
        Set<Bill> failList = new HashSet<>();
        Set<Bill> ignoreList = new HashSet<>();
        List<Bill> billList = billDataGateway.searchByOperationObj(operationObject);
        billList.stream().forEach(bill -> {
            try {
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(bill.getBillYm())) {
                    Optional<Notice> noticeOptional = noticeRequestGenerator.genPaymentRequestNoticeRequest(
                            bill, operationObject.getCustom(), callerInfo
                    );
                    if (noticeOptional.isPresent()) {
                        noticeRepository.save(noticeOptional.get());
                        successList.add(bill);
                    } else {
                        failList.add(bill);
                        throw new InvalidNoticeRequestException();
                    }
                } else {
                    ignoreList.add(bill);
                }
            } catch (Exception ex) {
                logger.error("OperationObject:{};{}", operationObject.toString(), ex.getMessage());
                failList.add(bill);
            }
        });
        return genPaymentRequestMailResponseMap(successList, failList, ignoreList);
    }

    public String getRecipientListStr(MimeMessageHelper mimeMessageHelper) throws MessagingException {
        Set<String> recipientSet = new HashSet<>();
        for (Address address : mimeMessageHelper.getMimeMessage().getAllRecipients()) {
            recipientSet.add(address.toString());
        }
        return String.join(",", recipientSet);
    }

    private Map genPaymentRequestMailResponseMap(Set<Bill> successList, Set<Bill> failList, Set<Bill> ignoreList) {
        Map dataMap = new HashMap();
        dataMap.put("status", "success");
        dataMap.put("title", "操作完成");
        StringBuilder stringBuilder = new StringBuilder();
        if (!successList.isEmpty()) {
            stringBuilder.append("寄送: " + successList.size() + "個項目\n");
        }
        if (!ignoreList.isEmpty()) {
            stringBuilder.append("忽略: " + successList.size() + "個項目\n");
        }
        if (!failList.isEmpty()) {
            stringBuilder.append(successList.size() + "個項目處理錯誤\n");
        }
        dataMap.put("message", stringBuilder.toString());
        return dataMap;
    }

}
