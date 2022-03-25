package com.gateweb.charge.enumeration;

/**
 * 必須要承認cron pattern有極限，但沒關系，我們已經把cycle隔離了
 * 現在，用enum來描述他
 */
public enum CycleType {
    CRON("排程表達式"),
    ANY("依照合約內容"),
    YEAR("一年"),
    MONTH("一個月"),
    SEASON("一季"),
    GW_RENTAL_CAL("關網月租計算(年繳OR季繳)"),
    GW_OVERAGE_BIL("關網超額收費區間"),
    GW_OVERAGE_BIL_CAL("關網超額計算區間");

    public final String description;

    CycleType(String desc) {
        this.description = desc;
    }
}
