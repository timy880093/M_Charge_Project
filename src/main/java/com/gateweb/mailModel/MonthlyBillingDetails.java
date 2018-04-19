package com.gateweb.mailModel;

import com.gateweb.charge.model.BillCycleEntity;
import com.gateweb.charge.model.ChargeModeCycleEntity;
import com.gateweb.charge.model.PackageModeEntity;
import com.gateweb.charge.vo.CashVO;

import java.util.List;

/**
 * Created by Eason on 4/19/2018.
 */
public class MonthlyBillingDetails {
    CashVO cashVO;
    PackageModeEntity packageModeEntity;
    ChargeModeCycleEntity chargeModeCycleEntity;
    List<BillCycleEntity> billCycleEntityList;

    public List<BillCycleEntity> getBillCycleEntityList() {
        return billCycleEntityList;
    }

    public void setBillCycleEntityList(List<BillCycleEntity> billCycleEntityList) {
        this.billCycleEntityList = billCycleEntityList;
    }

    public CashVO getCashVO() {
        return cashVO;
    }

    public void setCashVO(CashVO cashVO) {
        this.cashVO = cashVO;
    }

    public ChargeModeCycleEntity getChargeModeCycleEntity() {
        return chargeModeCycleEntity;
    }

    public void setChargeModeCycleEntity(ChargeModeCycleEntity chargeModeCycleEntity) {
        this.chargeModeCycleEntity = chargeModeCycleEntity;
    }

    public PackageModeEntity getPackageModeEntity() {
        return packageModeEntity;
    }

    public void setPackageModeEntity(PackageModeEntity packageModeEntity) {
        this.packageModeEntity = packageModeEntity;
    }
}
