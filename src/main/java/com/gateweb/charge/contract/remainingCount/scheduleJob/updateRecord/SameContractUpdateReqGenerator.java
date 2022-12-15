package com.gateweb.charge.contract.remainingCount.scheduleJob.updateRecord;

import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrame;

public class SameContractUpdateReqGenerator implements UpdateReqGenerator {

    @Override
    public RemainingRecordUpdateReq gen(RemainingRecordFrame frame, int newUsage, String businessNo) {
        //組合
        RemainingRecordUpdateReq remainingRecordUpdateReq = new RemainingRecordUpdateReq(
                businessNo
                , frame
                , newUsage
                , frame.getPrevRecord().getRemaining() - newUsage
        );
        return remainingRecordUpdateReq;
    }
}
