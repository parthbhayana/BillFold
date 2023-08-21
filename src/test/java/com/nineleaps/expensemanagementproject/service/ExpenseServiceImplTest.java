package com.nineleaps.expensemanagementproject.service;


import com.nineleaps.expensemanagementproject.DTO.ExpenseDTO;
import com.nineleaps.expensemanagementproject.repository.CategoryRepository;
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
import org.springframework.scheduling.TaskScheduler;

class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeServiceImpl employeeService;

    @Mock
    private CurrencyExchangeServiceImpl currencyExchange;


    @Mock
    private IReportsService reportServices;

    @Mock
    private ReportsRepository reportsRepository;
    @Mock
    private CategoryRepository categoryRepository;


    @Mock
    private IEmailService emailService;

    @InjectMocks
    private IExpenseService expenseService = new ExpenseServiceImpl();



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testAddExpense_Success() {
        // Arrange
        Long employeeId = 1L;
        Long categoryId = 2L;
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setAmount((long) 100.0);
        expenseDTO.setCurrency("USD");
        expenseDTO.setDate(LocalDate.now());
        expenseDTO.setDescription("Test Expense");
        expenseDTO.setMerchantName("Test Merchant");
        expenseDTO.setSupportingDocuments("Document URL".getBytes());

        Employee employee = new Employee();
        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);

        Category category = new Category();
        when(categoryRepository.getCategoryByCategoryId(categoryId)).thenReturn(category);

        double exchangeRate = 74.0;
        when(currencyExchange.getExchangeRate("USD", expenseDTO.getDate().toString())).thenReturn(exchangeRate);

        // Set up the behavior of expenseRepository.save method
        when(expenseRepository.save(any(Expense.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Expense result = expenseService.addExpense(expenseDTO, employeeId, categoryId);

        // Assert
        assertNotNull(result, "Expense should not be null");
        assertEquals(employee, result.getEmployee());
        assertEquals(expenseDTO.getAmount(), result.getAmount());
        assertEquals(expenseDTO.getCurrency(), result.getCurrency());
        assertEquals(expenseDTO.getDescription(), result.getDescription());
        assertEquals(expenseDTO.getMerchantName(), result.getMerchantName());
        assertEquals(expenseDTO.getSupportingDocuments(), result.getSupportingDocuments());
        assertEquals(expenseDTO.getDate(), result.getDate());
        assertEquals(exchangeRate * expenseDTO.getAmount(), result.getAmountINR(), 0.01); // Allow a small delta for double comparison
        verify(expenseRepository, times(1)).save(any(Expense.class));
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
        Expense result = reportServices.updateExpense(reportId, employeeId);


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



    }


    private ExpenseDTO createSampleExpenseDTO() {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setMerchantName("Test Merchant");
        expenseDTO.setDate(LocalDate.now());
        expenseDTO.setAmount((long) 100.0);
        expenseDTO.setDescription("Test Expense");
        expenseDTO.setSupportingDocuments("test.pdf".getBytes());
        expenseDTO.setCurrency("USD");
        return expenseDTO;
    }

    @Test
    void testUpdateExpenses_NotReported_NotHidden_NotRejected_Success() {
        // Arrange
        Long expenseId = 1L;
        ExpenseDTO expenseDTO = createSampleExpenseDTO();
        Expense existingExpense = new Expense();
        existingExpense.setExpenseId(expenseId);
        existingExpense.setIsHidden(false);
        existingExpense.setIsReported(false);
        existingExpense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);

        when(expenseRepository.findById(expenseId)).thenReturn(java.util.Optional.of(existingExpense));
        when(currencyExchange.getExchangeRate(eq("USD"), anyString())).thenReturn(1.0);
        when(expenseRepository.save(existingExpense)).thenReturn(existingExpense); // Mock the save method to return existingExpense

        // Act
        Expense updatedExpense = expenseService.updateExpenses(expenseDTO, expenseId);

        // Assert
        assertNotNull(updatedExpense); // Ensure updatedExpense is not null
        assertEquals(expenseDTO.getMerchantName(), updatedExpense.getMerchantName());
        assertEquals(expenseDTO.getDate(), updatedExpense.getDate());
        assertEquals(expenseDTO.getAmount(), updatedExpense.getAmount());
        assertEquals(expenseDTO.getDescription(), updatedExpense.getDescription());
        assertEquals(expenseDTO.getSupportingDocuments(), updatedExpense.getSupportingDocuments());
        assertNotNull(updatedExpense.getDateCreated());
        assertEquals(expenseDTO.getCurrency(), updatedExpense.getCurrency());
        assertEquals(expenseDTO.getAmount(), updatedExpense.getAmountINR(), 0.001); // Use a small delta value for float comparison
        verify(expenseRepository, times(1)).save(existingExpense); // Verify that the save method was called once with existingExpense
    }


    @Test
    void testUpdateExpenses_RejectedExpense_Success() {
        // Arrange
        Long expenseId = 1L;
        ExpenseDTO expenseDTO = createSampleExpenseDTO();
        Expense existingExpense = new Expense();
        existingExpense.setExpenseId(expenseId);
        existingExpense.setIsHidden(false);
        existingExpense.setIsReported(true);
        existingExpense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.REJECTED);

        when(expenseRepository.findById(expenseId)).thenReturn(java.util.Optional.of(existingExpense));
        when(currencyExchange.getExchangeRate(eq("USD"), anyString())).thenReturn(1.0);
        when(expenseRepository.save(existingExpense)).thenReturn(existingExpense); // Mock the save method to return existingExpense

        // Act
        Expense updatedExpense = expenseService.updateExpenses(expenseDTO, expenseId);

        // Assert
        assertNotNull(updatedExpense); // Ensure updatedExpense is not null
        assertEquals(expenseDTO.getMerchantName(), updatedExpense.getMerchantName());
        assertEquals(expenseDTO.getDate(), updatedExpense.getDate());
        assertEquals(expenseDTO.getAmount(), updatedExpense.getAmount());
        assertEquals(expenseDTO.getDescription(), updatedExpense.getDescription());
        assertEquals(expenseDTO.getSupportingDocuments(), updatedExpense.getSupportingDocuments());
        assertNotNull(updatedExpense.getDateCreated());
        assertEquals(expenseDTO.getCurrency(), updatedExpense.getCurrency());
        assertEquals(expenseDTO.getAmount(), updatedExpense.getAmountINR(), 0.001); // Use a small delta value for float comparison
        verify(expenseRepository, times(1)).save(existingExpense); // Verify that the save method was called once with existingExpense
    }


    @Test
    public void testUpdateExpenses_HiddenExpense_ThrowsException() {
        // Arrange
        Long expenseId = 1L;
        ExpenseDTO expenseDTO = createSampleExpenseDTO();
        Expense existingExpense = new Expense();
        existingExpense.setExpenseId(expenseId);
        existingExpense.setIsHidden(true);
        existingExpense.setIsReported(false);

        when(expenseRepository.findById(expenseId)).thenReturn(java.util.Optional.of(existingExpense));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> expenseService.updateExpenses(expenseDTO, expenseId));
        verify(expenseRepository, never()).save(any());
    }

    @Test
    public void testUpdateExpenses_ReportedExpense_ThrowsException() {
        // Arrange
        Long expenseId = 1L;
        ExpenseDTO expenseDTO = createSampleExpenseDTO();
        Expense existingExpense = new Expense();
        existingExpense.setExpenseId(expenseId);
        existingExpense.setIsHidden(false);
        existingExpense.setIsReported(true);
        existingExpense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);

        when(expenseRepository.findById(expenseId)).thenReturn(java.util.Optional.of(existingExpense));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> expenseService.updateExpenses(expenseDTO, expenseId));
        verify(expenseRepository, never()).save(any());
    }





}