package com.gateweb.charge.service;

import com.gateweb.charge.exception.CancelAlreadyPaidBillException;
import com.gateweb.charge.exception.PayAlreadyException;
import com.gateweb.charge.frontEndIntegration.bean.OperationObject;
import com.gateweb.charge.frontEndIntegration.bean.OutToBillRequest;
import com.gateweb.charge.frontEndIntegration.bean.PayInfo;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Company;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface BillService {

    void transactionPayBill(PayInfo payInfo, Long callerId) throws PayAlreadyException;

    void transactionInBill(Long billId, CallerInfo callerInfo) throws PayAlreadyException;

    void transactionCancelBillByBillId(Long billId, CallerInfo callerInfo) throws CancelAlreadyPaidBillException;

    void transactionCancelPayment(Long billId, CallerInfo callerInfo) throws CancelAlreadyPaidBillException;

    void transactionCancelPayment(Bill bill, CallerInfo callerInfo);

    boolean sendPaymentRequestMailByCompanyAndAlertMessage(Long billId, String alertMessage, CallerInfo callerInfo);

    void sendPaymentRequestMailByRecipient(Long billId, String recipient, Boolean isCorrection, CallerInfo callerInfo);

    List<Bill> serverSideProcessingSearchByPageInfo(PageInfo pageInfo);

    List<Bill> outBill(
            Bill template
            , Collection<BillingItem> billingItemCollection
            , boolean minimumFeeCheck
            , Long callerId);

    @Transactional
    void transactionOutToBillByAjaxRequest(OutToBillRequest outToBillRequest, Long callerId);
}
