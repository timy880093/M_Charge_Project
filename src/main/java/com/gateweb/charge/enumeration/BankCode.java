package com.gateweb.charge.enumeration;

public enum BankCode {
    中國信託_CTBC("822"), 上海銀行_SCSB("011");

    private String code;

    BankCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
