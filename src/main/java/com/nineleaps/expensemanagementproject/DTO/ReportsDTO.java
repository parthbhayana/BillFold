package com.nineleaps.expensemanagementproject.DTO;

public class ReportsDTO {
    private String reportTitle;
    private String reportDescription;

    public ReportsDTO() {
    }

    public ReportsDTO(String reportTitle, String reportDescription) {
        this.reportTitle = reportTitle;
        this.reportDescription = reportDescription;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }
}
