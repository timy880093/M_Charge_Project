package com.gateweb.charge.contract.bean.request;

import com.gateweb.charge.frontEndIntegration.enumeration.SweetAlertStatus;
import com.gateweb.charge.frontEndIntegration.bean.SweetAlertResponse;

public class ContractSaveRes extends SweetAlertResponse {

    public ContractSaveRes() {
    }

    public ContractSaveRes(SweetAlertStatus sweetAlertStatus, String title, String message) {
        super(sweetAlertStatus, title, message);
    }
}
