package com.gateweb.mailModel;

import com.gateweb.charge.model.BillCycleEntity;
import com.gateweb.charge.model.ChargeModeGradeEntity;
import com.gateweb.charge.model.PackageModeEntity;
import com.gateweb.charge.vo.CashVO;

import java.util.List;

/**
 * Created by Eason on 4/19/2018.
 */
public class HierarchicalBillingDetails {
    CashVO cashVO;
    PackageModeEntity packageModeEntity;
    ChargeModeGradeEntity chargeModeGradeEntity;
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

    public ChargeModeGradeEntity getChargeModeGradeEntity() {
        return chargeModeGradeEntity;
    }

    public void setChargeModeGradeEntity(ChargeModeGradeEntity chargeModeGradeEntity) {
        this.chargeModeGradeEntity = chargeModeGradeEntity;
    }

    public PackageModeEntity getPackageModeEntity() {
        return packageModeEntity;
    }

    public void setPackageModeEntity(PackageModeEntity packageModeEntity) {
        this.packageModeEntity = packageModeEntity;
    }
}
