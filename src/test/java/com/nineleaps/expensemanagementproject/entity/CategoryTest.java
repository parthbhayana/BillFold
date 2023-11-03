package com.nineleaps.expensemanagementproject.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void testConstructor() {
        // Arrange and Act
        Category actualCategory = new Category();
        actualCategory.setCategoryDescription("Category Description");
        actualCategory.setCategoryId(1L);
        actualCategory.setCategoryTotal(1L);
        ArrayList<Expense> expenseList = new ArrayList<>();
        actualCategory.setExpenseList(expenseList);
        actualCategory.setIsHidden(true);

        // Assert
        assertEquals("Category Description", actualCategory.getCategoryDescription());
        assertEquals(1L, actualCategory.getCategoryId().longValue());
        assertEquals(1L, actualCategory.getCategoryTotal());
        assertSame(expenseList, actualCategory.getExpenseList());
        assertTrue(actualCategory.getIsHidden());
    }


    @Test
    void testConstructor2() {
        // Arrange and Act
        Category actualCategory = new Category(1L, "Category Description", 1L, true, new ArrayList<>());

        // Assert
        assertEquals("Category Description", actualCategory.getCategoryDescription());
        assertTrue(actualCategory.getIsHidden());
        assertTrue(actualCategory.getExpenseList().isEmpty());
        assertEquals(1L, actualCategory.getCategoryTotal());
        assertEquals(1L, actualCategory.getCategoryId().longValue());
    }

    @Test
    void testConstructor3() {
        // Arrange
        Category category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);

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

        Expense expense = new Expense();
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("Category Description");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("The characteristics of someone or something");
        expense.setEmployee(employee);
        expense.setExpenseId(1L);
        expense.setFile("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);

        // Act
        Category actualCategory = new Category(1L, "Category Description", 1L, true, expenseList);

        // Assert
        assertEquals("Category Description", actualCategory.getCategoryDescription());
        assertTrue(actualCategory.getIsHidden());
        assertEquals(1, actualCategory.getExpenseList().size());
        assertEquals(1L, actualCategory.getCategoryTotal());
        assertEquals(1L, actualCategory.getCategoryId().longValue());
    }

    @Test
    void testConstructor4() {
        // Arrange
        Category category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);

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

        Expense expense = new Expense();
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("Category Description");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("The characteristics of someone or something");
        expense.setEmployee(employee);
        expense.setExpenseId(1L);
        expense.setFile("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        Category category2 = new Category();
        category2.setCategoryDescription("42");
        category2.setCategoryId(2L);
        category2.setCategoryTotal(0L);
        category2.setExpenseList(new ArrayList<>());
        category2.setIsHidden(false);

        Employee employee2 = new Employee();
        employee2.setEmployeeEmail("john.smith@example.org");
        employee2.setEmployeeId(2L);
        employee2.setExpenseList(new ArrayList<>());
        employee2.setFirstName("John");
        employee2.setHrEmail("john.smith@example.org");
        employee2.setHrName("EMPLOYEE");
        employee2.setImageUrl("Image Url");
        employee2.setIsFinanceAdmin(false);
        employee2.setIsHidden(false);
        employee2.setLastName("Smith");
        employee2.setLndEmail("john.smith@example.org");
        employee2.setLndName("EMPLOYEE");
        employee2.setManagerEmail("john.smith@example.org");
        employee2.setManagerName("EMPLOYEE");
        employee2.setMiddleName("EMPLOYEE");
        employee2.setMobileNumber(0L);
        employee2.setOfficialEmployeeId("Official Employee Id");
        employee2.setRole("EMPLOYEE");
        employee2.setToken("Token");

        Reports reports2 = new Reports();
        reports2.setDateCreated(LocalDate.of(1970, 1, 1));
        reports2.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports2.setEmployeeId(2L);
        reports2.setEmployeeMail("42");
        reports2.setEmployeeName("42");
        reports2.setExpensesCount(1L);
        reports2.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports2.setFinanceApprovalStatus(FinanceApprovalStatus.REJECTED);
        reports2.setFinanceComments("42");
        reports2.setIsHidden(false);
        reports2.setIsSubmitted(false);
        reports2.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports2.setManagerApprovalStatus(ManagerApprovalStatus.REJECTED);
        reports2.setManagerComments("42");
        reports2.setManagerEmail("john.smith@example.org");
        reports2.setManagerReviewTime("42");
        reports2.setOfficialEmployeeId("Official Employee Id");
        reports2.setReportId(2L);
        reports2.setReportTitle("Mr");
        reports2.setTotalAmount(0.5f);
        reports2.setTotalApprovedAmount(0.5f);

        Expense expense2 = new Expense();
        expense2.setAmount(0.5d);
        expense2.setAmountApproved(0.5d);
        expense2.setCategory(category2);
        expense2.setCategoryDescription("42");
        expense2.setDate(LocalDate.of(1970, 1, 1));
        expense2.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense2.setDescription("Description");
        expense2.setEmployee(employee2);
        expense2.setExpenseId(2L);
        expense2.setFile("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
        expense2.setFileName("File Name");
        expense2.setFinanceApprovalStatus(FinanceApprovalStatus.REJECTED);
        expense2.setIsHidden(false);
        expense2.setIsReported(false);
        expense2.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.REJECTED);
        expense2.setMerchantName("42");
        expense2.setPotentialDuplicate(false);
        expense2.setReportTitle("Mr");
        expense2.setReports(reports2);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense2);
        expenseList.add(expense);

        // Act
        Category actualCategory = new Category(1L, "Category Description", 1L, true, expenseList);

        // Assert
        assertEquals("Category Description", actualCategory.getCategoryDescription());
        assertTrue(actualCategory.getIsHidden());
        assertEquals(2, actualCategory.getExpenseList().size());
        assertEquals(1L, actualCategory.getCategoryTotal());
        assertEquals(1L, actualCategory.getCategoryId().longValue());
    }
}

