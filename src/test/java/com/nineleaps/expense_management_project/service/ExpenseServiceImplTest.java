package com.nineleaps.expense_management_project.service;

import com.nineleaps.expense_management_project.dto.ExpenseDTO;
import com.nineleaps.expense_management_project.entity.*;
import com.nineleaps.expense_management_project.repository.CategoryRepository;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private EmployeeServiceImpl employeeService;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void testGetExpenseById() {
        // Create a mock Expense object and configure repository
        Expense expense = new Expense();
        when(expenseRepository.findById(anyLong())).thenReturn(Optional.of(expense));

        // Call the service method
        Expense result = expenseService.getExpenseById(1L);

        // Verify that the result matches the mock object
        assertEquals(expense, result);
    }


    @Test
    public void testDeleteExpenseById() {
        // Call the service method
        expenseService.deleteExpenseById(1L);

        // Verify that the repository delete method was called with the correct ID
        verify(expenseRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testExpenseConstructor() {
        // Arrange
        Boolean isReported = true;
        Boolean isHidden = false;
        String reportTitle = "Expense Report";
        Double amountApproved = 1000.0;
        FinanceApprovalStatus financeApprovalStatus = FinanceApprovalStatus.APPROVED;
        ManagerApprovalStatusExpense managerApprovalStatusExpense = ManagerApprovalStatusExpense.APPROVED;
        Boolean potentialDuplicate = false;

        // Act
        Expense expense = new Expense(
                isReported, isHidden, reportTitle, amountApproved,
                financeApprovalStatus, managerApprovalStatusExpense, potentialDuplicate);

        // Assert
        assertNotNull(expense);
        assertEquals(isReported, expense.getIsReported());
        assertEquals(isHidden, expense.getIsHidden());
        assertEquals(reportTitle, expense.getReportTitle());
        assertEquals(amountApproved, expense.getAmountApproved());
        assertEquals(financeApprovalStatus, expense.getFinanceApprovalStatus());
        assertEquals(managerApprovalStatusExpense, expense.getManagerApprovalStatusExpense());
        assertEquals(potentialDuplicate, expense.getPotentialDuplicate());
    }


    @Test
    public void testExpenseAdditionalConstructor() {
        // Arrange
        byte[] file = new byte[]{1, 2, 3};
        String fileName = "expense.pdf";
        Employee employee = new Employee(); // Create an Employee object or mock it
        Reports reports = new Reports(); // Create a Reports object or mock it
        Category category = new Category(); // Create a Category object or mock it

        // Act
        Expense expense = new Expense(file, fileName, employee, reports, category);

        // Assert
        assertNotNull(expense);
        assertArrayEquals(file, expense.getFile());
        assertEquals(fileName, expense.getFileName());
        assertEquals(employee, expense.getEmployee());
        assertEquals(reports, expense.getReports());
        assertEquals(category, expense.getCategory());
    }


    @Test
    public void testGetExpenseByEmployeeId_WhenEmployeeExists() {
        // Sample data
        Long employeeId = 1L;

        // Create a sample employee
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);

        // Create a list of expenses for the employee
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense());
        expenses.add(new Expense());

        // Mock the behavior of employeeRepository to return the employee when findById is called
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // Mock the behavior of expenseRepository to return the list of expenses for the employee
        when(expenseRepository.findByEmployeeAndIsHidden(employee, false, Sort.by(Sort.Direction.DESC, "dateCreated"))).thenReturn(expenses);

        // Call the method to be tested
        List<Expense> result = expenseService.getExpenseByEmployeeId(employeeId);

        // Verify that the method returned the list of expenses
        assertEquals(expenses, result);
    }

    @Test
    public void testGetExpenseByEmployeeId_WhenEmployeeDoesNotExist() {
        // Sample data
        Long employeeId = 1L;

        // Mock the behavior of employeeRepository to return an empty Optional
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Call the method to be tested
        List<Expense> result = expenseService.getExpenseByEmployeeId(employeeId);

        // Verify that the method returned an empty list
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetExpensesByEmployeeId_WhenEmployeeFound() {
        // Sample data
        Long employeeId = 1L;

        // Create a sample employee
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);

        // Create a sample list of expenses
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense());
        expenses.add(new Expense());

        // Mock the behavior of employeeRepository to return the employee when findById is called
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // Mock the behavior of expenseRepository to return the list of expenses when findByEmployeeAndIsReported is called
        when(expenseRepository.findByEmployeeAndIsReported(employee, false)).thenReturn(expenses);

        // Call the method to be tested
        List<Expense> result = expenseService.getExpensesByEmployeeId(employeeId);

        // Verify that the result contains the expected expenses
        assertEquals(expenses, result);
    }

    @Test
    public void testGetExpensesByEmployeeId_WhenEmployeeNotFound() {
        // Sample data
        Long employeeId = 1L;

        // Mock the behavior of employeeRepository to return an empty optional when findById is called
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Call the method to be tested
        List<Expense> result = expenseService.getExpensesByEmployeeId(employeeId);

        // Verify that the result is an empty list
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testAddExpense() throws IllegalAccessException {
        // Mock employee and category
        Employee employee = new Employee();
        when(employeeService.getEmployeeById(anyLong())).thenReturn(employee);

        Category category = new Category();
        category.setCategoryDescription("TestCategory");
        when(categoryRepository.getCategoryByCategoryId(anyLong())).thenReturn(category);

        // Mock existing expenses (potential duplicates)
        List<Expense> existingExpenses = new ArrayList<>();
        when(expenseRepository.findByEmployeeAndAmountAndDateAndCategoryAndMerchantNameAndIsHidden(
                any(), anyDouble(), any(), any(), any(), anyBoolean()
        )).thenReturn(existingExpenses);

        // Create an ExpenseDTO
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setAmount(100.0);
        expenseDTO.setDate(LocalDate.from(LocalDateTime.now()));
        expenseDTO.setDescription("Test Expense");
        expenseDTO.setMerchantName("Test Merchant");
        expenseDTO.setFile(new byte[0]);
        expenseDTO.setFileName("test.pdf");

        // Test adding a new expense
        String result = expenseService.addExpense(expenseDTO, 1L, 2L);
        assertEquals("Expense Added!", result);

        // Test adding a potential duplicate expense
        existingExpenses.add(new Expense()); // Add a mock existing expense
        result = expenseService.addExpense(expenseDTO, 1L, 2L);
        assertEquals("Expense might be a potential duplicate!", result);

        // Verify that the expenseRepository.save method is called when adding a new expense
        verify(expenseRepository, times(1)).save(any());
    }


}