package com.nineleaps.expense_management_project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.nineleaps.expense_management_project.dto.ExpenseDTO;
import com.nineleaps.expense_management_project.entity.Category;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.entity.Expense;
import com.nineleaps.expense_management_project.entity.FinanceApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatusExpense;
import com.nineleaps.expense_management_project.entity.Reports;
import com.nineleaps.expense_management_project.firebase.PushNotificationRequest;
import com.nineleaps.expense_management_project.firebase.PushNotificationService;
import com.nineleaps.expense_management_project.repository.CategoryRepository;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;

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
        when(expenseRepository.findByEmployeeAndAmountAndDateAndCategoryAndMerchantNameAndIsHidden(Mockito.<Employee>any(), Mockito.<Double>any(), Mockito.<LocalDate>any(), Mockito.<Category>any(), Mockito.<String>any(), anyBoolean())).thenReturn(new ArrayList<>());

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setDate(LocalDate.now());
        assertEquals("Expense Added!", expenseServiceImpl.addExpense(expenseDTO, 1L, 1L));
        verify(categoryRepository).getCategoryByCategoryId(Mockito.<Long>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(expenseRepository).save(Mockito.<Expense>any());
        verify(expenseRepository).findByEmployeeAndAmountAndDateAndCategoryAndMerchantNameAndIsHidden(Mockito.<Employee>any(), Mockito.<Double>any(), Mockito.<LocalDate>any(), Mockito.<Category>any(), Mockito.<String>any(), anyBoolean());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#addExpense(ExpenseDTO, Long, Long)}
     */
    @Test
    void testAddExpense2() throws IllegalAccessException {
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
        when(expenseRepository.findByEmployeeAndAmountAndDateAndCategoryAndMerchantNameAndIsHidden(Mockito.<Employee>any(), Mockito.<Double>any(), Mockito.<LocalDate>any(), Mockito.<Category>any(), Mockito.<String>any(), anyBoolean())).thenThrow(new IllegalStateException("Expense Added!"));

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setDate(LocalDate.now());
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.addExpense(expenseDTO, 1L, 1L));
        verify(categoryRepository).getCategoryByCategoryId(Mockito.<Long>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(expenseRepository).findByEmployeeAndAmountAndDateAndCategoryAndMerchantNameAndIsHidden(Mockito.<Employee>any(), Mockito.<Double>any(), Mockito.<LocalDate>any(), Mockito.<Category>any(), Mockito.<String>any(), anyBoolean());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#addPotentialDuplicateExpense(ExpenseDTO, Long, Long)}
     */
    @Test
    void testAddPotentialDuplicateExpense() throws UnsupportedEncodingException {
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
        assertSame(expense, expenseServiceImpl.addPotentialDuplicateExpense(expenseDTO, 1L, 1L));
        verify(categoryRepository).getCategoryByCategoryId(Mockito.<Long>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(expenseRepository).save(Mockito.<Expense>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#addPotentialDuplicateExpense(ExpenseDTO, Long, Long)}
     */
    @Test
    void testAddPotentialDuplicateExpense2() {
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
        when(expenseRepository.save(Mockito.<Expense>any())).thenThrow(new EntityNotFoundException("An error occurred"));

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setDate(LocalDate.now());
        assertThrows(EntityNotFoundException.class, () -> expenseServiceImpl.addPotentialDuplicateExpense(expenseDTO, 1L, 1L));
        verify(categoryRepository).getCategoryByCategoryId(Mockito.<Long>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(expenseRepository).save(Mockito.<Expense>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getAllExpenses()}
     */
    @Test
    void testGetAllExpenses() {
        when(expenseRepository.findByIsReportedAndDateBefore(anyBoolean(), Mockito.<LocalDate>any())).thenReturn(new ArrayList<>());
        ArrayList<Expense> expenseList = new ArrayList<>();
        when(expenseRepository.findAll()).thenReturn(expenseList);
        List<Expense> actualAllExpenses = expenseServiceImpl.getAllExpenses();
        assertSame(expenseList, actualAllExpenses);
        assertTrue(actualAllExpenses.isEmpty());
        verify(expenseRepository).findByIsReportedAndDateBefore(anyBoolean(), Mockito.<LocalDate>any());
        verify(expenseRepository).findAll();
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getAllExpenses()}
     */
    @Test
    void testGetAllExpenses2() throws UnsupportedEncodingException {
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
        when(expenseRepository.findByIsReportedAndDateBefore(anyBoolean(), Mockito.<LocalDate>any())).thenReturn(expenseList);
        ArrayList<Expense> expenseList2 = new ArrayList<>();
        when(expenseRepository.findAll()).thenReturn(expenseList2);
        List<Expense> actualAllExpenses = expenseServiceImpl.getAllExpenses();
        assertSame(expenseList2, actualAllExpenses);
        assertTrue(actualAllExpenses.isEmpty());
        verify(expenseRepository).save(Mockito.<Expense>any());
        verify(expenseRepository).findByIsReportedAndDateBefore(anyBoolean(), Mockito.<LocalDate>any());
        verify(expenseRepository).findAll();
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getExpenseById(Long)}
     */
    @Test
    void testGetExpenseById() throws UnsupportedEncodingException {
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
        assertSame(expense, expenseServiceImpl.getExpenseById(1L));
        verify(expenseRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#updateExpense(Long, Long)}
     */
    @Test
    void testUpdateExpense() throws UnsupportedEncodingException {
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
        assertSame(expense2, expenseServiceImpl.updateExpense(1L, 1L));
        verify(expenseRepository).save(Mockito.<Expense>any());
        verify(expenseRepository).findById(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#updateExpense(Long, Long)}
     */
    @Test
    void testUpdateExpense2() throws UnsupportedEncodingException {
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
        when(expenseRepository.save(Mockito.<Expense>any())).thenThrow(new EntityNotFoundException("An error occurred"));
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
        assertThrows(EntityNotFoundException.class, () -> expenseServiceImpl.updateExpense(1L, 1L));
        verify(expenseRepository).save(Mockito.<Expense>any());
        verify(expenseRepository).findById(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#deleteExpenseById(Long)}
     */
    @Test
    void testDeleteExpenseById() {
        doNothing().when(expenseRepository).deleteById(Mockito.<Long>any());
        expenseServiceImpl.deleteExpenseById(1L);
        verify(expenseRepository).deleteById(Mockito.<Long>any());
        assertTrue(expenseServiceImpl.getAllExpenses().isEmpty());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#deleteExpenseById(Long)}
     */
    @Test
    void testDeleteExpenseById2() {
        doThrow(new EntityNotFoundException("An error occurred")).when(expenseRepository).deleteById(Mockito.<Long>any());
        assertThrows(EntityNotFoundException.class, () -> expenseServiceImpl.deleteExpenseById(1L));
        verify(expenseRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getExpenseByEmployeeId(Long)}
     */
    @Test
    void testGetExpenseByEmployeeId() {
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
        when(expenseRepository.findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any())).thenReturn(expenseList);
        List<Expense> actualExpenseByEmployeeId = expenseServiceImpl.getExpenseByEmployeeId(1L);
        assertSame(expenseList, actualExpenseByEmployeeId);
        assertTrue(actualExpenseByEmployeeId.isEmpty());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(expenseRepository).findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getExpenseByEmployeeId(Long)}
     */
    @Test
    void testGetExpenseByEmployeeId2() {
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
        when(expenseRepository.findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any())).thenThrow(new EntityNotFoundException("An error occurred"));
        assertThrows(EntityNotFoundException.class, () -> expenseServiceImpl.getExpenseByEmployeeId(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(expenseRepository).findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getExpenseByEmployeeId(Long)}
     */
    @Test
    void testGetExpenseByEmployeeId3() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertTrue(expenseServiceImpl.getExpenseByEmployeeId(1L).isEmpty());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#updateExpenses(ExpenseDTO, Long)}
     */
    @Test
    void testUpdateExpenses() throws UnsupportedEncodingException {
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
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.updateExpenses(new ExpenseDTO(), 1L));
        verify(expenseRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#updateExpenses(ExpenseDTO, Long)}
     */
    @Test
    void testUpdateExpenses2() throws UnsupportedEncodingException {
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
        when(expense.getManagerApprovalStatusExpense()).thenThrow(new EntityNotFoundException("An error occurred"));
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
        assertThrows(EntityNotFoundException.class, () -> expenseServiceImpl.updateExpenses(new ExpenseDTO(), 1L));
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

    /**
     * Method under test: {@link ExpenseServiceImpl#getExpensesByEmployeeId(Long)}
     */
    @Test
    void testGetExpensesByEmployeeId() {
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
        when(expenseRepository.findByEmployeeAndIsReported(Mockito.<Employee>any(), anyBoolean())).thenReturn(expenseList);
        List<Expense> actualExpensesByEmployeeId = expenseServiceImpl.getExpensesByEmployeeId(1L);
        assertSame(expenseList, actualExpensesByEmployeeId);
        assertTrue(actualExpensesByEmployeeId.isEmpty());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(expenseRepository).findByEmployeeAndIsReported(Mockito.<Employee>any(), anyBoolean());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getExpensesByEmployeeId(Long)}
     */
    @Test
    void testGetExpensesByEmployeeId2() {
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
        when(expenseRepository.findByEmployeeAndIsReported(Mockito.<Employee>any(), anyBoolean())).thenThrow(new EntityNotFoundException("An error occurred"));
        assertThrows(EntityNotFoundException.class, () -> expenseServiceImpl.getExpensesByEmployeeId(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(expenseRepository).findByEmployeeAndIsReported(Mockito.<Employee>any(), anyBoolean());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getExpensesByEmployeeId(Long)}
     */
    @Test
    void testGetExpensesByEmployeeId3() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertTrue(expenseServiceImpl.getExpensesByEmployeeId(1L).isEmpty());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#removeTaggedExpense(Long)}
     */
    @Test
    void testRemoveTaggedExpense() throws UnsupportedEncodingException {
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
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.removeTaggedExpense(1L));
        verify(expenseRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getExpenseByReportId(Long)}
     */
    @Test
    void testGetExpenseByReportId() {
        ArrayList<Expense> expenseList = new ArrayList<>();
        when(expenseRepository.findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any())).thenReturn(expenseList);

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
        List<Expense> actualExpenseByReportId = expenseServiceImpl.getExpenseByReportId(1L);
        assertSame(expenseList, actualExpenseByReportId);
        assertTrue(actualExpenseByReportId.isEmpty());
        verify(expenseRepository).findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getExpenseByReportId(Long)}
     */
    @Test
    void testGetExpenseByReportId2() {
        when(expenseRepository.findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any())).thenThrow(new EntityNotFoundException("An error occurred"));

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
        assertThrows(EntityNotFoundException.class, () -> expenseServiceImpl.getExpenseByReportId(1L));
        verify(expenseRepository).findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getExpenseByReportId(Long)}
     */
    @Test
    void testGetExpenseByReportId3() {
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertTrue(expenseServiceImpl.getExpenseByReportId(1L).isEmpty());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#hideExpense(Long)}
     */
    @Test
    void testHideExpense() throws UnsupportedEncodingException {
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
        assertThrows(IllegalStateException.class, () -> expenseServiceImpl.hideExpense(1L));
        verify(expenseRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#hideExpense(Long)}
     */
    @Test
    void testHideExpense2() throws UnsupportedEncodingException {
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
        when(expense.getReportTitle()).thenThrow(new EntityNotFoundException("An error occurred"));
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
        assertThrows(EntityNotFoundException.class, () -> expenseServiceImpl.hideExpense(1L));
        verify(expenseRepository).findById(Mockito.<Long>any());
        verify(expense).getIsReported();
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

//    @Test
//    void testSendExpenseReminder() {
//        doNothing().when(iEmailService).reminderMailToEmployee(Mockito.<List<Long>>any());
//        when(expenseRepository.findByIsReportedAndIsHidden(anyBoolean(), anyBoolean())).thenReturn(new ArrayList<>());
//        expenseServiceImpl.sendExpenseReminder();
//        verify(iEmailService).reminderMailToEmployee(Mockito.<List<Long>>any());
//        verify(expenseRepository).findByIsReportedAndIsHidden(anyBoolean(), anyBoolean());
//        assertTrue(expenseServiceImpl.getAllExpenses().isEmpty());
//    }

//    @Test
//    void testSendExpenseReminder2() {
//        when(expenseRepository.findByIsReportedAndIsHidden(anyBoolean(), anyBoolean())).thenThrow(new EntityNotFoundException("An error occurred"));
//        assertThrows(EntityNotFoundException.class, () -> expenseServiceImpl.sendExpenseReminder());
//        verify(expenseRepository).findByIsReportedAndIsHidden(anyBoolean(), anyBoolean());
//    }


//    @Test
//    void testSendExpenseReminder3() throws UnsupportedEncodingException {
//        doNothing().when(iEmailService).reminderMailToEmployee(Mockito.<List<Long>>any());
//
//        Category category = new Category();
//        category.setCategoryDescription("Category Description");
//        category.setCategoryId(1L);
//        category.setCategoryTotal(1L);
//        category.setExpenseList(new ArrayList<>());
//        category.setIsHidden(true);
//
//        Employee employee = new Employee();
//        employee.setEmployeeEmail("jane.doe@example.org");
//        employee.setEmployeeId(1L);
//        employee.setExpenseList(new ArrayList<>());
//        employee.setFirstName("Jane");
//        employee.setHrEmail("jane.doe@example.org");
//        employee.setHrName("Hr Name");
//        employee.setImageUrl("https://example.org/example");
//        employee.setIsFinanceAdmin(true);
//        employee.setIsHidden(true);
//        employee.setLastName("Doe");
//        employee.setLndEmail("jane.doe@example.org");
//        employee.setLndName("Lnd Name");
//        employee.setManagerEmail("jane.doe@example.org");
//        employee.setManagerName("Manager Name");
//        employee.setMiddleName("Middle Name");
//        employee.setMobileNumber(1L);
//        employee.setOfficialEmployeeId("42");
//        employee.setRole("Role");
//        employee.setToken("ABC123");
//
//        Reports reports = new Reports();
//        reports.setDateCreated(LocalDate.of(1970, 1, 1));
//        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
//        reports.setEmployeeId(1L);
//        reports.setEmployeeMail("Employee Mail");
//        reports.setEmployeeName("Employee Name");
//        reports.setExpensesCount(3L);
//        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
//        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
//        reports.setFinanceComments("Finance Comments");
//        reports.setIsHidden(true);
//        reports.setIsSubmitted(true);
//        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
//        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
//        reports.setManagerComments("Manager Comments");
//        reports.setManagerEmail("jane.doe@example.org");
//        reports.setManagerReviewTime("Manager Review Time");
//        reports.setOfficialEmployeeId("42");
//        reports.setReportId(1L);
//        reports.setReportTitle("Dr");
//        reports.setTotalAmount(10.0f);
//        reports.setTotalApprovedAmount(10.0f);
//
//        Expense expense = new Expense();
//        expense.setAmount(10.0d);
//        expense.setAmountApproved(10.0d);
//        expense.setCategory(category);
//        expense.setCategoryDescription("Category Description");
//        expense.setDate(LocalDate.of(1970, 1, 1));
//        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
//        expense.setDescription("The characteristics of someone or something");
//        expense.setEmployee(employee);
//        expense.setExpenseId(1L);
//        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
//        expense.setFileName("foo.txt");
//        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
//        expense.setIsHidden(true);
//        expense.setIsReported(true);
//        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
//        expense.setMerchantName("Merchant Name");
//        expense.setPotentialDuplicate(true);
//        expense.setReportTitle("Dr");
//        expense.setReports(reports);
//
//        ArrayList<Expense> expenseList = new ArrayList<>();
//        expenseList.add(expense);
//        when(expenseRepository.findByIsReportedAndIsHidden(anyBoolean(), anyBoolean())).thenReturn(expenseList);
//        //expenseServiceImpl.sendExpenseReminder();
//        verify(iEmailService).reminderMailToEmployee(Mockito.<List<Long>>any());
//        verify(expenseRepository).findByIsReportedAndIsHidden(anyBoolean(), anyBoolean());
//        assertTrue(expenseServiceImpl.getAllExpenses().isEmpty());
//    }
//
//
//    @Test
//    void testSendExpenseReminder4() throws UnsupportedEncodingException {
//        doNothing().when(iEmailService).reminderMailToEmployee(Mockito.<List<Long>>any());
//
//        Category category = new Category();
//        category.setCategoryDescription("Category Description");
//        category.setCategoryId(1L);
//        category.setCategoryTotal(1L);
//        category.setExpenseList(new ArrayList<>());
//        category.setIsHidden(true);
//
//        Employee employee = new Employee();
//        employee.setEmployeeEmail("jane.doe@example.org");
//        employee.setEmployeeId(1L);
//        employee.setExpenseList(new ArrayList<>());
//        employee.setFirstName("Jane");
//        employee.setHrEmail("jane.doe@example.org");
//        employee.setHrName("Hr Name");
//        employee.setImageUrl("https://example.org/example");
//        employee.setIsFinanceAdmin(true);
//        employee.setIsHidden(true);
//        employee.setLastName("Doe");
//        employee.setLndEmail("jane.doe@example.org");
//        employee.setLndName("Lnd Name");
//        employee.setManagerEmail("jane.doe@example.org");
//        employee.setManagerName("Manager Name");
//        employee.setMiddleName("Middle Name");
//        employee.setMobileNumber(1L);
//        employee.setOfficialEmployeeId("42");
//        employee.setRole("Role");
//        employee.setToken("ABC123");
//
//        Reports reports = new Reports();
//        reports.setDateCreated(LocalDate.of(1970, 1, 1));
//        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
//        reports.setEmployeeId(1L);
//        reports.setEmployeeMail("Employee Mail");
//        reports.setEmployeeName("Employee Name");
//        reports.setExpensesCount(3L);
//        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
//        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
//        reports.setFinanceComments("Finance Comments");
//        reports.setIsHidden(true);
//        reports.setIsSubmitted(true);
//        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
//        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
//        reports.setManagerComments("Manager Comments");
//        reports.setManagerEmail("jane.doe@example.org");
//        reports.setManagerReviewTime("Manager Review Time");
//        reports.setOfficialEmployeeId("42");
//        reports.setReportId(1L);
//        reports.setReportTitle("Dr");
//        reports.setTotalAmount(10.0f);
//        reports.setTotalApprovedAmount(10.0f);
//        Expense expense = mock(Expense.class);
//        when(expense.getDate()).thenReturn(LocalDate.now());
//        doNothing().when(expense).setAmount(Mockito.<Double>any());
//        doNothing().when(expense).setAmountApproved(Mockito.<Double>any());
//        doNothing().when(expense).setCategory(Mockito.<Category>any());
//        doNothing().when(expense).setCategoryDescription(Mockito.<String>any());
//        doNothing().when(expense).setDate(Mockito.<LocalDate>any());
//        doNothing().when(expense).setDateCreated(Mockito.<LocalDateTime>any());
//        doNothing().when(expense).setDescription(Mockito.<String>any());
//        doNothing().when(expense).setEmployee(Mockito.<Employee>any());
//        doNothing().when(expense).setExpenseId(Mockito.<Long>any());
//        doNothing().when(expense).setFile(Mockito.<byte[]>any());
//        doNothing().when(expense).setFileName(Mockito.<String>any());
//        doNothing().when(expense).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
//        doNothing().when(expense).setIsHidden(Mockito.<Boolean>any());
//        doNothing().when(expense).setIsReported(Mockito.<Boolean>any());
//        doNothing().when(expense).setManagerApprovalStatusExpense(Mockito.<ManagerApprovalStatusExpense>any());
//        doNothing().when(expense).setMerchantName(Mockito.<String>any());
//        doNothing().when(expense).setPotentialDuplicate(Mockito.<Boolean>any());
//        doNothing().when(expense).setReportTitle(Mockito.<String>any());
//        doNothing().when(expense).setReports(Mockito.<Reports>any());
//        expense.setAmount(10.0d);
//        expense.setAmountApproved(10.0d);
//        expense.setCategory(category);
//        expense.setCategoryDescription("Category Description");
//        expense.setDate(LocalDate.of(1970, 1, 1));
//        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
//        expense.setDescription("The characteristics of someone or something");
//        expense.setEmployee(employee);
//        expense.setExpenseId(1L);
//        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
//        expense.setFileName("foo.txt");
//        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
//        expense.setIsHidden(true);
//        expense.setIsReported(true);
//        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
//        expense.setMerchantName("Merchant Name");
//        expense.setPotentialDuplicate(true);
//        expense.setReportTitle("Dr");
//        expense.setReports(reports);
//
//        ArrayList<Expense> expenseList = new ArrayList<>();
//        expenseList.add(expense);
//        when(expenseRepository.findByIsReportedAndIsHidden(anyBoolean(), anyBoolean())).thenReturn(expenseList);
//        //expenseServiceImpl.sendExpenseReminder();
//        verify(iEmailService).reminderMailToEmployee(Mockito.<List<Long>>any());
//        verify(expenseRepository).findByIsReportedAndIsHidden(anyBoolean(), anyBoolean());
//        verify(expense).getDate();
//        verify(expense).setAmount(Mockito.<Double>any());
//        verify(expense).setAmountApproved(Mockito.<Double>any());
//        verify(expense).setCategory(Mockito.<Category>any());
//        verify(expense).setCategoryDescription(Mockito.<String>any());
//        verify(expense).setDate(Mockito.<LocalDate>any());
//        verify(expense).setDateCreated(Mockito.<LocalDateTime>any());
//        verify(expense).setDescription(Mockito.<String>any());
//        verify(expense).setEmployee(Mockito.<Employee>any());
//        verify(expense).setExpenseId(Mockito.<Long>any());
//        verify(expense).setFile(Mockito.<byte[]>any());
//        verify(expense).setFileName(Mockito.<String>any());
//        verify(expense).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
//        verify(expense).setIsHidden(Mockito.<Boolean>any());
//        verify(expense).setIsReported(Mockito.<Boolean>any());
//        verify(expense).setManagerApprovalStatusExpense(Mockito.<ManagerApprovalStatusExpense>any());
//        verify(expense).setMerchantName(Mockito.<String>any());
//        verify(expense).setPotentialDuplicate(Mockito.<Boolean>any());
//        verify(expense).setReportTitle(Mockito.<String>any());
//        verify(expense).setReports(Mockito.<Reports>any());
//    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getRejectedExpensesByReportId(Long)}
     */
    @Test
    void testGetRejectedExpensesByReportId() {
        when(expenseRepository.findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any())).thenReturn(new ArrayList<>());

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
        assertTrue(expenseServiceImpl.getRejectedExpensesByReportId(1L).isEmpty());
        verify(expenseRepository).findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getRejectedExpensesByReportId(Long)}
     */
    @Test
    void testGetRejectedExpensesByReportId2() {
        when(expenseRepository.findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any())).thenThrow(new EntityNotFoundException("An error occurred"));

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
        assertThrows(EntityNotFoundException.class, () -> expenseServiceImpl.getRejectedExpensesByReportId(1L));
        verify(expenseRepository).findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getRejectedExpensesByReportId(Long)}
     */
    @Test
    void testGetRejectedExpensesByReportId3() {
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
        expense.setFile(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
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
        when(expenseRepository.findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any())).thenReturn(expenseList);

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
        assertTrue(expenseServiceImpl.getRejectedExpensesByReportId(1L).isEmpty());
        verify(expenseRepository).findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getRejectedExpensesByReportId(Long)}
     */
    @Test
    void testGetRejectedExpensesByReportId4() {
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
        expense.setFile(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
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
        expense2.setFile(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
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
        when(expenseRepository.findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any())).thenReturn(expenseList);

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
        assertEquals(1, expenseServiceImpl.getRejectedExpensesByReportId(1L).size());
        verify(expenseRepository).findByReportsAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ExpenseServiceImpl#getRejectedExpensesByReportId(Long)}
     */
    @Test
    void testGetRejectedExpensesByReportId5() {
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertTrue(expenseServiceImpl.getRejectedExpensesByReportId(1L).isEmpty());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testUpdateExpenses_Success() {
        // Arrange
        Long expenseId = 1L;
        ExpenseDTO expenseDTO = new ExpenseDTO(/* provide necessary data */);
        Expense existingExpense = new Expense(/* create an existing expense */);

        // Mocking the repository method calls
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Expense updatedExpense = expenseServiceImpl.updateExpenses(expenseDTO, expenseId);

        // Assert
        assertNotNull(updatedExpense);
        assertEquals(expenseDTO.getMerchantName(), updatedExpense.getMerchantName());
        assertEquals(expenseDTO.getDate(), updatedExpense.getDate());
        assertEquals(expenseDTO.getAmount(), updatedExpense.getAmount());
        assertEquals(expenseDTO.getDescription(), updatedExpense.getDescription());
        assertEquals(expenseDTO.getFile(), updatedExpense.getFile());
    }

    @Test
    void testUpdateExpenses_ExpenseNotFound() {
        // Arrange
        Long expenseId = 1L;
        ExpenseDTO expenseDTO = new ExpenseDTO(/* provide necessary data */);

        // Mocking the repository method calls
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> {
            expenseServiceImpl.updateExpenses(expenseDTO, expenseId);
        });
    }

    @Test
    void testUpdateExpenses_ExpenseIsHidden() {
        // Arrange
        Long expenseId = 1L;
        ExpenseDTO expenseDTO = new ExpenseDTO(/* provide necessary data */);
        Expense existingExpense = new Expense(/* create an existing expense with hidden=true */);
        existingExpense.setIsHidden(true);

        // Mocking the repository method calls
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(existingExpense));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> {
            expenseServiceImpl.updateExpenses(expenseDTO, expenseId);
        });
    }


    @Test
    void testUpdateExpenses_ExpenseIsReported() {
        // Arrange
        Long expenseId = 1L;

        // Create an ExpenseDTO with necessary data
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setAmount(100.0);  // Set the amount
        expenseDTO.setDescription("Sample expense description");
        expenseDTO.setMerchantName("Sample Merchant");
        expenseDTO.setFile(new byte[]{1, 2, 3}); // Example byte array
        expenseDTO.setFileName("sample.pdf");
        expenseDTO.setDate(LocalDate.now()); // Set the date

        // Create an existing expense with isReported=true
        Expense existingExpense = new Expense();
        // You can set other properties of existingExpense if needed
        existingExpense.setIsReported(true);
        existingExpense.setIsHidden(true);
        existingExpense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
        // Mocking the repository method calls
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(existingExpense));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> {
            expenseServiceImpl.updateExpenses(expenseDTO, expenseId);
        });
    }


    @Test
    void testGetExpenseById_ExpenseNotFound() {
        // Arrange
        Long expenseId = 0L; // An ID that does not exist

        // Mock the behavior of expenseRepository.findById when the expense is not found
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> {
            expenseServiceImpl.getExpenseById(expenseId);
        });
    }

//    @Test
//    void testRemoveTaggedExpense_ExpenseExistsAndNotHidden() {
//        // Arrange
//        Long expenseId = 1L; // An existing expense ID
//        Expense existingExpense = new Expense();
//        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(existingExpense));
//
//        // Act
//        Expense result = expenseServiceImpl.removeTaggedExpense(expenseId);
//
//        // Assert
////        assertFalse(result.getIsReported());
//        //assertNull(result.getReports());
//        verify(expenseRepository, times(1)).save(existingExpense);
//    }

    @Test
    void testRemoveTaggedExpense_ExpenseExistsAndIsHidden() {
        // Arrange
        Long expenseId = 1L; // An existing expense ID
        Expense existingExpense = new Expense();
        existingExpense.setIsHidden(true);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(existingExpense));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> {
            expenseServiceImpl.removeTaggedExpense(expenseId);
        });
        verify(expenseRepository, never()).save(existingExpense);
    }

    @Test
    void testRemoveTaggedExpense_ExpenseDoesNotExist() {
        // Arrange
        Long expenseId = 1L; // An ID that does not exist
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> {
            expenseServiceImpl.removeTaggedExpense(expenseId);
        });
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    void testRemoveTaggedExpense_ExpenseNotHidden() {
        // Arrange
        Long expenseId = 1L; // An existing expense ID
        Expense existingExpense = new Expense();
        existingExpense.setIsHidden(false);

        // Mock the repository method calls
        Mockito.when(expenseRepository.findById(expenseId)).thenReturn(java.util.Optional.of(existingExpense));

        // Act
        Expense result = expenseServiceImpl.removeTaggedExpense(expenseId);

        // Assert
        Mockito.verify(expenseRepository, Mockito.times(1)).save(existingExpense);
        // You can add further assertions based on your use case
    }


}