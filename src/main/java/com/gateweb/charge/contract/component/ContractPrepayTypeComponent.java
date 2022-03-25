package com.gateweb.charge.contract.component;

import com.gateweb.charge.enumeration.ContractPrepayType;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.repository.ChargePackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContractPrepayTypeComponent {
    @Autowired
    ChargePackageRepository chargePackageRepository;

    public Boolean isPrepayByRemainingCount(Contract contract) {
        ContractPrepayType contractPrepayType = getContractPrepayType(contract);
        if (contractPrepayType.equals(ContractPrepayType.PREPAY_BY_REMAINING_COUNT)) {
            return true;
        } else {
            return false;
        }
    }

    public ContractPrepayType getContractPrepayType(Contract contract) {
        boolean isRemainingCountType = chargePackageRepository.findChargeByRemainingCountFlagByPackageId(
                contract.getPackageId()
        ).isPresent();
        if (isRemainingCountType) {
            return ContractPrepayType.PREPAY_BY_REMAINING_COUNT;
        } else {
            return ContractPrepayType.PREPAY_BY_MONTH;
        }
    }

}
