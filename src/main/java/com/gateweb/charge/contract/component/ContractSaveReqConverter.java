package com.gateweb.charge.contract.component;

import com.gateweb.charge.contract.bean.request.ContractSaveReq;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.utils.bean.BeanConverterUtils;

import java.util.Optional;

public class ContractSaveReqConverter {
    final private static BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    public static Optional<ContractSaveReq> genContractSaveReq(String jsonBody, CallerInfo callerInfo) {
        Optional<ContractSaveReq> contractSaveReqOptional = beanConverterUtils.convertJsonToObjWithTypeCast(
                jsonBody, ContractSaveReq.class
        );
        if (contractSaveReqOptional.isPresent()) {
            contractSaveReqOptional.get().setCallerId(callerInfo.getUserEntity().getUserId().longValue());
            return contractSaveReqOptional;
        }
        return Optional.empty();
    }
}
