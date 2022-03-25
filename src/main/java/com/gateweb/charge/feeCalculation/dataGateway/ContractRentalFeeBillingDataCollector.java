package com.gateweb.charge.feeCalculation.dataGateway;

import com.gateweb.charge.chargePolicy.ChargePolicyProvider;
import com.gateweb.charge.chargePolicy.bean.ChargePolicy;
import com.gateweb.charge.enumeration.ChargePolicyType;
import com.gateweb.charge.feeCalculation.bean.ContractRentalFeeBillingData;
import com.gateweb.charge.feeCalculation.dataCounter.DataCounterGateway;
import com.gateweb.charge.service.BillingService;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.ChargeRuleRepository;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.gateweb.orm.charge.repository.PackageRefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ContractRentalFeeBillingDataCollector {
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    ChargeRuleRepository chargeRuleRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    BillingService billingService;
    @Autowired
    DataCounterGateway dataCounterGateway;
    @Autowired
    ChargePolicyProvider chargePolicyProvider;
    @Autowired
    CommonRentalFeeBillingDataGenerator commonRentalFeeBillingDataGenerator;
    @Autowired
    RemainingCountRentalFeeBillingDataGenerator remainingCountRentalFeeBillingDataGenerator;

    //todo: refactor
    public Collection<ContractRentalFeeBillingData> collect(Long contractId) {
        Set<ContractRentalFeeBillingData> billingDataSet = new HashSet<>();
        Optional<Contract> contractOptional = contractRepository.findById(contractId);
        if (contractOptional.isPresent()) {
            //找到Prepaid的資料
            Optional<ChargePolicy> chargePolicyOptional = chargePolicyProvider.genChargePolicy(
                    ChargePolicyType.RENTAL
                    , contractOptional.get()
            );
            if (chargePolicyOptional.isPresent()) {
                if (chargePolicyOptional.get().isChargeByRemainingCount()) {
                    billingDataSet.addAll(
                            remainingCountRentalFeeBillingDataGenerator.gen(
                                    contractOptional.get(),
                                    chargePolicyOptional.get()
                            )
                    );
                } else {
                    billingDataSet.addAll(
                            commonRentalFeeBillingDataGenerator.gen(
                                    contractOptional.get(),
                                    chargePolicyOptional.get()
                            )
                    );
                }
            }

        }
        return billingDataSet;
    }

}
