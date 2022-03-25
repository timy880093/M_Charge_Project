package com.gateweb.charge.scheduleJob.component;

import com.gateweb.orm.charge.entity.ChargeRule;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.NewGrade;
import com.gateweb.orm.charge.entity.PackageRef;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
import com.gateweb.orm.charge.repository.ChargeRuleRepository;
import com.gateweb.orm.charge.repository.NewGradeRepository;
import com.gateweb.orm.charge.repository.PackageRefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class InvoiceRemainingCountWriter {
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    ChargeRuleRepository chargeRuleRepository;
    @Autowired
    NewGradeRepository newGradeRepository;

    public Optional<InvoiceRemaining> genInvoiceRemainingCount(Contract contract) {
        Optional<InvoiceRemaining> result = Optional.empty();
        List<PackageRef> packageRefList = packageRefRepository.findByFromPackageId(contract.getPackageId());
        if (packageRefList.size() == 1) {
            Optional<ChargeRule> chargeRuleOptional = chargeRuleRepository.findByChargeRuleId(
                    packageRefList.get(0).getToChargeRuleId()
            );
            if (chargeRuleOptional.isPresent()) {
                Optional<NewGrade> gradeOptional = newGradeRepository.findByGradeId(chargeRuleOptional.get().getGradeId());
                if (gradeOptional.isPresent()) {
                    InvoiceRemaining invoiceRemaining = new InvoiceRemaining();
                    invoiceRemaining.setRemaining(gradeOptional.get().getCntEnd());
                    invoiceRemaining.setContractId(contract.getContractId());
                    invoiceRemaining.setCreateDate(LocalDateTime.now());
                    invoiceRemaining.setCompanyId(contract.getCompanyId());
                    result = Optional.of(invoiceRemaining);
                }
            }
        }
        return result;
    }
}
