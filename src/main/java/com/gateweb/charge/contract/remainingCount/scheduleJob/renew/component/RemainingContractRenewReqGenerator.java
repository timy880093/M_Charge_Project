package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component;

import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.RemainingContractDispatchData;
import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.RemainingContractRenewReq;

import java.util.Optional;

public interface RemainingContractRenewReqGenerator {
    public Optional<RemainingContractRenewReq> gen(RemainingContractDispatchData data);

}

