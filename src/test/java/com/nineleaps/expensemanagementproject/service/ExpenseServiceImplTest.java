package com.nineleaps.expensemanagementproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nineleaps.expensemanagementproject.DTO.ExpenseDTO;
import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.FinanceApprovalStatus;
import com.nineleaps.expensemanagementproject.entity.ManagerApprovalStatus;
import com.nineleaps.expensemanagementproject.entity.ManagerApprovalStatusExpense;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.firebase.PushNotificationService;
import com.nineleaps.expensemanagementproject.repository.CategoryRepository;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ExpenseServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ExpenseServiceImplTest {
    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseServiceImpl expenseServiceImpl;

    @MockBean
    private IEmailService iEmailService;

    @MockBean
    private IEmployeeService iEmployeeService;

    @MockBean
    private IReportsService iReportsService;

    @MockBean
    private PushNotificationService pushNotificationService;

    @MockBean
    private ReportsRepository reportsRepository;

    /**
     * Method under test: {@link ExpenseServiceImpl#addExpense(ExpenseDTO, Long, Long)}
     */
    @Test
    void testAddExpense() throws UnsupportedEncodingException, IllegalAccessException {
        // Arrange
        Category category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);
        when(categoryRepository.getCategoryByCategoryId(Mockito.<Long>any())).thenReturn(category);

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
        when(iEmployeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(employee);

        Category category2 = new Category();
        category2.setCategoryDescription("Category Description");
        category2.setCategoryId(1L);
        category2.setCategoryTotal(1L);
        category2.setExpenseList(new ArrayList<>());
        category2.setIsHidden(true);

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
        expense.setCategory(category2);
        expense.setCategoryDescription("Category Description");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("The characteristics of someone or something");
        expense.setEmployee(employee2);
        expense.setExpenseId(1L);
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        when(expenseRepository.save(Mockito.<Expense>any())).thenReturn(expense);
        when(expenseRepository.findByEmployeeAndAmountAndDateAndCategoryAndMerchantNameAndIsHidden(Mockito.<Employee>any(),
                Mockito.<Double>any(), Mockito.<LocalDate>any(), Mockito.<Category>any(), Mockito.<String>any(), anyBoolean()))
                .thenReturn(new ArrayList<>());

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setDate(LocalDate.now());

        // Act and Assert
        assertEquals("Expense Added!", expenseServiceImpl.addExpense(expenseDTO, 1L, 1L));
        verify(categoryRepository).getCategoryByCategoryId(Mockito.<Long>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(expenseRepository).save(Mockito.<Expense>any());
        verify(expenseRepository).findByEmployeeAndAmountAndDateAndCategoryAndMerchantNameAndIsHidden(
                Mockito.<Employee>any(), Mockito.<Double>any(), Mockito.<LocalDate>any(), Mockito.<Category>any(),
                Mockito.<String>any(), anyBoolean());
    }


    @Test
    void testAddExpense2() throws IllegalAccessException {
        // Arrange
        Category category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);
        when(categoryRepository.getCategoryByCategoryId(Mockito.<Long>any())).thenReturn(category);

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
        when(iEmployeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(employee);
        when(expenseRepository.findByEmployeeAndAmountAndDateAndCategoryAndMerchantNameAndIsHidden(
                Mockito.<Employee>any(), Mockito.<Double>any(), Mockito.<LocalDate>any(), Mockito.<Category>any(),
                Mockito.<String>any(), anyBoolean())).thenThrow(new IllegalStateException("Expense Added!"));

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setDate(LocalDate.now());

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.addExpense(expenseDTO, 1L, 1L));
        verify(categoryRepository).getCategoryByCategoryId(Mockito.<Long>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(expenseRepository).findByEmployeeAndAmountAndDateAndCategoryAndMerchantNameAndIsHidden(
                Mockito.<Employee>any(), Mockito.<Double>any(), Mockito.<LocalDate>any(), Mockito.<Category>any(),
                Mockito.<String>any(), anyBoolean());
    }


    @Test
    void testAddPotentialDuplicateExpense() throws UnsupportedEncodingException {
        // Arrange
        Category category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);
        when(categoryRepository.getCategoryByCategoryId(Mockito.<Long>any())).thenReturn(category);

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
        when(iEmployeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(employee);

        Category category2 = new Category();
        category2.setCategoryDescription("Category Description");
        category2.setCategoryId(1L);
        category2.setCategoryTotal(1L);
        category2.setExpenseList(new ArrayList<>());
        category2.setIsHidden(true);

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
        expense.setCategory(category2);
        expense.setCategoryDescription("Category Description");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("The characteristics of someone or something");
        expense.setEmployee(employee2);
        expense.setExpenseId(1L);
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        when(expenseRepository.save(Mockito.<Expense>any())).thenReturn(expense);

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setDate(LocalDate.now());

        // Act and Assert
        assertSame(expense, expenseServiceImpl.addPotentialDuplicateExpense(expenseDTO, 1L, 1L));
        verify(categoryRepository).getCategoryByCategoryId(Mockito.<Long>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(expenseRepository).save(Mockito.<Expense>any());
    }

    @Test
    void testAddPotentialDuplicateExpense2() {
        // Arrange
        Category category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);
        when(categoryRepository.getCategoryByCategoryId(Mockito.<Long>any())).thenReturn(category);

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
        when(iEmployeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(employee);
        when(expenseRepository.save(Mockito.<Expense>any())).thenThrow(new IllegalStateException("foo"));

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setDate(LocalDate.now());

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> expenseServiceImpl.addPotentialDuplicateExpense(expenseDTO, 1L, 1L));
        verify(categoryRepository).getCategoryByCategoryId(Mockito.<Long>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(expenseRepository).save(Mockito.<Expense>any());
    }


    @Test
    void testGetAllExpenses() {
        // Arrange
        when(expenseRepository.findByIsReportedAndDateBefore(anyBoolean(), Mockito.<LocalDate>any()))
                .thenReturn(new ArrayList<>());
        ArrayList<Expense> expenseList = new ArrayList<>();
        when(expenseRepository.findAll()).thenReturn(expenseList);

        // Act
        List<Expense> actualAllExpenses = expenseServiceImpl.getAllExpenses();

        // Assert
        assertSame(expenseList, actualAllExpenses);
        assertTrue(actualAllExpenses.isEmpty());
        verify(expenseRepository).findByIsReportedAndDateBefore(anyBoolean(), Mockito.<LocalDate>any());
        verify(expenseRepository).findAll();
    }

    @Test
    void testGetAllExpenses2() throws UnsupportedEncodingException {
        // Arrange
        Category category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1L);
        category.setCategoryTotal(60L);
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
        employee.setMobileNumber(60L);
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
        expense.setFile("A<A<A<A<".getBytes("UTF-8"));
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

        Category category2 = new Category();
        category2.setCategoryDescription("Category Description");
        category2.setCategoryId(1L);
        category2.setCategoryTotal(1L);
        category2.setExpenseList(new ArrayList<>());
        category2.setIsHidden(true);

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

        Expense expense2 = new Expense();
        expense2.setAmount(10.0d);
        expense2.setAmountApproved(10.0d);
        expense2.setCategory(category2);
        expense2.setCategoryDescription("Category Description");
        expense2.setDate(LocalDate.of(1970, 1, 1));
        expense2.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense2.setDescription("The characteristics of someone or something");
        expense2.setEmployee(employee2);
        expense2.setExpenseId(1L);
        expense2.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense2.setFileName("foo.txt");
        expense2.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense2.setIsHidden(true);
        expense2.setIsReported(true);
        expense2.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense2.setMerchantName("Merchant Name");
        expense2.setPotentialDuplicate(true);
        expense2.setReportTitle("Dr");
        expense2.setReports(reports2);
        when(expenseRepository.save(Mockito.<Expense>any())).thenReturn(expense2);
        when(expenseRepository.findByIsReportedAndDateBefore(anyBoolean(), Mockito.<LocalDate>any()))
                .thenReturn(expenseList);
        ArrayList<Expense> expenseList2 = new ArrayList<>();
        when(expenseRepository.findAll()).thenReturn(expenseList2);

        // Act
        List<Expense> actualAllExpenses = expenseServiceImpl.getAllExpenses();

        // Assert
        assertSame(expenseList2, actualAllExpenses);
        assertTrue(actualAllExpenses.isEmpty());
        verify(expenseRepository).save(Mockito.<Expense>any());
        verify(expenseRepository).findByIsReportedAndDateBefore(anyBoolean(), Mockito.<LocalDate>any());
        verify(expenseRepository).findAll();
    }

    @Test
    void testGetExpenseById() throws UnsupportedEncodingException {
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
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        Optional<Expense> ofResult = Optional.of(expense);
        when(expenseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertSame(expense, expenseServiceImpl.getExpenseById(1L));
        verify(expenseRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testUpdateExpense() throws UnsupportedEncodingException {
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
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        Optional<Expense> ofResult = Optional.of(expense);

        Category category2 = new Category();
        category2.setCategoryDescription("Category Description");
        category2.setCategoryId(1L);
        category2.setCategoryTotal(1L);
        category2.setExpenseList(new ArrayList<>());
        category2.setIsHidden(true);

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

        Expense expense2 = new Expense();
        expense2.setAmount(10.0d);
        expense2.setAmountApproved(10.0d);
        expense2.setCategory(category2);
        expense2.setCategoryDescription("Category Description");
        expense2.setDate(LocalDate.of(1970, 1, 1));
        expense2.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense2.setDescription("The characteristics of someone or something");
        expense2.setEmployee(employee2);
        expense2.setExpenseId(1L);
        expense2.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense2.setFileName("foo.txt");
        expense2.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense2.setIsHidden(true);
        expense2.setIsReported(true);
        expense2.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense2.setMerchantName("Merchant Name");
        expense2.setPotentialDuplicate(true);
        expense2.setReportTitle("Dr");
        expense2.setReports(reports2);
        when(expenseRepository.save(Mockito.<Expense>any())).thenReturn(expense2);
        when(expenseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Reports reports3 = new Reports();
        reports3.setDateCreated(LocalDate.of(1970, 1, 1));
        reports3.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports3.setEmployeeId(1L);
        reports3.setEmployeeMail("Employee Mail");
        reports3.setEmployeeName("Employee Name");
        reports3.setExpensesCount(3L);
        reports3.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports3.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports3.setFinanceComments("Finance Comments");
        reports3.setIsHidden(true);
        reports3.setIsSubmitted(true);
        reports3.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports3.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports3.setManagerComments("Manager Comments");
        reports3.setManagerEmail("jane.doe@example.org");
        reports3.setManagerReviewTime("Manager Review Time");
        reports3.setOfficialEmployeeId("42");
        reports3.setReportId(1L);
        reports3.setReportTitle("Dr");
        reports3.setTotalAmount(10.0f);
        reports3.setTotalApprovedAmount(10.0f);
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports3);

        // Act and Assert
        assertSame(expense2, expenseServiceImpl.updateExpense(1L, 1L));
        verify(expenseRepository).save(Mockito.<Expense>any());
        verify(expenseRepository).findById(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }

    @Test
    void testUpdateExpense2() throws UnsupportedEncodingException {
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
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        Optional<Expense> ofResult = Optional.of(expense);
        when(expenseRepository.save(Mockito.<Expense>any())).thenThrow(new IllegalStateException("foo"));
        when(expenseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports2);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.updateExpense(1L, 1L));
        verify(expenseRepository).save(Mockito.<Expense>any());
        verify(expenseRepository).findById(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }

    @Test
    void testDeleteExpenseById() {
        // Arrange
        doNothing().when(expenseRepository).deleteById(Mockito.<Long>any());

        // Act
        expenseServiceImpl.deleteExpenseById(1L);

        // Assert that nothing has changed
        verify(expenseRepository).deleteById(Mockito.<Long>any());
        assertTrue(expenseServiceImpl.getAllExpenses().isEmpty());
    }


    @Test
    void testDeleteExpenseById2() {
        // Arrange
        doThrow(new IllegalStateException("foo")).when(expenseRepository).deleteById(Mockito.<Long>any());

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.deleteExpenseById(1L));
        verify(expenseRepository).deleteById(Mockito.<Long>any());
    }


    @Test
    void testGetExpenseByEmployeeId() {
        // Arrange
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
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ArrayList<Expense> expenseList = new ArrayList<>();
        when(expenseRepository.findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any()))
                .thenReturn(expenseList);

        // Act
        List<Expense> actualExpenseByEmployeeId = expenseServiceImpl.getExpenseByEmployeeId(1L);

        // Assert
        assertSame(expenseList, actualExpenseByEmployeeId);
        assertTrue(actualExpenseByEmployeeId.isEmpty());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(expenseRepository).findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any());
    }


    @Test
    void testGetExpenseByEmployeeId2() {
        // Arrange
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
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(expenseRepository.findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any()))
                .thenThrow(new IllegalStateException("dateCreated"));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.getExpenseByEmployeeId(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(expenseRepository).findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any());
    }


    @Test
    void testUpdateSupportingDocument() {
        // Arrange, Act and Assert
        assertNull(expenseServiceImpl.updateSupportingDocument("Supporting Doc", 1L));
    }

    @Test
    void testUpdateExpenses() throws UnsupportedEncodingException {
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
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        Optional<Expense> ofResult = Optional.of(expense);
        when(expenseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.updateExpenses(new ExpenseDTO(), 1L));
        verify(expenseRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testUpdateExpenses2() throws UnsupportedEncodingException {
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
        Expense expense = mock(Expense.class);
        when(expense.getManagerApprovalStatusExpense()).thenThrow(new IllegalStateException("foo"));
        when(expense.getIsHidden()).thenReturn(true);
        when(expense.getIsReported()).thenReturn(true);
        doNothing().when(expense).setAmount(Mockito.<Double>any());
        doNothing().when(expense).setAmountApproved(Mockito.<Double>any());
        doNothing().when(expense).setCategory(Mockito.<Category>any());
        doNothing().when(expense).setCategoryDescription(Mockito.<String>any());
        doNothing().when(expense).setDate(Mockito.<LocalDate>any());
        doNothing().when(expense).setDateCreated(Mockito.<LocalDateTime>any());
        doNothing().when(expense).setDescription(Mockito.<String>any());
        doNothing().when(expense).setEmployee(Mockito.<Employee>any());
        doNothing().when(expense).setExpenseId(Mockito.<Long>any());
        doNothing().when(expense).setFile(Mockito.<byte[]>any());
        doNothing().when(expense).setFileName(Mockito.<String>any());
        doNothing().when(expense).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(expense).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(expense).setIsReported(Mockito.<Boolean>any());
        doNothing().when(expense).setManagerApprovalStatusExpense(Mockito.<ManagerApprovalStatusExpense>any());
        doNothing().when(expense).setMerchantName(Mockito.<String>any());
        doNothing().when(expense).setPotentialDuplicate(Mockito.<Boolean>any());
        doNothing().when(expense).setReportTitle(Mockito.<String>any());
        doNothing().when(expense).setReports(Mockito.<Reports>any());
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("Category Description");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("The characteristics of someone or something");
        expense.setEmployee(employee);
        expense.setExpenseId(1L);
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        Optional<Expense> ofResult = Optional.of(expense);
        when(expenseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.updateExpenses(new ExpenseDTO(), 1L));
        verify(expenseRepository).findById(Mockito.<Long>any());
        verify(expense).getManagerApprovalStatusExpense();
        verify(expense).getIsHidden();
        verify(expense).getIsReported();
        verify(expense).setAmount(Mockito.<Double>any());
        verify(expense).setAmountApproved(Mockito.<Double>any());
        verify(expense).setCategory(Mockito.<Category>any());
        verify(expense).setCategoryDescription(Mockito.<String>any());
        verify(expense).setDate(Mockito.<LocalDate>any());
        verify(expense).setDateCreated(Mockito.<LocalDateTime>any());
        verify(expense).setDescription(Mockito.<String>any());
        verify(expense).setEmployee(Mockito.<Employee>any());
        verify(expense).setExpenseId(Mockito.<Long>any());
        verify(expense).setFile(Mockito.<byte[]>any());
        verify(expense).setFileName(Mockito.<String>any());
        verify(expense).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(expense).setIsHidden(Mockito.<Boolean>any());
        verify(expense).setIsReported(Mockito.<Boolean>any());
        verify(expense).setManagerApprovalStatusExpense(Mockito.<ManagerApprovalStatusExpense>any());
        verify(expense).setMerchantName(Mockito.<String>any());
        verify(expense).setPotentialDuplicate(Mockito.<Boolean>any());
        verify(expense).setReportTitle(Mockito.<String>any());
        verify(expense).setReports(Mockito.<Reports>any());
    }

    @Test
    void testUpdateExpenses3() throws UnsupportedEncodingException {
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
        Expense expense = mock(Expense.class);
        when(expense.getManagerApprovalStatusExpense()).thenThrow(new IllegalStateException("foo"));
        when(expense.getIsHidden()).thenReturn(false);
        when(expense.getIsReported()).thenReturn(true);
        doNothing().when(expense).setAmount(Mockito.<Double>any());
        doNothing().when(expense).setAmountApproved(Mockito.<Double>any());
        doNothing().when(expense).setCategory(Mockito.<Category>any());
        doNothing().when(expense).setCategoryDescription(Mockito.<String>any());
        doNothing().when(expense).setDate(Mockito.<LocalDate>any());
        doNothing().when(expense).setDateCreated(Mockito.<LocalDateTime>any());
        doNothing().when(expense).setDescription(Mockito.<String>any());
        doNothing().when(expense).setEmployee(Mockito.<Employee>any());
        doNothing().when(expense).setExpenseId(Mockito.<Long>any());
        doNothing().when(expense).setFile(Mockito.<byte[]>any());
        doNothing().when(expense).setFileName(Mockito.<String>any());
        doNothing().when(expense).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(expense).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(expense).setIsReported(Mockito.<Boolean>any());
        doNothing().when(expense).setManagerApprovalStatusExpense(Mockito.<ManagerApprovalStatusExpense>any());
        doNothing().when(expense).setMerchantName(Mockito.<String>any());
        doNothing().when(expense).setPotentialDuplicate(Mockito.<Boolean>any());
        doNothing().when(expense).setReportTitle(Mockito.<String>any());
        doNothing().when(expense).setReports(Mockito.<Reports>any());
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("Category Description");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("The characteristics of someone or something");
        expense.setEmployee(employee);
        expense.setExpenseId(1L);
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        Optional<Expense> ofResult = Optional.of(expense);
        when(expenseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.updateExpenses(new ExpenseDTO(), 1L));
        verify(expenseRepository).findById(Mockito.<Long>any());
        verify(expense).getManagerApprovalStatusExpense();
        verify(expense).getIsHidden();
        verify(expense, atLeast(1)).getIsReported();
        verify(expense).setAmount(Mockito.<Double>any());
        verify(expense).setAmountApproved(Mockito.<Double>any());
        verify(expense).setCategory(Mockito.<Category>any());
        verify(expense).setCategoryDescription(Mockito.<String>any());
        verify(expense).setDate(Mockito.<LocalDate>any());
        verify(expense).setDateCreated(Mockito.<LocalDateTime>any());
        verify(expense).setDescription(Mockito.<String>any());
        verify(expense).setEmployee(Mockito.<Employee>any());
        verify(expense).setExpenseId(Mockito.<Long>any());
        verify(expense).setFile(Mockito.<byte[]>any());
        verify(expense).setFileName(Mockito.<String>any());
        verify(expense).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(expense).setIsHidden(Mockito.<Boolean>any());
        verify(expense).setIsReported(Mockito.<Boolean>any());
        verify(expense).setManagerApprovalStatusExpense(Mockito.<ManagerApprovalStatusExpense>any());
        verify(expense).setMerchantName(Mockito.<String>any());
        verify(expense).setPotentialDuplicate(Mockito.<Boolean>any());
        verify(expense).setReportTitle(Mockito.<String>any());
        verify(expense).setReports(Mockito.<Reports>any());
    }


    @Test
    void testUpdateExpenses4() throws UnsupportedEncodingException {
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
        Expense expense = mock(Expense.class);
        when(expense.getIsHidden()).thenReturn(true);
        when(expense.getIsReported()).thenReturn(false);
        doNothing().when(expense).setAmount(Mockito.<Double>any());
        doNothing().when(expense).setAmountApproved(Mockito.<Double>any());
        doNothing().when(expense).setCategory(Mockito.<Category>any());
        doNothing().when(expense).setCategoryDescription(Mockito.<String>any());
        doNothing().when(expense).setDate(Mockito.<LocalDate>any());
        doNothing().when(expense).setDateCreated(Mockito.<LocalDateTime>any());
        doNothing().when(expense).setDescription(Mockito.<String>any());
        doNothing().when(expense).setEmployee(Mockito.<Employee>any());
        doNothing().when(expense).setExpenseId(Mockito.<Long>any());
        doNothing().when(expense).setFile(Mockito.<byte[]>any());
        doNothing().when(expense).setFileName(Mockito.<String>any());
        doNothing().when(expense).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(expense).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(expense).setIsReported(Mockito.<Boolean>any());
        doNothing().when(expense).setManagerApprovalStatusExpense(Mockito.<ManagerApprovalStatusExpense>any());
        doNothing().when(expense).setMerchantName(Mockito.<String>any());
        doNothing().when(expense).setPotentialDuplicate(Mockito.<Boolean>any());
        doNothing().when(expense).setReportTitle(Mockito.<String>any());
        doNothing().when(expense).setReports(Mockito.<Reports>any());
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("Category Description");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("The characteristics of someone or something");
        expense.setEmployee(employee);
        expense.setExpenseId(1L);
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        Optional<Expense> ofResult = Optional.of(expense);
        when(expenseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.updateExpenses(new ExpenseDTO(), 1L));
        verify(expenseRepository).findById(Mockito.<Long>any());
        verify(expense, atLeast(1)).getIsHidden();
        verify(expense).getIsReported();
        verify(expense).setAmount(Mockito.<Double>any());
        verify(expense).setAmountApproved(Mockito.<Double>any());
        verify(expense).setCategory(Mockito.<Category>any());
        verify(expense).setCategoryDescription(Mockito.<String>any());
        verify(expense).setDate(Mockito.<LocalDate>any());
        verify(expense).setDateCreated(Mockito.<LocalDateTime>any());
        verify(expense).setDescription(Mockito.<String>any());
        verify(expense).setEmployee(Mockito.<Employee>any());
        verify(expense).setExpenseId(Mockito.<Long>any());
        verify(expense).setFile(Mockito.<byte[]>any());
        verify(expense).setFileName(Mockito.<String>any());
        verify(expense).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(expense).setIsHidden(Mockito.<Boolean>any());
        verify(expense).setIsReported(Mockito.<Boolean>any());
        verify(expense).setManagerApprovalStatusExpense(Mockito.<ManagerApprovalStatusExpense>any());
        verify(expense).setMerchantName(Mockito.<String>any());
        verify(expense).setPotentialDuplicate(Mockito.<Boolean>any());
        verify(expense).setReportTitle(Mockito.<String>any());
        verify(expense).setReports(Mockito.<Reports>any());
    }

    @Test
    void testGetExpensesByEmployeeId() {
        // Arrange
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
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ArrayList<Expense> expenseList = new ArrayList<>();
        when(expenseRepository.findByEmployeeAndIsReported(Mockito.<Employee>any(), anyBoolean()))
                .thenReturn(expenseList);

        // Act
        List<Expense> actualExpensesByEmployeeId = expenseServiceImpl.getExpensesByEmployeeId(1L);

        // Assert
        assertSame(expenseList, actualExpensesByEmployeeId);
        assertTrue(actualExpensesByEmployeeId.isEmpty());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(expenseRepository).findByEmployeeAndIsReported(Mockito.<Employee>any(), anyBoolean());
    }

    @Test
    void testGetExpensesByEmployeeId2() {
        // Arrange
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
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(expenseRepository.findByEmployeeAndIsReported(Mockito.<Employee>any(), anyBoolean()))
                .thenThrow(new IllegalStateException("foo"));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.getExpensesByEmployeeId(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(expenseRepository).findByEmployeeAndIsReported(Mockito.<Employee>any(), anyBoolean());
    }


    @Test
    void testRemoveTaggedExpense() throws UnsupportedEncodingException {
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
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        Optional<Expense> ofResult = Optional.of(expense);
        when(expenseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.removeTaggedExpense(1L));
        verify(expenseRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testGetExpenseByReportId() {
        // Arrange
        ArrayList<Expense> expenseList = new ArrayList<>();
        when(expenseRepository.findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any()))
                .thenReturn(expenseList);

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
        Optional<Reports> ofResult = Optional.of(reports);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        List<Expense> actualExpenseByReportId = expenseServiceImpl.getExpenseByReportId(1L);

        // Assert
        assertSame(expenseList, actualExpenseByReportId);
        assertTrue(actualExpenseByReportId.isEmpty());
        verify(expenseRepository).findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testGetExpenseByReportId2() {
        // Arrange
        when(expenseRepository.findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any()))
                .thenThrow(new IllegalStateException("foo"));

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
        Optional<Reports> ofResult = Optional.of(reports);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.getExpenseByReportId(1L));
        verify(expenseRepository).findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testHideExpense() throws UnsupportedEncodingException {
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
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        Optional<Expense> ofResult = Optional.of(expense);
        when(expenseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.hideExpense(1L));
        verify(expenseRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testHideExpense2() throws UnsupportedEncodingException {
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
        Expense expense = mock(Expense.class);
        when(expense.getReportTitle()).thenThrow(new IllegalStateException("foo"));
        when(expense.getIsReported()).thenReturn(true);
        doNothing().when(expense).setAmount(Mockito.<Double>any());
        doNothing().when(expense).setAmountApproved(Mockito.<Double>any());
        doNothing().when(expense).setCategory(Mockito.<Category>any());
        doNothing().when(expense).setCategoryDescription(Mockito.<String>any());
        doNothing().when(expense).setDate(Mockito.<LocalDate>any());
        doNothing().when(expense).setDateCreated(Mockito.<LocalDateTime>any());
        doNothing().when(expense).setDescription(Mockito.<String>any());
        doNothing().when(expense).setEmployee(Mockito.<Employee>any());
        doNothing().when(expense).setExpenseId(Mockito.<Long>any());
        doNothing().when(expense).setFile(Mockito.<byte[]>any());
        doNothing().when(expense).setFileName(Mockito.<String>any());
        doNothing().when(expense).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(expense).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(expense).setIsReported(Mockito.<Boolean>any());
        doNothing().when(expense).setManagerApprovalStatusExpense(Mockito.<ManagerApprovalStatusExpense>any());
        doNothing().when(expense).setMerchantName(Mockito.<String>any());
        doNothing().when(expense).setPotentialDuplicate(Mockito.<Boolean>any());
        doNothing().when(expense).setReportTitle(Mockito.<String>any());
        doNothing().when(expense).setReports(Mockito.<Reports>any());
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("Category Description");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("The characteristics of someone or something");
        expense.setEmployee(employee);
        expense.setExpenseId(1L);
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        Optional<Expense> ofResult = Optional.of(expense);
        when(expenseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.hideExpense(1L));
        verify(expenseRepository).findById(Mockito.<Long>any());
        verify(expense, atLeast(1)).getIsReported();
        verify(expense).getReportTitle();
        verify(expense).setAmount(Mockito.<Double>any());
        verify(expense).setAmountApproved(Mockito.<Double>any());
        verify(expense).setCategory(Mockito.<Category>any());
        verify(expense).setCategoryDescription(Mockito.<String>any());
        verify(expense).setDate(Mockito.<LocalDate>any());
        verify(expense).setDateCreated(Mockito.<LocalDateTime>any());
        verify(expense).setDescription(Mockito.<String>any());
        verify(expense).setEmployee(Mockito.<Employee>any());
        verify(expense).setExpenseId(Mockito.<Long>any());
        verify(expense).setFile(Mockito.<byte[]>any());
        verify(expense).setFileName(Mockito.<String>any());
        verify(expense).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(expense).setIsHidden(Mockito.<Boolean>any());
        verify(expense).setIsReported(Mockito.<Boolean>any());
        verify(expense).setManagerApprovalStatusExpense(Mockito.<ManagerApprovalStatusExpense>any());
        verify(expense).setMerchantName(Mockito.<String>any());
        verify(expense).setPotentialDuplicate(Mockito.<Boolean>any());
        verify(expense).setReportTitle(Mockito.<String>any());
        verify(expense).setReports(Mockito.<Reports>any());
    }

    @Test
    void testSendExpenseReminder() {
        // Arrange
        doNothing().when(iEmailService).reminderMailToEmployee(Mockito.<List<Long>>any());
        when(expenseRepository.findByIsReportedAndIsHidden(anyBoolean(), anyBoolean())).thenReturn(new ArrayList<>());

        // Act
        expenseServiceImpl.sendExpenseReminder();

        // Assert that nothing has changed
        verify(iEmailService).reminderMailToEmployee(Mockito.<List<Long>>any());
        verify(expenseRepository).findByIsReportedAndIsHidden(anyBoolean(), anyBoolean());
        assertTrue(expenseServiceImpl.getAllExpenses().isEmpty());
    }

    @Test
    void testSendExpenseReminder2() {
        // Arrange
        when(expenseRepository.findByIsReportedAndIsHidden(anyBoolean(), anyBoolean()))
                .thenThrow(new IllegalStateException("foo"));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.sendExpenseReminder());
        verify(expenseRepository).findByIsReportedAndIsHidden(anyBoolean(), anyBoolean());
    }


    @Test
    void testSendExpenseReminder3() throws UnsupportedEncodingException {
        // Arrange
        doNothing().when(iEmailService).reminderMailToEmployee(Mockito.<List<Long>>any());

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
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
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
        when(expenseRepository.findByIsReportedAndIsHidden(anyBoolean(), anyBoolean())).thenReturn(expenseList);

        // Act
        expenseServiceImpl.sendExpenseReminder();

        // Assert that nothing has changed
        verify(iEmailService).reminderMailToEmployee(Mockito.<List<Long>>any());
        verify(expenseRepository).findByIsReportedAndIsHidden(anyBoolean(), anyBoolean());
        assertTrue(expenseServiceImpl.getAllExpenses().isEmpty());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#sendExpenseReminder()}
     */
    @Test
    void testSendExpenseReminder4() throws UnsupportedEncodingException {
        // Arrange
        doNothing().when(iEmailService).reminderMailToEmployee(Mockito.<List<Long>>any());

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
        Expense expense = mock(Expense.class);
        when(expense.getDate()).thenReturn(LocalDate.now());
        doNothing().when(expense).setAmount(Mockito.<Double>any());
        doNothing().when(expense).setAmountApproved(Mockito.<Double>any());
        doNothing().when(expense).setCategory(Mockito.<Category>any());
        doNothing().when(expense).setCategoryDescription(Mockito.<String>any());
        doNothing().when(expense).setDate(Mockito.<LocalDate>any());
        doNothing().when(expense).setDateCreated(Mockito.<LocalDateTime>any());
        doNothing().when(expense).setDescription(Mockito.<String>any());
        doNothing().when(expense).setEmployee(Mockito.<Employee>any());
        doNothing().when(expense).setExpenseId(Mockito.<Long>any());
        doNothing().when(expense).setFile(Mockito.<byte[]>any());
        doNothing().when(expense).setFileName(Mockito.<String>any());
        doNothing().when(expense).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(expense).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(expense).setIsReported(Mockito.<Boolean>any());
        doNothing().when(expense).setManagerApprovalStatusExpense(Mockito.<ManagerApprovalStatusExpense>any());
        doNothing().when(expense).setMerchantName(Mockito.<String>any());
        doNothing().when(expense).setPotentialDuplicate(Mockito.<Boolean>any());
        doNothing().when(expense).setReportTitle(Mockito.<String>any());
        doNothing().when(expense).setReports(Mockito.<Reports>any());
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("Category Description");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("The characteristics of someone or something");
        expense.setEmployee(employee);
        expense.setExpenseId(1L);
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
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
        when(expenseRepository.findByIsReportedAndIsHidden(anyBoolean(), anyBoolean())).thenReturn(expenseList);

        // Act
        expenseServiceImpl.sendExpenseReminder();

        // Assert
        verify(iEmailService).reminderMailToEmployee(Mockito.<List<Long>>any());
        verify(expenseRepository).findByIsReportedAndIsHidden(anyBoolean(), anyBoolean());
        verify(expense).getDate();
        verify(expense).setAmount(Mockito.<Double>any());
        verify(expense).setAmountApproved(Mockito.<Double>any());
        verify(expense).setCategory(Mockito.<Category>any());
        verify(expense).setCategoryDescription(Mockito.<String>any());
        verify(expense).setDate(Mockito.<LocalDate>any());
        verify(expense).setDateCreated(Mockito.<LocalDateTime>any());
        verify(expense).setDescription(Mockito.<String>any());
        verify(expense).setEmployee(Mockito.<Employee>any());
        verify(expense).setExpenseId(Mockito.<Long>any());
        verify(expense).setFile(Mockito.<byte[]>any());
        verify(expense).setFileName(Mockito.<String>any());
        verify(expense).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(expense).setIsHidden(Mockito.<Boolean>any());
        verify(expense).setIsReported(Mockito.<Boolean>any());
        verify(expense).setManagerApprovalStatusExpense(Mockito.<ManagerApprovalStatusExpense>any());
        verify(expense).setMerchantName(Mockito.<String>any());
        verify(expense).setPotentialDuplicate(Mockito.<Boolean>any());
        verify(expense).setReportTitle(Mockito.<String>any());
        verify(expense).setReports(Mockito.<Reports>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getRejectedExpensesByReportId(Long)}
     */
    @Test
    void testGetRejectedExpensesByReportId() {
        // Arrange
        when(expenseRepository.findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any()))
                .thenReturn(new ArrayList<>());

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
        Optional<Reports> ofResult = Optional.of(reports);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertTrue(expenseServiceImpl.getRejectedExpensesByReportId(1L).isEmpty());
        verify(expenseRepository).findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getRejectedExpensesByReportId(Long)}
     */
    @Test
    void testGetRejectedExpensesByReportId2() {
        // Arrange
        when(expenseRepository.findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any()))
                .thenThrow(new IllegalStateException("foo"));

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
        Optional<Reports> ofResult = Optional.of(reports);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.getRejectedExpensesByReportId(1L));
        verify(expenseRepository).findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getRejectedExpensesByReportId(Long)}
     */
    @Test
    void testGetRejectedExpensesByReportId3() throws UnsupportedEncodingException {
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
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
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
        when(expenseRepository.findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any()))
                .thenReturn(expenseList);

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
        Optional<Reports> ofResult = Optional.of(reports2);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertTrue(expenseServiceImpl.getRejectedExpensesByReportId(1L).isEmpty());
        verify(expenseRepository).findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getRejectedExpensesByReportId(Long)}
     */
    @Test
    void testGetRejectedExpensesByReportId4() throws UnsupportedEncodingException {
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
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
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
        expense2.setFile("AXAXAXAX".getBytes("UTF-8"));
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
        when(expenseRepository.findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any()))
                .thenReturn(expenseList);

        Reports reports3 = new Reports();
        reports3.setDateCreated(LocalDate.of(1970, 1, 1));
        reports3.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports3.setEmployeeId(1L);
        reports3.setEmployeeMail("Employee Mail");
        reports3.setEmployeeName("Employee Name");
        reports3.setExpensesCount(3L);
        reports3.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports3.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports3.setFinanceComments("Finance Comments");
        reports3.setIsHidden(true);
        reports3.setIsSubmitted(true);
        reports3.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports3.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports3.setManagerComments("Manager Comments");
        reports3.setManagerEmail("jane.doe@example.org");
        reports3.setManagerReviewTime("Manager Review Time");
        reports3.setOfficialEmployeeId("42");
        reports3.setReportId(1L);
        reports3.setReportTitle("Dr");
        reports3.setTotalAmount(10.0f);
        reports3.setTotalApprovedAmount(10.0f);
        Optional<Reports> ofResult = Optional.of(reports3);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertEquals(1, expenseServiceImpl.getRejectedExpensesByReportId(1L).size());
        verify(expenseRepository).findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }
}

