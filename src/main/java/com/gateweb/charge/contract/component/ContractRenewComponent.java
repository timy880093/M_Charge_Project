package com.gateweb.charge.contract.component;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.utils.ContractRenewIntervalGenerator;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.charge.service.dataGateway.ContractDataGateway;
import com.gateweb.orm.charge.entity.ChargePackage;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.PackageRef;
import com.gateweb.orm.charge.repository.ChargePackageRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.gateweb.orm.charge.repository.PackageRefRepository;
import com.gateweb.utils.ConcurrentUtils;
import com.gateweb.utils.LocalDateTimeUtils;
import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.gateweb.utils.ConcurrentUtils.pool;

@Component
public class ContractRenewComponent {
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    ContractDataGateway contractDataGateway;
    @Autowired
    ContractInitializer contractInitializer;
    @Autowired
    ChargePackageRepository chargePackageRepository;

    EventBus chargeSystemEventBus;

    @Autowired
    @Qualifier("chargeSystemEventBus")
    public void setChargeSystemEventBus(EventBus chargeSystemEventBus) {
        this.chargeSystemEventBus = chargeSystemEventBus;
    }

    /**
     * renew合約的條件是續約年月+2個月的一個月
     * 以202201為例：要出帳並續約的範圍就是20220301~20220331
     *
     * @param ymStr
     * @return
     */
    public Optional<CustomInterval> getRenewIntervalByYmStr(String ymStr) {
        Optional<CustomInterval> renewInterval = Optional.empty();
        Optional<YearMonth> yearMonthOpt = LocalDateTimeUtils.parseYearMonthFromString(ymStr, "yyyyMM");
        if (yearMonthOpt.isPresent()) {
            renewInterval = Optional.of(new CustomInterval(
                    yearMonthOpt.get().atDay(1).atStartOfDay().plusMonths(2)
                    , yearMonthOpt.get().plusMonths(3).atDay(1).atStartOfDay().minusSeconds(1)
            ));
        }
        return renewInterval;
    }

    public Set<Contract> renewContractByInterval(CustomInterval renewInterval, Long companyId, Long callerId) {
        Set<Contract> contractSet = contractDataGateway.findByCompanyIdAndContractStatusAndExpirationDateBetween(
                companyId, ContractStatus.E, renewInterval
        );
        return new HashSet<>(renewContractCollection(contractSet, callerId));
    }

    public Set<Contract> renewContractByInterval(CustomInterval renewInterval, Long callerId) {
        Set<Contract> contractSet = contractDataGateway.findByContractStatusAndExpirationDateBetween(
                ContractStatus.E
                , renewInterval
        );
        return new HashSet<>(renewContractCollection(contractSet, callerId));
    }

    private Collection<Contract> renewContractCollection(Collection<Contract> contractCollection, Long callerId) {
        Set<Contract> renewContractSet = new HashSet<>();
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();
        contractCollection.stream().forEach(contract -> {
            completableFutureList.add(CompletableFuture.runAsync(() -> {
                Optional<Contract> renewContractOpt = renewContract(contract, callerId);
                if (renewContractOpt.isPresent()) {
                    renewContractSet.add(renewContractOpt.get());
                }
            }, pool));
        });
        ConcurrentUtils.completableGet(completableFutureList);
        return renewContractSet;
    }

    /**
     * 因為有客戶合約調整的需求，在period調整成為14的時候
     * 因為新系統使用原來的參數的關系，因此會自動以14進行續約
     * 但考量到首次調整過後的14要在下次續約改為12已是一年後，實務上操作很困難
     * 因此商量能否把高於12個月的期間在續約時都自動改為12
     *
     * @param contract
     */
    public void periodRenewModifier(Contract contract) {
        if (contract.getPeriodMonth() > 12) {
            contract.setPeriodMonth(12);
        }
    }

    public boolean isContractRenewable(Contract contract) {
        boolean result = true;
        if (contract.getStatus().equals(ContractStatus.C)) {
            result = false;
        }
        if (contract.getEffectiveDate() == null) {
            result = false;
        }
        if (contract.getExpirationDate() == null) {
            result = false;
        }
        if (contract.getEffectiveDate().isAfter(contract.getExpirationDate())) {
            result = false;
        }
        return result;
    }

