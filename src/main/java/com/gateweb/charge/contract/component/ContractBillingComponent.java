package com.gateweb.charge.contract.component;

import com.gateweb.charge.chargePolicy.ChargePolicyProvider;
import com.gateweb.charge.chargePolicy.bean.ChargePolicy;
import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.enumeration.ChargePlan;
import com.gateweb.charge.enumeration.ChargePolicyType;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.enumeration.PaidPlan;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.charge.service.BillService;
import com.gateweb.orm.charge.entity.*;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.PackageRefRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class ContractBillingComponent {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    BillService billService;
    @Autowired
    ChargePolicyProvider chargePolicyProvider;

    EventBus chargeSystemEventBus;

    @Autowired
    @Qualifier("chargeSystemEventBus")
    public void setChargeSystemEventBus(EventBus chargeSystemEventBus) {
        this.chargeSystemEventBus = chargeSystemEventBus;
    }

    public void billingContract(
            Contract contract
            , String previousEventId
            , Long callerId) {
        if (contract.getStatus().equals(ContractStatus.B) || contract.getStatus().equals(ContractStatus.E)) {
            Optional<Company> companyOptional = companyRepository.findByCompanyId(contract.getCompanyId().intValue());
            //取得可出帳預繳項目
            Set<BillingItem> billingItemSet = getBillablePrepaidBillingItem(contract);
            //取得可出帳帳期
            Optional<CustomInterval> targetChargeIntervalOpt = getBillableChargeInterval(contract, billingItemSet);
            if (targetChargeIntervalOpt.isPresent()) {
                //依據可出帳帳期過濾預繳項目
                billingItemSet = billingItemOverlapWithChargeIntervalFilter(billingItemSet, targetChargeIntervalOpt.get());
            }

            if (!billingItemSet.isEmpty() && companyOptional.isPresent()) {
                String resultRemark = String.valueOf(new Date().getTime());
                Optional<String> remarkStrOpt = LocalDateTimeUtils.parseLocalDateTimeToString(LocalDateTime.now(), "yyyy/MM/dd HH:mm:ss");
                if (remarkStrOpt.isPresent()) {
                    resultRemark = remarkStrOpt.get();
                }
                Bill billTemplate = new Bill();
                billTemplate.setBillRemark(resultRemark);
                billTemplate.setCompanyId(companyOptional.get().getCompanyId().longValue());
                //出帳帳單
                billService.outBill(
                        billTemplate
                        , billingItemSet
                        , false
                        , callerId
                );
                ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                        EventSource.CONTRACT
                        , EventAction.BILLING
                        , contract.getContractId()
                        , previousEventId
                        , callerId
                );
                chargeSystemEventBus.post(chargeSystemEvent);
            }
        }
    }

    public Set<BillingItem> getBillablePrepaidBillingItem(Contract contract) {
        Set<BillingItem> billingItemList = new HashSet<>();
        Set<PackageRef> packageRefSet = new HashSet<>(packageRefRepository.findByFromPackageId(contract.getPackageId()));
        //找到這個合約產生的預繳費用
        packageRefSet.stream().forEach(packageRef -> {
            Set<BillingItem> billingItemResultSet = new HashSet<>(
                    billingItemRepository.findByCompanyIdAndPackageRefIdAndPaidPlanAndChargePlanAndIsMemoIsFalseAndBillIdIsNull(
                            contract.getCompanyId()
                            , packageRef.getPackageRefId()
                            , PaidPlan.PRE_PAID
                            , ChargePlan.INITIATION
                    ));
            billingItemList.addAll(billingItemResultSet);
        });
        return billingItemList;
    }

    public Optional<CustomInterval> getBillableChargeInterval(final Contract contract, final Set<BillingItem> billingItemList) {
        Optional<CustomInterval> targetInterval = Optional.empty();
        //合約區間
        CustomInterval contractInterval = new CustomInterval(
                contract.getEffectiveDate(),
                contract.getExpirationDate()
        );
        Optional<ChargePolicy> chargePolicyOptional = chargePolicyProvider.genChargePolicy(
                ChargePolicyType.RENTAL
                , contract
        );
        if (chargePolicyOptional.isPresent()) {
            // 取得帳期區間切片表
            List<CustomInterval> chargeIntervalList
                    = chargePolicyOptional.get().getChargeCycle().doPartitioning(contractInterval);
            //過濾不需要考慮的帳期
            chargeIntervalList = chargeIntervalOverlapWithBillingItemFilter(chargeIntervalList, billingItemList);
            //選擇目標帳期
            targetInterval = chargeIntervalDistinctFilter(chargeIntervalList);
        }
        return targetInterval;
    }

    public Set<BillingItem> billingItemOverlapWithChargeIntervalFilter(Set<BillingItem> billingItemList, CustomInterval chargeInterval) {
        Set<BillingItem> targetBillingItemList = new HashSet<>();
        billingItemList.stream().forEach(billingItem -> {
            if (chargeIntervalTimeOverlapWithBillingItem(chargeInterval, billingItem)) {
                targetBillingItemList.add(billingItem);
            }
        });
        return targetBillingItemList;
    }

    public List<CustomInterval> chargeIntervalOverlapWithBillingItemFilter(
            List<CustomInterval> chargeIntervalList, Collection<BillingItem> billingItemList) {
        List<CustomInterval> targetChargeInterval = new ArrayList<>();
        chargeIntervalList.stream().forEach(chargeInterval -> {
            billingItemList.stream().forEach(billingItem -> {
                if (chargeIntervalTimeOverlapWithBillingItem(chargeInterval, billingItem)) {
                    targetChargeInterval.add(chargeInterval);
                }
            });
        });
        return targetChargeInterval;
    }

    public boolean chargeIntervalTimeOverlapWithBillingItem(CustomInterval chargeInterval, BillingItem billingItem) {
        CustomInterval calculateInterval = new CustomInterval(
                billingItem.getCalculateFromDate(), billingItem.getCalculateToDate()
        );
        return chargeInterval.overlaps(calculateInterval);
    }

    /**
     * 季繳邏輯
     *
     * @param chargeIntervalList
     * @return
     */
    public Optional<CustomInterval> chargeIntervalDistinctFilter(final List<CustomInterval> chargeIntervalList) {
        //大於1就進行篩選
        if (chargeIntervalList.size() > 1 || chargeIntervalList.size() == 1) {
            return Optional.of(chargeIntervalList.get(0));
        } else {
            return Optional.empty();
        }
    }
}
