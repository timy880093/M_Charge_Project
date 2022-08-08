package com.gateweb.charge.contract.component;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.enumeration.PaidPlan;
import com.gateweb.charge.feeCalculation.bean.ChargeByRemainingCountCalData;
import com.gateweb.charge.service.dataGateway.ContractDataGateway;
import com.gateweb.orm.charge.entity.*;
import com.gateweb.orm.charge.repository.ChargeRuleRepository;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.orm.charge.repository.NewGradeRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class RemainingContractComponent {
    final Logger logger = LogManager.getLogger(getClass());

    ChargeRuleRepository chargeRuleRepository;
    NewGradeRepository newGradeRepository;
    InvoiceRemainingRepository invoiceRemainingRepository;
    ContractPrepayTypeComponent contractPrepayTypeComponent;
    ContractDataGateway contractDataGateway;
    RemainingRecordModelComponent remainingRecordModelComponent;

    public RemainingContractComponent(ChargeRuleRepository chargeRuleRepository, NewGradeRepository newGradeRepository, InvoiceRemainingRepository invoiceRemainingRepository, ContractPrepayTypeComponent contractPrepayTypeComponent, ContractDataGateway contractDataGateway, RemainingRecordModelComponent remainingRecordModelComponent) {
        this.chargeRuleRepository = chargeRuleRepository;
        this.newGradeRepository = newGradeRepository;
        this.invoiceRemainingRepository = invoiceRemainingRepository;
        this.contractPrepayTypeComponent = contractPrepayTypeComponent;
        this.contractDataGateway = contractDataGateway;
        this.remainingRecordModelComponent = remainingRecordModelComponent;
    }

    public boolean isChargeByRemainingCount(Long packageId) {
        Optional<ChargeRule> chargeRuleOptional = chargeRuleRepository.findChargeRuleByPackageIdAndPaidPlan(
                packageId
                , PaidPlan.PRE_PAID.name()
        );
        if (chargeRuleOptional.isPresent()) {
            return chargeRuleOptional.get().getChargeByRemainingCount();
        } else {
            return false;
        }
    }

    public Set<ChargeByRemainingCountCalData> chargeByRemainingCountCalDataCollector(Collection<Company> companyCollection) {
        Set<ChargeByRemainingCountCalData> chargeByRemainingCountCalDataSet = new HashSet<>();
        companyCollection.stream().forEach(company -> {
            Optional<ChargeByRemainingCountCalData> chargeByRemainingCountCalDataOptional
                    = chargeByRemainingCountCalDataCollector(company);
            chargeByRemainingCountCalDataOptional.ifPresent(chargeByRemainingCountCalData -> {
                chargeByRemainingCountCalDataSet.add(chargeByRemainingCountCalData);
            });
        });
        return chargeByRemainingCountCalDataSet;
    }

    public Optional<ChargeByRemainingCountCalData> chargeByRemainingCountCalDataCollector(Company company) {
        Optional result = Optional.empty();
        try {
            Optional<InvoiceRemaining> invoiceRemainingOptional
                    = invoiceRemainingRepository.findTopInvoiceRemainingByCompanyIdOrderByInvoiceDateDesc(
                    new Long(company.getCompanyId())
            );
            if (invoiceRemainingOptional.isPresent()) {
                Optional<Contract> contractOptional = contractDataGateway.findByContractIdAndStatus(
                        invoiceRemainingOptional.get().getContractId()
                        , ContractStatus.E
                );
                if (contractOptional.isPresent()) {
                    LocalDateTime now = LocalDateTime.now();
                    ChargeByRemainingCountCalData chargeByRemainingCountCalData = new ChargeByRemainingCountCalData();
                    chargeByRemainingCountCalData.setCompany(company);
                    chargeByRemainingCountCalData.setContract(contractOptional.get());
                    chargeByRemainingCountCalData.setExecutionDateTime(now);
                    chargeByRemainingCountCalData.setPreviousInvoiceRemaining(invoiceRemainingOptional.get());
                    chargeByRemainingCountCalData.setRenewByChargeRemainingContract(
                            contractPrepayTypeComponent.isPrepayByRemainingCount(contractOptional.get())
                    );
                    chargeByRemainingCountCalData.setNextCalculateIntervalList(
                            remainingRecordModelComponent.genNextLocalDateTimeList(
                                    invoiceRemainingOptional.get()
                            ));
                    result = Optional.of(chargeByRemainingCountCalData);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    public InvoiceRemaining genNextInvoiceRemaining(
            InvoiceRemaining previousInvoiceRemaining
            , Optional<Integer> usageCountOpt
            , LocalDateTime uploadDate) {
        InvoiceRemaining invoiceRemaining = new InvoiceRemaining();
        if (usageCountOpt.isPresent()) {
            invoiceRemaining.setUsage(usageCountOpt.get().intValue());
        } else {
            invoiceRemaining.setUsage(0);
        }
        invoiceRemaining.setRemaining(previousInvoiceRemaining.getRemaining() - invoiceRemaining.getUsage());
        invoiceRemaining.setCompanyId(previousInvoiceRemaining.getCompanyId());
        invoiceRemaining.setContractId(previousInvoiceRemaining.getContractId());
        invoiceRemaining.setUploadDate(uploadDate);
        invoiceRemaining.setContractId(previousInvoiceRemaining.getContractId());
        invoiceRemaining.setCreateDate(LocalDateTime.now());
        return invoiceRemaining;
    }

    public Optional<CustomInterval> genInvoiceDateInterval(String fromStr, String toStr) {
        Optional<LocalDate> fromStrOpt = LocalDateTimeUtils.parseLocalDateFromString(fromStr, "yyyyMMdd");
        Optional<LocalDate> toStrOpt = LocalDateTimeUtils.parseLocalDateFromString(toStr, "yyyyMMdd");
        if (fromStrOpt.isPresent() && toStrOpt.isPresent()) {
            LocalDateTime startLocalDateTime = fromStrOpt.get().plusDays(1).atStartOfDay();
            LocalDateTime endLocalDateTime = toStrOpt.get().plusDays(1).atStartOfDay().minusSeconds(1);
            return Optional.of(
                    new CustomInterval(startLocalDateTime, endLocalDateTime)
            );
        }
        return Optional.empty();
    }
}
