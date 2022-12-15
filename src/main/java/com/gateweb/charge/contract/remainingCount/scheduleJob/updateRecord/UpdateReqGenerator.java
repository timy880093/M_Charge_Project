package com.gateweb.charge.contract.remainingCount.scheduleJob.updateRecord;

import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrame;

public interface UpdateReqGenerator {
    public RemainingRecordUpdateReq gen(RemainingRecordFrame frame, int newUsage, String businessNo);
}
