package com.gateweb.charge.service.impl;

import com.gateweb.charge.enumeration.BillStatus;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.charge.exception.CancelAlreadyPaidBillException;
import com.gateweb.charge.exception.PayAlreadyException;
import com.gateweb.charge.feeCalculation.billingItemGenerator.BillingItemFeeCalculator;
import com.gateweb.charge.frontEndIntegration.bean.OutToBillRequest;
import com.gateweb.charge.frontEndIntegration.bean.PayInfo;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.notice.component.NoticeRequestGenerator;
import com.gateweb.charge.service.BillService;
import com.gateweb.charge.service.dataGateway.BillDataGateway;
import com.gateweb.charge.service.dataGateway.BillingItemDataGateway;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.BillRepository;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.PackageRefRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import com.gateweb.utils.bean.BeanConverterUtils;
import com.google.common.eventbus.EventBus;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {
    protected final Logger logger = LogManager.getLogger(getClass());
    final BillingItemFeeCalculator billingItemFeeCalculator = new BillingItemFeeCalculator();

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    BillRepository billRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    BillDataGateway billDataGatewayImpl;
    @Autowired
    NoticeService noticeService;
    @Autowired
    BillingItemDataGateway billingItemDataGateway;
    @Autowired
    @Qualifier("chargeSystemEventBus")
    EventBus chargeSystemEventBus;
    @Autowired
    NoticeRequestGenerator noticeRequestGenerator;

    @Override
    public void transactionPayBill(PayInfo payInfo, Long callerId) throws PayAlreadyException {
        Optional<Bill> billOptional = billRepository.findById(payInfo.getBillId());
        if (billOptional.isPresent()) {
            if (billOptional.get().getBillStatus().equals(BillStatus.C)) {
                //寫入繳費記錄
                billOptional.get().setBillStatus(BillStatus.P);
                billOptional.get().setPaidDate(LocalDateTimeUtils.fromTimestamp(new Timestamp(payInfo.getPayTimestamp())));
                billOptional.get().setActualReceived(Integer.parseInt(payInfo.getPayAmount()));
                billOptional.get().setPaymentRemark(payInfo.getPaymentRemark());
                billOptional.get().setPaymentMethod(payInfo.getPaymentMethod());
                billRepository.save(billOptional.get());
                ChargeSystemEvent billPaidEvent = new ChargeSystemEvent(
                        EventSource.BILL
                        , EventAction.PAID
                        , payInfo.getBillId()
                        , callerId
                );
                chargeSystemEventBus.post(billPaidEvent);
            } else {
                throw new PayAlreadyException();
            }
        }
    }

    @Override
    public void transactionInBill(Long billId, CallerInfo callerInfo) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isPresent()) {
            //寫入繳費記錄
            billOptional.get().setBillStatus(BillStatus.P);
            billOptional.get().setPaidDate(LocalDateTime.now());
            billRepository.save(billOptional.get());
            ChargeSystemEvent billPaidEvent = new ChargeSystemEvent(
                    EventSource.BILL
                    , EventAction.PAID
                    , billId
                    , callerInfo.getUserEntity().getUserId().longValue()
            );
            chargeSystemEventBus.post(billPaidEvent);
        }
    }

    @Transactional
    public void transactionCancelBillByBillId(Long billId, CallerInfo callerInfo) throws CancelAlreadyPaidBillException {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isPresent()) {
            transactionCancelBill(billOptional.get(), callerInfo);
        }
    }

    public void transactionCancelBill(Bill bill, CallerInfo callerInfo) throws CancelAlreadyPaidBillException {
        //已入帳的無法刪除
        if (bill.getBillStatus().equals(BillStatus.C)) {
            Set<BillingItem> billingItemSet = new HashSet<>(billingItemRepository.findByBillId(bill.getBillId()));
            //清空資料
            billingItemSet.stream().forEach(billingItem -> {
                billingItem.setBillId(null);
                billingItem.setTaxAmount(null);
                billingItem.setTaxIncludedAmount(null);
                billingItemRepository.save(billingItem);
            });
            billRepository.delete(bill);
            ChargeSystemEvent billPaidEvent = new ChargeSystemEvent(
                    EventSource.BILL
                    , EventAction.DELETE
                    , bill.getBillId()
                    , callerInfo.getUserEntity().getUserId().longValue()
            );
            chargeSystemEventBus.post(billPaidEvent);
        } else {
            throw new CancelAlreadyPaidBillException();
        }
    }

    @Override
    public void transactionCancelPayment(Long billId, CallerInfo callerInfo) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isPresent()) {
            transactionCancelPayment(billOptional.get(), callerInfo);
        }
    }

    @Override
    public void transactionCancelPayment(Bill bill, CallerInfo callerInfo) {
        Collection<BillingItem> billingItemSet = billingItemRepository.findByBillId(bill.getBillId());
        //扣抵類型的無法取消入帳
        if (bill.getBillStatus().equals(BillStatus.P) && !billingItemSet.isEmpty()) {
            Optional<BillingItem> deductBillingItemOpt = billingItemSet.stream().filter(billingItem -> {
                return billingItem.getDeductId() != null;
            }).findAny();
            if (!deductBillingItemOpt.isPresent()) {
                bill.setBillStatus(BillStatus.C);
                bill.setPaidDate(null);
                bill.setPaymentMethod(null);
                billRepository.save(bill);
            }
        }
    }

    @Override
    public boolean sendPaymentRequestMailByCompanyAndAlertMessage(Long billId, String alertMessage, CallerInfo callerInfo) {
        boolean result = false;
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isPresent()) {
            Optional<Company> companyOptional = companyRepository.findByCompanyId(billOptional.get().getCompanyId().intValue());
            if (companyOptional.isPresent()) {
                noticeRequestGenerator.sendPaymentRequestNoticeWithCustomAlertMessage(
                        billOptional.get(),
                        companyOptional.get(),
                        alertMessage,
                        callerInfo
                );
                result = true;
            }
        }
        return result;
    }

    @Override
    public void sendPaymentRequestMailByRecipient(Long billId, String recipient, Boolean isCorrection, CallerInfo callerInfo) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isPresent()) {
            if (isCorrection) {
                noticeRequestGenerator.sendPaymentRequestCorrectionNoticeWithoutCc(billOptional.get(), recipient, callerInfo);
            } else {
                noticeRequestGenerator.sendPaymentRequestNoticeWithoutCc(billOptional.get(), recipient, callerInfo);
            }
        }
    }

    @Override
    public List<Bill> serverSideProcessingSearchByPageInfo(PageInfo pageInfo) {
        //依照客製的搜尋條件進行過濾
        List<Bill> resultSet = billDataGatewayImpl.searchListByPageInfo(pageInfo);
        return resultSet;
    }

    /**
     * 不能在這裡直接進行出帳，因為他有可能先進行結清，再開立新的合約
     * 這樣想要新的合約費用與這些結清一起出帳就不行了
     *
     * @param template
     * @param billingItemCollection
     * @param callerId
     * @return
     */
    @Override
    public List<Bill> outBill(
            Bill template
            , Collection<BillingItem> billingItemCollection
            , boolean minimumFeeCheck
            , Long callerId) {
        List<Bill> resultList = new ArrayList<>();
        try {
            Bill bill = new Bill();
            BeanUtils.copyProperties(template, bill);
            bill.setTaxRate(BigDecimal.valueOf(0.05).setScale(4, RoundingMode.HALF_UP));
            bill.setCreateDate(LocalDateTime.now());
            bill.setCreatorId(callerId);
            bill.setBillStatus(BillStatus.C);
            bill.setCreateDate(LocalDateTime.now());

            billingItemFeeCalculator.calculateBillingItemCollection(bill, billingItemCollection);

            if (!minimumFeeCheck || bill.getTaxExcludedAmount().compareTo(new BigDecimal(500)) >= 0) {
                billRepository.save(bill);
                resultList.add(bill);
                billingItemCollection.stream().forEach(billingItem -> {
                    billingItem.setBillId(bill.getBillId());
                    billingItemRepository.save(billingItem);
                });
                ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                        EventSource.BILL
                        , EventAction.CREATE
                        , bill.getBillId()
                        , callerId
                );
                chargeSystemEventBus.post(chargeSystemEvent);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return resultList;
    }

    @Override
    @Transactional
    public void transactionOutToBillByAjaxRequest(OutToBillRequest outToBillRequest, Long callerId) {
        Set<BillingItem> conditionResultSet =
                billingItemDataGateway.searchListByConditionObject(outToBillRequest.getCondition()).stream().filter(billingItem -> {
                    if (billingItem.getIsMemo() || billingItem.getBillId() != null) {
                        return false;
                    } else {
                        return true;
                    }
                }).collect(Collectors.toSet());

        Bill billTemplate = createBillTemplateByRequest(outToBillRequest);

        //依照資料來源指定minimumFeeCheck
        //因為可能包含多個公司，需要查詢處理
        //依公司分類
        MultiValueMap multiValueCompanyMap = new MultiValueMap();
        conditionResultSet.stream().forEach(billingItem -> {
            multiValueCompanyMap.put(billingItem.getCompanyId(), billingItem);
        });

        multiValueCompanyMap.keySet().stream().forEach(key -> {
            Long companyId = Long.valueOf(String.valueOf(key));
            Optional<Company> companyOptional = companyRepository.findByCompanyId(companyId.intValue());
            if (companyOptional.isPresent()) {
                billTemplate.setCompanyId(companyOptional.get().getCompanyId().longValue());
                List<BillingItem> billingItemSet = (List<BillingItem>) multiValueCompanyMap.get(key);
                if (!billingItemSet.isEmpty()) {
                    outBill(
                            billTemplate
                            , billingItemSet
                            , outToBillRequest.isMinimumFeeCheck()
                            , callerId
                    );
                }
            }
        });

    }

    /**
     * 自動填寫內容邏輯
     *
     * @param outToBillRequest
     * @return
     */
    public Bill createBillTemplateByRequest(OutToBillRequest outToBillRequest) {
        Bill template = new Bill();
        if (outToBillRequest.getBillRemark() != null && !StringUtils.isEmpty(outToBillRequest.getBillRemark())) {
            template.setBillRemark(outToBillRequest.getBillRemark());
        } else {
            template.setBillRemark(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        }
        //依照calYearMonth來產生billYm
        if (outToBillRequest.getBillYm() != null) {
            template.setBillYm(outToBillRequest.getBillYm());
        } else {
            template.setBillYm(outToBillRequest.getCondition().getCalFeeYearMonth());
        }
        return template;
    }

}
