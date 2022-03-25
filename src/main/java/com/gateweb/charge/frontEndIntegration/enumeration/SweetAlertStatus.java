package com.gateweb.charge.frontEndIntegration.enumeration;

public enum SweetAlertStatus {
    SUCCESS("成功"), WARNING("訊息"), ERROR("錯誤");

    public final String description;

    SweetAlertStatus(String desc) {
        this.description = desc;
    }
}
