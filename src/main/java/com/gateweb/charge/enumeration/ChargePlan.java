package com.gateweb.charge.enumeration;

/**
 * 其實這個是contractChargeCycle的表示。
 * 只是在沒有package或contract的情況下需要拿其中一種狀態擴展定義，或定義一個新的。
 */
public enum ChargePlan {
    INITIATION("初始化"),
    PERIODIC("週期"),
    TERMINATION("終止"),
    SUSPENSION("暫停"),
    SUSPENSION_PERIODIC("暫停區間週期"),
    RE_ACTIVATION("重啟"),
    USAGE("使用");

    public final String description;

    ChargePlan(String description) {
        this.description = description;
    }
}
