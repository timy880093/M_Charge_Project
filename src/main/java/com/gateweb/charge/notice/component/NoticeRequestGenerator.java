package com.gateweb.charge.notice.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.gateweb.charge.enumeration.NoticeStatus;
import com.gateweb.charge.enumeration.NoticeType;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.notice.bean.NoticeCustom;
import com.gateweb.orm.charge.entity.*;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.NoticeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.gateweb.charge.notice.utils.CompanyRecipientUtils.noticeCompanyRecipientEncode;

@Component
public class NoticeRequestGenerator {
    Logger logger = LoggerFactory.getLogger(NoticeRequestGenerator.class);

    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    NoticeCustomConverter noticeCustomConverter;
    @Autowired
    CompanyRepository companyRepository;

    private boolean hasValidRecipient(JsonNode custom) {
        if (!custom.has("recipient")) {
            return false;
        }
        if (StringUtils.isEmpty(custom.get("recipient").textValue())) {
            return false;
        }
        return true;
    }

    private Optional<NoticeType> getNoticeType(JsonNode custom) {
        Optional<NoticeType> noticeTypeOptional = Optional.empty();
        if (hasValidRecipient(custom)) {
            if (custom.has("correction")) {
                Boolean isCorrection = custom.get("correction").booleanValue();
                if (isCorrection) {
                    noticeTypeOptional = Optional.of(NoticeType.PAYMENT_REQUEST_CORRECTION_MAIL_NO_CC);
                } else {
                    noticeTypeOptional = Optional.of(NoticeType.PAYMENT_REQUEST_MAIL_NO_CC);
                }
            }
        } else {
            noticeTypeOptional = Optional.of(NoticeType.PAYMENT_REQUEST_MAIL);
        }
        return noticeTypeOptional;
    }

    public Optional<Notice> genPaymentRequestNoticeRequest(Bill bill, JsonNode custom, CallerInfo callerInfo) {
        try {
            boolean hasValidRecipient = hasValidRecipient(custom);
            Optional<NoticeType> noticeTypeOptional = getNoticeType(custom);
            if (!noticeTypeOptional.isPresent()) {
                return Optional.empty();
            } else {
                Notice notice = new Notice();
                notice.setNoticeType(noticeTypeOptional.get());
                notice.setCustomJson(custom.toString());
                notice.setBillId(bill.getBillId());
                notice.setCompanyId(bill.getCompanyId());
                notice.setNoticeStatus(NoticeStatus.WAIT_FOR_FIRST_SEND);
                notice.setCreateDate(LocalDateTime.now());
                notice.setCreatorId(callerInfo.getUserEntity().getUserId().longValue());
                if (hasValidRecipient) {
                    notice.setRecipient(custom.get("recipient").textValue());
                } else {
                    Optional<Company> companyOptional = companyRepository.findByCompanyId(bill.getCompanyId().intValue());
                    if (companyOptional.isPresent()) {
                        notice.setRecipient(noticeCompanyRecipientEncode(companyOptional.get()));
                    } else {
                        return Optional.empty();
                    }
                }
                return Optional.of(notice);
            }
        } catch (Exception ex) {
            logger.error("bill:{};custom:{}", bill, custom);
        }
        return Optional.empty();
    }

    public Optional<Notice> genPaymentRequestDueNoticeRequest(Bill bill, JsonNode custom, CallerInfo callerInfo) {
        try {
            boolean hasValidRecipient = hasValidRecipient(custom);
            Notice notice = new Notice();
            notice.setNoticeType(NoticeType.PAYMENT_DUE_MAIL);
            notice.setCustomJson(custom.toString());
            notice.setBillId(bill.getBillId());
            notice.setCompanyId(bill.getCompanyId());
            notice.setNoticeStatus(NoticeStatus.WAIT_FOR_FIRST_SEND);
            notice.setCreateDate(LocalDateTime.now());
            notice.setCreatorId(callerInfo.getUserEntity().getUserId().longValue());
            if (hasValidRecipient) {
                notice.setRecipient(custom.get("recipient").textValue());
            } else {
                Optional<Company> companyOptional = companyRepository.findByCompanyId(bill.getCompanyId().intValue());
                if (companyOptional.isPresent()) {
                    notice.setRecipient(noticeCompanyRecipientEncode(companyOptional.get()));
                } else {
                    return Optional.empty();
                }
            }
            return Optional.of(notice);
        } catch (Exception ex) {
            logger.error("bill:{};custom:{}", bill, custom);
        }
        return Optional.empty();
    }

