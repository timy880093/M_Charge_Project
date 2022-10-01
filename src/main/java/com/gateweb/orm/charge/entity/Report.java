/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import com.gateweb.charge.enumeration.ReportType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(name = "report", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "report_pkey", columnNames = {"report_id"})
})
public class Report implements Serializable {

    private static final long serialVersionUID = 2122356404;

    private String reportId;
    private ReportType reportType;
    private LocalDateTime createDate;

    public Report() {
    }

    public Report(Report value) {
        this.reportId = value.reportId;
        this.reportType = value.reportType;
        this.createDate = value.createDate;
    }

    public Report(
            String reportId,
            ReportType reportType,
            LocalDateTime createDate
    ) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.createDate = createDate;
    }

    @Id
    @Column(name = "report_id", nullable = false, length = 36)
    @NotNull
    @Size(max = 36)
    public String getReportId() {
        return this.reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type")
    public ReportType getReportType() {
        return this.reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    @Column(name = "create_date")
    public LocalDateTime getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Report (");

        sb.append(reportId);
        sb.append(", ").append(reportType);
        sb.append(", ").append(createDate);

        sb.append(")");
        return sb.toString();
    }
}