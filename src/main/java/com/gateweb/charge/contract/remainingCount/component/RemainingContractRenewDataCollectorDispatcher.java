package com.gateweb.charge.contract.remainingCount.component;

import com.gateweb.charge.contract.remainingCount.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.remainingCount.bean.RemainingContractRenewReq;

import java.util.Optional;

public interface RemainingContractRenewDataCollectorDispatcher {
    Optional<ChargeRemainingCountRenewData> execute(RemainingContractRenewReq remainingContractRenewReq);
}
