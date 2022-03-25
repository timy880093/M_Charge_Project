/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:14 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.orm.charge.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * @author pkliu
 */
@Entity
@Table(name = "charge_mode_cycle_add")
public class ChargeModeCycleAddEntity implements Serializable {

//long serialVersionUID jdk tool: serialver.exe 

    /**
     * addition_id java.lang.Integer , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addition_id", unique = true, nullable = false)
    protected java.lang.Integer additionId;

    /**
     * end_date
     */
    @Column(name = "end_date")
    protected java.util.Date endDate;

    /**
     * real_start_date
     */
    @Column(name = "real_start_date")
    protected java.util.Date realStartDate;

    /**
     * free_month
     */
    @Column(name = "free_month")
    protected java.lang.Integer freeMonth;

    /**
     * addition_quantity
     */
    @Column(name = "addition_quantity")
    protected java.lang.Integer additionQuantity;

    /**
     * creator_id
     */
    @Column(name = "creator_id")
    protected java.lang.Integer creatorId;

    /**
     * modifier_id
     */
    @Column(name = "modifier_id")
    protected java.lang.Integer modifierId;

    /**
     * real_end_date
     */
    @Column(name = "real_end_date")
    protected java.util.Date realEndDate;

    /**
     * create_date
     */
    @Column(name = "create_date")
    protected java.sql.Timestamp createDate;

    /**
     * modify_date
     */
    @Column(name = "modify_date")
    protected java.sql.Timestamp modifyDate;

    /**
     * gift_price
     */
    @Column(name = "gift_price")
    protected java.math.BigDecimal giftPrice;

    /**
     * start_date
     */
    @Column(name = "start_date")
    protected java.util.Date startDate;


    /**
     * 002
     *
     * @return java.lang.Integer additionId
     */
    public java.lang.Integer getAdditionId() {
        return this.additionId;
    }

    /**
     * 0001
     *
     * @param data Set the additionId
     */
    public void setAdditionId(java.lang.Integer data) {
        this.additionId = data;    //zzz
    }

    /**
     * 002
     *
     * @return java.util.Date endDate
     */
    public java.util.Date getEndDate() {
        return this.endDate;
    }

    /**
     * 0001
     *
     * @param data Set the endDate
     */
    public void setEndDate(java.util.Date data) {
        this.endDate = data;
    }

    /**
     * 002
     *
     * @return java.util.Date realStartDate
     */
    public java.util.Date getRealStartDate() {
        return this.realStartDate;
    }

    /**
     * 0001
     *
     * @param data Set the realStartDate
     */
    public void setRealStartDate(java.util.Date data) {
        this.realStartDate = data;
    }

    /**
     * 002
     *
     * @return java.lang.Integer freeMonth
     */
    public java.lang.Integer getFreeMonth() {
        return this.freeMonth;
    }

    /**
     * 0001
     *
     * @param data Set the freeMonth
     */
    public void setFreeMonth(java.lang.Integer data) {
        this.freeMonth = data;
    }

    /**
     * 002
     *
     * @return java.lang.Integer additionQuantity
     */
    public java.lang.Integer getAdditionQuantity() {
        return this.additionQuantity;
    }

    /**
     * 0001
     *
     * @param data Set the additionQuantity
     */
    public void setAdditionQuantity(java.lang.Integer data) {
        this.additionQuantity = data;
    }

    /**
     * 002
     *
     * @return java.lang.Integer creatorId
     */
    public java.lang.Integer getCreatorId() {
        return this.creatorId;
    }

    /**
     * 0001
     *
     * @param data Set the creatorId
     */
    public void setCreatorId(java.lang.Integer data) {
        this.creatorId = data;
    }

    /**
     * 002
     *
     * @return java.lang.Integer modifierId
     */
    public java.lang.Integer getModifierId() {
        return this.modifierId;
    }

    /**
     * 0001
     *
     * @param data Set the modifierId
     */
    public void setModifierId(java.lang.Integer data) {
        this.modifierId = data;
    }

