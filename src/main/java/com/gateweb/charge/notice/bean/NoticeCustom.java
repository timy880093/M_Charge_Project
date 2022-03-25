package com.gateweb.charge.notice.bean;

public class NoticeCustom {
    String extraNoticeMessage;
    String recipient;
    String correction;

    public NoticeCustom() {
    }

    public NoticeCustom(String extraNoticeMessage, String recipient, String correction) {
        this.extraNoticeMessage = extraNoticeMessage;
        this.recipient = recipient;
        this.correction = correction;
    }

    public String getExtraNoticeMessage() {
        return extraNoticeMessage;
    }

    public void setExtraNoticeMessage(String extraNoticeMessage) {
        this.extraNoticeMessage = extraNoticeMessage;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getCorrection() {
        return correction;
    }

    public void setCorrection(String correction) {
        this.correction = correction;
    }

    @Override
    public String toString() {
        return "NoticeCustom{" +
                "extraNoticeMessage='" + extraNoticeMessage + '\'' +
                ", recipient='" + recipient + '\'' +
                ", correction='" + correction + '\'' +
                '}';
    }
}
