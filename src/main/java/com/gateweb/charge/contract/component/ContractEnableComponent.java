package com.gateweb.charge.contract.component;

import com.gateweb.charge.bill.component.PaidStatusComponent;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ContractEnableComponent {
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    PaidStatusComponent paidStatusComponent;

    EventBus chargeSystemEventBus;

    @Autowired
    @Qualifier("chargeSystemEventBus")
    public void setChargeSystemEventBus(EventBus chargeSystemEventBus) {
        this.chargeSystemEventBus = chargeSystemEventBus;
    }

    public void enableContract(Contract contract, Long callerId) {
        contract.setStatus(ContractStatus.E);
        contractRepository.save(contract);
        //有成功啟用
        ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                EventSource.CONTRACT
                , EventAction.ENABLE
                , contract.getContractId()
                , callerId
        );
        chargeSystemEventBus.post(chargeSystemEvent);
    }

}
