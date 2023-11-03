package com.nineleaps.expensemanagementproject.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class ReportsTest {

    @Test
    void testConstructor() {
        // Arrange and Act
        Reports actualReports = new Reports();
        LocalDate dateCreated = LocalDate.of(1970, 1, 1);
        actualReports.setDateCreated(dateCreated);
        LocalDate dateSubmitted = LocalDate.of(1970, 1, 1);
        actualReports.setDateSubmitted(dateSubmitted);
        actualReports.setEmployeeId(1L);
        actualReports.setEmployeeMail("Employee Mail");
        actualReports.setEmployeeName("Employee Name");
        actualReports.setExpensesCount(3L);
        LocalDate financeActionDate = LocalDate.of(1970, 1, 1);
        actualReports.setFinanceActionDate(financeActionDate);
        actualReports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        actualReports.setFinanceComments("Finance Comments");
        actualReports.setIsHidden(true);
        actualReports.setIsSubmitted(true);
        LocalDate managerActionDate = LocalDate.of(1970, 1, 1);
        actualReports.setManagerActionDate(managerActionDate);
        actualReports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        actualReports.setManagerComments("Manager Comments");
        actualReports.setManagerEmail("jane.doe@example.org");
        actualReports.setManagerReviewTime("Manager Review Time");
        actualReports.setOfficialEmployeeId("42");
        actualReports.setReportId(1L);
        actualReports.setReportTitle("Dr");
        actualReports.setTotalAmount(10.0f);
        actualReports.setTotalApprovedAmount(10.0f);
        LocalDate actualDateCreated = actualReports.getDateCreated();
        LocalDate actualDateSubmitted = actualReports.getDateSubmitted();
        Long actualEmployeeId = actualReports.getEmployeeId();
        String actualEmployeeMail = actualReports.getEmployeeMail();
        String actualEmployeeName = actualReports.getEmployeeName();
        Long actualExpensesCount = actualReports.getExpensesCount();
        LocalDate actualFinanceActionDate = actualReports.getFinanceActionDate();
        FinanceApprovalStatus actualFinanceApprovalStatus = actualReports.getFinanceApprovalStatus();
        String actualFinanceComments = actualReports.getFinanceComments();
        Boolean actualIsHidden = actualReports.getIsHidden();
        Boolean actualIsSubmitted = actualReports.getIsSubmitted();
        LocalDate actualManagerActionDate = actualReports.getManagerActionDate();
        ManagerApprovalStatus actualManagerApprovalStatus = actualReports.getManagerApprovalStatus();
        String actualManagerComments = actualReports.getManagerComments();
        String actualManagerEmail = actualReports.getManagerEmail();
        String actualManagerReviewTime = actualReports.getManagerReviewTime();
        String actualOfficialEmployeeId = actualReports.getOfficialEmployeeId();
        Long actualReportId = actualReports.getReportId();
        String actualReportTitle = actualReports.getReportTitle();
        float actualTotalAmount = actualReports.getTotalAmount();
        float actualTotalApprovedAmount = actualReports.getTotalApprovedAmount();

        // Assert that nothing has changed
        assertSame(dateCreated, actualDateCreated);
        assertSame(dateSubmitted, actualDateSubmitted);
        assertEquals(1L, actualEmployeeId.longValue());
        assertEquals("Employee Mail", actualEmployeeMail);
        assertEquals("Employee Name", actualEmployeeName);
        assertEquals(3L, actualExpensesCount.longValue());
        assertSame(financeActionDate, actualFinanceActionDate);
        assertEquals(FinanceApprovalStatus.REIMBURSED, actualFinanceApprovalStatus);
        assertEquals("Finance Comments", actualFinanceComments);
        assertTrue(actualIsHidden);
        assertTrue(actualIsSubmitted);
        assertSame(managerActionDate, actualManagerActionDate);
        assertEquals(ManagerApprovalStatus.APPROVED, actualManagerApprovalStatus);
        assertEquals("Manager Comments", actualManagerComments);
        assertEquals("jane.doe@example.org", actualManagerEmail);
        assertEquals("Manager Review Time", actualManagerReviewTime);
        assertEquals("42", actualOfficialEmployeeId);
        assertEquals(1L, actualReportId.longValue());
        assertEquals("Dr", actualReportTitle);
        assertEquals(10.0f, actualTotalAmount);
        assertEquals(10.0f, actualTotalApprovedAmount);
    }

    @Test
    void testConstructor2() {
        // Arrange
        LocalDate dateSubmitted = LocalDate.of(1970, 1, 1);
        LocalDate dateCreated = LocalDate.of(1970, 1, 1);
        LocalDate managerActionDate = LocalDate.of(1970, 1, 1);

        // Act
        Reports actualReports = new Reports(1L, 1L, "Employee Name", "42", "Dr", "Manager Comments", "Finance Comments",
                true, "Employee Mail", dateSubmitted, dateCreated, managerActionDate, LocalDate.of(1970, 1, 1), 10.0f, 10.0f,
                true, "jane.doe@example.org", "Manager Review Time", FinanceApprovalStatus.REIMBURSED,
                ManagerApprovalStatus.APPROVED, 3L);
        LocalDate dateCreated2 = LocalDate.of(1970, 1, 1);
        actualReports.setDateCreated(dateCreated2);
        LocalDate dateSubmitted2 = LocalDate.of(1970, 1, 1);
        actualReports.setDateSubmitted(dateSubmitted2);
        actualReports.setEmployeeId(1L);
        actualReports.setEmployeeMail("Employee Mail");
        actualReports.setEmployeeName("Employee Name");
        actualReports.setExpensesCount(3L);
        LocalDate financeActionDate = LocalDate.of(1970, 1, 1);
        actualReports.setFinanceActionDate(financeActionDate);
        actualReports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        actualReports.setFinanceComments("Finance Comments");
        actualReports.setIsHidden(true);
        actualReports.setIsSubmitted(true);
        LocalDate managerActionDate2 = LocalDate.of(1970, 1, 1);
        actualReports.setManagerActionDate(managerActionDate2);
        actualReports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        actualReports.setManagerComments("Manager Comments");
        actualReports.setManagerEmail("jane.doe@example.org");
        actualReports.setManagerReviewTime("Manager Review Time");
        actualReports.setOfficialEmployeeId("42");
        actualReports.setReportId(1L);
        actualReports.setReportTitle("Dr");
        actualReports.setTotalAmount(10.0f);
        actualReports.setTotalApprovedAmount(10.0f);
        LocalDate actualDateCreated = actualReports.getDateCreated();
        LocalDate actualDateSubmitted = actualReports.getDateSubmitted();
        Long actualEmployeeId = actualReports.getEmployeeId();
        String actualEmployeeMail = actualReports.getEmployeeMail();
        String actualEmployeeName = actualReports.getEmployeeName();
        Long actualExpensesCount = actualReports.getExpensesCount();
        LocalDate actualFinanceActionDate = actualReports.getFinanceActionDate();
        FinanceApprovalStatus actualFinanceApprovalStatus = actualReports.getFinanceApprovalStatus();
        String actualFinanceComments = actualReports.getFinanceComments();
        Boolean actualIsHidden = actualReports.getIsHidden();
        Boolean actualIsSubmitted = actualReports.getIsSubmitted();
        LocalDate actualManagerActionDate = actualReports.getManagerActionDate();
        ManagerApprovalStatus actualManagerApprovalStatus = actualReports.getManagerApprovalStatus();
        String actualManagerComments = actualReports.getManagerComments();
        String actualManagerEmail = actualReports.getManagerEmail();
        String actualManagerReviewTime = actualReports.getManagerReviewTime();
        String actualOfficialEmployeeId = actualReports.getOfficialEmployeeId();
        Long actualReportId = actualReports.getReportId();
        String actualReportTitle = actualReports.getReportTitle();
        float actualTotalAmount = actualReports.getTotalAmount();
        float actualTotalApprovedAmount = actualReports.getTotalApprovedAmount();

        // Assert that nothing has changed
        assertSame(dateCreated2, actualDateCreated);
        assertSame(dateSubmitted2, actualDateSubmitted);
        assertEquals(1L, actualEmployeeId.longValue());
        assertEquals("Employee Mail", actualEmployeeMail);
        assertEquals("Employee Name", actualEmployeeName);
        assertEquals(3L, actualExpensesCount.longValue());
        assertSame(financeActionDate, actualFinanceActionDate);
        assertEquals(FinanceApprovalStatus.REIMBURSED, actualFinanceApprovalStatus);
        assertEquals("Finance Comments", actualFinanceComments);
        assertTrue(actualIsHidden);
        assertTrue(actualIsSubmitted);
        assertSame(managerActionDate2, actualManagerActionDate);
        assertEquals(ManagerApprovalStatus.APPROVED, actualManagerApprovalStatus);
        assertEquals("Manager Comments", actualManagerComments);
        assertEquals("jane.doe@example.org", actualManagerEmail);
        assertEquals("Manager Review Time", actualManagerReviewTime);
        assertEquals("42", actualOfficialEmployeeId);
        assertEquals(1L, actualReportId.longValue());
        assertEquals("Dr", actualReportTitle);
        assertEquals(10.0f, actualTotalAmount);
        assertEquals(10.0f, actualTotalApprovedAmount);
    }
}

