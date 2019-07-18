package com.gate.web.displaybeans;

import com.gateweb.charge.model.Company;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/7/14
 * Time: 上午 10:21
 * To change this template use File | Settings | File Templates.
 */
public class CompanyVO extends Company {
    private String creator;
    private String modifier;

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
