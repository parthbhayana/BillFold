package com.nineleaps.expensemanagementproject.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {

    @Test
    void getExpenseId() {
        Long expenseId = 1L;
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);
        assertEquals(expenseId, expense.getExpenseId());
    }

    @Test
    void setExpenseId() {
        Long expenseId = 2L;
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);
        assertEquals(expenseId, expense.getExpenseId());
    }

    @Test
    void getMerchantName() {
        String merchantName = "ABC Store";
        Expense expense = new Expense();
        expense.setMerchantName(merchantName);
        assertEquals(merchantName, expense.getMerchantName());
    }

    @Test
    void setMerchantName() {
        String merchantName = "XYZ Store";
        Expense expense = new Expense();
        expense.setMerchantName(merchantName);
        assertEquals(merchantName, expense.getMerchantName());
    }

    @Test
    void getDate() {
        LocalDate date = LocalDate.of(2023, 7, 10);
        Expense expense = new Expense();
        expense.setDate(date);
        assertEquals(date, expense.getDate());
    }

    @Test
    void setDate() {
        LocalDate date = LocalDate.of(2023, 7, 15);
        Expense expense = new Expense();
        expense.setDate(date);
        assertEquals(date, expense.getDate());
    }

    @Test
    void getDateCreated() {
        LocalDateTime dateCreated = LocalDateTime.of(2023, 7, 10, 12, 0);
        Expense expense = new Expense();
        expense.setDateCreated(dateCreated);
        assertEquals(dateCreated, expense.getDateCreated());
    }

    @Test
    void setDateCreated() {
        LocalDateTime dateCreated = LocalDateTime.of(2023, 7, 15, 9, 30);
        Expense expense = new Expense();
        expense.setDateCreated(dateCreated);
        assertEquals(dateCreated, expense.getDateCreated());
    }

    @Test
    void getCurrency() {
        String currency = "USD";
        Expense expense = new Expense();
        expense.setCurrency(currency);
        assertEquals(currency, expense.getCurrency());
    }

    @Test
    void setCurrency() {
        String currency = "EUR";
        Expense expense = new Expense();
        expense.setCurrency(currency);
        assertEquals(currency, expense.getCurrency());
    }

    @Test
    void getAmount() {
        Long amount = 1000L;
        Expense expense = new Expense();
        expense.setAmount(amount);
        assertEquals(amount, expense.getAmount());
    }

    @Test
    void setAmount() {
        Long amount = 2000L;
        Expense expense = new Expense();
        expense.setAmount(amount);
        assertEquals(amount, expense.getAmount());
    }

    @Test
    void getAmountINR() {
        float amountINR = 1500.0f;
        Expense expense = new Expense();
        expense.setAmountINR(amountINR);
        assertEquals(amountINR, expense.getAmountINR());
    }

    @Test
    void setAmountINR() {
        float amountINR = 2500.0f;
        Expense expense = new Expense();
        expense.setAmountINR(amountINR);
        assertEquals(amountINR, expense.getAmountINR());
    }

    @Test
    void getDescription() {
        String description = "Expense for dinner";
        Expense expense = new Expense();
        expense.setDescription(description);
        assertEquals(description, expense.getDescription());
    }

    @Test
    void setDescription() {
        String description = "Expense for lunch";
        Expense expense = new Expense();
        expense.setDescription(description);
        assertEquals(description, expense.getDescription());
    }

    @Test
    void getCategoryDescription() {
        String categoryDescription = "Food";
        Expense expense = new Expense();
        expense.setCategoryDescription(categoryDescription);
        assertEquals(categoryDescription, expense.getCategoryDescription());
    }

    @Test
    void setCategoryDescription() {
        String categoryDescription = "Transportation";
        Expense expense = new Expense();
        expense.setCategoryDescription(categoryDescription);
        assertEquals(categoryDescription, expense.getCategoryDescription());
    }

    @Test
    void getIsReported() {
        Boolean isReported = true;
        Expense expense = new Expense();
        expense.setIsReported(isReported);
        assertEquals(isReported, expense.getIsReported());
    }

    @Test
    void setIsReported() {
        Boolean isReported = false;
        Expense expense = new Expense();
        expense.setIsReported(isReported);
        assertEquals(isReported, expense.getIsReported());
    }

    @Test
    void getIsHidden() {
        Boolean isHidden = true;
        Expense expense = new Expense();
        expense.setIsHidden(isHidden);
        assertEquals(isHidden, expense.getIsHidden());
    }

    @Test
    void setIsHidden() {
        Boolean isHidden = false;
        Expense expense = new Expense();
        expense.setIsHidden(isHidden);
        assertEquals(isHidden, expense.getIsHidden());
    }

    @Test
    void getReportTitle() {
        String reportTitle = "Expense Report 2023";
        Expense expense = new Expense();
        expense.setReportTitle(reportTitle);
        assertEquals(reportTitle, expense.getReportTitle());
    }

    @Test
    void setReportTitle() {
        String reportTitle = "Expense Report Q2 2023";
        Expense expense = new Expense();
        expense.setReportTitle(reportTitle);
        assertEquals(reportTitle, expense.getReportTitle());
    }

    @Test
    void getAmountApproved() {
        Float amountApproved = 500.0f;
        Expense expense = new Expense();
        expense.setAmountApproved(amountApproved);
        assertEquals(amountApproved, expense.getAmountApproved());
    }

    @Test
    void setAmountApproved() {
        Float amountApproved = 1000.0f;
        Expense expense = new Expense();
        expense.setAmountApproved(amountApproved);
        assertEquals(amountApproved, expense.getAmountApproved());
    }

    @Test
    void getFinanceApprovalStatus() {
        FinanceApprovalStatus financeApprovalStatus = FinanceApprovalStatus.APPROVED;
        Expense expense = new Expense();
        expense.setFinanceApprovalStatus(financeApprovalStatus);
        assertEquals(financeApprovalStatus, expense.getFinanceApprovalStatus());
    }

    @Test
    void setFinanceApprovalStatus() {
        FinanceApprovalStatus financeApprovalStatus = FinanceApprovalStatus.PENDING;
        Expense expense = new Expense();
        expense.setFinanceApprovalStatus(financeApprovalStatus);
        assertEquals(financeApprovalStatus, expense.getFinanceApprovalStatus());
    }

    @Test
    void getManagerApprovalStatusExpense() {
        ManagerApprovalStatusExpense managerApprovalStatusExpense = ManagerApprovalStatusExpense.APPROVED;
        Expense expense = new Expense();
        expense.setManagerApprovalStatusExpense(managerApprovalStatusExpense);
        assertEquals(managerApprovalStatusExpense, expense.getManagerApprovalStatusExpense());
    }

    @Test
    void setManagerApprovalStatusExpense() {
        ManagerApprovalStatusExpense managerApprovalStatusExpense = ManagerApprovalStatusExpense.PENDING;
        Expense expense = new Expense();
        expense.setManagerApprovalStatusExpense(managerApprovalStatusExpense);
        assertEquals(managerApprovalStatusExpense, expense.getManagerApprovalStatusExpense());
    }

    @Test
    void getSupportingDocuments() {
        byte[] supportingDocuments = {1, 2, 3};
        Expense expense = new Expense();
        expense.setSupportingDocuments(supportingDocuments);
        assertArrayEquals(supportingDocuments, expense.getSupportingDocuments());
    }

    @Test
    void setSupportingDocuments() {
        byte[] supportingDocuments = {4, 5, 6};
        Expense expense = new Expense();
        expense.setSupportingDocuments(supportingDocuments);
        assertArrayEquals(supportingDocuments, expense.getSupportingDocuments());
    }

    @Test
    void getEmployee() {
        Employee employee = new Employee();
        Expense expense = new Expense();
        expense.setEmployee(employee);
        assertEquals(employee, expense.getEmployee());
    }

    @Test
    void setEmployee() {
        Employee employee = new Employee();
        Expense expense = new Expense();
        expense.setEmployee(employee);
        assertEquals(employee, expense.getEmployee());
    }

    @Test
    void getReports() {
        Reports reports = new Reports();
        Expense expense = new Expense();
        expense.setReports(reports);
        assertEquals(reports, expense.getReports());
    }

    @Test
    void setReports() {
        Reports reports = new Reports();
        Expense expense = new Expense();
        expense.setReports(reports);
        assertEquals(reports, expense.getReports());
    }

    @Test
    void getCategory() {
        Category category = new Category();
        Expense expense = new Expense();
        expense.setCategory(category);
        assertEquals(category, expense.getCategory());
    }

    @Test
    void setCategory() {
        Category category = new Category();
        Expense expense = new Expense();
        expense.setCategory(category);
        assertEquals(category, expense.getCategory());
    }
    @Test
    void getAmountApprovedINR() {
        Double amountApprovedINR = 1000.0;
        Expense expense = new Expense();
        expense.setAmountApprovedINR(amountApprovedINR);
        assertEquals(amountApprovedINR, expense.getAmountApprovedINR());
    }

    @Test
    void setAmountApprovedINR() {
        Double amountApprovedINR = 2000.0;
        Expense expense = new Expense();
        expense.setAmountApprovedINR(amountApprovedINR);
        assertEquals(amountApprovedINR, expense.getAmountApprovedINR());
    }
}