    /**
     * 002
     *
     * @return java.util.Date realEndDate
     */
    public java.util.Date getRealEndDate() {
        return this.realEndDate;
    }

    /**
     * 0001
     *
     * @param data Set the realEndDate
     */
    public void setRealEndDate(java.util.Date data) {
        this.realEndDate = data;
    }

    /**
     * 002
     *
     * @return java.sql.Timestamp createDate
     */
    public java.sql.Timestamp getCreateDate() {
        return this.createDate;
    }

    /**
     * 0001
     *
     * @param data Set the createDate
     */
    public void setCreateDate(java.sql.Timestamp data) {
        this.createDate = data;
    }

    /**
     * 002
     *
     * @return java.sql.Timestamp modifyDate
     */
    public java.sql.Timestamp getModifyDate() {
        return this.modifyDate;
    }

    /**
     * 0001
     *
     * @param data Set the modifyDate
     */
    public void setModifyDate(java.sql.Timestamp data) {
        this.modifyDate = data;
    }

    /**
     * 002
     *
     * @return java.math.BigDecimal giftPrice
     */
    public java.math.BigDecimal getGiftPrice() {
        return this.giftPrice;
    }

    /**
     * 0001
     *
     * @param data Set the giftPrice
     */
    public void setGiftPrice(java.math.BigDecimal data) {
        this.giftPrice = data;
    }

    /**
     * 002
     *
     * @return java.util.Date startDate
     */
    public java.util.Date getStartDate() {
        return this.startDate;
    }

    /**
     * 0001
     *
     * @param data Set the startDate
     */
    public void setStartDate(java.util.Date data) {
        this.startDate = data;
    }


    /**
     *
     */
    public ChargeModeCycleAddEntity() {
    }

    /**
     * full constructor
     *
     * @param additionId
     * @param endDate
     * @param realStartDate
     * @param freeMonth
     * @param additionQuantity
     * @param creatorId
     * @param modifierId
     * @param realEndDate
     * @param createDate
     * @param modifyDate
     * @param giftPrice
     * @param startDate
     */
    public ChargeModeCycleAddEntity(
            java.lang.Integer additionId
            , java.util.Date endDate
            , java.util.Date realStartDate
            , java.lang.Integer freeMonth
            , java.lang.Integer additionQuantity
            , java.lang.Integer creatorId
            , java.lang.Integer modifierId
            , java.util.Date realEndDate
            , java.sql.Timestamp createDate
            , java.sql.Timestamp modifyDate
            , java.math.BigDecimal giftPrice
            , java.util.Date startDate
    ) {
        this.setAdditionId(additionId);
        this.setEndDate(endDate);
        this.setRealStartDate(realStartDate);
        this.setFreeMonth(freeMonth);
        this.setAdditionQuantity(additionQuantity);
        this.setCreatorId(creatorId);
        this.setModifierId(modifierId);
        this.setRealEndDate(realEndDate);
        this.setCreateDate(createDate);
        this.setModifyDate(modifyDate);
        this.setGiftPrice(giftPrice);
        this.setStartDate(startDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(additionId, endDate, realStartDate, freeMonth, additionQuantity, creatorId, modifierId, realEndDate, createDate, modifyDate, giftPrice, startDate);
    }

    @Override
    public String toString() {
        return "ChargeModeCycleAddEntity{" +
                "additionId=" + additionId +
                ", endDate=" + endDate +
                ", realStartDate=" + realStartDate +
                ", freeMonth=" + freeMonth +
                ", additionQuantity=" + additionQuantity +
                ", creatorId=" + creatorId +
                ", modifierId=" + modifierId +
                ", realEndDate=" + realEndDate +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", giftPrice=" + giftPrice +
                ", startDate=" + startDate +
                '}';
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @return true is equal, false is not equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || !(obj instanceof ChargeModeCycleAddEntity))
            return false;

        ChargeModeCycleAddEntity key = (ChargeModeCycleAddEntity) obj;
        if (this.additionId != key.additionId)
            return false;

        return true;
    }

}