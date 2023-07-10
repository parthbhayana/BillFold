package com.nineleaps.expensemanagementproject.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReportsTest {

    @Test
    void getReportId() {
        Reports reports = new Reports();
        reports.setReportId(1L);
        assertEquals(1L, reports.getReportId());
    }

    @Test
    void setReportId() {
        Reports reports = new Reports();
        reports.setReportId(1L);
        assertEquals(1L, reports.getReportId());
    }

    @Test
    void getEmployeeId() {
        Reports reports = new Reports();
        reports.setEmployeeId(1L);
        assertEquals(1L, reports.getEmployeeId());
    }

    @Test
    void setEmployeeId() {
        Reports reports = new Reports();
        reports.setEmployeeId(1L);
        assertEquals(1L, reports.getEmployeeId());
    }

    @Test
    void getEmployeeName() {
        Reports reports = new Reports();
        reports.setEmployeeName("John Doe");
        assertEquals("John Doe", reports.getEmployeeName());
    }

    @Test
    void setEmployeeName() {
        Reports reports = new Reports();
        reports.setEmployeeName("John Doe");
        assertEquals("John Doe", reports.getEmployeeName());
    }

    @Test
    void getOfficialEmployeeId() {
        Reports reports = new Reports();
        reports.setOfficialEmployeeId("E123");
        assertEquals("E123", reports.getOfficialEmployeeId());
    }

    @Test
    void setOfficialEmployeeId() {
        Reports reports = new Reports();
        reports.setOfficialEmployeeId("E123");
        assertEquals("E123", reports.getOfficialEmployeeId());
    }

    @Test
    void getReportTitle() {
        Reports reports = new Reports();
        reports.setReportTitle("Expense Report");
        assertEquals("Expense Report", reports.getReportTitle());
    }

    @Test
    void setReportTitle() {
        Reports reports = new Reports();
        reports.setReportTitle("Expense Report");
        assertEquals("Expense Report", reports.getReportTitle());
    }

    @Test
    void getReportDescription() {
        Reports reports = new Reports();
        reports.setReportDescription("Expense Report Description");
        assertEquals("Expense Report Description", reports.getReportDescription());
    }

    @Test
    void setReportDescription() {
        Reports reports = new Reports();
        reports.setReportDescription("Expense Report Description");
        assertEquals("Expense Report Description", reports.getReportDescription());
    }

    @Test
    void getManagerComments() {
        Reports reports = new Reports();
        reports.setManagerComments("Manager Comments");
        assertEquals("Manager Comments", reports.getManagerComments());
    }

    @Test
    void setManagerComments() {
        Reports reports = new Reports();
        reports.setManagerComments("Manager Comments");
        assertEquals("Manager Comments", reports.getManagerComments());
    }

    @Test
    void getFinanceComments() {
        Reports reports = new Reports();
        reports.setFinanceComments("Finance Comments");
        assertEquals("Finance Comments", reports.getFinanceComments());
    }

    @Test
    void setFinanceComments() {
        Reports reports = new Reports();
        reports.setFinanceComments("Finance Comments");
        assertEquals("Finance Comments", reports.getFinanceComments());
    }

    @Test
    void getIsSubmitted() {
        Reports reports = new Reports();
        reports.setIsSubmitted(true);
        assertTrue(reports.getIsSubmitted());
    }

    @Test
    void setIsSubmitted() {
        Reports reports = new Reports();
        reports.setIsSubmitted(true);
        assertTrue(reports.getIsSubmitted());
    }

    @Test
    void getEmployeeMail() {
        Reports reports = new Reports();
        reports.setEmployeeMail("john.doe@example.com");
        assertEquals("john.doe@example.com", reports.getEmployeeMail());
    }

    @Test
    void setEmployeeMail() {
        Reports reports = new Reports();
        reports.setEmployeeMail("john.doe@example.com");
        assertEquals("john.doe@example.com", reports.getEmployeeMail());
    }

    @Test
    void getDateSubmitted() {
        Reports reports = new Reports();
        LocalDate dateSubmitted = LocalDate.now();
        reports.setDateSubmitted(dateSubmitted);
        assertEquals(dateSubmitted, reports.getDateSubmitted());
    }

    @Test
    void setDateSubmitted() {
        Reports reports = new Reports();
        LocalDate dateSubmitted = LocalDate.now();
        reports.setDateSubmitted(dateSubmitted);
        assertEquals(dateSubmitted, reports.getDateSubmitted());
    }

    @Test
    void getDateCreated() {
        Reports reports = new Reports();
        LocalDate dateCreated = LocalDate.now();
        reports.setDateCreated(dateCreated);
        assertEquals(dateCreated, reports.getDateCreated());
    }

    @Test
    void setDateCreated() {
        Reports reports = new Reports();
        LocalDate dateCreated = LocalDate.now();
        reports.setDateCreated(dateCreated);
        assertEquals(dateCreated, reports.getDateCreated());
    }

    @Test
    void getManagerActionDate() {
        Reports reports = new Reports();
        LocalDate managerActionDate = LocalDate.now();
        reports.setManagerActionDate(managerActionDate);
        assertEquals(managerActionDate, reports.getManagerActionDate());
    }

    @Test
    void setManagerActionDate() {
        Reports reports = new Reports();
        LocalDate managerActionDate = LocalDate.now();
        reports.setManagerActionDate(managerActionDate);
        assertEquals(managerActionDate, reports.getManagerActionDate());
    }

    @Test
    void getFinanceActionDate() {
        Reports reports = new Reports();
        LocalDate financeActionDate = LocalDate.now();
        reports.setFinanceActionDate(financeActionDate);
        assertEquals(financeActionDate, reports.getFinanceActionDate());
    }

    @Test
    void setFinanceActionDate() {
        Reports reports = new Reports();
        LocalDate financeActionDate = LocalDate.now();
        reports.setFinanceActionDate(financeActionDate);
        assertEquals(financeActionDate, reports.getFinanceActionDate());
    }

    @Test
    void getCurrency() {
        Reports reports = new Reports();
        reports.setCurrency("USD");
        assertEquals("USD", reports.getCurrency());
    }

    @Test
    void setCurrency() {
        Reports reports = new Reports();
        reports.setCurrency("USD");
        assertEquals("USD", reports.getCurrency());
    }

    @Test
    void getTotalAmountINR() {
        Reports reports = new Reports();
        reports.setTotalAmountINR(1000.0f);
        assertEquals(1000.0f, reports.getTotalAmountINR(), 0.01);
    }

    @Test
    void setTotalAmountINR() {
        Reports reports = new Reports();
        reports.setTotalAmountINR(1000.0f);
        assertEquals(1000.0f, reports.getTotalAmountINR(), 0.01);
    }

    @Test
    void getTotalApprovedAmountINR() {
        Reports reports = new Reports();
        reports.setTotalApprovedAmountINR(800.0f);
        assertEquals(800.0f, reports.getTotalApprovedAmountINR(), 0.01);
    }

    @Test
    void setTotalApprovedAmountCurrency() {
        Reports reports = new Reports();
        reports.setTotalApprovedAmountCurrency(80.0f);
        assertEquals(80.0f, reports.getTotalApprovedAmountCurrency(), 0.01);
    }

    @Test
    void getIsHidden() {
        Reports reports = new Reports();
        reports.setIsHidden(true);
        assertTrue(reports.getIsHidden());
    }

    @Test
    void setIsHidden() {
        Reports reports = new Reports();
        reports.setIsHidden(true);
        assertTrue(reports.getIsHidden());
    }

    @Test
    void getManagerEmail() {
        Reports reports = new Reports();
        reports.setManagerEmail("manager@example.com");
        assertEquals("manager@example.com", reports.getManagerEmail());
    }

    @Test
    void setManagerEmail() {
        Reports reports = new Reports();
        reports.setManagerEmail("manager@example.com");
        assertEquals("manager@example.com", reports.getManagerEmail());
    }

    @Test
    void getManagerReviewTime() {
        Reports reports = new Reports();
        reports.setManagerReviewTime("2 hours");
        assertEquals("2 hours", reports.getManagerReviewTime());
    }

    @Test
    void setManagerReviewTime() {
        Reports reports = new Reports();
        reports.setManagerReviewTime("2 hours");
        assertEquals("2 hours", reports.getManagerReviewTime());
    }

    @Test
    void getFinanceapprovalstatus() {
        Reports reports = new Reports();
        reports.setFinanceapprovalstatus(FinanceApprovalStatus.APPROVED);
        assertEquals(FinanceApprovalStatus.APPROVED, reports.getFinanceapprovalstatus());
    }

    @Test
    void setFinanceapprovalstatus() {
        Reports reports = new Reports();
        reports.setFinanceapprovalstatus(FinanceApprovalStatus.APPROVED);
        assertEquals(FinanceApprovalStatus.APPROVED, reports.getFinanceapprovalstatus());
    }

    @Test
    void getManagerapprovalstatus() {
        Reports reports = new Reports();
        reports.setManagerapprovalstatus(ManagerApprovalStatus.APPROVED);
        assertEquals(ManagerApprovalStatus.APPROVED, reports.getManagerapprovalstatus());
    }

    @Test
    void setManagerapprovalstatus() {
        Reports reports = new Reports();
        reports.setManagerapprovalstatus(ManagerApprovalStatus.APPROVED);
        assertEquals(ManagerApprovalStatus.APPROVED, reports.getManagerapprovalstatus());
    }
}