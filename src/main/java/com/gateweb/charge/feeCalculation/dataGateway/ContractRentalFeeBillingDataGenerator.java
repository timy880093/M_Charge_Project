package com.gateweb.charge.feeCalculation.dataGateway;

import com.gateweb.charge.chargePolicy.bean.ChargePolicy;
import com.gateweb.charge.feeCalculation.bean.ContractRentalFeeBillingData;
import com.gateweb.orm.charge.entity.Contract;

import java.util.Collection;

public interface ContractRentalFeeBillingDataGenerator {
    Collection<ContractRentalFeeBillingData> gen(Contract contract, ChargePolicy chargePolicy);
}
