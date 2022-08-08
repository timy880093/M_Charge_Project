package com.gateweb.charge.contract.component;

import com.gateweb.charge.contract.bean.request.ContractSaveReq;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.charge.exception.ContractIntervalOverlapException;
import com.gateweb.charge.exception.ContractNotFoundException;
import com.gateweb.charge.exception.ContractTypeAmbiguousException;
import com.gateweb.charge.mapper.ContractMapper;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ContractSaveComponent {
    @Autowired
    ContractValidationComponent contractValidationComponent;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    ContractMapper contractMapper;

    public void executeUpdate(ContractSaveReq contractSaveReq) throws ContractIntervalOverlapException, ContractTypeAmbiguousException, ContractNotFoundException {
        Contract contractVo = saveReqToContract(contractSaveReq);
        Optional<Contract> existContractOptional = contractRepository.findById(contractSaveReq.getContractId());
        if (!existContractOptional.isPresent()) {
            throw new ContractNotFoundException();
        }
        contractMapper.updateContractFromVo(contractVo, existContractOptional.get());
        contractValidationComponent.contractValidation(existContractOptional.get());
        contractRepository.save(existContractOptional.get());
    }

    public Contract executeCreate(ContractSaveReq contractSaveReq) throws ContractIntervalOverlapException, ContractTypeAmbiguousException {
        Contract contractVo = saveReqToContract(contractSaveReq);
        if (contractVo.getFirstInvoiceDateAsEffectiveDate() == null) {
            contractVo.setFirstInvoiceDateAsEffectiveDate(false);
        }
        if (contractVo.getExpirationDate() != null) {
            contractVo.setExpirationDate(
                    LocalDateTimeUtils.pushToLastSecond(contractVo.getExpirationDate())
            );
        }
        contractVo.setCreateDate(LocalDateTime.now());
        contractVo.setStatus(ContractStatus.C);
        contractValidationComponent.contractValidation(contractVo);
        contractRepository.save(contractVo);
        return contractVo;
    }

    public Contract saveReqToContract(ContractSaveReq contractSaveReq) {
        Contract contract = new Contract();
        contract.setCompanyId(contractSaveReq.getCompanyId());
        contract.setPackageId(contractSaveReq.getPackageId());
        contract.setRenewPackageId(contractSaveReq.getRenewPackageId());
        contract.setName(contractSaveReq.getContractName());
        contract.setIsFirstContract(contractSaveReq.getFirstContract());
        contract.setFirstInvoiceDateAsEffectiveDate(contractSaveReq.getFirstInvoiceDateAsEffectiveDate());
        contract.setPeriodMonth(contractSaveReq.getPeriodMonth());
        contract.setAutoRenew(contractSaveReq.getAutoRenew());
        contract.setEffectiveDate(contractSaveReq.getEffectiveDate());
        contract.setExpirationDate(contractSaveReq.getExpirationDate());
        contract.setInstallationDate(contractSaveReq.getInstallationDate());
        contract.setCreatorId(contractSaveReq.getCallerId());
        return contract;
    }
}
