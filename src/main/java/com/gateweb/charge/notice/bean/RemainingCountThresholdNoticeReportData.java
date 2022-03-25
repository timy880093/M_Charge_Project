package com.gateweb.charge.notice.bean;

public class RemainingCountThresholdNoticeReportData {
    String companyName;
    int remaining;
    String executionDateTimeDesc;
    String[] recipients;
    String ctbcVirtualAccount;
    String nextRenewPackageName;
    String nextPrepaymentFee;

    public RemainingCountThresholdNoticeReportData() {
    }

    public RemainingCountThresholdNoticeReportData(String companyName, int remaining, String executionDateTimeDesc, String[] recipients, String ctbcVirtualAccount, String nextRenewPackageName, String nextPrepaymentFee) {
        this.companyName = companyName;
        this.remaining = remaining;
        this.executionDateTimeDesc = executionDateTimeDesc;
        this.recipients = recipients;
        this.ctbcVirtualAccount = ctbcVirtualAccount;
        this.nextRenewPackageName = nextRenewPackageName;
        this.nextPrepaymentFee = nextPrepaymentFee;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public String getExecutionDateTimeDesc() {
        return executionDateTimeDesc;
    }

    public void setExecutionDateTimeDesc(String executionDateTimeDesc) {
        this.executionDateTimeDesc = executionDateTimeDesc;
    }

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public String getCtbcVirtualAccount() {
        return ctbcVirtualAccount;
    }

    public void setCtbcVirtualAccount(String ctbcVirtualAccount) {
        this.ctbcVirtualAccount = ctbcVirtualAccount;
    }

    public String getNextRenewPackageName() {
        return nextRenewPackageName;
    }

    public void setNextRenewPackageName(String nextRenewPackageName) {
        this.nextRenewPackageName = nextRenewPackageName;
    }

    public String getNextPrepaymentFee() {
        return nextPrepaymentFee;
    }

    public void setNextPrepaymentFee(String nextPrepaymentFee) {
        this.nextPrepaymentFee = nextPrepaymentFee;
    }
}
