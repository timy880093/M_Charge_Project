package com.gate.web.displaybeans;

import dao.WarrantyEntity;


public class WarrantyVO extends WarrantyEntity {
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
