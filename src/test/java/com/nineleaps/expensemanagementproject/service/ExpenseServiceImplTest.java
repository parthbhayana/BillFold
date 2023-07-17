package com.nineleaps.expensemanagementproject.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.nineleaps.expensemanagementproject.entity.*;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private CategoryServiceImpl currencyRates;



    @Mock
    private IReportsService reportServices;

    @Mock
    private ReportsRepository reportsRepository;


    @Mock
    private IEmailService emailService;

    @InjectMocks
    private IExpenseService expenseService = new ExpenseServiceImpl();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testGetAllExpenses() {
        // Mock data
        LocalDate sixtyDaysAgo = LocalDate.now().minusDays(60);
        List<Expense> unreportedExpenses = new ArrayList<>();
        Expense expense = new Expense();
        unreportedExpenses.add(expense);

        // Mock dependencies
        when(expenseRepository.findByIsReportedAndDateBefore(false, sixtyDaysAgo)).thenReturn(unreportedExpenses);
        when(expenseRepository.findAll()).thenReturn(unreportedExpenses);
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        // Perform the test
        List<Expense> result = expenseService.getAllExpenses();

        // Verify the results
        Assertions.assertNotNull(result);
        // Add more assertions as needed
    }

    @Test
    void testGetExpenseById() {
        // Mock data
        Long expenseId = 1L;
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);

        // Mock dependencies
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));

        // Perform the test
        Expense result = expenseService.getExpenseById(expenseId);

        // Verify the results
        Assertions.assertNotNull(result);
        assertEquals(expenseId, result.getExpenseId());

    }

    @Test
    void testUpdateExpense() {
        // Mock data
        Long reportId = 1L;
        Long employeeId = 1L;
        Expense expense = new Expense();
        Reports report = new Reports();
        report.setReportId(reportId);

        // Mock dependencies
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        when(expenseRepository.findById(employeeId)).thenReturn(Optional.of(expense));
        when(reportServices.getReportById(reportId)).thenReturn(report);

        // Perform the test
        Expense result = expenseService.updateExpense(reportId, employeeId);

        // Verify the results
        Assertions.assertNotNull(result);
        // Add more assertions as needed
    }

    @Test
    void testDeleteExpenseById() {
        // Mock data
        Long expenseId = 1L;

        // Perform the test
        expenseService.deleteExpenseById(expenseId);

        // Verify the results
        Mockito.verify(expenseRepository, Mockito.times(1)).deleteById(expenseId);
    }

    @Test
    void testGetExpenseByEmployeeId() {
        // Mock data
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense());

        // Mock dependencies
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(expenseRepository.findByEmployeeAndIsHidden(employee, false)).thenReturn(expenses);

        // Perform the test
        List<Expense> result = expenseService.getExpenseByEmployeeId(employeeId);

        // Verify the results
        Assertions.assertNotNull(result);
        // Add more assertions as needed
    }

     @Test
    void testGetExpensesByEmployeeId() {
        // Mock data
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense());

        // Mock dependencies
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(expenseRepository.findByEmployeeAndIsReported(employee, false)).thenReturn(expenses);

        // Perform the test
        List<Expense> result = expenseService.getExpensesByEmployeeId(employeeId);

        // Verify the results
        Assertions.assertNotNull(result);
        // Add more assertions as needed
    }

    @Test
    void testRemoveTaggedExpense() {
        // Mock data
        Long expenseId = 1L;
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);

        // Mock dependencies
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        // Perform the test
        Expense result = expenseService.removeTaggedExpense(expenseId);

        // Verify the results
        Assertions.assertNotNull(result);
        // Add more assertions as needed
    }

    @Test
    void testGetExpenseByReportId() {
        // Mock data
        Long reportId = 1L;
        Reports report = new Reports();
        report.setReportId(reportId);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense());

        // Mock dependencies
        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(expenseRepository.findByReportsAndIsHidden(report, false)).thenReturn(expenses);

        // Perform the test
        List<Expense> result = expenseService.getExpenseByReportId(reportId);

        // Verify the results
        Assertions.assertNotNull(result);
        // Add more assertions as needed
    }

    @Test
    void testHideExpense() {
        // Mock data
        Long expenseId = 1L;
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);

        // Mock dependencies
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));

        // Perform the test
        expenseService.hideExpense(expenseId);

        // Verify the results
        // Add more assertions as needed
    }

    @Test
    void testSendExpenseReminder() {
        // Mock data
        LocalDate currentDate = LocalDate.now();
        List<Expense> expenseList = new ArrayList<>();
        Expense expense = new Expense();
        expense.setDate(currentDate);
        expenseList.add(expense);

        // Mock dependencies
        when(expenseRepository.findByIsReportedAndIsHidden(false, false)).thenReturn(expenseList);
        Mockito.doNothing().when(emailService).reminderMailToEmployee(Mockito.anyList());

        // Perform the test


    }
}
