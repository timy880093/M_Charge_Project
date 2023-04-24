package com.gateweb.charge.service.dataGateway;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrameComponent;
import com.gateweb.charge.contract.remainingCount.remainingRecordFrame.RemainingRecordFrameUtils;
import com.gateweb.charge.report.bean.InvoiceRemainingHistoryView;
import com.gateweb.orm.charge.entity.InvoiceRemainingData;
import com.gateweb.orm.charge.repository.InvoiceRemainingDataRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InvoiceRemainingHistoryViewDataGateway {
    protected final Logger logger = LogManager.getLogger(getClass());
    @Autowired
    InvoiceRemainingDataRepository invoiceRemainingDataRepository;
    @Autowired
    RemainingRecordFrameComponent remainingRecordFrameComponent;

    public List<InvoiceRemainingHistoryView> findByCompanyId(Long companyId) {
        List<InvoiceRemainingData> invoiceRemainingDataList
                = invoiceRemainingDataRepository.findInvoiceRemainingData(companyId);
        return fromDataToView(invoiceRemainingDataList);
    }

    private List<InvoiceRemainingHistoryView> fromDataToView(List<InvoiceRemainingData> invoiceRemainingDataList) {
        List<InvoiceRemainingHistoryView> invoiceRemainingHistoryViewList = new ArrayList<>();
        //排序資料
        List<InvoiceRemainingData> sortedList = invoiceRemainingDataList.stream().sorted(
                Comparator.comparing(InvoiceRemainingData::getInvoiceDate)
        ).collect(Collectors.toList());

        for (int i = 0; i < sortedList.size(); i++) {
            Optional<String> updateTimestampOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                    sortedList.get(i).getModifyDate()
                    , "yyyy/MM/dd HH:mm:ss"
            );
            if (!updateTimestampOpt.isPresent()) {
                break;
            }
            if (i > 0) {
                Optional<CustomInterval> calculateIntervalOpt = RemainingRecordFrameUtils.genRemainingRecordInvoiceDateInterval(
                        sortedList.get(i - 1).getInvoiceDate()
                        , sortedList.get(i).getInvoiceDate()
                );
                if (calculateIntervalOpt.isPresent()) {
                    Optional<String> calculateStartDateStrOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                            calculateIntervalOpt.get().getStartLocalDateTime()
                            , "yyyy/MM/dd HH:mm:ss"
                    );
                    Optional<String> calculateEndDateStrOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                            calculateIntervalOpt.get().getEndLocalDateTime()
                            , "yyyy/MM/dd HH:mm:ss"
                    );
                    if (calculateEndDateStrOpt.isPresent() && calculateEndDateStrOpt.isPresent()) {
                        InvoiceRemainingHistoryView invoiceRemainingHistoryView = new InvoiceRemainingHistoryView(
                                sortedList.get(i).getInvoiceRemainingId()
                                , sortedList.get(i).getBusinessNo()
                                , sortedList.get(i).getPackageName()
                                , sortedList.get(i).getUsage()
                                , sortedList.get(i).getRemaining()
                                , calculateStartDateStrOpt.get()
                                , calculateEndDateStrOpt.get()
                                , updateTimestampOpt.get()
                        );
                        invoiceRemainingHistoryViewList.add(invoiceRemainingHistoryView);
                    }
                }
            } else {
                InvoiceRemainingHistoryView invoiceRemainingHistoryView = new InvoiceRemainingHistoryView(
                        sortedList.get(i).getInvoiceRemainingId()
                        , sortedList.get(i).getBusinessNo()
                        , sortedList.get(i).getPackageName()
                        , sortedList.get(i).getUsage()
                        , sortedList.get(i).getRemaining()
                        , ""
                        , ""
                        , updateTimestampOpt.get()
                );
                invoiceRemainingHistoryViewList.add(invoiceRemainingHistoryView);
            }
        }
        return invoiceRemainingHistoryViewList;
    }
}
