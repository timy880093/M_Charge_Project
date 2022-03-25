package com.gateweb.charge.enumeration;

public enum ContractStatus {
    C("新建"), B("結帳中"), E("啟用"), T("終止"), S("暫停"), D("停用");

    public final String description;

    ContractStatus(String desc) {
        this.description = desc;
    }
}
