package com.gate.web.formbeans;

import java.sql.Timestamp;

import com.gateweb.charge.model.GiftEntity;


public class GiftBean{
    private String giftId;
    private String companyId;
    private String giftDate;
    private String giftCnt;
    private String creatorId;
    private String modifierId;

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getGiftDate() {
        return giftDate;
    }

    public void setGiftDate(String giftDate) {
        this.giftDate = giftDate;
    }

    public String getGiftCnt() {
        return giftCnt;
    }

    public void setGiftCnt(String giftCnt) {
        this.giftCnt = giftCnt;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }
}
