package com.nineleaps.expense_management_project.dto;

// A Data Transfer Object (DTO) class for representing report information
public class ReportsDTO {
    private String reportTitle; // Report title

    // Default constructor
    public ReportsDTO() {
    }

    // Parameterized constructor to initialize report title
    public ReportsDTO(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    // Getter method to retrieve the report title
    public String getReportTitle() {
        return reportTitle;
    }

    // Setter method to set the report title
    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }
}
