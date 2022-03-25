package com.gateweb.charge.feeCalculation.dataCounter;

import com.gateweb.charge.enumeration.ChargeBaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataCounterGateway {

    @Autowired
    IasrDataCounterByInvoiceDate iasrDataCounterByInvoiceDate;
    @Autowired
    IasrDataCounterByUploadDate iasrDataCounterByUploadDate;

    public Optional<DataCounter> getDataCounter(ChargeBaseType chargeBaseType) {
        DataCounter dataCounter = null;
        switch (chargeBaseType) {
            case INVOICE_AMOUNT_SUM:
                dataCounter = iasrDataCounterByInvoiceDate;
                break;
            case INVOICE_AMOUNT_SUM_BY_UPLOAD_DATE:
                dataCounter = iasrDataCounterByUploadDate;
                break;
        }
        return Optional.ofNullable(dataCounter);
    }
}
