package com.nineleaps.expensemanagementproject.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class ExpenseTest {

    @Test
    void testConstructor() throws UnsupportedEncodingException {
        // Arrange and Act
        Expense actualExpense = new Expense();
        actualExpense.setAmount(10.0d);
        actualExpense.setAmountApproved(10.0d);
        Category category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);
        actualExpense.setCategory(category);
        actualExpense.setCategoryDescription("Category Description");
        LocalDate date = LocalDate.of(1970, 1, 1);
        actualExpense.setDate(date);
        LocalDateTime dateCreated = LocalDate.of(1970, 1, 1).atStartOfDay();
        actualExpense.setDateCreated(dateCreated);
        actualExpense.setDescription("The characteristics of someone or something");
        Employee employee = new Employee();
        employee.setEmployeeEmail("jane.doe@example.org");
        employee.setEmployeeId(1L);
        employee.setExpenseList(new ArrayList<>());
        employee.setFirstName("Jane");
        employee.setHrEmail("jane.doe@example.org");
        employee.setHrName("Hr Name");
        employee.setImageUrl("https://example.org/example");
        employee.setIsFinanceAdmin(true);
        employee.setIsHidden(true);
        employee.setLastName("Doe");
        employee.setLndEmail("jane.doe@example.org");
        employee.setLndName("Lnd Name");
        employee.setManagerEmail("jane.doe@example.org");
        employee.setManagerName("Manager Name");
        employee.setMiddleName("Middle Name");
        employee.setMobileNumber(1L);
        employee.setOfficialEmployeeId("42");
        employee.setRole("Role");
        employee.setToken("ABC123");
        actualExpense.setEmployee(employee);
        actualExpense.setExpenseId(1L);
        byte[] file = "AXAXAXAX".getBytes("UTF-8");
        actualExpense.setFile(file);
        actualExpense.setFileName("foo.txt");
        actualExpense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        actualExpense.setIsHidden(true);
        actualExpense.setIsReported(true);
        actualExpense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        actualExpense.setMerchantName("Merchant Name");
        actualExpense.setPotentialDuplicate(true);
        actualExpense.setReportTitle("Dr");
        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);
        actualExpense.setReports(reports);
        Double actualAmount = actualExpense.getAmount();
        Double actualAmountApproved = actualExpense.getAmountApproved();
        Category actualCategory = actualExpense.getCategory();
        String actualCategoryDescription = actualExpense.getCategoryDescription();
        LocalDate actualDate = actualExpense.getDate();
        LocalDateTime actualDateCreated = actualExpense.getDateCreated();
        String actualDescription = actualExpense.getDescription();
        Employee actualEmployee = actualExpense.getEmployee();
        Long actualExpenseId = actualExpense.getExpenseId();
        byte[] actualFile = actualExpense.getFile();
        String actualFileName = actualExpense.getFileName();
        FinanceApprovalStatus actualFinanceApprovalStatus = actualExpense.getFinanceApprovalStatus();
        Boolean actualIsHidden = actualExpense.getIsHidden();
        Boolean actualIsReported = actualExpense.getIsReported();
        ManagerApprovalStatusExpense actualManagerApprovalStatusExpense = actualExpense.getManagerApprovalStatusExpense();
        String actualMerchantName = actualExpense.getMerchantName();
        Boolean actualPotentialDuplicate = actualExpense.getPotentialDuplicate();
        String actualReportTitle = actualExpense.getReportTitle();
        Reports actualReports = actualExpense.getReports();

        // Assert that nothing has changed
        assertEquals(10.0d, actualAmount.doubleValue());
        assertEquals(10.0d, actualAmountApproved.doubleValue());
        assertSame(category, actualCategory);
        assertEquals("Category Description", actualCategoryDescription);
        assertSame(date, actualDate);
        assertSame(dateCreated, actualDateCreated);
        assertEquals("The characteristics of someone or something", actualDescription);
        assertSame(employee, actualEmployee);
        assertEquals(1L, actualExpenseId.longValue());
        assertSame(file, actualFile);
        assertEquals("foo.txt", actualFileName);
        assertEquals(FinanceApprovalStatus.REIMBURSED, actualFinanceApprovalStatus);
        assertTrue(actualIsHidden);
        assertTrue(actualIsReported);
        assertEquals(ManagerApprovalStatusExpense.APPROVED, actualManagerApprovalStatusExpense);
        assertEquals("Merchant Name", actualMerchantName);
        assertTrue(actualPotentialDuplicate);
        assertEquals("Dr", actualReportTitle);
        assertSame(reports, actualReports);
    }


    @Test
    void testConstructor2() throws UnsupportedEncodingException {
        // Arrange
        LocalDate date = LocalDate.of(1970, 1, 1);
        LocalDateTime dateCreated = LocalDate.of(1970, 1, 1).atStartOfDay();
        byte[] file = "AXAXAXAX".getBytes("UTF-8");

        Employee employee = new Employee();
        employee.setEmployeeEmail("jane.doe@example.org");
        employee.setEmployeeId(1L);
        employee.setExpenseList(new ArrayList<>());
        employee.setFirstName("Jane");
        employee.setHrEmail("jane.doe@example.org");
        employee.setHrName("Hr Name");
        employee.setImageUrl("https://example.org/example");
        employee.setIsFinanceAdmin(true);
        employee.setIsHidden(true);
        employee.setLastName("Doe");
        employee.setLndEmail("jane.doe@example.org");
        employee.setLndName("Lnd Name");
        employee.setManagerEmail("jane.doe@example.org");
        employee.setManagerName("Manager Name");
        employee.setMiddleName("Middle Name");
        employee.setMobileNumber(1L);
        employee.setOfficialEmployeeId("42");
        employee.setRole("Role");
        employee.setToken("ABC123");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Category category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);

        // Act
        Expense actualExpense = new Expense(1L, "Merchant Name", date, dateCreated, 10.0d,
                "The characteristics of someone or something", "Category Description", true, true, "Dr", 10.0d,
                FinanceApprovalStatus.REIMBURSED, ManagerApprovalStatusExpense.APPROVED, true, file, "foo.txt", employee,
                reports, category);
        actualExpense.setAmount(10.0d);
        actualExpense.setAmountApproved(10.0d);
        Category category2 = new Category();
        category2.setCategoryDescription("Category Description");
        category2.setCategoryId(1L);
        category2.setCategoryTotal(1L);
        category2.setExpenseList(new ArrayList<>());
        category2.setIsHidden(true);
        actualExpense.setCategory(category2);
        actualExpense.setCategoryDescription("Category Description");
        LocalDate date2 = LocalDate.of(1970, 1, 1);
        actualExpense.setDate(date2);
        LocalDateTime dateCreated2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        actualExpense.setDateCreated(dateCreated2);
        actualExpense.setDescription("The characteristics of someone or something");
        Employee employee2 = new Employee();
        employee2.setEmployeeEmail("jane.doe@example.org");
        employee2.setEmployeeId(1L);
        employee2.setExpenseList(new ArrayList<>());
        employee2.setFirstName("Jane");
        employee2.setHrEmail("jane.doe@example.org");
        employee2.setHrName("Hr Name");
        employee2.setImageUrl("https://example.org/example");
        employee2.setIsFinanceAdmin(true);
        employee2.setIsHidden(true);
        employee2.setLastName("Doe");
        employee2.setLndEmail("jane.doe@example.org");
        employee2.setLndName("Lnd Name");
        employee2.setManagerEmail("jane.doe@example.org");
        employee2.setManagerName("Manager Name");
        employee2.setMiddleName("Middle Name");
        employee2.setMobileNumber(1L);
        employee2.setOfficialEmployeeId("42");
        employee2.setRole("Role");
        employee2.setToken("ABC123");
        actualExpense.setEmployee(employee2);
        actualExpense.setExpenseId(1L);
        byte[] file2 = "AXAXAXAX".getBytes("UTF-8");
        actualExpense.setFile(file2);
        actualExpense.setFileName("foo.txt");
        actualExpense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        actualExpense.setIsHidden(true);
        actualExpense.setIsReported(true);
        actualExpense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        actualExpense.setMerchantName("Merchant Name");
        actualExpense.setPotentialDuplicate(true);
        actualExpense.setReportTitle("Dr");
        Reports reports2 = new Reports();
        reports2.setDateCreated(LocalDate.of(1970, 1, 1));
        reports2.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports2.setEmployeeId(1L);
        reports2.setEmployeeMail("Employee Mail");
        reports2.setEmployeeName("Employee Name");
        reports2.setExpensesCount(3L);
        reports2.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports2.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports2.setFinanceComments("Finance Comments");
        reports2.setIsHidden(true);
        reports2.setIsSubmitted(true);
        reports2.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports2.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports2.setManagerComments("Manager Comments");
        reports2.setManagerEmail("jane.doe@example.org");
        reports2.setManagerReviewTime("Manager Review Time");
        reports2.setOfficialEmployeeId("42");
        reports2.setReportId(1L);
        reports2.setReportTitle("Dr");
        reports2.setTotalAmount(10.0f);
        reports2.setTotalApprovedAmount(10.0f);
        actualExpense.setReports(reports2);
        Double actualAmount = actualExpense.getAmount();
        Double actualAmountApproved = actualExpense.getAmountApproved();
        Category actualCategory = actualExpense.getCategory();
        String actualCategoryDescription = actualExpense.getCategoryDescription();
        LocalDate actualDate = actualExpense.getDate();
        LocalDateTime actualDateCreated = actualExpense.getDateCreated();
        String actualDescription = actualExpense.getDescription();
        Employee actualEmployee = actualExpense.getEmployee();
        Long actualExpenseId = actualExpense.getExpenseId();
        byte[] actualFile = actualExpense.getFile();
        String actualFileName = actualExpense.getFileName();
        FinanceApprovalStatus actualFinanceApprovalStatus = actualExpense.getFinanceApprovalStatus();
        Boolean actualIsHidden = actualExpense.getIsHidden();
        Boolean actualIsReported = actualExpense.getIsReported();
        ManagerApprovalStatusExpense actualManagerApprovalStatusExpense = actualExpense.getManagerApprovalStatusExpense();
        String actualMerchantName = actualExpense.getMerchantName();
        Boolean actualPotentialDuplicate = actualExpense.getPotentialDuplicate();
        String actualReportTitle = actualExpense.getReportTitle();
        Reports actualReports = actualExpense.getReports();

        // Assert that nothing has changed
        assertEquals(10.0d, actualAmount.doubleValue());
        assertEquals(10.0d, actualAmountApproved.doubleValue());
        assertSame(category2, actualCategory);
        assertEquals("Category Description", actualCategoryDescription);
        assertSame(date2, actualDate);
        assertSame(dateCreated2, actualDateCreated);
        assertEquals("The characteristics of someone or something", actualDescription);
        assertSame(employee2, actualEmployee);
        assertEquals(1L, actualExpenseId.longValue());
        assertSame(file2, actualFile);
        assertEquals("foo.txt", actualFileName);
        assertEquals(FinanceApprovalStatus.REIMBURSED, actualFinanceApprovalStatus);
        assertTrue(actualIsHidden);
        assertTrue(actualIsReported);
        assertEquals(ManagerApprovalStatusExpense.APPROVED, actualManagerApprovalStatusExpense);
        assertEquals("Merchant Name", actualMerchantName);
        assertTrue(actualPotentialDuplicate);
        assertEquals("Dr", actualReportTitle);
        assertSame(reports2, actualReports);
    }
}

