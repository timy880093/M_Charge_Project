package com.gateweb.charge.contract.component;

import com.gateweb.charge.contract.beanValidation.ValidContractPeriodMonth;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.exception.ContractIntervalOverlapException;
import com.gateweb.charge.exception.ContractTypeAmbiguousException;
import com.gateweb.charge.service.dataGateway.ContractDataGateway;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.utils.CustomIntervalUtils;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Component
public class ContractValidationComponent {
    @Autowired
    ContractDataGateway contractDataGateway;
    @Autowired
    CompanyRepository companyRepository;

    public boolean contractEndDateCalculateCheck(@ValidContractPeriodMonth final Contract contract) {
        boolean result = false;
        if ((contract.getInstallationDate() != null || !contract.getFirstInvoiceDateAsEffectiveDate())
                && contract.getEffectiveDate() == null) {
            result = true;
        }
        return result;
    }

    public void contractValidation(@ValidContractPeriodMonth final Contract contract)
            throws ContractIntervalOverlapException, ContractTypeAmbiguousException {
        boolean contractTypeValidation = contractTypeValidation(contract);
        int periodValidation = contractStartDateEndDateValidation(contract);
        if (contractTypeValidation) {
            if (periodValidation == 1) {
                boolean validContractInterval = contractIntervalValidation(contract);
                if (!validContractInterval) {
                    throw new ContractIntervalOverlapException();
                }
            }
        } else {
            throw new ContractTypeAmbiguousException();
        }
    }

    /**
     * A	B	C	Output
     * 0	0	0	F
     * 0	0	1	T
     * 0	1	0	F
     * 0	1	1	T
     * 1	0	0	T
     * 1	0	1	T
     * 1	1	0	T
     * 1	1	1	F
     *
     * @param contract
     * @return
     */
    public boolean contractTypeValidation(Contract contract) {
        int startDateEndDateValidationResult = contractStartDateEndDateValidation(contract);
        boolean a;
        if (startDateEndDateValidationResult == 1) {
            a = true;
        } else if (startDateEndDateValidationResult == 0) {
            a = false;
        } else {
            return false;
        }
        boolean b = isAutoFulfillmentTypeContract(contract);
        boolean c = contract.getStatus().equals(ContractStatus.C);

        return (a && !c) || (!a && c) || (!b && c);
    }

    public boolean isAutoFulfillmentTypeContract(Contract contract) {
        return contract.getFirstInvoiceDateAsEffectiveDate() || contract.getInstallationDate() != null;
    }

    public boolean contractIntervalValidation(final Contract contract) {
        boolean result = true;
        //起始日及終止日檢查
        Interval contractInterval = CustomIntervalUtils.getJodaInterval(
                contract.getEffectiveDate()
                , contract.getExpirationDate()
        );
        //目標狀態
        List<Contract> contractList = contractDataGateway.findByCompanyIdAndContractStatusIn(
                contract.getCompanyId()
                , ContractStatus.E
                , ContractStatus.C
        );
        //遍歷每一個可能性
        for (Contract existContract : contractList) {
            if (existContract.getContractId().equals(contract.getContractId())) {
                //跳過自己
                continue;
            }
            Interval existInterval = CustomIntervalUtils.getJodaInterval(
                    existContract.getEffectiveDate()
                    , existContract.getExpirationDate()
            );
            if (existInterval.overlaps(contractInterval)) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 定義錯誤的合約日期時間
     *
     * @param contract
     * @return
     */
    public int contractStartDateEndDateValidation(Contract contract) {
        if (contract.getEffectiveDate() != null && contract.getExpirationDate() != null) {
            if (contract.getEffectiveDate().isBefore(contract.getExpirationDate())) {
                return 1;
            }
        } else if (contract.getEffectiveDate() == null && contract.getExpirationDate() == null) {
            return 0;
        }
        return -1;
    }
}
