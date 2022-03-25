package com.gateweb.charge.contract.component;

import com.gateweb.charge.contract.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.bean.request.RemainingContractRenewReq;

import java.util.Optional;

public interface RemainingContractRenewDataCollector {
    Optional<ChargeRemainingCountRenewData> execute(RemainingContractRenewReq remainingContractRenewReq);
}
