package com.gateweb.charge.contract.remainingCount.component;

import com.gateweb.charge.contract.remainingCount.bean.ChargeRemainingCountRenewData;
import com.gateweb.charge.contract.remainingCount.bean.RemainingRecordModel;
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

    Optional<ChargeRemainingCountRenewData> execute(Contract originalContract, Contract renewedContract, RemainingRecordModel remainingRecordModel) {

        //找出需要更新的項目
        List<InvoiceRemaining> updateRecordList =
                invoiceRemainingRepository.findByCompanyIdAndInvoiceDateGreaterThanOrderByInvoiceDate(
                        originalContract.getCompanyId()
                        , remainingRecordModel.getTargetRecord().getInvoiceDate()
                );
        //更新計數
        Integer remaining = remainingRecordModel.getTargetRecord().getRemaining();
        for (InvoiceRemaining updateRemaining : updateRecordList) {
            updateRemaining.setRemaining(remaining - updateRemaining.getUsage());
            remaining = updateRemaining.getRemaining();
        }
        ChargeRemainingCountRenewData chargeRemainingCountRenewData = new ChargeRemainingCountRenewData(
                originalContract
                , renewedContract
                , remainingRecordModel
                , updateRecordList
        );
        return Optional.of(chargeRemainingCountRenewData);

    }
}
