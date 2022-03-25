package com.gateweb.charge.feeCalculation.dataCounter;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;

import java.util.Optional;

public interface DataCounter {
    Optional<Integer> count(String businessNo, CustomInterval customInterval);
}
