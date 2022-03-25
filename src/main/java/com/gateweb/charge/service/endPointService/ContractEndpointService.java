package com.gateweb.charge.service.endPointService;

import com.gateweb.charge.contract.bean.request.*;
import com.gateweb.charge.contract.utils.ContractRenewIntervalGenerator;
import com.gateweb.charge.contract.component.ContractSaveComponent;
import com.gateweb.charge.contract.component.ContractTerminateComponent;
import com.gateweb.charge.frontEndIntegration.enumeration.SweetAlertStatus;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.charge.exception.ContractIntervalOverlapException;
import com.gateweb.charge.exception.ContractTypeAmbiguousException;
import com.gateweb.orm.charge.entity.Contract;
import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class ContractEndpointService {
    protected final Logger logger = LogManager.getLogger(getClass());

    ContractSaveComponent contractSaveComponent;
    ContractTerminateComponent contractTerminateComponent;
    EventBus chargeSystemEventBus;

    public ContractEndpointService(ContractSaveComponent contractSaveComponent, ContractTerminateComponent contractTerminateComponent, EventBus chargeSystemEventBus) {
        this.contractSaveComponent = contractSaveComponent;
        this.contractTerminateComponent = contractTerminateComponent;
        this.chargeSystemEventBus = chargeSystemEventBus;
    }

    public ExpirationDateFulfillRes contractExpirationDateFulfillment(ExpirationDateFulfillReq expirationDateFulfillReq) {
        ExpirationDateFulfillRes expirationDateFulfillRes = new ExpirationDateFulfillRes();
        if (expirationDateFulfillReq.getEffectiveDate() != null
                && expirationDateFulfillReq.getPeriodMonth() != null
                && expirationDateFulfillReq.getPeriodMonth() instanceof Integer) {
            Optional<LocalDateTime> expirationDateTimeOptional = ContractRenewIntervalGenerator.getContractExpirationDate(
                    expirationDateFulfillReq.getEffectiveDate()
                    , expirationDateFulfillReq.getPeriodMonth()
            );
            if (expirationDateTimeOptional.isPresent()) {
                expirationDateFulfillRes = new ExpirationDateFulfillRes(
                        expirationDateTimeOptional.get().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                );
            }
        }
        return expirationDateFulfillRes;
    }

    public ContractSaveRes contractSaveOrUpdate(ContractSaveReq contractSaveReq, Long callerId) {
        ContractSaveRes contractSaveRes = new ContractSaveRes();
        try {
            if (contractSaveReq.getContractId() == null) {
                Contract contract = contractSaveComponent.executeCreate(contractSaveReq);
                ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                        EventSource.CONTRACT,
                        EventAction.CREATE,
                        contract.getContractId(),
                        callerId
                );
                chargeSystemEventBus.post(chargeSystemEvent);
            } else {
                contractSaveComponent.executeUpdate(contractSaveReq);
            }
            contractSaveRes.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
            contractSaveRes.setMessage("修改成功");
        } catch (ContractIntervalOverlapException e) {
            contractSaveRes.setSweetAlertStatus(SweetAlertStatus.ERROR);
            contractSaveRes.setMessage("合約區間重疊");
        } catch (ContractTypeAmbiguousException e) {
            contractSaveRes.setSweetAlertStatus(SweetAlertStatus.ERROR);
            contractSaveRes.setMessage("模糊不清的合約類型(請正確填寫合約起迄日及兩種自動產生合約起始日的欄位)");
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            contractSaveRes.setSweetAlertStatus(SweetAlertStatus.ERROR);
            contractSaveRes.setMessage("未預期的錯誤");
        }
        return contractSaveRes;
    }

    public ContractDisableRes disableContract(Contract contract, Long callerId) {
        ContractDisableRes contractDisableRes = new ContractDisableRes();
        try {
            contractTerminateComponent.disableContract(contract);
            ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                    EventSource.CONTRACT,
                    EventAction.TERMINATE,
                    contract.getContractId(),
                    callerId
            );
            chargeSystemEventBus.post(chargeSystemEvent);
            contractDisableRes.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
            contractDisableRes.setMessage("終止成功");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            contractDisableRes.setSweetAlertStatus(SweetAlertStatus.ERROR);
            contractDisableRes.setMessage("終止失敗");
        }
        return contractDisableRes;
    }

}
