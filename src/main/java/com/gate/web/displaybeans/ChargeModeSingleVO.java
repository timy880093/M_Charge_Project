package com.gate.web.displaybeans;

import com.gateweb.charge.model.ChargeModeSingleEntity;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/9/17
 * Time: 上午 11:56
 * To change this template use File | Settings | File Templates.
 */
public class ChargeModeSingleVO extends ChargeModeSingleEntity{
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