    public boolean isContractPeriodOverlap(LocalDateTime previousContractExpirationDate, Contract contract) {
        boolean result = false;
        Interval newInterval = new Interval(
                Timestamp.valueOf(contract.getEffectiveDate()).getTime(),
                Timestamp.valueOf(contract.getExpirationDate()).getTime()
        );
        // 查出此合約結束後時間的所有合約(非disabled)
        List<Contract> afterContractList = (List<Contract>) contractRepository
                .findByCompanyIdIsAndEffectiveDateAfterAndStatusIsNot(
                        contract.getCompanyId()
                        , previousContractExpirationDate
                        , ContractStatus.D
                );
        //todo:應調整為monopoly check
        // 遍歷合約，若有重疊，就不自動續約
        for (Contract afterContract : afterContractList) {
            Interval afterContractInterval = new Interval(
                    Timestamp.valueOf(afterContract.getEffectiveDate()).getTime(),
                    Timestamp.valueOf(afterContract.getExpirationDate()).getTime()
            );
            if (afterContractInterval.overlaps(newInterval)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public Optional<Contract> renewContract(Contract contract, Long callerId) {
        Optional<Contract> result = Optional.empty();
        try {
            CustomInterval newContractInterval = ContractRenewIntervalGenerator.genGeneralRenewInterval(contract);
            Contract newCreateContract = genRenewContract(contract, newContractInterval);
            periodRenewModifier(newCreateContract);
            boolean isValid = isContractRenewable(contract)
                    && !isContractPeriodOverlap(contract.getExpirationDate(), newCreateContract);

            if (isValid) {
                Set<PackageRef> packageRefSet = new HashSet<>();
                if (contract.getRenewPackageId() != null) {
                    packageRefSet.addAll(packageRefRepository.findByFromPackageId(contract.getRenewPackageId()));
                } else {
                    packageRefSet.addAll(packageRefRepository.findByFromPackageId(contract.getPackageId()));
                }
                newCreateContract = contractRepository.save(newCreateContract);
                result = Optional.of(newCreateContract);
                ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                        EventSource.CONTRACT
                        , EventAction.CREATE
                        , newCreateContract.getContractId()
                        , callerId
                );
                chargeSystemEventBus.post(chargeSystemEvent);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
     * renew from remaining count table
     *
     * @return
     */
    public Contract genRenewContract(final Contract originalContract, CustomInterval newContractInterval) {
        Contract renewContract = new Contract();
        renewContract.setPackageId(getRenewPackageId(originalContract));
        renewContract.setCompanyId(originalContract.getCompanyId());
        renewContract.setCreatorId(originalContract.getCreatorId());
        renewContract.setEffectiveDate(newContractInterval.getStartLocalDateTime());
        renewContract.setExpirationDate(newContractInterval.getEndLocalDateTime());
        renewContract.setPeriodMonth(originalContract.getPeriodMonth());
        renewContract.setName(getRenewContractName(renewContract));
        renewContract.setAutoRenew(originalContract.getAutoRenew());
        renewContract.setRemark("auto renew");
        renewContract.setIsFirstContract(false);
        renewContract.setFirstInvoiceDateAsEffectiveDate(false);
        renewContract.setAllowPartialBilling(true);
        renewContract.setStatus(ContractStatus.C);
        renewContract.setCreateDate(LocalDateTime.now());
        renewContract.setCreatorId(originalContract.getCreatorId());
        return renewContract;
    }

    public Optional<Contract> genRenewRemainingContract(
            final Contract contract
            , LocalDateTime newEffectiveDate
            , LocalDateTime executionDateTime) {
        Optional<LocalDateTime> newExpirationDateOpt = ContractRenewIntervalGenerator.getContractExpirationDate(
                newEffectiveDate
                , contract.getPeriodMonth()
        );
        if (newExpirationDateOpt.isPresent()
                && executionDateTime.isAfter(newExpirationDateOpt.get())) {
            CustomInterval newContractInterval = new CustomInterval(newEffectiveDate, newExpirationDateOpt.get());
            return Optional.of(
                    genRenewContract(
                            contract
                            , newContractInterval
                    )
            );
        } else {
            return Optional.empty();
        }
    }

    public Optional<Contract> genRenewRemainingContract(final Contract contract, LocalDateTime newEffectiveDate) {
        return genRenewRemainingContract(contract, newEffectiveDate, LocalDateTime.now());
    }

    public Optional<Contract> genRenewRemainingContract(final Contract contract, String prevInvoiceDate) {
        Optional<CustomInterval> newContractIntervalOpt = ContractRenewIntervalGenerator.genRemainingTypeRenewInterval(
                contract
                , prevInvoiceDate
        );
        if (newContractIntervalOpt.isPresent()) {
            return Optional.of(
                    genRenewContract(
                            contract
                            , newContractIntervalOpt.get()
                    )
            );
        } else {
            return Optional.empty();
        }
    }

    public Long getRenewPackageId(Contract contract) {
        if (contract.getRenewPackageId() != null) {
            return contract.getRenewPackageId();
        } else {
            return contract.getPackageId();
        }
    }

    public String getRenewContractName(Contract contract) {
        Optional<ChargePackage> chargePackageOptional;
        if (contract.getRenewPackageId() != null) {
            chargePackageOptional = chargePackageRepository.findById(contract.getRenewPackageId());
        } else {
            chargePackageOptional = chargePackageRepository.findById(contract.getPackageId());
        }
        if (chargePackageOptional.isPresent()) {
            return chargePackageOptional.get().getName();
        } else {
            return "";
        }
    }

}
