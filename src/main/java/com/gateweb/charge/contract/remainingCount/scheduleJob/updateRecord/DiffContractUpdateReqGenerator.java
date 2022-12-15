package com.gateweb.charge.contract.remainingCount.scheduleJob.updateRecord;

import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrame;

public class DiffContractUpdateReqGenerator implements UpdateReqGenerator {

    @Override
    public RemainingRecordUpdateReq gen(RemainingRecordFrame frame, int newUsage, String businessNo) {
        int newRemaining = frame.getTargetRecord().getRemaining()
                - (newUsage - frame.getTargetRecord().getUsage());
        RemainingRecordUpdateReq remainingRecordUpdateReq = new RemainingRecordUpdateReq(
                businessNo
                , frame
                , newUsage
                , newRemaining
        );
        return remainingRecordUpdateReq;
    }
}
