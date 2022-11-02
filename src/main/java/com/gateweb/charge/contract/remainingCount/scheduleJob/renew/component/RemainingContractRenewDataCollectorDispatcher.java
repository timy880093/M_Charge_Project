package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component;

import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.RemainingContractRenewReq;

import java.util.Optional;

public interface RemainingContractRenewDataCollectorDispatcher {
    Optional<ChargeRemainingCountRenewData> execute(RemainingContractRenewReq remainingContractRenewReq);
}
