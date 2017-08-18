package com.gate.web.beans;

import org.aspectj.org.eclipse.jdt.internal.core.util.Util;

/**
 * Created by emily on 2016/1/5.
 */
public class CalOver implements Comparable<CalOver>{
    private String billId;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    @Override
    public int compareTo(CalOver o) {
        Integer oldObj = (null == this.billId)?0:Integer.parseInt(this.billId);
        Integer newObj = (null == o.getBillId())?0:Integer.parseInt(o.getBillId());
        return Integer.compare(oldObj, newObj);
    }

}
