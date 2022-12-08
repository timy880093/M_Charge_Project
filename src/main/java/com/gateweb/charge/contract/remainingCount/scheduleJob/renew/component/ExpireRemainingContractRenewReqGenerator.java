package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component;

import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.RemainingContractDispatchData;
import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.RemainingContractRenewReq;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrame;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrameComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ExpireRemainingContractRenewReqGenerator implements RemainingContractRenewReqGenerator {
    @Autowired
    RemainingRecordFrameComponent remainingRecordFrameComponent;

    @Override
    public Optional<RemainingContractRenewReq> gen(RemainingContractDispatchData data) {
        Optional<RemainingRecordFrame> remainingRecordModelOpt = remainingRecordFrameComponent.getRemainingRecordModel(
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
