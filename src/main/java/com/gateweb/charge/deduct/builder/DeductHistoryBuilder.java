package com.gateweb.charge.deduct.builder;

import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Deduct;
import com.gateweb.orm.charge.entity.DeductHistory;
import com.gateweb.charge.component.propertyProvider.ContextComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Scope(value = "prototype")
public class DeductHistoryBuilder extends ContextComponent {
    DeductHistory deductHistory = new DeductHistory();

    public DeductHistoryBuilder getBuilder() {
        return super.getComponent();
    }

    public DeductHistoryBuilder withAmount(BigDecimal amount) {
        deductHistory.setAmount(amount);
        return this;
    }

    public DeductHistoryBuilder withDefaultTimestamp() {
        deductHistory.setCreateDate(LocalDateTime.now());
        return this;
    }

    public DeductHistoryBuilder withDeduct(Deduct deduct) {
        deductHistory.setDeductId(deduct.getDeductId());
        return this;
    }

    public DeductHistoryBuilder withDeductBillingItem(BillingItem billingItem) {
        deductHistory.setDeductBillingItemId(billingItem.getBillingItemId());
        return this;
    }

    public DeductHistory build() {
        return deductHistory;
    }
}
