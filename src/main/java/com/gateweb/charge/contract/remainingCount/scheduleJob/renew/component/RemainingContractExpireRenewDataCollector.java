package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component;

import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.RemainingContractRenewReq;
import com.gateweb.orm.charge.entity.Contract;

import java.util.Optional;

public interface RemainingContractExpireRenewDataCollector {
    Optional<ChargeRemainingCountRenewData> execute(RemainingContractRenewReq remainingContractRenewReq, Contract renewedContract);
}
