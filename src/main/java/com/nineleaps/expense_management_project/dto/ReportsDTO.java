package com.nineleaps.expense_management_project.dto;

public class ReportsDTO {
    private String reportTitle;

    public ReportsDTO() {
    }

    public ReportsDTO(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

}