package com.gateweb.charge.contract.remainingCount.scheduleJob.renew.component;

import com.gateweb.charge.contract.remainingCount.source.RemainingCountAmountProvider;
import com.gateweb.orm.charge.entity.ChargeRule;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.ChargeRuleRepository;
import com.gateweb.orm.charge.repository.InvoiceRemainingRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class RemainingContractInitializer {
    @Autowired
    ChargeRuleRepository chargeRuleRepository;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    RemainingCountAmountProvider remainingCountAmountProvider;

    public Optional<InvoiceRemaining> chargeByRemainingCountInitializer(Contract contract) {
        Optional<ChargeRule> chargeRuleOpt = chargeRuleRepository.getChargeRuleByPackageIdAndRemainingCountIsTrue(contract.getPackageId());
        Optional<InvoiceRemaining> resultOpt = chargeRuleOpt.flatMap(chargeRule -> {
            return genInvoiceRemainingCountRecord(contract, chargeRule);
        });
        return resultOpt;
    }

    /**
     * 如果續完約還是負的，就直接改合約日期了
     *
     * @param contract
     * @param chargeRule
     * @return
     */
    public Optional<InvoiceRemaining> genInvoiceRemainingCountRecord(Contract contract, ChargeRule chargeRule) {
        Optional<InvoiceRemaining> result = Optional.empty();
        Optional<Integer> remainingCountOpt = remainingCountAmountProvider.getRemainingCountFromChargeRule(chargeRule);
        Optional<InvoiceRemaining> prevInvoiceRemainingOptional
                = invoiceRemainingRepository.findTopInvoiceRemainingByCompanyIdOrderByInvoiceDateDesc(
                contract.getCompanyId()
        );
        //實際用於計算的區間因為計算規則改變的關系改為下一天，因為計算規則中不包含當天
        Optional<String> invoiceDateOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                contract.getEffectiveDate().minusDays(1)
                , "yyyyMMdd"
        );
        if (remainingCountOpt.isPresent() && invoiceDateOpt.isPresent()) {
            InvoiceRemaining invoiceRemaining = new InvoiceRemaining();
            invoiceRemaining.setContractId(contract.getContractId());
            invoiceRemaining.setCompanyId(contract.getCompanyId());
            invoiceRemaining.setCreateDate(LocalDateTime.now());
            invoiceRemaining.setModifyDate(LocalDateTime.now());
            if (prevInvoiceRemainingOptional.isPresent()) {
                if (prevInvoiceRemainingOptional.get().getRemaining() < 0) {
                    invoiceRemaining.setRemaining(remainingCountOpt.get() + prevInvoiceRemainingOptional.get().getRemaining());
                } else {
                    invoiceRemaining.setRemaining(remainingCountOpt.get());
                }
                invoiceRemaining.setInvoiceDate(prevInvoiceRemainingOptional.get().getInvoiceDate());
            } else {
                invoiceRemaining.setRemaining(remainingCountOpt.get());
                invoiceRemaining.setInvoiceDate(invoiceDateOpt.get());
            }
            invoiceRemaining.setUsage(0);
            invoiceRemaining.setUploadDate(contract.getEffectiveDate());
            result = Optional.of(invoiceRemaining);
        }
        return result;
    }
}
