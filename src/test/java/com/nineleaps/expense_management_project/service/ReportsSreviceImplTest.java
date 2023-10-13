package com.nineleaps.expense_management_project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.nineleaps.expense_management_project.controller.ReportsController;
import com.nineleaps.expense_management_project.dto.ReportsDTO;
import com.nineleaps.expense_management_project.entity.Category;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.entity.Expense;
import com.nineleaps.expense_management_project.entity.FinanceApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatusExpense;
import com.nineleaps.expense_management_project.entity.Reports;
import com.nineleaps.expense_management_project.firebase.PushNotificationRequest;
import com.nineleaps.expense_management_project.firebase.PushNotificationService;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;

import java.io.IOException;

import java.io.UnsupportedEncodingException;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;

import org.hibernate.ObjectNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



@ContextConfiguration(classes = {ReportsServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ReportsServiceImplTest {
    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private ExpenseRepository expenseRepository;

    @MockBean
    private IEmailService iEmailService;

    @MockBean
    private IEmployeeService iEmployeeService;

    @MockBean
    private IExpenseService iExpenseService;

    @MockBean
    private PushNotificationService pushNotificationService;

    @MockBean
    private ReportsRepository reportsRepository;

    @Autowired
    private ReportsServiceImpl reportsServiceImpl;

    @InjectMocks
    private ReportsController reportsController;

    @Mock
    private ReportsServiceImpl reportsService;

    private MockMvc mockMvc;

    /**
     * Method under test: {@link ReportsServiceImpl#getAllReports(Long)}
     */
    @Test
    void testGetAllReports() {
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
        when(expenseRepository.findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any())).thenReturn(new ArrayList<>());
        assertTrue(reportsServiceImpl.getAllReports(1L).isEmpty());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(expenseRepository).findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllReports(Long)}
     */
    @Test
    void testGetAllReports2() {
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
        when(expenseRepository.findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any())).thenThrow(new IllegalStateException("dateCreated"));
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getAllReports(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(expenseRepository).findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllReports(Long)}
     */
    @Test
    void testGetAllReports3() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertTrue(reportsServiceImpl.getAllReports(1L).isEmpty());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllReports(Long)}
     */
    @Test
    void testGetAllReports4() {
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

        Category category = new Category();
        category.setCategoryDescription("dateCreated");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);

        Employee employee2 = new Employee();
        employee2.setEmployeeEmail("jane.doe@example.org");
        employee2.setEmployeeId(1L);
        employee2.setExpenseList(new ArrayList<>());
        employee2.setFirstName("Jane");
        employee2.setHrEmail("jane.doe@example.org");
        employee2.setHrName("dateCreated");
        employee2.setImageUrl("https://example.org/example");
        employee2.setIsFinanceAdmin(true);
        employee2.setIsHidden(true);
        employee2.setLastName("Doe");
        employee2.setLndEmail("jane.doe@example.org");
        employee2.setLndName("dateCreated");
        employee2.setManagerEmail("jane.doe@example.org");
        employee2.setManagerName("dateCreated");
        employee2.setMiddleName("dateCreated");
        employee2.setMobileNumber(1L);
        employee2.setOfficialEmployeeId("42");
        employee2.setRole("dateCreated");
        employee2.setToken("ABC123");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("dateCreated");
        reports.setEmployeeName("dateCreated");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("dateCreated");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("dateCreated");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("dateCreated");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Expense expense = new Expense();
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("dateCreated");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("The characteristics of someone or something");
        expense.setEmployee(employee2);
        expense.setExpenseId(1L);
        expense.setFile(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("dateCreated");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(expenseRepository.findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any())).thenReturn(expenseList);
        assertEquals(1, reportsServiceImpl.getAllReports(1L).size());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(expenseRepository).findByEmployeeAndIsHidden(Mockito.<Employee>any(), anyBoolean(), Mockito.<Sort>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportById(Long)}
     */
    @Test
    void testGetReportById() {
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
        assertSame(reports, reportsServiceImpl.getReportById(1L));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportById(Long)}
     */
    @Test
    void testGetReportById2() {
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(RuntimeException.class, () -> reportsServiceImpl.getReportById(1L));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportById(Long)}
     */
    @Test
    void testGetReportById3() {
        when(reportsRepository.findById(Mockito.<Long>any())).thenThrow(new IllegalStateException("Report not found"));
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getReportById(1L));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#addReport(ReportsDTO, Long, List)}
     */
    @Test
    void testAddReport() {
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
        when(reportsRepository.save(Mockito.<Reports>any())).thenThrow(new RuntimeException("Report not found"));
        ReportsDTO reportsDTO = new ReportsDTO("Dr");
        assertThrows(RuntimeException.class, () -> reportsServiceImpl.addReport(reportsDTO, 1L, new ArrayList<>()));
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(reportsRepository).save(Mockito.<Reports>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#addReport(ReportsDTO, Long, List)}
     */
    @Test
    void testAddReport2() {
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
        when(expenseRepository.findAllById(Mockito.<Iterable<Long>>any())).thenReturn(new ArrayList<>());

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
        when(reportsRepository.save(Mockito.<Reports>any())).thenReturn(reports);
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        ReportsDTO reportsDTO = new ReportsDTO("Dr");
        assertThrows(RuntimeException.class, () -> reportsServiceImpl.addReport(reportsDTO, 1L, new ArrayList<>()));
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(expenseRepository).findAllById(Mockito.<Iterable<Long>>any());
        verify(reportsRepository).save(Mockito.<Reports>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#editReport(Long, String, String, List, List)}
     */
    @Test
    void testEditReport() {
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        ArrayList<Long> addExpenseIds = new ArrayList<>();
        assertThrows(RuntimeException.class, () -> reportsServiceImpl.editReport(1L, "Dr", "Report Description", addExpenseIds, new ArrayList<>()));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#addExpenseToReport(Long, List)}
     */
    @Test
    void testAddExpenseToReport() {
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(RuntimeException.class, () -> reportsServiceImpl.addExpenseToReport(1L, new ArrayList<>()));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportByEmpId(Long, String)}
     */
    @Test
    void testGetReportByEmpId() {
        assertThrows(IllegalArgumentException.class, () -> reportsServiceImpl.getReportByEmpId(1L, "Request"));
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportByEmpId(Long, String)}
     */
    @Test
    void testGetReportByEmpId2() {
        when(reportsRepository.getReportsByEmployeeIdAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<Long>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(new ArrayList<>());
        assertTrue(reportsServiceImpl.getReportByEmpId(1L, "approved").isEmpty());
        verify(reportsRepository, atLeast(1)).getReportsByEmployeeIdAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<Long>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportByEmpId(Long, String)}
     */
    @Test
    void testGetReportByEmpId3() {
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.getReportsByEmployeeIdAndIsSubmittedAndIsHidden(Mockito.<Long>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(reportsList);
        List<Reports> actualReportByEmpId = reportsServiceImpl.getReportByEmpId(1L, "drafts");
        assertSame(reportsList, actualReportByEmpId);
        assertTrue(actualReportByEmpId.isEmpty());
        verify(reportsRepository).getReportsByEmployeeIdAndIsSubmittedAndIsHidden(Mockito.<Long>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportByEmpId(Long, String)}
     */
    @Test
    void testGetReportByEmpId4() {
        when(reportsRepository.getReportsByEmployeeIdAndIsSubmittedAndIsHidden(Mockito.<Long>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenThrow(new IllegalStateException("drafts"));
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getReportByEmpId(1L, "drafts"));
        verify(reportsRepository).getReportsByEmployeeIdAndIsSubmittedAndIsHidden(Mockito.<Long>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportByEmpId(Long, String)}
     */
    @Test
    void testGetReportByEmpId5() {
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.getReportsByEmployeeIdAndManagerApprovalStatusAndIsHidden(Mockito.<Long>any(), Mockito.<ManagerApprovalStatus>any(), anyBoolean())).thenReturn(reportsList);
        List<Reports> actualReportByEmpId = reportsServiceImpl.getReportByEmpId(1L, "rejected");
        assertSame(reportsList, actualReportByEmpId);
        assertTrue(actualReportByEmpId.isEmpty());
        verify(reportsRepository).getReportsByEmployeeIdAndManagerApprovalStatusAndIsHidden(Mockito.<Long>any(), Mockito.<ManagerApprovalStatus>any(), anyBoolean());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsSubmittedToUser(String, String)}
     */
    @Test
    void testGetReportsSubmittedToUser() {
        assertThrows(IllegalArgumentException.class, () -> reportsServiceImpl.getReportsSubmittedToUser("jane.doe@example.org", "Request"));
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsSubmittedToUser(String, String)}
     */
    @Test
    void testGetReportsSubmittedToUser2() {
        when(reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(new ArrayList<>());
        assertTrue(reportsServiceImpl.getReportsSubmittedToUser("jane.doe@example.org", "approved").isEmpty());
        verify(reportsRepository, atLeast(1)).findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsSubmittedToUser(String, String)}
     */
    @Test
    void testGetReportsSubmittedToUser3() {
        when(reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenThrow(new IllegalStateException("approved"));
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getReportsSubmittedToUser("jane.doe@example.org", "approved"));
        verify(reportsRepository).findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsSubmittedToUser(String, String)}
     */
    @Test
    void testGetReportsSubmittedToUser4() {
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(reportsList);
        List<Reports> actualReportsSubmittedToUser =
                reportsServiceImpl.getReportsSubmittedToUser("jane.doe@example.org", "pending");
        assertSame(reportsList, actualReportsSubmittedToUser);
        assertTrue(actualReportsSubmittedToUser.isEmpty());
        verify(reportsRepository).findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsSubmittedToUser(String, String)}
     */
    @Test
    void testGetReportsSubmittedToUser5() {
        when(reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenThrow(new IllegalStateException("approved"));
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getReportsSubmittedToUser("jane.doe@example.org", "pending"));
        verify(reportsRepository).findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsSubmittedToUser(String, String)}
     */
    @Test
    void testGetReportsSubmittedToUser6() {
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsHidden(Mockito.<String>any(), Mockito.<ManagerApprovalStatus>any(), anyBoolean())).thenReturn(reportsList);
        List<Reports> actualReportsSubmittedToUser =
                reportsServiceImpl.getReportsSubmittedToUser("jane.doe@example.org", "rejected");
        assertSame(reportsList, actualReportsSubmittedToUser);
        assertTrue(actualReportsSubmittedToUser.isEmpty());
        verify(reportsRepository).findByManagerEmailAndManagerApprovalStatusAndIsHidden(Mockito.<String>any(), Mockito.<ManagerApprovalStatus>any(), anyBoolean());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsSubmittedToUser(String, String)}
     */
    @Test
    void testGetReportsSubmittedToUser7() {
        when(reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsHidden(Mockito.<String>any(), Mockito.<ManagerApprovalStatus>any(), anyBoolean())).thenThrow(new IllegalStateException("rejected"));
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getReportsSubmittedToUser("jane.doe@example.org", "rejected"));
        verify(reportsRepository).findByManagerEmailAndManagerApprovalStatusAndIsHidden(Mockito.<String>any(), Mockito.<ManagerApprovalStatus>any(), anyBoolean());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllSubmittedReports()}
     */
    @Test
    void testGetAllSubmittedReports() {
        when(reportsRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(reportsServiceImpl.getAllSubmittedReports().isEmpty());
        verify(reportsRepository).findAll();
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllSubmittedReports()}
     */
    @Test
    void testGetAllSubmittedReports2() {
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

        ArrayList<Reports> reportsList = new ArrayList<>();
        reportsList.add(reports);
        when(reportsRepository.findAll()).thenReturn(reportsList);
        assertEquals(1, reportsServiceImpl.getAllSubmittedReports().size());
        verify(reportsRepository).findAll();
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllSubmittedReports()}
     */
    @Test
    void testGetAllSubmittedReports3() {
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

        ArrayList<Reports> reportsList = new ArrayList<>();
        reportsList.add(reports2);
        reportsList.add(reports);
        when(reportsRepository.findAll()).thenReturn(reportsList);
        assertEquals(1, reportsServiceImpl.getAllSubmittedReports().size());
        verify(reportsRepository).findAll();
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllSubmittedReports()}
     */
    @Test
    void testGetAllSubmittedReports4() {
        when(reportsRepository.findAll()).thenThrow(new IllegalStateException("foo"));
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getAllSubmittedReports());
        verify(reportsRepository).findAll();
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllReportsApprovedByManager(String)}
     */
    @Test
    void testGetAllReportsApprovedByManager() {
        assertThrows(IllegalArgumentException.class, () -> reportsServiceImpl.getAllReportsApprovedByManager("Request"));
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllReportsApprovedByManager(String)}
     */
    @Test
    void testGetAllReportsApprovedByManager2() {
        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(new ArrayList<>());
        assertTrue(reportsServiceImpl.getAllReportsApprovedByManager("approved").isEmpty());
        verify(reportsRepository, atLeast(1)).findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllReportsApprovedByManager(String)}
     */
    @Test
    void testGetAllReportsApprovedByManager3() {
        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenThrow(new IllegalStateException("approved"));
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getAllReportsApprovedByManager("approved"));
        verify(reportsRepository).findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllReportsApprovedByManager(String)}
     */
    @Test
    void testGetAllReportsApprovedByManager4() {
        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(new ArrayList<>());
        assertTrue(reportsServiceImpl.getAllReportsApprovedByManager("pending").isEmpty());
        verify(reportsRepository, atLeast(1)).findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllReportsApprovedByManager(String)}
     */
    @Test
    void testGetAllReportsApprovedByManager5() {
        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenThrow(new IllegalStateException("approved"));
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getAllReportsApprovedByManager("pending"));
        verify(reportsRepository).findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllReportsApprovedByManager(String)}
     */
    @Test
    void testGetAllReportsApprovedByManager6() {
        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(new ArrayList<>());
        assertTrue(reportsServiceImpl.getAllReportsApprovedByManager("reimbursed").isEmpty());
        verify(reportsRepository, atLeast(1)).findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllReportsApprovedByManager(String)}
     */
    @Test
    void testGetAllReportsApprovedByManager7() {
        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenThrow(new IllegalStateException("approved"));
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getAllReportsApprovedByManager("reimbursed"));
        verify(reportsRepository).findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllReportsApprovedByManager(String)}
     */
    @Test
    void testGetAllReportsApprovedByManager8() {
        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(new ArrayList<>());
        assertTrue(reportsServiceImpl.getAllReportsApprovedByManager("rejected").isEmpty());
        verify(reportsRepository, atLeast(1)).findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAllReportsApprovedByManager(String)}
     */
    @Test
    void testGetAllReportsApprovedByManager9() {
        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenThrow(new IllegalStateException("approved"));
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getAllReportsApprovedByManager("rejected"));
        verify(reportsRepository).findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<ManagerApprovalStatus>any(), Mockito.<FinanceApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#submitReport(Long, String, HttpServletResponse)}
     */
    @Test
    void testSubmitReport() throws IOException, MessagingException {
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(RuntimeException.class, () -> reportsServiceImpl.submitReport(1L, "jane.doe@example.org", new Response()));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#submitReport(Long, HttpServletResponse)}
     */
    @Test
    void testSubmitReport2() throws IOException, MessagingException {
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(RuntimeException.class, () -> reportsServiceImpl.submitReport(1L, new Response()));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#reimburseReportByFinance(ArrayList, String)}
     */
//    @Test
//    void testReimburseReportByFinance() {
//        ArrayList<Long> reportIds = new ArrayList<>();
//        reportsServiceImpl.reimburseReportByFinance(reportIds, "Comments");
//        assertEquals(reportIds, reportsServiceImpl.getAllSubmittedReports());
//    }

    /**
     * Method under test: {@link ReportsServiceImpl#reimburseReportByFinance(ArrayList, String)}
     */
    @Test
    void testReimburseReportByFinance2() {
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

        ArrayList<Long> reportIds = new ArrayList<>();
        reportIds.add(1L);
        assertThrows(ObjectNotFoundException.class, () -> reportsServiceImpl.reimburseReportByFinance(reportIds, "Comments"));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#reimburseReportByFinance(ArrayList, String)}
     */
    @Test
    void testReimburseReportByFinance3() {
        doNothing().when(iEmailService).userReimbursedNotification(Mockito.<Long>any());

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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        doNothing().when(pushNotificationService).sendPushNotificationToToken(Mockito.<PushNotificationRequest>any());
        Reports reports = mock(Reports.class);
        when(reports.getReportTitle()).thenReturn("Dr");
        when(reports.getManagerApprovalStatus()).thenReturn(ManagerApprovalStatus.APPROVED);
        when(reports.getIsHidden()).thenReturn(false);
        when(reports.getIsSubmitted()).thenReturn(true);
        when(reports.getEmployeeId()).thenReturn(1L);
        doNothing().when(reports).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports).setManagerComments(Mockito.<String>any());
        doNothing().when(reports).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports).setReportId(Mockito.<Long>any());
        doNothing().when(reports).setReportTitle(Mockito.<String>any());
        doNothing().when(reports).setTotalAmount(anyFloat());
        doNothing().when(reports).setTotalApprovedAmount(anyFloat());
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
        when(reportsRepository.save(Mockito.<Reports>any())).thenReturn(reports2);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        ArrayList<Long> reportIds = new ArrayList<>();
        reportIds.add(1L);
        reportsServiceImpl.reimburseReportByFinance(reportIds, "Comments");
        verify(iEmailService).userReimbursedNotification(Mockito.<Long>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(pushNotificationService).sendPushNotificationToToken(Mockito.<PushNotificationRequest>any());
        verify(reportsRepository).save(Mockito.<Reports>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(reports).getManagerApprovalStatus();
        verify(reports).getIsHidden();
        verify(reports).getIsSubmitted();
        verify(reports).getEmployeeId();
        verify(reports).getReportTitle();
        verify(reports).setDateCreated(Mockito.<LocalDate>any());
        verify(reports).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports).setEmployeeId(Mockito.<Long>any());
        verify(reports).setEmployeeMail(Mockito.<String>any());
        verify(reports).setEmployeeName(Mockito.<String>any());
        verify(reports).setExpensesCount(Mockito.<Long>any());
        verify(reports, atLeast(1)).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports, atLeast(1)).setFinanceComments(Mockito.<String>any());
        verify(reports).setIsHidden(Mockito.<Boolean>any());
        verify(reports).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports).setManagerComments(Mockito.<String>any());
        verify(reports).setManagerEmail(Mockito.<String>any());
        verify(reports).setManagerReviewTime(Mockito.<String>any());
        verify(reports).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports).setReportId(Mockito.<Long>any());
        verify(reports).setReportTitle(Mockito.<String>any());
        verify(reports).setTotalAmount(anyFloat());
        verify(reports).setTotalApprovedAmount(anyFloat());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#reimburseReportByFinance(ArrayList, String)}
     */
    @Test
    void testReimburseReportByFinance4() {
        doNothing().when(iEmailService).userReimbursedNotification(Mockito.<Long>any());

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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        Reports reports = mock(Reports.class);
        when(reports.getReportTitle()).thenThrow(new IllegalStateException("Your expense report is pushed to reimbursement."));
        when(reports.getManagerApprovalStatus()).thenReturn(ManagerApprovalStatus.APPROVED);
        when(reports.getIsHidden()).thenReturn(false);
        when(reports.getIsSubmitted()).thenReturn(true);
        when(reports.getEmployeeId()).thenReturn(1L);
        doNothing().when(reports).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports).setManagerComments(Mockito.<String>any());
        doNothing().when(reports).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports).setReportId(Mockito.<Long>any());
        doNothing().when(reports).setReportTitle(Mockito.<String>any());
        doNothing().when(reports).setTotalAmount(anyFloat());
        doNothing().when(reports).setTotalApprovedAmount(anyFloat());
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
        when(reportsRepository.save(Mockito.<Reports>any())).thenReturn(reports2);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        ArrayList<Long> reportIds = new ArrayList<>();
        reportIds.add(1L);
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.reimburseReportByFinance(reportIds, "Comments"));
        verify(iEmailService).userReimbursedNotification(Mockito.<Long>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(reportsRepository).save(Mockito.<Reports>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(reports).getManagerApprovalStatus();
        verify(reports).getIsHidden();
        verify(reports).getIsSubmitted();
        verify(reports).getEmployeeId();
        verify(reports).getReportTitle();
        verify(reports).setDateCreated(Mockito.<LocalDate>any());
        verify(reports).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports).setEmployeeId(Mockito.<Long>any());
        verify(reports).setEmployeeMail(Mockito.<String>any());
        verify(reports).setEmployeeName(Mockito.<String>any());
        verify(reports).setExpensesCount(Mockito.<Long>any());
        verify(reports, atLeast(1)).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports, atLeast(1)).setFinanceComments(Mockito.<String>any());
        verify(reports).setIsHidden(Mockito.<Boolean>any());
        verify(reports).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports).setManagerComments(Mockito.<String>any());
        verify(reports).setManagerEmail(Mockito.<String>any());
        verify(reports).setManagerReviewTime(Mockito.<String>any());
        verify(reports).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports).setReportId(Mockito.<Long>any());
        verify(reports).setReportTitle(Mockito.<String>any());
        verify(reports).setTotalAmount(anyFloat());
        verify(reports).setTotalApprovedAmount(anyFloat());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#reimburseReportByFinance(ArrayList, String)}
     */
    @Test
    void testReimburseReportByFinance5() throws UnsupportedEncodingException {
        doNothing().when(iEmailService).userReimbursedNotification(Mockito.<Long>any());

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

        Category category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);

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
        expense.setCategory(category);
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

        Category category2 = new Category();
        category2.setCategoryDescription("Your expense report is pushed to reimbursement.");
        category2.setCategoryId(1L);
        category2.setCategoryTotal(1L);
        category2.setExpenseList(new ArrayList<>());
        category2.setIsHidden(true);

        Employee employee3 = new Employee();
        employee3.setEmployeeEmail("jane.doe@example.org");
        employee3.setEmployeeId(1L);
        employee3.setExpenseList(new ArrayList<>());
        employee3.setFirstName("Jane");
        employee3.setHrEmail("jane.doe@example.org");
        employee3.setHrName("Your expense report is pushed to reimbursement.");
        employee3.setImageUrl("https://example.org/example");
        employee3.setIsFinanceAdmin(true);
        employee3.setIsHidden(true);
        employee3.setLastName("Doe");
        employee3.setLndEmail("jane.doe@example.org");
        employee3.setLndName("Your expense report is pushed to reimbursement.");
        employee3.setManagerEmail("jane.doe@example.org");
        employee3.setManagerName("Your expense report is pushed to reimbursement.");
        employee3.setMiddleName("Your expense report is pushed to reimbursement.");
        employee3.setMobileNumber(1L);
        employee3.setOfficialEmployeeId("42");
        employee3.setRole("Your expense report is pushed to reimbursement.");
        employee3.setToken("ABC123");

        Reports reports2 = new Reports();
        reports2.setDateCreated(LocalDate.of(1970, 1, 1));
        reports2.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports2.setEmployeeId(1L);
        reports2.setEmployeeMail("Your expense report is pushed to reimbursement.");
        reports2.setEmployeeName("Your expense report is pushed to reimbursement.");
        reports2.setExpensesCount(3L);
        reports2.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports2.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports2.setFinanceComments("Your expense report is pushed to reimbursement.");
        reports2.setIsHidden(true);
        reports2.setIsSubmitted(true);
        reports2.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports2.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports2.setManagerComments("Your expense report is pushed to reimbursement.");
        reports2.setManagerEmail("jane.doe@example.org");
        reports2.setManagerReviewTime("Your expense report is pushed to reimbursement.");
        reports2.setOfficialEmployeeId("42");
        reports2.setReportId(1L);
        reports2.setReportTitle("Dr");
        reports2.setTotalAmount(10.0f);
        reports2.setTotalApprovedAmount(10.0f);

        Expense expense2 = new Expense();
        expense2.setAmount(10.0d);
        expense2.setAmountApproved(10.0d);
        expense2.setCategory(category2);
        expense2.setCategoryDescription("Your expense report is pushed to reimbursement.");
        expense2.setDate(LocalDate.of(1970, 1, 1));
        expense2.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense2.setDescription("The characteristics of someone or something");
        expense2.setEmployee(employee3);
        expense2.setExpenseId(1L);
        expense2.setFile(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        expense2.setFileName("foo.txt");
        expense2.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense2.setIsHidden(true);
        expense2.setIsReported(true);
        expense2.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense2.setMerchantName("Your expense report is pushed to reimbursement.");
        expense2.setPotentialDuplicate(true);
        expense2.setReportTitle("Dr");
        expense2.setReports(reports2);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense2);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);
        doNothing().when(pushNotificationService).sendPushNotificationToToken(Mockito.<PushNotificationRequest>any());
        Reports reports3 = mock(Reports.class);
        when(reports3.getReportTitle()).thenReturn("Dr");
        when(reports3.getManagerApprovalStatus()).thenReturn(ManagerApprovalStatus.APPROVED);
        when(reports3.getIsHidden()).thenReturn(false);
        when(reports3.getIsSubmitted()).thenReturn(true);
        when(reports3.getEmployeeId()).thenReturn(1L);
        doNothing().when(reports3).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports3).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports3).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports3).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports3).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports3).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports3).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports3).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports3).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports3).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports3).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports3).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports3).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports3).setManagerComments(Mockito.<String>any());
        doNothing().when(reports3).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports3).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports3).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports3).setReportId(Mockito.<Long>any());
        doNothing().when(reports3).setReportTitle(Mockito.<String>any());
        doNothing().when(reports3).setTotalAmount(anyFloat());
        doNothing().when(reports3).setTotalApprovedAmount(anyFloat());
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

        Reports reports4 = new Reports();
        reports4.setDateCreated(LocalDate.of(1970, 1, 1));
        reports4.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports4.setEmployeeId(1L);
        reports4.setEmployeeMail("Employee Mail");
        reports4.setEmployeeName("Employee Name");
        reports4.setExpensesCount(3L);
        reports4.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports4.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports4.setFinanceComments("Finance Comments");
        reports4.setIsHidden(true);
        reports4.setIsSubmitted(true);
        reports4.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports4.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports4.setManagerComments("Manager Comments");
        reports4.setManagerEmail("jane.doe@example.org");
        reports4.setManagerReviewTime("Manager Review Time");
        reports4.setOfficialEmployeeId("42");
        reports4.setReportId(1L);
        reports4.setReportTitle("Dr");
        reports4.setTotalAmount(10.0f);
        reports4.setTotalApprovedAmount(10.0f);
        when(reportsRepository.save(Mockito.<Reports>any())).thenReturn(reports4);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        ArrayList<Long> reportIds = new ArrayList<>();
        reportIds.add(1L);
        reportsServiceImpl.reimburseReportByFinance(reportIds, "Comments");
        verify(iEmailService).userReimbursedNotification(Mockito.<Long>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(expenseRepository).save(Mockito.<Expense>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(pushNotificationService).sendPushNotificationToToken(Mockito.<PushNotificationRequest>any());
        verify(reportsRepository).save(Mockito.<Reports>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(reports3).getManagerApprovalStatus();
        verify(reports3).getIsHidden();
        verify(reports3).getIsSubmitted();
        verify(reports3).getEmployeeId();
        verify(reports3).getReportTitle();
        verify(reports3).setDateCreated(Mockito.<LocalDate>any());
        verify(reports3).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports3).setEmployeeId(Mockito.<Long>any());
        verify(reports3).setEmployeeMail(Mockito.<String>any());
        verify(reports3).setEmployeeName(Mockito.<String>any());
        verify(reports3).setExpensesCount(Mockito.<Long>any());
        verify(reports3, atLeast(1)).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports3, atLeast(1)).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports3, atLeast(1)).setFinanceComments(Mockito.<String>any());
        verify(reports3).setIsHidden(Mockito.<Boolean>any());
        verify(reports3).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports3).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports3).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports3).setManagerComments(Mockito.<String>any());
        verify(reports3).setManagerEmail(Mockito.<String>any());
        verify(reports3).setManagerReviewTime(Mockito.<String>any());
        verify(reports3).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports3).setReportId(Mockito.<Long>any());
        verify(reports3).setReportTitle(Mockito.<String>any());
        verify(reports3).setTotalAmount(anyFloat());
        verify(reports3).setTotalApprovedAmount(anyFloat());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#rejectReportByFinance(Long, String)}
     */
    @Test
    void testRejectReportByFinance() {
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
        assertThrows(ObjectNotFoundException.class, () -> reportsServiceImpl.rejectReportByFinance(1L, "Comments"));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#rejectReportByFinance(Long, String)}
     */
    @Test
    void testRejectReportByFinance2() {
        Reports reports = mock(Reports.class);
        when(reports.getIsHidden()).thenThrow(new RuntimeException("Accounts admin rejected your expense report."));
        when(reports.getIsSubmitted()).thenReturn(true);
        when(reports.getEmployeeId()).thenReturn(1L);
        doNothing().when(reports).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports).setManagerComments(Mockito.<String>any());
        doNothing().when(reports).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports).setReportId(Mockito.<Long>any());
        doNothing().when(reports).setReportTitle(Mockito.<String>any());
        doNothing().when(reports).setTotalAmount(anyFloat());
        doNothing().when(reports).setTotalApprovedAmount(anyFloat());
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
        assertThrows(RuntimeException.class, () -> reportsServiceImpl.rejectReportByFinance(1L, "Comments"));
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(reports).getIsHidden();
        verify(reports).getIsSubmitted();
        verify(reports).getEmployeeId();
        verify(reports).setDateCreated(Mockito.<LocalDate>any());
        verify(reports).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports).setEmployeeId(Mockito.<Long>any());
        verify(reports).setEmployeeMail(Mockito.<String>any());
        verify(reports).setEmployeeName(Mockito.<String>any());
        verify(reports).setExpensesCount(Mockito.<Long>any());
        verify(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports).setFinanceComments(Mockito.<String>any());
        verify(reports).setIsHidden(Mockito.<Boolean>any());
        verify(reports).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports).setManagerComments(Mockito.<String>any());
        verify(reports).setManagerEmail(Mockito.<String>any());
        verify(reports).setManagerReviewTime(Mockito.<String>any());
        verify(reports).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports).setReportId(Mockito.<Long>any());
        verify(reports).setReportTitle(Mockito.<String>any());
        verify(reports).setTotalAmount(anyFloat());
        verify(reports).setTotalApprovedAmount(anyFloat());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#rejectReportByFinance(Long, String)}
     */
    @Test
    void testRejectReportByFinance3() {
        doNothing().when(iEmailService).userRejectedByFinanceNotification(Mockito.<Long>any());

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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        doNothing().when(pushNotificationService).sendPushNotificationToToken(Mockito.<PushNotificationRequest>any());
        Reports reports = mock(Reports.class);
        when(reports.getManagerApprovalStatus()).thenReturn(ManagerApprovalStatus.APPROVED);
        when(reports.getIsHidden()).thenReturn(false);
        when(reports.getReportTitle()).thenReturn("Dr");
        when(reports.getIsSubmitted()).thenReturn(true);
        when(reports.getEmployeeId()).thenReturn(1L);
        doNothing().when(reports).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports).setManagerComments(Mockito.<String>any());
        doNothing().when(reports).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports).setReportId(Mockito.<Long>any());
        doNothing().when(reports).setReportTitle(Mockito.<String>any());
        doNothing().when(reports).setTotalAmount(anyFloat());
        doNothing().when(reports).setTotalApprovedAmount(anyFloat());
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
        when(reportsRepository.save(Mockito.<Reports>any())).thenReturn(reports2);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        reportsServiceImpl.rejectReportByFinance(1L, "Comments");
        verify(iEmailService).userRejectedByFinanceNotification(Mockito.<Long>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(pushNotificationService).sendPushNotificationToToken(Mockito.<PushNotificationRequest>any());
        verify(reportsRepository).save(Mockito.<Reports>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(reports).getManagerApprovalStatus();
        verify(reports, atLeast(1)).getIsHidden();
        verify(reports, atLeast(1)).getIsSubmitted();
        verify(reports).getEmployeeId();
        verify(reports).getReportTitle();
        verify(reports).setDateCreated(Mockito.<LocalDate>any());
        verify(reports).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports).setEmployeeId(Mockito.<Long>any());
        verify(reports).setEmployeeMail(Mockito.<String>any());
        verify(reports).setEmployeeName(Mockito.<String>any());
        verify(reports).setExpensesCount(Mockito.<Long>any());
        verify(reports, atLeast(1)).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports, atLeast(1)).setFinanceComments(Mockito.<String>any());
        verify(reports).setIsHidden(Mockito.<Boolean>any());
        verify(reports).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports).setManagerComments(Mockito.<String>any());
        verify(reports).setManagerEmail(Mockito.<String>any());
        verify(reports).setManagerReviewTime(Mockito.<String>any());
        verify(reports).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports).setReportId(Mockito.<Long>any());
        verify(reports).setReportTitle(Mockito.<String>any());
        verify(reports).setTotalAmount(anyFloat());
        verify(reports).setTotalApprovedAmount(anyFloat());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#rejectReportByFinance(Long, String)}
     */
    @Test
    void testRejectReportByFinance4() {
        Reports reports = mock(Reports.class);
        when(reports.getManagerApprovalStatus()).thenReturn(ManagerApprovalStatus.APPROVED);
        when(reports.getIsHidden()).thenReturn(false);
        when(reports.getIsSubmitted()).thenReturn(true);
        when(reports.getEmployeeId()).thenReturn(1L);
        doNothing().when(reports).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports).setManagerComments(Mockito.<String>any());
        doNothing().when(reports).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports).setReportId(Mockito.<Long>any());
        doNothing().when(reports).setReportTitle(Mockito.<String>any());
        doNothing().when(reports).setTotalAmount(anyFloat());
        doNothing().when(reports).setTotalApprovedAmount(anyFloat());
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
        when(reportsRepository.save(Mockito.<Reports>any())).thenThrow(new IllegalStateException("Accounts admin rejected your expense report."));
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.rejectReportByFinance(1L, "Comments"));
        verify(reportsRepository).save(Mockito.<Reports>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(reports).getManagerApprovalStatus();
        verify(reports, atLeast(1)).getIsHidden();
        verify(reports, atLeast(1)).getIsSubmitted();
        verify(reports).getEmployeeId();
        verify(reports).setDateCreated(Mockito.<LocalDate>any());
        verify(reports).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports).setEmployeeId(Mockito.<Long>any());
        verify(reports).setEmployeeMail(Mockito.<String>any());
        verify(reports).setEmployeeName(Mockito.<String>any());
        verify(reports).setExpensesCount(Mockito.<Long>any());
        verify(reports, atLeast(1)).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports, atLeast(1)).setFinanceComments(Mockito.<String>any());
        verify(reports).setIsHidden(Mockito.<Boolean>any());
        verify(reports).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports).setManagerComments(Mockito.<String>any());
        verify(reports).setManagerEmail(Mockito.<String>any());
        verify(reports).setManagerReviewTime(Mockito.<String>any());
        verify(reports).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports).setReportId(Mockito.<Long>any());
        verify(reports).setReportTitle(Mockito.<String>any());
        verify(reports).setTotalAmount(anyFloat());
        verify(reports).setTotalApprovedAmount(anyFloat());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#totalAmount(Long)}
     */
    @Test
    void testTotalAmount() {
        when(expenseRepository.findByReports(Mockito.<Optional<Reports>>any())).thenReturn(new ArrayList<>());

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
        assertEquals(0.0f, reportsServiceImpl.totalAmount(1L));
        verify(expenseRepository).findByReports(Mockito.<Optional<Reports>>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#totalAmount(Long)}
     */
    @Test
    void testTotalAmount2() {
        when(expenseRepository.findByReports(Mockito.<Optional<Reports>>any())).thenThrow(new IllegalStateException("foo"));

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
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.totalAmount(1L));
        verify(expenseRepository).findByReports(Mockito.<Optional<Reports>>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#totalAmount(Long)}
     */
    @Test
    void testTotalAmount3() {
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
        when(expenseRepository.findByReports(Mockito.<Optional<Reports>>any())).thenReturn(expenseList);

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
        assertEquals(10.0f, reportsServiceImpl.totalAmount(1L));
        verify(expenseRepository).findByReports(Mockito.<Optional<Reports>>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#totalAmount(Long)}
     */
    @Test
    void testTotalAmount4() {
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertEquals(0.0f, reportsServiceImpl.totalAmount(1L));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#totalApprovedAmount(Long)}
     */
    @Test
    void testTotalApprovedAmount() {
        when(expenseRepository.findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(new ArrayList<>());

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
        assertEquals(0.0f, reportsServiceImpl.totalApprovedAmount(1L));
        verify(expenseRepository).findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#totalApprovedAmount(Long)}
     */
    @Test
    void testTotalApprovedAmount2() {
        when(expenseRepository.findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenThrow(new IllegalStateException("foo"));

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
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.totalApprovedAmount(1L));
        verify(expenseRepository).findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#totalApprovedAmount(Long)}
     */
    @Test
    void testTotalApprovedAmount3() {
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
        when(expenseRepository.findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(expenseList);

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
        assertEquals(10.0f, reportsServiceImpl.totalApprovedAmount(1L));
        verify(expenseRepository).findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#totalApprovedAmount(Long)}
     */
    @Test
    void testTotalApprovedAmount4() {
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(RuntimeException.class, () -> reportsServiceImpl.totalApprovedAmount(1L));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#hideReport(Long)}
     */
    @Test
    void testHideReport() {
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());

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
        when(reportsRepository.save(Mockito.<Reports>any())).thenReturn(reports2);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        reportsServiceImpl.hideReport(1L);
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(reportsRepository).save(Mockito.<Reports>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#hideReport(Long)}
     */
    @Test
    void testHideReport2() {
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());

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
        when(reportsRepository.save(Mockito.<Reports>any())).thenThrow(new IllegalStateException("foo"));
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.hideReport(1L));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(reportsRepository).save(Mockito.<Reports>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#hideReport(Long)}
     */
    @Test
    void testHideReport3() throws UnsupportedEncodingException {
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
        when(expenseRepository.save(Mockito.<Expense>any())).thenReturn(expense);

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
        expense2.setFile(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        expense2.setFileName("foo.txt");
        expense2.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense2.setIsHidden(true);
        expense2.setIsReported(true);
        expense2.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense2.setMerchantName("Merchant Name");
        expense2.setPotentialDuplicate(true);
        expense2.setReportTitle("Dr");
        expense2.setReports(reports2);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense2);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);

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

        Reports reports4 = new Reports();
        reports4.setDateCreated(LocalDate.of(1970, 1, 1));
        reports4.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports4.setEmployeeId(1L);
        reports4.setEmployeeMail("Employee Mail");
        reports4.setEmployeeName("Employee Name");
        reports4.setExpensesCount(3L);
        reports4.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports4.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports4.setFinanceComments("Finance Comments");
        reports4.setIsHidden(true);
        reports4.setIsSubmitted(true);
        reports4.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports4.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports4.setManagerComments("Manager Comments");
        reports4.setManagerEmail("jane.doe@example.org");
        reports4.setManagerReviewTime("Manager Review Time");
        reports4.setOfficialEmployeeId("42");
        reports4.setReportId(1L);
        reports4.setReportTitle("Dr");
        reports4.setTotalAmount(10.0f);
        reports4.setTotalApprovedAmount(10.0f);
        when(reportsRepository.save(Mockito.<Reports>any())).thenReturn(reports4);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        reportsServiceImpl.hideReport(1L);
        verify(expenseRepository).save(Mockito.<Expense>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(reportsRepository).save(Mockito.<Reports>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsInDateRange(LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsInDateRange() {
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> reportsServiceImpl.getReportsInDateRange(startDate, LocalDate.of(1970, 1, 1), "Request"));
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsInDateRange(LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsInDateRange2() {
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any())).thenReturn(reportsList);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        List<Reports> actualReportsInDateRange = reportsServiceImpl.getReportsInDateRange(startDate, LocalDate.of(1970, 1, 1), "approved");
        assertSame(reportsList, actualReportsInDateRange);
        assertTrue(actualReportsInDateRange.isEmpty());
        verify(reportsRepository).findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsInDateRange(LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsInDateRange3() {
        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any())).thenThrow(new IllegalStateException("approved"));
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getReportsInDateRange(startDate, LocalDate.of(1970, 1, 1), "approved"));
        verify(reportsRepository).findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsInDateRange(LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsInDateRange4() {
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any())).thenReturn(reportsList);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        List<Reports> actualReportsInDateRange = reportsServiceImpl.getReportsInDateRange(startDate, LocalDate.of(1970, 1, 1), "pending");
        assertSame(reportsList, actualReportsInDateRange);
        assertTrue(actualReportsInDateRange.isEmpty());
        verify(reportsRepository).findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsInDateRange(LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsInDateRange5() {
        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any())).thenThrow(new IllegalStateException("approved"));
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getReportsInDateRange(startDate, LocalDate.of(1970, 1, 1), "pending"));
        verify(reportsRepository).findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsInDateRange(LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsInDateRange6() {
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any())).thenReturn(reportsList);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        List<Reports> actualReportsInDateRange = reportsServiceImpl.getReportsInDateRange(startDate, LocalDate.of(1970, 1, 1), "reimbursed");
        assertSame(reportsList, actualReportsInDateRange);
        assertTrue(actualReportsInDateRange.isEmpty());
        verify(reportsRepository).findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsInDateRange(LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsInDateRange7() {
        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any())).thenThrow(new IllegalStateException("approved"));
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getReportsInDateRange(startDate, LocalDate.of(1970, 1, 1), "reimbursed"));
        verify(reportsRepository).findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsInDateRange(LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsInDateRange8() {
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any())).thenReturn(reportsList);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        List<Reports> actualReportsInDateRange = reportsServiceImpl.getReportsInDateRange(startDate, LocalDate.of(1970, 1, 1), "rejected");
        assertSame(reportsList, actualReportsInDateRange);
        assertTrue(actualReportsInDateRange.isEmpty());
        verify(reportsRepository).findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getReportsInDateRange(LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsInDateRange9() {
        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any())).thenThrow(new IllegalStateException("approved"));
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getReportsInDateRange(startDate, LocalDate.of(1970, 1, 1), "rejected"));
        verify(reportsRepository).findByDateSubmittedBetweenAndFinanceApprovalStatus(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<FinanceApprovalStatus>any());
    }

    /**
     * Method under test:
     * {@link ReportsServiceImpl#getReportsSubmittedToUserInDateRange(String, LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsSubmittedToUserInDateRange() {
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> reportsServiceImpl.getReportsSubmittedToUserInDateRange("jane.doe@example.org", startDate, LocalDate.of(1970, 1, 1), "Request"));
    }

    /**
     * Method under test:
     * {@link ReportsServiceImpl#getReportsSubmittedToUserInDateRange(String, LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsSubmittedToUserInDateRange2() {
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(reportsList);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        List<Reports> actualReportsSubmittedToUserInDateRange = reportsServiceImpl.getReportsSubmittedToUserInDateRange("jane.doe@example.org", startDate, LocalDate.of(1970, 1, 1), "approved");
        assertSame(reportsList, actualReportsSubmittedToUserInDateRange);
        assertTrue(actualReportsSubmittedToUserInDateRange.isEmpty());
        verify(reportsRepository).findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test:
     * {@link ReportsServiceImpl#getReportsSubmittedToUserInDateRange(String, LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsSubmittedToUserInDateRange3() {
        when(reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenThrow(new IllegalStateException("approved"));
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getReportsSubmittedToUserInDateRange("jane.doe@example.org", startDate, LocalDate.of(1970, 1, 1), "approved"));
        verify(reportsRepository).findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test:
     * {@link ReportsServiceImpl#getReportsSubmittedToUserInDateRange(String, LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsSubmittedToUserInDateRange4() {
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(reportsList);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        List<Reports> actualReportsSubmittedToUserInDateRange = reportsServiceImpl.getReportsSubmittedToUserInDateRange("jane.doe@example.org", startDate, LocalDate.of(1970, 1, 1), "pending");
        assertSame(reportsList, actualReportsSubmittedToUserInDateRange);
        assertTrue(actualReportsSubmittedToUserInDateRange.isEmpty());
        verify(reportsRepository).findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test:
     * {@link ReportsServiceImpl#getReportsSubmittedToUserInDateRange(String, LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsSubmittedToUserInDateRange5() {
        when(reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenThrow(new IllegalStateException("approved"));
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getReportsSubmittedToUserInDateRange("jane.doe@example.org", startDate, LocalDate.of(1970, 1, 1), "pending"));
        verify(reportsRepository).findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test:
     * {@link ReportsServiceImpl#getReportsSubmittedToUserInDateRange(String, LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsSubmittedToUserInDateRange6() {
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(reportsList);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        List<Reports> actualReportsSubmittedToUserInDateRange = reportsServiceImpl.getReportsSubmittedToUserInDateRange("jane.doe@example.org", startDate, LocalDate.of(1970, 1, 1), "rejected");
        assertSame(reportsList, actualReportsSubmittedToUserInDateRange);
        assertTrue(actualReportsSubmittedToUserInDateRange.isEmpty());
        verify(reportsRepository).findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test:
     * {@link ReportsServiceImpl#getReportsSubmittedToUserInDateRange(String, LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsSubmittedToUserInDateRange7() {
        when(reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenThrow(new IllegalStateException("approved"));
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getReportsSubmittedToUserInDateRange("jane.doe@example.org", startDate, LocalDate.of(1970, 1, 1), "rejected"));
        verify(reportsRepository).findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<String>any(), Mockito.<LocalDate>any(), Mockito.<LocalDate>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAmountOfReportsInDateRange(LocalDate, LocalDate)}
     */
    @Test
    void testGetAmountOfReportsInDateRange() {
        when(reportsRepository.findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(new ArrayList<>());
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("0.0 INR", reportsServiceImpl.getAmountOfReportsInDateRange(startDate, LocalDate.of(1970, 1, 1)));
        verify(reportsRepository).findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAmountOfReportsInDateRange(LocalDate, LocalDate)}
     */
    @Test
    void testGetAmountOfReportsInDateRange2() {
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

        ArrayList<Reports> reportsList = new ArrayList<>();
        reportsList.add(reports);
        when(reportsRepository.findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(reportsList);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("10.0 INR", reportsServiceImpl.getAmountOfReportsInDateRange(startDate, LocalDate.of(1970, 1, 1)));
        verify(reportsRepository).findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#getAmountOfReportsInDateRange(LocalDate, LocalDate)}
     */
    @Test
    void testGetAmountOfReportsInDateRange3() {
        when(reportsRepository.findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenThrow(new IllegalStateException("foo"));
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.getAmountOfReportsInDateRange(startDate, LocalDate.of(1970, 1, 1)));
        verify(reportsRepository).findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    /**
     * Method under test:
     * {@link ReportsServiceImpl#updateExpenseStatus(Long, List, List, Map, String, String, HttpServletResponse)}
     */
    @Test
    void testUpdateExpenseStatus() throws IOException, MessagingException {
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
        ArrayList<Long> approveExpenseIds = new ArrayList<>();
        ArrayList<Long> rejectExpenseIds = new ArrayList<>();
        HashMap<Long, Float> partiallyApprovedMap = new HashMap<>();
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.updateExpenseStatus(1L, approveExpenseIds, rejectExpenseIds, partiallyApprovedMap, "Review Time", "Comments", new Response()));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link ReportsServiceImpl#updateExpenseStatus(Long, List, List, Map, String, String, HttpServletResponse)}
     */
    @Test
    void testUpdateExpenseStatus2() throws IOException, MessagingException {
        when(expenseRepository.findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(new ArrayList<>());
        Reports reports = mock(Reports.class);
        when(reports.getIsHidden()).thenReturn(false);
        when(reports.getIsSubmitted()).thenReturn(true);
        doNothing().when(reports).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports).setManagerComments(Mockito.<String>any());
        doNothing().when(reports).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports).setReportId(Mockito.<Long>any());
        doNothing().when(reports).setReportTitle(Mockito.<String>any());
        doNothing().when(reports).setTotalAmount(anyFloat());
        doNothing().when(reports).setTotalApprovedAmount(anyFloat());
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
        when(reportsRepository.save(Mockito.<Reports>any())).thenReturn(reports2);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ArrayList<Long> approveExpenseIds = new ArrayList<>();
        ArrayList<Long> rejectExpenseIds = new ArrayList<>();
        HashMap<Long, Float> partiallyApprovedMap = new HashMap<>();
        reportsServiceImpl.updateExpenseStatus(1L, approveExpenseIds, rejectExpenseIds, partiallyApprovedMap, "Review Time", "Comments", new Response());
        verify(expenseRepository).findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
        verify(reportsRepository, atLeast(1)).save(Mockito.<Reports>any());
        verify(reportsRepository, atLeast(1)).findById(Mockito.<Long>any());
        verify(reports).getIsHidden();
        verify(reports).getIsSubmitted();
        verify(reports).setDateCreated(Mockito.<LocalDate>any());
        verify(reports).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports).setEmployeeId(Mockito.<Long>any());
        verify(reports).setEmployeeMail(Mockito.<String>any());
        verify(reports).setEmployeeName(Mockito.<String>any());
        verify(reports).setExpensesCount(Mockito.<Long>any());
        verify(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports).setFinanceComments(Mockito.<String>any());
        verify(reports).setIsHidden(Mockito.<Boolean>any());
        verify(reports).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports, atLeast(1)).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports, atLeast(1)).setManagerComments(Mockito.<String>any());
        verify(reports).setManagerEmail(Mockito.<String>any());
        verify(reports, atLeast(1)).setManagerReviewTime(Mockito.<String>any());
        verify(reports).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports).setReportId(Mockito.<Long>any());
        verify(reports).setReportTitle(Mockito.<String>any());
        verify(reports).setTotalAmount(anyFloat());
        verify(reports, atLeast(1)).setTotalApprovedAmount(anyFloat());
    }

    /**
     * Method under test:
     * {@link ReportsServiceImpl#updateExpenseStatus(Long, List, List, Map, String, String, HttpServletResponse)}
     */
    @Test
    void testUpdateExpenseStatus3() throws IOException, MessagingException {
        Reports reports = mock(Reports.class);
        when(reports.getIsHidden()).thenReturn(false);
        when(reports.getIsSubmitted()).thenReturn(true);
        doNothing().when(reports).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports).setManagerComments(Mockito.<String>any());
        doNothing().when(reports).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports).setReportId(Mockito.<Long>any());
        doNothing().when(reports).setReportTitle(Mockito.<String>any());
        doNothing().when(reports).setTotalAmount(anyFloat());
        doNothing().when(reports).setTotalApprovedAmount(anyFloat());
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
        when(reportsRepository.save(Mockito.<Reports>any())).thenThrow(new IllegalStateException("foo"));
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ArrayList<Long> approveExpenseIds = new ArrayList<>();
        ArrayList<Long> rejectExpenseIds = new ArrayList<>();
        HashMap<Long, Float> partiallyApprovedMap = new HashMap<>();
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.updateExpenseStatus(1L, approveExpenseIds, rejectExpenseIds, partiallyApprovedMap, "Review Time", "Comments", new Response()));
        verify(reportsRepository).save(Mockito.<Reports>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(reports).getIsHidden();
        verify(reports).getIsSubmitted();
        verify(reports).setDateCreated(Mockito.<LocalDate>any());
        verify(reports).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports).setEmployeeId(Mockito.<Long>any());
        verify(reports).setEmployeeMail(Mockito.<String>any());
        verify(reports).setEmployeeName(Mockito.<String>any());
        verify(reports).setExpensesCount(Mockito.<Long>any());
        verify(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports).setFinanceComments(Mockito.<String>any());
        verify(reports).setIsHidden(Mockito.<Boolean>any());
        verify(reports).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports).setManagerComments(Mockito.<String>any());
        verify(reports).setManagerEmail(Mockito.<String>any());
        verify(reports).setManagerReviewTime(Mockito.<String>any());
        verify(reports).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports).setReportId(Mockito.<Long>any());
        verify(reports).setReportTitle(Mockito.<String>any());
        verify(reports).setTotalAmount(anyFloat());
        verify(reports).setTotalApprovedAmount(anyFloat());
    }

    /**
     * Method under test:
     * {@link ReportsServiceImpl#updateExpenseStatus(Long, List, List, Map, String, String, HttpServletResponse)}
     */
    @Test
    void testUpdateExpenseStatus4() throws IOException, MessagingException {
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
        when(expenseRepository.findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(expenseList);
        Reports reports2 = mock(Reports.class);
        when(reports2.getIsHidden()).thenReturn(false);
        when(reports2.getIsSubmitted()).thenReturn(true);
        doNothing().when(reports2).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports2).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports2).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports2).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports2).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports2).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports2).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports2).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports2).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports2).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports2).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports2).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports2).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports2).setManagerComments(Mockito.<String>any());
        doNothing().when(reports2).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports2).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports2).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports2).setReportId(Mockito.<Long>any());
        doNothing().when(reports2).setReportTitle(Mockito.<String>any());
        doNothing().when(reports2).setTotalAmount(anyFloat());
        doNothing().when(reports2).setTotalApprovedAmount(anyFloat());
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
        when(reportsRepository.save(Mockito.<Reports>any())).thenReturn(reports3);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ArrayList<Long> approveExpenseIds = new ArrayList<>();
        ArrayList<Long> rejectExpenseIds = new ArrayList<>();
        HashMap<Long, Float> partiallyApprovedMap = new HashMap<>();
        reportsServiceImpl.updateExpenseStatus(1L, approveExpenseIds, rejectExpenseIds, partiallyApprovedMap, "Review Time", "Comments", new Response());
        verify(expenseRepository).findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
        verify(reportsRepository, atLeast(1)).save(Mockito.<Reports>any());
        verify(reportsRepository, atLeast(1)).findById(Mockito.<Long>any());
        verify(reports2).getIsHidden();
        verify(reports2).getIsSubmitted();
        verify(reports2).setDateCreated(Mockito.<LocalDate>any());
        verify(reports2).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports2).setEmployeeId(Mockito.<Long>any());
        verify(reports2).setEmployeeMail(Mockito.<String>any());
        verify(reports2).setEmployeeName(Mockito.<String>any());
        verify(reports2).setExpensesCount(Mockito.<Long>any());
        verify(reports2).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports2, atLeast(1)).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports2).setFinanceComments(Mockito.<String>any());
        verify(reports2).setIsHidden(Mockito.<Boolean>any());
        verify(reports2).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports2, atLeast(1)).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports2, atLeast(1)).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports2, atLeast(1)).setManagerComments(Mockito.<String>any());
        verify(reports2).setManagerEmail(Mockito.<String>any());
        verify(reports2, atLeast(1)).setManagerReviewTime(Mockito.<String>any());
        verify(reports2).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports2).setReportId(Mockito.<Long>any());
        verify(reports2).setReportTitle(Mockito.<String>any());
        verify(reports2).setTotalAmount(anyFloat());
        verify(reports2, atLeast(1)).setTotalApprovedAmount(anyFloat());
    }

    /**
     * Method under test:
     * {@link ReportsServiceImpl#updateExpenseStatus(Long, List, List, Map, String, String, HttpServletResponse)}
     */
    @Test
    void testUpdateExpenseStatus5() throws IOException, MessagingException {
        Reports reports = mock(Reports.class);
        when(reports.getIsHidden()).thenReturn(false);
        when(reports.getIsSubmitted()).thenReturn(false);
        doNothing().when(reports).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports).setManagerComments(Mockito.<String>any());
        doNothing().when(reports).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports).setReportId(Mockito.<Long>any());
        doNothing().when(reports).setReportTitle(Mockito.<String>any());
        doNothing().when(reports).setTotalAmount(anyFloat());
        doNothing().when(reports).setTotalApprovedAmount(anyFloat());
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
        ArrayList<Long> approveExpenseIds = new ArrayList<>();
        ArrayList<Long> rejectExpenseIds = new ArrayList<>();
        HashMap<Long, Float> partiallyApprovedMap = new HashMap<>();
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.updateExpenseStatus(1L, approveExpenseIds, rejectExpenseIds, partiallyApprovedMap, "Review Time", "Comments", new Response()));
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(reports).getIsHidden();
        verify(reports).getIsSubmitted();
        verify(reports).setDateCreated(Mockito.<LocalDate>any());
        verify(reports).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports).setEmployeeId(Mockito.<Long>any());
        verify(reports).setEmployeeMail(Mockito.<String>any());
        verify(reports).setEmployeeName(Mockito.<String>any());
        verify(reports).setExpensesCount(Mockito.<Long>any());
        verify(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports).setFinanceComments(Mockito.<String>any());
        verify(reports).setIsHidden(Mockito.<Boolean>any());
        verify(reports).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports).setManagerComments(Mockito.<String>any());
        verify(reports).setManagerEmail(Mockito.<String>any());
        verify(reports).setManagerReviewTime(Mockito.<String>any());
        verify(reports).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports).setReportId(Mockito.<Long>any());
        verify(reports).setReportTitle(Mockito.<String>any());
        verify(reports).setTotalAmount(anyFloat());
        verify(reports).setTotalApprovedAmount(anyFloat());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#notifyHR(Long)}
     */
    @Test
    void testNotifyHR() throws MessagingException {
        doNothing().when(iEmailService).notifyHr(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());

        Employee employee = new Employee();
        employee.setEmployeeEmail("jane.doe@example.org");
        employee.setEmployeeId(1L);
        ArrayList<Expense> expenseList = new ArrayList<>();
        employee.setExpenseList(expenseList);
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
        reportsServiceImpl.notifyHR(1L);
        verify(iEmailService).notifyHr(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        assertEquals(expenseList, reportsServiceImpl.getAllSubmittedReports());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#notifyHR(Long)}
     */
    @Test
    void testNotifyHR2() throws MessagingException {
        doThrow(new IllegalStateException("foo")).when(iEmailService).notifyHr(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());

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
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.notifyHR(1L));
        verify(iEmailService).notifyHr(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#notifyLnD(Long)}
     */
    @Test
    void testNotifyLnD() throws MessagingException {
        doNothing().when(iEmailService).notifyLnD(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());

        Employee employee = new Employee();
        employee.setEmployeeEmail("jane.doe@example.org");
        employee.setEmployeeId(1L);
        ArrayList<Expense> expenseList = new ArrayList<>();
        employee.setExpenseList(expenseList);
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
        reportsServiceImpl.notifyLnD(1L);
        verify(iEmailService).notifyLnD(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        assertEquals(expenseList, reportsServiceImpl.getAllSubmittedReports());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#notifyLnD(Long)}
     */
    @Test
    void testNotifyLnD2() throws MessagingException {
        doThrow(new IllegalStateException("foo")).when(iEmailService).notifyLnD(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());

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
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.notifyLnD(1L));
        verify(iEmailService).notifyLnD(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#sendReportNotApprovedByManagerReminder()}
     */
    @Test
    void testSendReportNotApprovedByManagerReminder() {
        doNothing().when(iEmailService).reminderMailToManager(Mockito.<List<Long>>any());
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.findBymanagerApprovalStatus(Mockito.<ManagerApprovalStatus>any())).thenReturn(reportsList);
        reportsServiceImpl.sendReportNotApprovedByManagerReminder();
        verify(iEmailService).reminderMailToManager(Mockito.<List<Long>>any());
        verify(reportsRepository).findBymanagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        assertEquals(reportsList, reportsServiceImpl.getAllSubmittedReports());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#sendReportNotApprovedByManagerReminder()}
     */
    @Test
    void testSendReportNotApprovedByManagerReminder2() {
        when(reportsRepository.findBymanagerApprovalStatus(Mockito.<ManagerApprovalStatus>any())).thenThrow(new IllegalStateException("foo"));
        assertThrows(IllegalStateException.class, () -> reportsServiceImpl.sendReportNotApprovedByManagerReminder());
        verify(reportsRepository).findBymanagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
    }

    /**
     * Method under test: {@link ReportsServiceImpl#sendReportNotApprovedByManagerReminder()}
     */
    @Test
    void testSendReportNotApprovedByManagerReminder3() {
        doNothing().when(iEmailService).reminderMailToManager(Mockito.<List<Long>>any());

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

        ArrayList<Reports> reportsList = new ArrayList<>();
        reportsList.add(reports);
        when(reportsRepository.findBymanagerApprovalStatus(Mockito.<ManagerApprovalStatus>any())).thenReturn(reportsList);
        reportsServiceImpl.sendReportNotApprovedByManagerReminder();
        verify(iEmailService).reminderMailToManager(Mockito.<List<Long>>any());
        verify(reportsRepository).findBymanagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        assertTrue(reportsServiceImpl.getAllSubmittedReports().isEmpty());
    }

    @Test
    void testAddExpensesToReport() throws Exception {
        // Create a list of expense IDs
        List<Long> expenseIds = new ArrayList<>();
        expenseIds.add(1L);
        expenseIds.add(2L);

        // Create a sample Reports object that the service will return
        Reports sampleReport = new Reports();
        sampleReport.setReportId(1L);

        // Mock the service method to return the sample Reports object
        Mockito.when(reportsService.addExpenseToReport(1L, expenseIds)).thenReturn(sampleReport);

        // Create and configure the mockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(reportsController).build();

        // Perform the PATCH request
        mockMvc.perform(MockMvcRequestBuilders.patch("/addExpenseToReport/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("expenseIds", "1", "2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reportId").value(1)); // Check the expected reportId in the response
    }

    @Test
    void testReimburseReportByFinance() throws Exception {
        // Create a list of report IDs
        List<Long> reportIds = new ArrayList<>();
        reportIds.add(1L);
        reportIds.add(2L);

        // Mock the service method
        Mockito.doNothing().when(reportsService).reimburseReportByFinance((ArrayList<Long>) reportIds, "Test Comments");

        // Create and configure the mockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(reportsController).build();

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/approveReportByFinance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("reportIds", "1", "2")
                        .param("comments", "Test Comments"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that the service method was called with the expected parameters
        Mockito.verify(reportsService, times(1)).reimburseReportByFinance((ArrayList<Long>) reportIds, "Test Comments");
    }



}