package com.gateweb.charge.contract.remainingCount.component;

import com.gateweb.charge.contract.remainingCount.bean.RemainingContractDispatchData;
import com.gateweb.charge.contract.remainingCount.bean.RemainingContractRenewReq;
import com.gateweb.charge.contract.remainingCount.bean.RemainingRecordModel;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NegativeRemainingContractRenewReqGenerator implements RemainingContractRenewReqGenerator {
    @Autowired
    RemainingRecordModelComponent remainingRecordModelComponent;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;

    @Override
    public Optional<RemainingContractRenewReq> gen(RemainingContractDispatchData data) {
        Optional<RemainingRecordModel> remainingRecordModelOpt = getRemainingRecordModel(data);
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

    public Optional<RemainingRecordModel> getRemainingRecordModel(RemainingContractDispatchData data) {
        boolean isFirstRecordOfTheContract = remainingRecordModelComponent.isFirstRecordOfTheContract(
                data.getTargetInvoiceRemaining()
        );
        if (isFirstRecordOfTheContract) {
            //負項為該合約的首個項目，需要向下再找一個，因為找不到前一個，所以他就當那個prevRecord
            Optional<InvoiceRemaining> nextRecordOpt = invoiceRemainingRepository.findTopInvoiceRemainingByCompanyIdAndContractIdAndInvoiceDateIsGreaterThanOrderByInvoiceDateAsc(
                    data.getTargetInvoiceRemaining().getCompanyId()
                    , data.getTargetInvoiceRemaining().getContractId()
                    , data.getTargetInvoiceRemaining().getInvoiceDate()
            );
            if (nextRecordOpt.isPresent()) {
                return remainingRecordModelComponent.genRemainingRecordModel(
                        data.getTargetInvoiceRemaining()
                        , nextRecordOpt.get()
                );
            }
        }
        return remainingRecordModelComponent.getRemainingRecordModel(
                data.getTargetInvoiceRemaining()
        );
    }
}
