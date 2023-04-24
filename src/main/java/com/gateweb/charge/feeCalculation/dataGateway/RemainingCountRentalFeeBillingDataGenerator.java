package com.gateweb.charge.feeCalculation.dataGateway;

import com.gateweb.charge.chargePolicy.bean.ChargePolicy;
import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.feeCalculation.bean.ContractRentalFeeBillingData;
import com.gateweb.orm.charge.entity.Contract;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 以張計費與PackageRef的數量無關
 */
@Component
public class RemainingCountRentalFeeBillingDataGenerator implements ContractRentalFeeBillingDataGenerator {

    public Collection<ContractRentalFeeBillingData> gen(Contract contract, ChargePolicy chargePolicy) {
        Set<ContractRentalFeeBillingData> billingDataSet = new HashSet<>();
        CustomInterval contractInterval = new CustomInterval(contract.getEffectiveDate(), contract.getExpirationDate());
        LocalDateTime expectedOutDate = contractInterval.getStartLocalDateTime().minusMonths(2).withHour(0).withMinute(0).withSecond(0);
        ContractRentalFeeBillingData billingData = new ContractRentalFeeBillingData();
        List<CustomInterval> intervalList = new ArrayList<>();
        intervalList.add(contractInterval);
        billingData.setCalculateIntervalList(intervalList);
        billingData.setChargePolicy(chargePolicy);
        billingData.setCompanyId(contract.getCompanyId());
        billingData.setPackageRefId(chargePolicy.getPackageRef().getPackageRefId());
        billingData.setExpectedOutDate(expectedOutDate);
        billingData.setContractId(contract.getContractId());
        billingDataSet.add(billingData);
        return billingDataSet;
    }
}
