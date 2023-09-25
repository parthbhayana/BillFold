package com.nineleaps.expense_management_project.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportsTest {

    @InjectMocks
    private Reports reports;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetReportId() {
        Long reportId = 1L;
        reports.setReportId(reportId);
        assertEquals(reportId, reports.getReportId());
    }

    @Test
    void testGetEmployeeId() {
        Long employeeId = 2L;
        reports.setEmployeeId(employeeId);
        assertEquals(employeeId, reports.getEmployeeId());
    }

    @Test
    void testGetEmployeeName() {
        String employeeName = "John Doe";
        reports.setEmployeeName(employeeName);
        assertEquals(employeeName, reports.getEmployeeName());
    }

    @Test
    void testGetOfficialEmployeeId() {
        String officialEmployeeId = "EMP001";
        reports.setOfficialEmployeeId(officialEmployeeId);
        assertEquals(officialEmployeeId, reports.getOfficialEmployeeId());
    }

    @Test
    void testGetReportTitle() {
        String reportTitle = "Expense Report";
        reports.setReportTitle(reportTitle);
        assertEquals(reportTitle, reports.getReportTitle());
    }

    @Test
    void testGetManagerComments() {
        String managerComments = "Manager's Comments";
        reports.setManagerComments(managerComments);
        assertEquals(managerComments, reports.getManagerComments());
    }

    @Test
    void testGetFinanceComments() {
        String financeComments = "Finance Comments";
        reports.setFinanceComments(financeComments);
        assertEquals(financeComments, reports.getFinanceComments());
    }

    @Test
    void testGetIsSubmitted() {
        Boolean isSubmitted = true;
        reports.setIsSubmitted(isSubmitted);
        assertEquals(isSubmitted, reports.getIsSubmitted());
    }

    @Test
    void testGetEmployeeMail() {
        String employeeMail = "john.doe@example.com";
        reports.setEmployeeMail(employeeMail);
        assertEquals(employeeMail, reports.getEmployeeMail());
    }

    @Test
    void testGetDateSubmitted() {
        LocalDate dateSubmitted = LocalDate.of(2023, 9, 21);
        reports.setDateSubmitted(dateSubmitted);
        assertEquals(dateSubmitted, reports.getDateSubmitted());
    }

    @Test
    void testGetDateCreated() {
        LocalDate dateCreated = LocalDate.of(2023, 9, 20);
        reports.setDateCreated(dateCreated);
        assertEquals(dateCreated, reports.getDateCreated());
    }

    @Test
    void testGetManagerActionDate() {
        LocalDate managerActionDate = LocalDate.of(2023, 9, 22);
        reports.setManagerActionDate(managerActionDate);
        assertEquals(managerActionDate, reports.getManagerActionDate());
    }

    @Test
    void testGetFinanceActionDate() {
        LocalDate financeActionDate = LocalDate.of(2023, 9, 23);
        reports.setFinanceActionDate(financeActionDate);
        assertEquals(financeActionDate, reports.getFinanceActionDate());
    }

    @Test
    void testGetTotalAmount() {
        float totalAmount = 1000.0f;
        reports.setTotalAmount(totalAmount);
        assertEquals(totalAmount, reports.getTotalAmount());
    }

    @Test
    void testGetTotalApprovedAmount() {
        float totalApprovedAmount = 900.0f;
        reports.setTotalApprovedAmount(totalApprovedAmount);
        assertEquals(totalApprovedAmount, reports.getTotalApprovedAmount());
    }

    @Test
    void testGetIsHidden() {
        Boolean isHidden = true;
        reports.setIsHidden(isHidden);
        assertEquals(isHidden, reports.getIsHidden());
    }

    @Test
    void testGetManagerEmail() {
        String managerEmail = "manager@example.com";
        reports.setManagerEmail(managerEmail);
        assertEquals(managerEmail, reports.getManagerEmail());
    }

    @Test
    void testGetManagerReviewTime() {
        String managerReviewTime = "10:30 AM";
        reports.setManagerReviewTime(managerReviewTime);
        assertEquals(managerReviewTime, reports.getManagerReviewTime());
    }

    @Test
    void testGetFinanceApprovalStatus() {
        FinanceApprovalStatus financeApprovalStatus = FinanceApprovalStatus.APPROVED;
        reports.setFinanceApprovalStatus(financeApprovalStatus);
        assertEquals(financeApprovalStatus, reports.getFinanceApprovalStatus());
    }

    @Test
    void testGetManagerApprovalStatus() {
        ManagerApprovalStatus managerApprovalStatus = ManagerApprovalStatus.APPROVED;
        reports.setManagerApprovalStatus(managerApprovalStatus);
        assertEquals(managerApprovalStatus, reports.getManagerApprovalStatus());
    }

    @Test
    void testGetExpensesCount() {
        Long expensesCount = 5L;
        reports.setExpensesCount(expensesCount);
        assertEquals(expensesCount, reports.getExpensesCount());
    }

    @Test
    public void testReportsConstructor() {
        // Arrange
        Long reportId = 1L;
        Long employeeId = 123L;
        String employeeName = "John Doe";
        String officialEmployeeId = "EMP001";
        String reportTitle = "Expense Report";
        String managerComments = "Manager's comments";
        String financeComments = "Finance's comments";

        // Act
        Reports reports = new Reports(
                reportId, employeeId, employeeName, officialEmployeeId, reportTitle, managerComments, financeComments);

        // Assert
        assertNotNull(reports);
        assertEquals(reportId, reports.getReportId());
        assertEquals(employeeId, reports.getEmployeeId());
        assertEquals(employeeName, reports.getEmployeeName());
        assertEquals(officialEmployeeId, reports.getOfficialEmployeeId());
        assertEquals(reportTitle, reports.getReportTitle());
        assertEquals(managerComments, reports.getManagerComments());
        assertEquals(financeComments, reports.getFinanceComments());
    }

    @Test
    public void testReportsAdditional1Constructor() {
        // Arrange
        Boolean isSubmitted = true;
        String employeeMail = "john.doe@example.com";
        LocalDate dateSubmitted = LocalDate.of(2023, 9, 15);
        LocalDate dateCreated = LocalDate.of(2023, 9, 10);
        LocalDate managerActionDate = LocalDate.of(2023, 9, 12);
        LocalDate financeActionDate = LocalDate.of(2023, 9, 14);
        float totalAmount = 1000.0f;

        // Act
        Reports reports = new Reports(
                isSubmitted, employeeMail, dateSubmitted, dateCreated,
                managerActionDate, financeActionDate, totalAmount);

        // Assert
        assertNotNull(reports);
        assertEquals(isSubmitted, reports.getIsSubmitted());
        assertEquals(employeeMail, reports.getEmployeeMail());
        assertEquals(dateSubmitted, reports.getDateSubmitted());
        assertEquals(dateCreated, reports.getDateCreated());
        assertEquals(managerActionDate, reports.getManagerActionDate());
        assertEquals(financeActionDate, reports.getFinanceActionDate());
        assertEquals(totalAmount, reports.getTotalAmount());
    }


    @Test
    public void testReportsAdiitional2Constructor() {
        // Arrange
        Boolean isSubmitted = true;
        String employeeMail = "john.doe@example.com";
        LocalDate dateSubmitted = LocalDate.of(2023, 9, 15);
        LocalDate dateCreated = LocalDate.of(2023, 9, 10);
        LocalDate managerActionDate = LocalDate.of(2023, 9, 12);
        LocalDate financeActionDate = LocalDate.of(2023, 9, 14);
        float totalAmount = 1000.0f;

        // Act
        Reports reports = new Reports(
                isSubmitted, employeeMail, dateSubmitted, dateCreated,
                managerActionDate, financeActionDate, totalAmount);

        // Assert
        assertNotNull(reports);
        assertEquals(isSubmitted, reports.getIsSubmitted());
        assertEquals(employeeMail, reports.getEmployeeMail());
        assertEquals(dateSubmitted, reports.getDateSubmitted());
        assertEquals(dateCreated, reports.getDateCreated());
        assertEquals(managerActionDate, reports.getManagerActionDate());
        assertEquals(financeActionDate, reports.getFinanceActionDate());
        assertEquals(totalAmount, reports.getTotalAmount());
    }

}
