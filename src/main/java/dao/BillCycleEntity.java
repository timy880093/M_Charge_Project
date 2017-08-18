package dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by emily on 2015/12/17.
 */
public class BillCycleEntity implements Serializable,Comparable<BillCycleEntity>{
    private Integer billId;
    private Integer billType;
    private Integer packageId;
    private Integer companyId;
    private String yearMonth;
    private BigDecimal price;
    private Integer cnt;
    private Integer cntLimit;
    private Integer cntGift;
    private Integer cntOver;
    private BigDecimal priceOver;
    private BigDecimal priceMax;
    private BigDecimal payOver;
    private BigDecimal payMonth;
    private Integer cashOutOverId;
    private Integer cashOutMonthId;
    private Integer cashInOverId;
    private Integer cashInMonthId;
    private String status;
    private String isPriceFree;
    private BigDecimal singlePrice;
    private Timestamp modifyDate;
    private Integer modifierId;
    private Timestamp createDate;
    private Integer creatorId;

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public Integer getCntLimit() {
        return cntLimit;
    }

    public void setCntLimit(Integer cntLimit) {
        this.cntLimit = cntLimit;
    }

    public Integer getCntGift() {
        return cntGift;
    }

    public void setCntGift(Integer cntGift) {
        this.cntGift = cntGift;
    }

    public Integer getCntOver() {
        return cntOver;
    }

    public void setCntOver(Integer cntOver) {
        this.cntOver = cntOver;
    }

    public BigDecimal getPriceOver() {
        return priceOver;
    }

    public void setPriceOver(BigDecimal priceOver) {
        this.priceOver = priceOver;
    }

    public BigDecimal getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(BigDecimal priceMax) {
        this.priceMax = priceMax;
    }

    public BigDecimal getPayOver() {
        return payOver;
    }

    public void setPayOver(BigDecimal payOver) {
        this.payOver = payOver;
    }

    public BigDecimal getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(BigDecimal payMonth) {
        this.payMonth = payMonth;
    }

    public Integer getCashOutOverId() {
        return cashOutOverId;
    }

    public void setCashOutOverId(Integer cashOutOverId) {
        this.cashOutOverId = cashOutOverId;
    }

    public Integer getCashOutMonthId() {
        return cashOutMonthId;
    }

    public void setCashOutMonthId(Integer cashOutMonthId) {
        this.cashOutMonthId = cashOutMonthId;
    }

    public Integer getCashInOverId() {
        return cashInOverId;
    }

    public void setCashInOverId(Integer cashInOverId) {
        this.cashInOverId = cashInOverId;
    }

    public Integer getCashInMonthId() {
        return cashInMonthId;
    }

    public void setCashInMonthId(Integer cashInMonthId) {
        this.cashInMonthId = cashInMonthId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsPriceFree() {
        return isPriceFree;
    }

    public void setIsPriceFree(String isPriceFree) {
        this.isPriceFree = isPriceFree;
    }

    public BigDecimal getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(BigDecimal singlePrice) {
        this.singlePrice = singlePrice;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getModifierId() {
        return modifierId;
    }

    public void setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BillCycleEntity that = (BillCycleEntity) o;

        if (billId != that.billId) return false;
        if (billType != null ? !billType.equals(that.billType) : that.billType != null) return false;
        if (packageId != null ? !packageId.equals(that.packageId) : that.packageId != null) return false;
        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (yearMonth != null ? !yearMonth.equals(that.yearMonth) : that.yearMonth != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (cnt != null ? !cnt.equals(that.cnt) : that.cnt != null) return false;
        if (cntLimit != null ? !cntLimit.equals(that.cntLimit) : that.cntLimit != null) return false;
        if (cntGift != null ? !cntGift.equals(that.cntGift) : that.cntGift != null) return false;
        if (cntOver != null ? !cntOver.equals(that.cntOver) : that.cntOver != null) return false;
        if (priceOver != null ? !priceOver.equals(that.priceOver) : that.priceOver != null) return false;
        if (priceMax != null ? !priceMax.equals(that.priceMax) : that.priceMax != null) return false;
        if (payOver != null ? !payOver.equals(that.payOver) : that.payOver != null) return false;
        if (payMonth != null ? !payMonth.equals(that.payMonth) : that.payMonth != null) return false;
        if (cashOutOverId != null ? !cashOutOverId.equals(that.cashOutOverId) : that.cashOutOverId != null)
            return false;
        if (cashOutMonthId != null ? !cashOutMonthId.equals(that.cashOutMonthId) : that.cashOutMonthId != null)
            return false;
        if (cashInOverId != null ? !cashInOverId.equals(that.cashInOverId) : that.cashInOverId != null) return false;
        if (cashInMonthId != null ? !cashInMonthId.equals(that.cashInMonthId) : that.cashInMonthId != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (isPriceFree != null ? !isPriceFree.equals(that.isPriceFree) : that.isPriceFree != null) return false;
        if (singlePrice != null ? !singlePrice.equals(that.singlePrice) : that.singlePrice != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (modifierId != null ? !modifierId.equals(that.modifierId) : that.modifierId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = billId != null ? billId.hashCode() : 0;
        result = 31 * result + (billType != null ? billType.hashCode() : 0);
        result = 31 * result + (packageId != null ? packageId.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (yearMonth != null ? yearMonth.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (cnt != null ? cnt.hashCode() : 0);
        result = 31 * result + (cntLimit != null ? cntLimit.hashCode() : 0);
        result = 31 * result + (cntGift != null ? cntGift.hashCode() : 0);
        result = 31 * result + (cntOver != null ? cntOver.hashCode() : 0);
        result = 31 * result + (priceOver != null ? priceOver.hashCode() : 0);
        result = 31 * result + (priceMax != null ? priceMax.hashCode() : 0);
        result = 31 * result + (payOver != null ? payOver.hashCode() : 0);
        result = 31 * result + (payMonth != null ? payMonth.hashCode() : 0);
        result = 31 * result + (cashOutOverId != null ? cashOutOverId.hashCode() : 0);
        result = 31 * result + (cashOutMonthId != null ? cashOutMonthId.hashCode() : 0);
        result = 31 * result + (cashInOverId != null ? cashInOverId.hashCode() : 0);
        result = 31 * result + (cashInMonthId != null ? cashInMonthId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (isPriceFree != null ? isPriceFree.hashCode() : 0);
        result = 31 * result + (singlePrice != null ? singlePrice.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (modifierId != null ? modifierId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(BillCycleEntity o) {
        Integer oldObj = (null == this.billId)?0:this.billId;
        Integer newObj = (null == o.getBillId())?0:this.billId;
        return Integer.compare(oldObj, newObj);
    }
}
