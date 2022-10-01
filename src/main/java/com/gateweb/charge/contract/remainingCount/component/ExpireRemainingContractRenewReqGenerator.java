package com.gateweb.charge.contract.remainingCount.component;

import com.gateweb.charge.contract.remainingCount.bean.RemainingContractDispatchData;
import com.gateweb.charge.contract.remainingCount.bean.RemainingContractRenewReq;
import com.gateweb.charge.contract.remainingCount.bean.RemainingRecordModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ExpireRemainingContractRenewReqGenerator implements RemainingContractRenewReqGenerator {
    @Autowired
    RemainingRecordModelComponent remainingRecordModelComponent;

    @Override
    public Optional<RemainingContractRenewReq> gen(RemainingContractDispatchData data) {
        Optional<RemainingRecordModel> remainingRecordModelOpt = remainingRecordModelComponent.getRemainingRecordModel(
                data.getTargetInvoiceRemaining()
        );
        if (remainingRecordModelOpt.isPresent()) {
            return Optional.of(new RemainingContractRenewReq(
                    data.getCompany()
                    , data.getRemainingContractRenewDataCollector()
                    , data.getContract()
                    , remainingRecordModelOpt.get()
            ));
        }
        return Optional.empty();
    }
}
