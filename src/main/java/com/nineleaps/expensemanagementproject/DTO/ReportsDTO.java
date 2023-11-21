package com.nineleaps.expensemanagementproject.DTO;

import lombok.Getter;

@Getter
public class ReportsDTO {
    private String reportTitle;

    public ReportsDTO() {
    }

    public ReportsDTO(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

}