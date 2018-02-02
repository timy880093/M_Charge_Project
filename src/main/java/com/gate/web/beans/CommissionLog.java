package com.gate.web.beans;

import com.gateweb.charge.model.CommissionLogEntity;

/**
 * Created by emily on 2016/1/12.
 */
public class CommissionLog extends CommissionLogEntity{
    private String dealerCompanyName; //經銷商公司名稱
    private String strCommissionType; //佣金類型說明
    private String strIsPaid; //是否付款說明
    private String strMainPercent; //佣金比例(有%)

    public String getDealerCompanyName() {
        return dealerCompanyName;
    }

    public void setDealerCompanyName(String dealerCompanyName) {
        this.dealerCompanyName = dealerCompanyName;
    }

    public String getStrCommissionType() {
        return strCommissionType;
    }

    public void setStrCommissionType(String strCommissionType) {
        this.strCommissionType = strCommissionType;
    }

    public String getStrIsPaid() {
        return strIsPaid;
    }

    public void setStrIsPaid(String strIsPaid) {
        this.strIsPaid = strIsPaid;
    }

    public String getStrMainPercent() {
        return strMainPercent;
    }

    public void setStrMainPercent(String strMainPercent) {
        this.strMainPercent = strMainPercent;
    }
}
