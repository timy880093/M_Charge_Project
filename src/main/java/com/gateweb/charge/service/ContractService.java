package com.gateweb.charge.service;


import com.gateweb.charge.exception.DeleteBilledBillingItemException;
import com.gateweb.charge.frontEndIntegration.bean.OutToBillRequest;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.view.ContractFetchView;

import java.util.List;

public interface ContractService {

    List<ContractFetchView> getNotExpireContractFetchViewList();

    List<ContractFetchView> searchAllContractFetchView();

    List<ContractFetchView> findDeductibleContractList();

    void calculateContractFee(OutToBillRequest outToBillRequest, Long callerI);

    void calculateContractFeeAndOutToBill(OutToBillRequest outToBillRequest, Long callerId);

    void enableContract(Long contractId, Long callerId);

    void enableContract(Contract contract, Long callerId);

    void initialContract(Long contractId, Long callerId);

    void cancelInitialContract(Contract contract, Long callerId) throws DeleteBilledBillingItemException;
}
