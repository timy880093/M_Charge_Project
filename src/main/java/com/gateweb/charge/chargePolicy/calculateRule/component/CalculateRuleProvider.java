package com.gateweb.charge.chargePolicy.calculateRule.component;

import com.gateweb.charge.chargePolicy.calculateRule.type.ACMixTypeCalculateRule;
import com.gateweb.charge.chargePolicy.calculateRule.type.ATypeCalculateRule;
import com.gateweb.charge.chargePolicy.calculateRule.type.CalculateRule;
import com.gateweb.charge.chargePolicy.calculateRule.type.GeneralCalculateRule;
import com.gateweb.orm.charge.entity.ChargeRule;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CalculateRuleProvider {
    public Optional<CalculateRule> getCalculateRule(ChargeRule chargeRule) {
        Optional<CalculateRule> calculateRuleOptional;
        if (chargeRule.getCirculation() && chargeRule.getAccumulation()) {
            calculateRuleOptional = Optional.of(new ACMixTypeCalculateRule());
        } else if (chargeRule.getAccumulation()) {
            calculateRuleOptional = Optional.of(new ATypeCalculateRule());
        } else {
            calculateRuleOptional = Optional.of(new GeneralCalculateRule());
        }
        return calculateRuleOptional;
    }
}
