package com.gateweb.charge.service;


import com.gateweb.charge.exception.*;
import com.gateweb.charge.frontEndIntegration.bean.OutToBillRequest;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.view.ContractFetchView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ContractService {

    List<ContractFetchView> getNotExpireContractFetchViewList();

    List<ContractFetchView> searchAllContractFetchView();

    Optional<Contract> saveOrUpdateContractByMap(Map<String, Object> map, CallerInfo callerInfo) throws ContractIntervalOverlapException, EmptyPackageRefListException, MissingRequiredPropertiesException, ContractTypeAmbiguousException;

    List<ContractFetchView> findDeductibleContractList();

    void calculateContractFeeAndOutToBill(OutToBillRequest outToBillRequest, Long callerId);

    void enableContract(Long contractId, Long callerId);

    void enableContract(Contract contract, Long callerId);

    void initialContract(Long contractId, Long callerId);

    void cancelInitialContract(Contract contract, Long callerId) throws DeleteBilledBillingItemException;

    void continueContractWithCurrentDateTime(Contract contract, CallerInfo callerInfo);
}
