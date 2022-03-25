package com.gateweb.charge.frontEndIntegration.enumeration;

public enum PageableQueryStatus {
    SUCCESS("成功"), ERROR("錯誤");

    public final String description;

    PageableQueryStatus(String desc) {
        this.description = desc;
    }
}
