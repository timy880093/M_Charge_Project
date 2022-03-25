package com.gateweb.charge.scheduleJob.quartzJobs;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.utils.ContractRenewIntervalGenerator;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.charge.scheduleJob.component.FindFirstInvoiceDateComponent;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@DisallowConcurrentExecution
public class ContractAutomationJob implements Job {
    final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    ContractRepository contractRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    FindFirstInvoiceDateComponent findFirstInvoiceDateComponent;

    EventBus chargeSystemEventBus;

    @Autowired
    @Qualifier("chargeSystemEventBus")
    public void setChargeSystemEventBus(EventBus chargeSystemEventBus) {
        this.chargeSystemEventBus = chargeSystemEventBus;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logger.info("contractAutoFulfillmentByInvoiceDate Start");
        contractAutoFulfillmentByInvoiceDate(348L);
        logger.info("contractAutoFulfillmentByInvoiceDate End");
        logger.info("contractAutoFulfillmentByInstallationDate Start");
        contractAutoFulfillmentByInstallationDate(348L);
        logger.info("contractAutoFulfillmentByInstallationDate End");
    }

    /**
     * 這裡不馬上初始化的原因是因為還有其它指標影響是否進行初始化
     * 初始化與否是由自動初始化並出帳這個屬性進行判斷的，無法合在一起
     */
    public void contractAutoFulfillmentByInvoiceDate(Long callerId) {
        Set<Contract> contractSet = new HashSet<>(contractRepository.findByStatusAndFirstInvoiceDateAsEffectiveDateIsTrue(ContractStatus.C));
        contractSet.stream().forEach(contract -> {
            try {
                Optional<CustomInterval> availableContractIntervalOpt = getAvailableContractInterval(contract);
                if (availableContractIntervalOpt.isPresent()) {
                    //使用該日期填入contract
                    contract.setEffectiveDate(availableContractIntervalOpt.get().getStartLocalDateTime());
                    contract.setExpirationDate(availableContractIntervalOpt.get().getEndLocalDateTime());
                    contractRepository.save(contract);
                    ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                            EventSource.CONTRACT,
                            EventAction.FULFILL_EFFECTIVE_DATE,
                            contract.getContractId(),
                            callerId
                    );
                    chargeSystemEventBus.post(chargeSystemEvent);
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        });
    }

    private Optional<CustomInterval> getAvailableContractInterval(Contract contract) {
        Optional<Company> companyOptional = companyRepository.findByCompanyId(contract.getCompanyId().intValue());
        return companyOptional.flatMap(company -> {
            return findFirstInvoiceDateComponent.findFirstInvoiceDateFromCompanyCreateDate(
                    companyOptional.get().getBusinessNo()
                    , companyOptional.get().getCreateDate().toLocalDateTime()
            );
        }).flatMap(invoiceDate -> {
            return LocalDateTimeUtils.parseLocalDateTimeFromString(
                    invoiceDate, "yyyyMMdd HH:mm:ss"
            );
        }).flatMap(effectiveDate -> {
            Optional<LocalDateTime> expirationDateOpt = ContractRenewIntervalGenerator.getContractExpirationDate(
                    effectiveDate
                    , contract.getPeriodMonth()
            );
            if (expirationDateOpt.isPresent()) {
                CustomInterval contractInterval = new CustomInterval();
                contractInterval.setStartLocalDateTime(effectiveDate);
                contractInterval.setEndLocalDateTime(expirationDateOpt.get());
                return Optional.of(contractInterval);
            } else {
                return Optional.empty();
            }
        });
    }

    /**
     * 判斷是否達到自動初始化門檻
     *
     * @param callerId
     * @return
     */
    public void contractAutoFulfillmentByInstallationDate(Long callerId) {
        Set<Contract> contractSet = new HashSet<>(contractRepository.findByStatusAndInstallationDateIsNotNull(ContractStatus.C));
        contractSet.stream().forEach(contract -> {
            //裝機日當天也有算，所以裝機日後的30天其實是+30-1為+29
            LocalDateTime thresholdDate = contract.getInstallationDate().plusDays(29);
            if (contract.getStatus().equals(ContractStatus.C)
                    && contract.getPeriodMonth() != null
                    && LocalDateTime.now().isAfter(thresholdDate)) {
                contract.setEffectiveDate(thresholdDate);
                //填寫起始日與終止日
                Optional<LocalDateTime> expirationDateTimeOptional = ContractRenewIntervalGenerator.getContractExpirationDate(
                        contract.getEffectiveDate()
                        , contract.getPeriodMonth()
                );
                if (expirationDateTimeOptional.isPresent()) {
                    contract.setExpirationDate(expirationDateTimeOptional.get());
                    contractRepository.save(contract);
                    ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                            EventSource.CONTRACT,
                            EventAction.FULFILL_EFFECTIVE_DATE,
                            contract.getContractId(),
                            callerId
                    );
                    chargeSystemEventBus.post(chargeSystemEvent);
                }
            }
        });
    }

}
