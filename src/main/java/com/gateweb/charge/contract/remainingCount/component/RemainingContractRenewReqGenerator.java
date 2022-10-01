package com.gateweb.charge.contract.remainingCount.component;

import com.gateweb.charge.contract.remainingCount.bean.RemainingContractDispatchData;
import com.gateweb.charge.contract.remainingCount.bean.RemainingContractRenewReq;

import java.util.Optional;

public interface RemainingContractRenewReqGenerator {
    public Optional<RemainingContractRenewReq> gen(RemainingContractDispatchData data);

}

