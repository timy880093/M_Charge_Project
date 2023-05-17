package com.gateweb.charge.infrastructure.propertyProvider;

public class MailProperty {
    String mailServerHost;
    Integer mailServerPort;
    String mailServerUsername;
    String mailServerPassword;
    String mailTransportProtocol;
    String smtpAuth;
    String smtpStarttlsEnable;
    String mailFrom;
    String[] mailForceTo;
    String[] mailCcTo;
    String[] mailDueCcTo;
    String[] mailOverdueCcTo;

    public MailProperty() {
    }

    public MailProperty(String mailServerHost, Integer mailServerPort, String mailServerUsername, String mailServerPassword, String mailTransportProtocol, String smtpAuth, String smtpStarttlsEnable, String mailFrom, String[] mailForceTo, String[] mailCcTo, String[] mailDueBccTo, String[] mailOverdueBccTo) {
        this.mailServerHost = mailServerHost;
        this.mailServerPort = mailServerPort;
        this.mailServerUsername = mailServerUsername;
        this.mailServerPassword = mailServerPassword;
        this.mailTransportProtocol = mailTransportProtocol;
        this.smtpAuth = smtpAuth;
        this.smtpStarttlsEnable = smtpStarttlsEnable;
        this.mailFrom = mailFrom;
        this.mailForceTo = mailForceTo;
        this.mailCcTo = mailCcTo;
        this.mailDueCcTo = mailDueBccTo;
        this.mailOverdueCcTo = mailOverdueBccTo;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public Integer getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(Integer mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public String getMailServerUsername() {
        return mailServerUsername;
    }

    public void setMailServerUsername(String mailServerUsername) {
        this.mailServerUsername = mailServerUsername;
    }

    public String getMailServerPassword() {
        return mailServerPassword;
    }

    public void setMailServerPassword(String mailServerPassword) {
        this.mailServerPassword = mailServerPassword;
    }

    public String getMailTransportProtocol() {
        return mailTransportProtocol;
    }

    public void setMailTransportProtocol(String mailTransportProtocol) {
        this.mailTransportProtocol = mailTransportProtocol;
    }

    public String getSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(String smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public String getSmtpStarttlsEnable() {
        return smtpStarttlsEnable;
    }

    public void setSmtpStarttlsEnable(String smtpStarttlsEnable) {
        this.smtpStarttlsEnable = smtpStarttlsEnable;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String[] getMailForceTo() {
        return mailForceTo;
    }

    public void setMailForceTo(String[] mailForceTo) {
        this.mailForceTo = mailForceTo;
    }

    public String[] getMailCcTo() {
        return mailCcTo;
    }

    public void setMailCcTo(String[] mailCcTo) {
        this.mailCcTo = mailCcTo;
    }

    public String[] getMailDueCcTo() {
        return mailDueCcTo;
    }

    public void setMailDueCcTo(String[] mailDueCcTo) {
        this.mailDueCcTo = mailDueCcTo;
    }

    public String[] getMailOverdueCcTo() {
        return mailOverdueCcTo;
    }

    public void setMailOverdueCcTo(String[] mailOverdueCcTo) {
        this.mailOverdueCcTo = mailOverdueCcTo;
    }
}
