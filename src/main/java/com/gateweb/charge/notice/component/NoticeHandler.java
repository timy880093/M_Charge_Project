package com.gateweb.charge.notice.component;

import com.gateweb.charge.enumeration.NoticeType;
import com.gateweb.orm.charge.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NoticeHandler {
    @Autowired
    ContractInitializeNoticeComponent contractInitializeNoticeComponent;
    @Autowired
    PaymentRequestMimeMessageHelperProvider paymentRequestMimeMessageHelperProvider;
    @Autowired
    PaymentDueRequestMimeMessageHelperProvider paymentDueRequestMimeMessageHelperProvider;
    @Autowired
    PaymentOverdueRequestMimeMessageHelperProvider paymentOverdueRequestMimeMessageHelperProvider;
    @Autowired
    RemainingCountThresholdNoticeComponent remainingCountThresholdNoticeComponent;
    @Autowired
    NoCcMailSender noCcMailSender;
    @Autowired
    GeneralMailSender generalMailSender;
    @Autowired
    DueMailSender dueMailSender;
    @Autowired
    OverdueMailSender overdueMailSender;

    public Optional<NoticeMimeMessageHelperSender> getMimeMessageHelperSender(Notice notice) {
        Optional<NoticeMimeMessageHelperSender> noticeMimeMessageHelperSenderOptional = Optional.empty();
        if (notice.getNoticeType().equals(NoticeType.PAYMENT_REQUEST_MAIL_NO_CC)) {
            noticeMimeMessageHelperSenderOptional = Optional.of(noCcMailSender);
        } else if (notice.getNoticeType().equals(NoticeType.PAYMENT_DUE_MAIL)) {
            noticeMimeMessageHelperSenderOptional = Optional.of(dueMailSender);
        } else if (notice.getNoticeType().equals(NoticeType.PAYMENT_OVERDUE_MAIL)) {
            noticeMimeMessageHelperSenderOptional = Optional.of(overdueMailSender);
        } else {
            noticeMimeMessageHelperSenderOptional = Optional.of(generalMailSender);
        }
        return noticeMimeMessageHelperSenderOptional;
    }

    public Optional<NoticeMimeMessageHelperProvider> getMimeMessageHelperProvider(Notice notice) {
        Optional<NoticeMimeMessageHelperProvider> noticeMimeMessageHelperProviderOptional = Optional.empty();
        if (isPaymentRequestType(notice.getNoticeType())) {
            noticeMimeMessageHelperProviderOptional = Optional.of(paymentRequestMimeMessageHelperProvider);
        }
        if (isInventoryNotice(notice.getNoticeType())) {
            noticeMimeMessageHelperProviderOptional = Optional.of(remainingCountThresholdNoticeComponent);
        }
        if (isEnabledNotice(notice.getNoticeType())) {
            noticeMimeMessageHelperProviderOptional = Optional.of(contractInitializeNoticeComponent);
        }
        if (isDueNotice(notice.getNoticeType())) {
            noticeMimeMessageHelperProviderOptional = Optional.of(paymentDueRequestMimeMessageHelperProvider);
        }
        if (isOverdueNotice(notice.getNoticeType())) {
            noticeMimeMessageHelperProviderOptional = Optional.of(paymentOverdueRequestMimeMessageHelperProvider);
        }
        return noticeMimeMessageHelperProviderOptional;
    }

    public boolean isPaymentRequestType(NoticeType noticeType) {
        if (noticeType.equals(NoticeType.PAYMENT_REQUEST_MAIL_NO_CC)
                || noticeType.equals(NoticeType.PAYMENT_REQUEST_MAIL)
                || noticeType.equals(NoticeType.PAYMENT_REQUEST_CORRECTION_MAIL)
                || noticeType.equals(NoticeType.PAYMENT_REQUEST_CORRECTION_MAIL_NO_CC)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInventoryNotice(NoticeType noticeType) {
        return noticeType.equals(NoticeType.REMAINING_COUNT_INVENTORY_ALERT_CC);
    }

    public boolean isEnabledNotice(NoticeType noticeType) {
        return noticeType.equals(NoticeType.CONTRACT_ENABLED_NOTICE);
    }

    private boolean isDueNotice(NoticeType noticeType) {
        return noticeType.equals(NoticeType.PAYMENT_DUE_MAIL);
    }

    private boolean isOverdueNotice(NoticeType noticeType) {
        return noticeType.equals(NoticeType.PAYMENT_OVERDUE_MAIL);
    }
}
