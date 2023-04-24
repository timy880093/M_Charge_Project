package com.gateweb.charge.notice.bean;

public class OBank {
    Boolean oBankAdvert;
    String oBankAdvertUrl;

    public OBank(Boolean oBankAdvert, String oBankAdvertUrl) {
        this.oBankAdvert = oBankAdvert;
        this.oBankAdvertUrl = oBankAdvertUrl;
    }

    public Boolean getoBankAdvert() {
        return oBankAdvert;
    }

    public void setoBankAdvert(Boolean oBankAdvert) {
        this.oBankAdvert = oBankAdvert;
    }

    public String getoBankAdvertUrl() {
        return oBankAdvertUrl;
    }

    public void setoBankAdvertUrl(String oBankAdvertUrl) {
        this.oBankAdvertUrl = oBankAdvertUrl;
    }
}
