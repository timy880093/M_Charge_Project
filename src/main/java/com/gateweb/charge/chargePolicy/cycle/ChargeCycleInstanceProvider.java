package com.gateweb.charge.chargePolicy.cycle;

import com.gateweb.charge.chargePolicy.cycle.builder.*;
import com.gateweb.charge.enumeration.CycleType;

import java.util.Optional;

public class ChargeCycleInstanceProvider {
    public Optional<ChargeCycle> genGeneralChargeCycleInstance(CycleType cycleType) {
        Optional<ChargeCycle> result = Optional.empty();
        if (cycleType.equals(CycleType.YEAR)) {
            result = Optional.of(new YearCycle(1));
        }
        if (cycleType.equals(CycleType.MONTH)) {
            result = Optional.of(new MonthCycle(1));
        }
        if (cycleType.equals(CycleType.SEASON)) {
            result = Optional.of(new SeasonCycle(1));
        }
        if (cycleType.equals(CycleType.ANY)) {
            result = Optional.of(new AnyCycle());
        }
        if (cycleType.equals(CycleType.GW_OVERAGE_BIL)) {
            result = Optional.of(new GwOverageBillingCycle());
        }
        if (cycleType.equals(CycleType.GW_OVERAGE_BIL_CAL)) {
            result = Optional.of(new GwOverageBillingCalCycle());
        }
        return result;
    }

    public Optional<ChargeCycle> genRentalChargeCycleInstance(CycleType cycleType, Integer limitPartition) {
        Optional<ChargeCycle> result = Optional.empty();
        if (cycleType.equals(CycleType.GW_RENTAL_CAL)) {
            result = Optional.of(new GwRentalCalculateCycle(1, limitPartition));
        }
        if (cycleType.equals(CycleType.ANY)) {
            result = Optional.of(new AnyCycle());
        }
        return result;
    }

    public Optional<ChargeCycle> genGeneralChargeCycleInstance(String cycleTypeName) {
        return genGeneralChargeCycleInstance(CycleType.valueOf(cycleTypeName));
    }

    public Optional<ChargeCycle> genRentalChargeCycleInstance(String cycleTypeName, Integer limitPartition) {
        return genRentalChargeCycleInstance(CycleType.valueOf(cycleTypeName), limitPartition);
    }
}
