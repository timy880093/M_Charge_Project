package com.gateweb.charge.feeCalculation.dataGateway;

import com.gateweb.charge.chargePolicy.bean.ChargePolicy;
import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.feeCalculation.bean.ContractRentalFeeBillingData;
import com.gateweb.charge.feeCalculation.component.ExpectedOutDateComponent;
import com.gateweb.charge.feeCalculation.dataCounter.DataCounter;
import com.gateweb.charge.feeCalculation.dataCounter.DataCounterGateway;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 這個是一般年繳類型產生預繳費用的步驟
 *
 * @return
 */
@Component
public class CommonRentalFeeBillingDataGenerator implements ContractRentalFeeBillingDataGenerator {
    @Autowired
    DataCounterGateway dataCounterGateway;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    ExpectedOutDateComponent expectedOutDateComponent;

    @Override
    public Collection<ContractRentalFeeBillingData> gen(Contract contract, ChargePolicy chargePolicy) {
        Set<ContractRentalFeeBillingData> billingDataSet = new HashSet<>();
        //取得資料計數器
        Optional<DataCounter> dataCounterOptional = dataCounterGateway.getDataCounter(chargePolicy.getChargeBaseType());
        if (dataCounterOptional.isPresent()) {
            CustomInterval contractInterval = new CustomInterval(contract.getEffectiveDate(), contract.getExpirationDate());
            // 取得帳期區間切片表
            List<CustomInterval> chargeIntervalList = chargePolicy.getChargeCycle().doPartitioning(contractInterval);
            List<CustomInterval> calculateIntervalList = chargePolicy.getCalculateCycle().doPartitioning(contractInterval);
            chargeIntervalList.stream().forEach(chargeInterval -> {
                //三個月前出帳
                LocalDateTime expectedOutDate = expectedOutDateComponent.getExpectedOutDate(chargeInterval.getStartLocalDateTime());
                //限制區間為區間內
                List<CustomInterval> sliceOfCalculateIntervalList = calculateIntervalList.stream()
                        .filter(sliceOfCalculateInterval -> {
                            if (sliceOfCalculateInterval.overlaps(chargeInterval)
                                    && (sliceOfCalculateInterval.getEndLocalDateTime().isBefore(chargeInterval.getEndLocalDateTime())
                                    || sliceOfCalculateInterval.getEndLocalDateTime().isEqual(chargeInterval.getEndLocalDateTime()))) {
                                return true;
                            } else {
                                return false;
                            }
                        }).collect(Collectors.toList());
                //查詢已經存在的時間
                List<BillingItem> existsBillingItemList = billingItemRepository
                        .findByCompanyIdAndPackageRefIdAndDeductIdIsNull(
                                contract.getCompanyId()
                                , chargePolicy.getPackageRef().getPackageRefId()
                        );
                //加入migration過來的項目
//                existsBillingItemList.addAll(findExistsMigrationBillingItemList(contract.getCompanyId()));
                //過濾已經存在的時間
                sliceOfCalculateIntervalList = filterExistsInterval(existsBillingItemList, sliceOfCalculateIntervalList);
                if (!sliceOfCalculateIntervalList.isEmpty()) {
                    ContractRentalFeeBillingData billingData = new ContractRentalFeeBillingData();
                    billingData.setCalculateIntervalList(sliceOfCalculateIntervalList);
                    billingData.setChargePolicy(chargePolicy);
                    billingData.setCompanyId(contract.getCompanyId());
                    billingData.setPackageRefId(chargePolicy.getPackageRef().getPackageRefId());
                    billingData.setExpectedOutDate(expectedOutDate);
                    billingData.setContractId(contract.getContractId());
                    billingDataSet.add(billingData);
                }
            });
        }
        return billingDataSet;
    }

    public List<CustomInterval> filterExistsInterval(List<BillingItem> billingItemList, List<CustomInterval> customIntervalList) {
        List<CustomInterval> overlapList = new ArrayList<>();
        customIntervalList.stream().forEach(customInterval -> {
            billingItemList.stream().forEach(billingItem -> {
                Interval existsInterval = new Interval(
                        Timestamp.valueOf(billingItem.getCalculateFromDate()).getTime()
                        , Timestamp.valueOf(billingItem.getCalculateToDate()).getTime()
                );
                Interval calculateInterval = new Interval(customInterval.getStartDateTime(), customInterval.getEndDateTime());
                if (existsInterval.overlaps(calculateInterval)) {
                    overlapList.add(customInterval);
                }
            });
        });
        try {
            customIntervalList.removeAll(overlapList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customIntervalList;
    }

    /**
     * TODO:為了migration的資料正確而做的方法
     * 舊計費因為可以跨合約進行計算，也就是說就算不是這個合約產生的帳單，他也可以透過其它合約計算寫入費用
     * 在未來完全汰換舊系統時就可以把這個拿掉
     *
     * @param companyId
     * @return
     */
//    @Deprecated
//    private List<BillingItem> findExistsMigrationBillingItemList(Long companyId) {
//        List<BillingItem> existsMigrationBillingItemList = new ArrayList<>(
//                billingItemRepository.findByCompanyIdAndBillingItemTypeAndProductCategoryId(
//                        companyId, BillingItemType.MIGRATION, 2L)
//        );
//        return existsMigrationBillingItemList;
//    }
}