    public Optional<Notice> genPaymentRequestOverdueNoticeRequest(Bill bill, JsonNode custom, CallerInfo callerInfo) {
        try {
            boolean hasValidRecipient = hasValidRecipient(custom);
            Notice notice = new Notice();
            notice.setNoticeType(NoticeType.PAYMENT_OVERDUE_MAIL);
            notice.setCustomJson(custom.toString());
            notice.setBillId(bill.getBillId());
            notice.setCompanyId(bill.getCompanyId());
            notice.setNoticeStatus(NoticeStatus.WAIT_FOR_FIRST_SEND);
            notice.setCreateDate(LocalDateTime.now());
            notice.setCreatorId(callerInfo.getUserEntity().getUserId().longValue());
            if (hasValidRecipient) {
                notice.setRecipient(custom.get("recipient").textValue());
            } else {
                Optional<Company> companyOptional = companyRepository.findByCompanyId(bill.getCompanyId().intValue());
                if (companyOptional.isPresent()) {
                    notice.setRecipient(noticeCompanyRecipientEncode(companyOptional.get()));
                } else {
                    return Optional.empty();
                }
            }
            return Optional.of(notice);
        } catch (Exception ex) {
            logger.error("bill:{};custom:{}", bill, custom);
        }
        return Optional.empty();
    }

    public void sendPaymentRequestNoticeWithCustomAlertMessage(
            Bill bill,
            Company company,
            String alertMessage,
            CallerInfo callerInfo) {
        Notice notice = createOneTimeBillPaymentRequestNoticeWithCustomRecipient(
                bill
                , company.getEmail1()
                , NoticeType.PAYMENT_REQUEST_MAIL
        );
        NoticeCustom noticeCustom = new NoticeCustom();
        noticeCustom.setExtraNoticeMessage(alertMessage);
        notice.setCustomJson(noticeCustomConverter.fromObj(noticeCustom));
        notice.setCreatorId(callerInfo.getUserEntity().getUserId().longValue());
        noticeRepository.save(notice);
    }

    public void sendPaymentRequestNotice(Bill bill, Company company, CallerInfo callerInfo) {
        Notice notice = createOneTimeBillPaymentRequestNoticeWithCustomRecipient(
                bill
                , company.getEmail1()
                , NoticeType.PAYMENT_REQUEST_MAIL
        );
        notice.setCreatorId(callerInfo.getUserEntity().getUserId().longValue());
        noticeRepository.save(notice);
    }

    public void sendPaymentRequestNoticeWithoutCc(Bill bill, String recipient, CallerInfo callerInfo) {
        Notice notice = createOneTimeBillPaymentRequestNoticeWithCustomRecipient(
                bill
                , recipient
                , NoticeType.PAYMENT_REQUEST_MAIL_NO_CC
        );
        notice.setCreatorId(callerInfo.getUserEntity().getCreatorId().longValue());
        notice.setCreateDate(LocalDateTime.now());
        noticeRepository.save(notice);
    }

    public void sendPaymentRequestCorrectionNoticeWithoutCc(Bill bill, String recipient, CallerInfo callerInfo) {
        Notice notice = createOneTimeBillPaymentRequestNoticeWithCustomRecipient(
                bill
                , recipient
                , NoticeType.PAYMENT_REQUEST_CORRECTION_MAIL_NO_CC
        );
        notice.setCreatorId(callerInfo.getUserEntity().getCreatorId().longValue());
        notice.setCreateDate(LocalDateTime.now());
        noticeRepository.save(notice);
    }

    public Notice createOneTimeBillPaymentRequestNoticeWithCustomRecipient(Bill bill, String recipient, NoticeType noticeType) {
        Notice notice = new Notice();
        notice.setNoticeType(noticeType);
        notice.setBillId(bill.getBillId());
        notice.setCompanyId(bill.getCompanyId());
        notice.setRequestBySystem(false);
        notice.setNoticeStatus(NoticeStatus.WAIT_FOR_FIRST_SEND);
        notice.setRecipient(recipient);
        notice.setCreateDate(LocalDateTime.now());
        return notice;
    }

    public Notice genContractEnabledMailNotice(Contract contract, Long callerId) {
        Notice notice = new Notice();
        notice.setNoticeType(NoticeType.CONTRACT_ENABLED_NOTICE);
        notice.setContractId(contract.getContractId());
        notice.setCompanyId(contract.getCompanyId());
        notice.setCreateDate(LocalDateTime.now());
        notice.setModifyDate(LocalDateTime.now());
        if (callerId != null) {
            notice.setCreatorId(callerId);
            notice.setModifierId(callerId);
            notice.setRequestBySystem(false);
        } else {
            notice.setRequestBySystem(true);
        }
        notice.setNoticeStatus(NoticeStatus.WAIT_FOR_FIRST_SEND);
        return notice;
    }

    public Notice genRemainingCountInventoryAlertMailNotice(InvoiceRemaining invoiceRemaining) {
        Notice notice = new Notice();
        notice.setInvoiceRemainingId(invoiceRemaining.getInvoiceRemainingId());
        notice.setCompanyId(invoiceRemaining.getCompanyId());
        notice.setContractId(invoiceRemaining.getContractId());
        notice.setNoticeStatus(NoticeStatus.WAIT_FOR_FIRST_SEND);
        notice.setNoticeType(NoticeType.REMAINING_COUNT_INVENTORY_ALERT_CC);
        notice.setCreateDate(LocalDateTime.now());
        notice.setRequestBySystem(true);
        return notice;
    }


}
