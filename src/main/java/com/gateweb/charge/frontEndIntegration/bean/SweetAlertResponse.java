package com.gateweb.charge.frontEndIntegration.bean;

import com.gateweb.charge.frontEndIntegration.enumeration.SweetAlertStatus;

public class SweetAlertResponse {
    SweetAlertStatus sweetAlertStatus;
    String title;
    String message;

    public SweetAlertResponse() {
    }

    public SweetAlertResponse(SweetAlertStatus sweetAlertStatus, String title, String message) {
        this.sweetAlertStatus = sweetAlertStatus;
        this.title = title;
        this.message = message;
    }

    public SweetAlertStatus getSweetAlertStatus() {
        return sweetAlertStatus;
    }

    public void setSweetAlertStatus(SweetAlertStatus sweetAlertStatus) {
        this.sweetAlertStatus = sweetAlertStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
