package com.nineleaps.expense_management_project.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class ExpenseTest {

    @InjectMocks
    private Expense expense;

    @Mock
    private Employee employee;

    @Mock
    private Reports reports;

    @Mock
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetExpenseId() {
        Long expenseId = 1L;
        expense.setExpenseId(expenseId);
        assertEquals(expenseId, expense.getExpenseId());
    }

    @Test
    void testGetMerchantName() {
        String merchantName = "Example Merchant";
        expense.setMerchantName(merchantName);
        assertEquals(merchantName, expense.getMerchantName());
    }

    @Test
    void testGetDate() {
        LocalDate date = LocalDate.of(2023, 9, 21);
        expense.setDate(date);
        assertEquals(date, expense.getDate());
    }

    @Test
    void testGetDateCreated() {
        LocalDateTime dateCreated = LocalDateTime.now();
        expense.setDateCreated(dateCreated);
        assertEquals(dateCreated, expense.getDateCreated());
    }

    @Test
    void testGetAmount() {
        Double amount = 100.0;
        expense.setAmount(amount);
        assertEquals(amount, expense.getAmount());
    }

    @Test
    void testGetDescription() {
        String description = "Expense Description";
        expense.setDescription(description);
        assertEquals(description, expense.getDescription());
    }

    @Test
    void testGetCategoryDescription() {
        String categoryDescription = "Category Description";
        expense.setCategoryDescription(categoryDescription);
        assertEquals(categoryDescription, expense.getCategoryDescription());
    }

    @Test
    void testGetIsReported() {
        Boolean isReported = true;
        expense.setIsReported(isReported);
        assertEquals(isReported, expense.getIsReported());
    }

    @Test
    void testGetIsHidden() {
        Boolean isHidden = true;
        expense.setIsHidden(isHidden);
        assertEquals(isHidden, expense.getIsHidden());
    }

    @Test
    void testGetReportTitle() {
        String reportTitle = "Expense Report";
        expense.setReportTitle(reportTitle);
        assertEquals(reportTitle, expense.getReportTitle());
    }

    @Test
    void testGetAmountApproved() {
        Double amountApproved = 90.0;
        expense.setAmountApproved(amountApproved);
        assertEquals(amountApproved, expense.getAmountApproved());
    }

    @Test
    void testGetFinanceApprovalStatus() {
        FinanceApprovalStatus approvalStatus = FinanceApprovalStatus.APPROVED;
        expense.setFinanceApprovalStatus(approvalStatus);
        assertEquals(approvalStatus, expense.getFinanceApprovalStatus());
    }

    @Test
    void testGetManagerApprovalStatusExpense() {
        ManagerApprovalStatusExpense approvalStatus = ManagerApprovalStatusExpense.PENDING;
        expense.setManagerApprovalStatusExpense(approvalStatus);
        assertEquals(approvalStatus, expense.getManagerApprovalStatusExpense());
    }

    @Test
    void testGetPotentialDuplicate() {
        Boolean potentialDuplicate = true;
        expense.setPotentialDuplicate(potentialDuplicate);
        assertEquals(potentialDuplicate, expense.getPotentialDuplicate());
    }

    @Test
    void testGetFile() {
        byte[] file = "Test File Content".getBytes();
        expense.setFile(file);
        assertArrayEquals(file, expense.getFile());
    }

    @Test
    void testGetFileName() {
        String fileName = "expense.pdf";
        expense.setFileName(fileName);
        assertEquals(fileName, expense.getFileName());
    }

    @Test
    void testGetEmployee() {
        expense.setEmployee(employee);
        assertEquals(employee, expense.getEmployee());
    }

    @Test
    void testGetReports() {
        expense.setReports(reports);
        assertEquals(reports, expense.getReports());
    }

    @Test
    void testGetCategory() {
        expense.setCategory(category);
        assertEquals(category, expense.getCategory());
    }

    @Test
    void testParameterizedConstructor() {
        // Create an Expense object using the parameterized constructor
        Long expenseId = 1L;
        String merchantName = "Test Merchant";
        LocalDate date = LocalDate.now();
        LocalDateTime dateCreated = LocalDateTime.now();
        Double amount = 100.0;
        String description = "Test Expense";
        String categoryDescription = "Test Category";

        Expense expense = new Expense(expenseId, merchantName, date, dateCreated, amount, description, categoryDescription);

        // Check if the values are correctly set
        assertEquals(expenseId, expense.getExpenseId());
        assertEquals(merchantName, expense.getMerchantName());
        assertEquals(date, expense.getDate());
        assertEquals(dateCreated, expense.getDateCreated());
        assertEquals(amount, expense.getAmount());
        assertEquals(description, expense.getDescription());
        assertEquals(categoryDescription, expense.getCategoryDescription());
    }

    @Test
    void testFileConstructor() {
        // Create an Expense object using the file constructor
        byte[] file = "TestFileData".getBytes();
        String fileName = "TestFileName";
        Employee employee = new Employee();
        Reports reports = new Reports();
        Category category = new Category();

        Expense expense = new Expense(file, fileName, employee, reports, category);

        // Check if the values are correctly set
        assertArrayEquals(file, expense.getFile());
        assertEquals(fileName, expense.getFileName());
        assertEquals(employee, expense.getEmployee());
        assertEquals(reports, expense.getReports());
        assertEquals(category, expense.getCategory());
    }


}