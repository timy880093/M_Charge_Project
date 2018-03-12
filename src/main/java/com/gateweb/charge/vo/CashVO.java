package com.gateweb.charge.vo;

import com.gateweb.charge.model.CashDetailEntity;
import com.gateweb.charge.model.CashMasterEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Eason on 3/7/2018.
 */
public class CashVO implements Serializable{
    CashMasterEntity cashMasterEntity;
    List<CashDetailEntity> cashDetailEntityList;

    public List<CashDetailEntity> getCashDetailEntityList() {
        return cashDetailEntityList;
    }

    public void setCashDetailEntityList(List<CashDetailEntity> cashDetailEntityList) {
        this.cashDetailEntityList = cashDetailEntityList;
    }

    public CashMasterEntity getCashMasterEntity() {
        return cashMasterEntity;
    }

    public void setCashMasterEntity(CashMasterEntity cashMasterEntity) {
        this.cashMasterEntity = cashMasterEntity;
    }

}
