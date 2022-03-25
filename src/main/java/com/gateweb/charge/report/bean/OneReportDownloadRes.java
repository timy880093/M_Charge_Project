package com.gateweb.charge.report.bean;

import com.gateweb.charge.frontEndIntegration.bean.SweetAlertResponse;
import com.gateweb.charge.frontEndIntegration.enumeration.SweetAlertStatus;

public class OneReportDownloadRes extends SweetAlertResponse {
    String reportBody;

    public OneReportDownloadRes() {
    }

    public OneReportDownloadRes(String reportBody) {
        this.reportBody = reportBody;
    }

    public OneReportDownloadRes(SweetAlertStatus sweetAlertStatus, String title, String message, String reportBody) {
        super(sweetAlertStatus, title, message);
        this.reportBody = reportBody;
    }

    public String getReportBody() {
        return reportBody;
    }

    public void setReportBody(String reportBody) {
        this.reportBody = reportBody;
    }

}
