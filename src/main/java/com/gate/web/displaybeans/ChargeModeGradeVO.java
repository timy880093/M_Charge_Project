package com.gate.web.displaybeans;


import com.gateweb.charge.model.ChargeModeGradeEntity;

/**
 * Created by emily on 2017/5/11.
 */
public class ChargeModeGradeVO extends ChargeModeGradeEntity{
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
