package com.nineleaps.expense_management_project.dto;

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