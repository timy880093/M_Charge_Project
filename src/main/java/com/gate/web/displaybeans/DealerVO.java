package com.gate.web.displaybeans;

import dao.DealerEntity;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/9/17
 * Time: 上午 11:56
 * To change this template use File | Settings | File Templates.
 */
public class DealerVO extends DealerEntity {
    private String companyName;
    private Integer companyType;
    private String creator;
    private String modifier;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
