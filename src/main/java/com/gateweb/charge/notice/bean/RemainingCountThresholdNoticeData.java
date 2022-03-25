package com.gateweb.charge.notice.bean;

import java.time.LocalDateTime;

@Deprecated
public class RemainingCountThresholdNoticeData {
    Long companyId;
    Long contractId;
    Long remaining;
    LocalDateTime executionDateTime;

    public RemainingCountThresholdNoticeData(Long companyId, Long contractId, Long remaining, LocalDateTime executionDateTime) {
        this.companyId = companyId;
        this.contractId = contractId;
        this.remaining = remaining;
        this.executionDateTime = executionDateTime;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getRemaining() {
        return remaining;
    }

    public void setRemaining(Long remaining) {
        this.remaining = remaining;
    }

    public LocalDateTime getExecutionDateTime() {
        return executionDateTime;
    }

    public void setExecutionDateTime(LocalDateTime executionDateTime) {
        this.executionDateTime = executionDateTime;
    }
}
