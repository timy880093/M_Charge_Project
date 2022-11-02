package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component;

import com.gateweb.charge.contract.remainingCount.scheduleJob.renew.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrame;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ChargeRemainingCountRenewDataGenerator {
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;

    Optional<ChargeRemainingCountRenewData> execute(Contract originalContract, Contract renewedContract, RemainingRecordFrame remainingRecordFrame) {
        //找出需要更新的項目
        List<InvoiceRemaining> updateRecordList =
                invoiceRemainingRepository.findByCompanyIdAndInvoiceDateGreaterThanOrderByInvoiceDate(
                        originalContract.getCompanyId()
                        , remainingRecordFrame.getTargetRecord().getInvoiceDate()
                );
        //更新計數
        Integer remaining = remainingRecordFrame.getTargetRecord().getRemaining();
        for (InvoiceRemaining updateRemaining : updateRecordList) {
            updateRemaining.setRemaining(remaining - updateRemaining.getUsage());
            remaining = updateRemaining.getRemaining();
        }
        ChargeRemainingCountRenewData chargeRemainingCountRenewData = new ChargeRemainingCountRenewData(
                originalContract
                , renewedContract
                , remainingRecordFrame
                , updateRecordList
        );
        return Optional.of(chargeRemainingCountRenewData);
    }
}
