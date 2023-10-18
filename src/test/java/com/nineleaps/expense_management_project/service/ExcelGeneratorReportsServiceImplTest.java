package com.nineleaps.expense_management_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyFloat;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.client.testing.util.TestableByteArrayOutputStream;
import com.nineleaps.expense_management_project.entity.Category;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.entity.Expense;
import com.nineleaps.expense_management_project.entity.FinanceApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatusExpense;
import com.nineleaps.expense_management_project.entity.Reports;
import com.nineleaps.expense_management_project.entity.StatusExcel;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ExcelGeneratorReportsServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ExcelGeneratorReportsServiceImplTest {
    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private ExcelGeneratorReportsServiceImpl excelGeneratorReportsServiceImpl;

    @MockBean
    private IExpenseService iExpenseService;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private ReportsRepository reportsRepository;

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcelAndSendEmail(HttpServletResponse, LocalDate, LocalDate,
     * StatusExcel)}
     */
    @Test
    void testGenerateExcelAndSendEmail() throws Exception {
        when(reportsRepository.findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(new ArrayList<>());
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("No data available for the selected period. So, Email can't be sent!", excelGeneratorReportsServiceImpl.generateExcelAndSendEmail(response, startDate, LocalDate.of(1970, 1, 1), StatusExcel.ALL));
        verify(reportsRepository).findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcelAndSendEmail(HttpServletResponse, LocalDate, LocalDate,
     * StatusExcel)}
     */
    @Test
    void testGenerateExcelAndSendEmail2() throws Exception {
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
        when(employeeRepository.findByRole(Mockito.<String>any())).thenReturn(employee);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("No data available for the selected period. So, Email can't be sent!");
        reports.setEmployeeName("No data available for the selected period. So, Email can't be sent!");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("No data available for the selected period. So, Email can't be sent!");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("No data available for the selected period. So, Email can't be sent!");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("No data available for the selected period. So, Email can't be sent!");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportsList = new ArrayList<>();
        reportsList.add(reports);
        when(reportsRepository.findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(reportsList);
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("Email sent successfully!", excelGeneratorReportsServiceImpl.generateExcelAndSendEmail(response, startDate, LocalDate.of(1970, 1, 1), StatusExcel.ALL));
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
        verify(reportsRepository).findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcelAndSendEmail(HttpServletResponse, LocalDate, LocalDate,
     * StatusExcel)}
     */
    @Test
    void testGenerateExcelAndSendEmail3() throws Exception {
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
        when(employeeRepository.findByRole(Mockito.<String>any())).thenReturn(employee);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        doThrow(new IllegalStateException("Billfold_All_reports")).when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("No data available for the selected period. So, Email can't be sent!");
        reports.setEmployeeName("No data available for the selected period. So, Email can't be sent!");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("No data available for the selected period. So, Email can't be sent!");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("No data available for the selected period. So, Email can't be sent!");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("No data available for the selected period. So, Email can't be sent!");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportsList = new ArrayList<>();
        reportsList.add(reports);
        when(reportsRepository.findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(reportsList);
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("Email not sent", excelGeneratorReportsServiceImpl.generateExcelAndSendEmail(response, startDate, LocalDate.of(1970, 1, 1), StatusExcel.ALL));
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
        verify(reportsRepository).findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcelAndSendEmail(HttpServletResponse, LocalDate, LocalDate,
     * StatusExcel)}
     */
    @Test
    void testGenerateExcelAndSendEmail4() throws Exception {
        Employee employee = mock(Employee.class);
        when(employee.getEmployeeEmail()).thenReturn("");
        doNothing().when(employee).setEmployeeEmail(Mockito.<String>any());
        doNothing().when(employee).setEmployeeId(Mockito.<Long>any());
        doNothing().when(employee).setExpenseList(Mockito.<List<Expense>>any());
        doNothing().when(employee).setFirstName(Mockito.<String>any());
        doNothing().when(employee).setHrEmail(Mockito.<String>any());
        doNothing().when(employee).setHrName(Mockito.<String>any());
        doNothing().when(employee).setImageUrl(Mockito.<String>any());
        doNothing().when(employee).setIsFinanceAdmin(Mockito.<Boolean>any());
        doNothing().when(employee).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(employee).setLastName(Mockito.<String>any());
        doNothing().when(employee).setLndEmail(Mockito.<String>any());
        doNothing().when(employee).setLndName(Mockito.<String>any());
        doNothing().when(employee).setManagerEmail(Mockito.<String>any());
        doNothing().when(employee).setManagerName(Mockito.<String>any());
        doNothing().when(employee).setMiddleName(Mockito.<String>any());
        doNothing().when(employee).setMobileNumber(Mockito.<Long>any());
        doNothing().when(employee).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(employee).setRole(Mockito.<String>any());
        doNothing().when(employee).setToken(Mockito.<String>any());
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
        when(employeeRepository.findByRole(Mockito.<String>any())).thenReturn(employee);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("No data available for the selected period. So, Email can't be sent!");
        reports.setEmployeeName("No data available for the selected period. So, Email can't be sent!");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("No data available for the selected period. So, Email can't be sent!");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("No data available for the selected period. So, Email can't be sent!");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("No data available for the selected period. So, Email can't be sent!");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportsList = new ArrayList<>();
        reportsList.add(reports);
        when(reportsRepository.findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(reportsList);
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("Email not sent", excelGeneratorReportsServiceImpl.generateExcelAndSendEmail(response, startDate, LocalDate.of(1970, 1, 1), StatusExcel.ALL));
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(employee).getEmployeeEmail();
        verify(employee).setEmployeeEmail(Mockito.<String>any());
        verify(employee).setEmployeeId(Mockito.<Long>any());
        verify(employee).setExpenseList(Mockito.<List<Expense>>any());
        verify(employee).setFirstName(Mockito.<String>any());
        verify(employee).setHrEmail(Mockito.<String>any());
        verify(employee).setHrName(Mockito.<String>any());
        verify(employee).setImageUrl(Mockito.<String>any());
        verify(employee).setIsFinanceAdmin(Mockito.<Boolean>any());
        verify(employee).setIsHidden(Mockito.<Boolean>any());
        verify(employee).setLastName(Mockito.<String>any());
        verify(employee).setLndEmail(Mockito.<String>any());
        verify(employee).setLndName(Mockito.<String>any());
        verify(employee).setManagerEmail(Mockito.<String>any());
        verify(employee).setManagerName(Mockito.<String>any());
        verify(employee).setMiddleName(Mockito.<String>any());
        verify(employee).setMobileNumber(Mockito.<Long>any());
        verify(employee).setOfficialEmployeeId(Mockito.<String>any());
        verify(employee).setRole(Mockito.<String>any());
        verify(employee).setToken(Mockito.<String>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(reportsRepository).findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcelAndSendEmail(HttpServletResponse, LocalDate, LocalDate,
     * StatusExcel)}
     */
    @Test
    void testGenerateExcelAndSendEmail5() throws Exception {
        Employee employee = mock(Employee.class);
        when(employee.getEmployeeEmail()).thenReturn("jane.doe@example.org");
        doNothing().when(employee).setEmployeeEmail(Mockito.<String>any());
        doNothing().when(employee).setEmployeeId(Mockito.<Long>any());
        doNothing().when(employee).setExpenseList(Mockito.<List<Expense>>any());
        doNothing().when(employee).setFirstName(Mockito.<String>any());
        doNothing().when(employee).setHrEmail(Mockito.<String>any());
        doNothing().when(employee).setHrName(Mockito.<String>any());
        doNothing().when(employee).setImageUrl(Mockito.<String>any());
        doNothing().when(employee).setIsFinanceAdmin(Mockito.<Boolean>any());
        doNothing().when(employee).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(employee).setLastName(Mockito.<String>any());
        doNothing().when(employee).setLndEmail(Mockito.<String>any());
        doNothing().when(employee).setLndName(Mockito.<String>any());
        doNothing().when(employee).setManagerEmail(Mockito.<String>any());
        doNothing().when(employee).setManagerName(Mockito.<String>any());
        doNothing().when(employee).setMiddleName(Mockito.<String>any());
        doNothing().when(employee).setMobileNumber(Mockito.<Long>any());
        doNothing().when(employee).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(employee).setRole(Mockito.<String>any());
        doNothing().when(employee).setToken(Mockito.<String>any());
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
        when(employeeRepository.findByRole(Mockito.<String>any())).thenReturn(employee);

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

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        Reports reports2 = new Reports();
        reports2.setDateCreated(LocalDate.of(1970, 1, 1));
        reports2.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports2.setEmployeeId(1L);
        reports2.setEmployeeMail("No data available for the selected period. So, Email can't be sent!");
        reports2.setEmployeeName("No data available for the selected period. So, Email can't be sent!");
        reports2.setExpensesCount(3L);
        reports2.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports2.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports2.setFinanceComments("No data available for the selected period. So, Email can't be sent!");
        reports2.setIsHidden(true);
        reports2.setIsSubmitted(true);
        reports2.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports2.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports2.setManagerComments("No data available for the selected period. So, Email can't be sent!");
        reports2.setManagerEmail("jane.doe@example.org");
        reports2.setManagerReviewTime("No data available for the selected period. So, Email can't be sent!");
        reports2.setOfficialEmployeeId("42");
        reports2.setReportId(1L);
        reports2.setReportTitle("Dr");
        reports2.setTotalAmount(10.0f);
        reports2.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportsList = new ArrayList<>();
        reportsList.add(reports2);
        when(reportsRepository.findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(reportsList);
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("Email sent successfully!", excelGeneratorReportsServiceImpl.generateExcelAndSendEmail(response, startDate, LocalDate.of(1970, 1, 1), StatusExcel.ALL));
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(employee).getEmployeeEmail();
        verify(employee).setEmployeeEmail(Mockito.<String>any());
        verify(employee).setEmployeeId(Mockito.<Long>any());
        verify(employee).setExpenseList(Mockito.<List<Expense>>any());
        verify(employee).setFirstName(Mockito.<String>any());
        verify(employee).setHrEmail(Mockito.<String>any());
        verify(employee).setHrName(Mockito.<String>any());
        verify(employee).setImageUrl(Mockito.<String>any());
        verify(employee).setIsFinanceAdmin(Mockito.<Boolean>any());
        verify(employee).setIsHidden(Mockito.<Boolean>any());
        verify(employee).setLastName(Mockito.<String>any());
        verify(employee).setLndEmail(Mockito.<String>any());
        verify(employee).setLndName(Mockito.<String>any());
        verify(employee).setManagerEmail(Mockito.<String>any());
        verify(employee).setManagerName(Mockito.<String>any());
        verify(employee).setMiddleName(Mockito.<String>any());
        verify(employee).setMobileNumber(Mockito.<Long>any());
        verify(employee).setOfficialEmployeeId(Mockito.<String>any());
        verify(employee).setRole(Mockito.<String>any());
        verify(employee).setToken(Mockito.<String>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
        verify(reportsRepository).findByDateSubmittedBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcel(ByteArrayOutputStream, LocalDate, LocalDate, StatusExcel,
     * List)}
     */
    @Test
    void testGenerateExcel() throws Exception {
        TestableByteArrayOutputStream excelStream = mock(TestableByteArrayOutputStream.class);
        doNothing().when(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.of(1970, 1, 1);
        excelGeneratorReportsServiceImpl.generateExcel(excelStream, startDate, endDate, StatusExcel.ALL, new ArrayList<>());
        verify(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcel(ByteArrayOutputStream, LocalDate, LocalDate, StatusExcel,
     * List)}
     */
    @Test
    void testGenerateExcel2() throws Exception {
        TestableByteArrayOutputStream excelStream = mock(TestableByteArrayOutputStream.class);
        doNothing().when(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.of(1970, 1, 1);
        excelGeneratorReportsServiceImpl.generateExcel(excelStream, startDate, endDate, StatusExcel.REIMBURSED, new ArrayList<>());
        verify(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcel(ByteArrayOutputStream, LocalDate, LocalDate, StatusExcel,
     * List)}
     */
    @Test
    void testGenerateExcel3() throws Exception {
        TestableByteArrayOutputStream excelStream = mock(TestableByteArrayOutputStream.class);
        doNothing().when(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.of(1970, 1, 1);
        excelGeneratorReportsServiceImpl.generateExcel(excelStream, startDate, endDate, StatusExcel.COMPLETED, new ArrayList<>());
        verify(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcel(ByteArrayOutputStream, LocalDate, LocalDate, StatusExcel,
     * List)}
     */
    @Test
    void testGenerateExcel4() throws Exception {
        TestableByteArrayOutputStream excelStream = mock(TestableByteArrayOutputStream.class);
        doNothing().when(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.of(1970, 1, 1);
        excelGeneratorReportsServiceImpl.generateExcel(excelStream, startDate, endDate, StatusExcel.PENDING, new ArrayList<>());
        verify(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcel(ByteArrayOutputStream, LocalDate, LocalDate, StatusExcel,
     * List)}
     */
    @Test
    void testGenerateExcel5() throws Exception {
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        TestableByteArrayOutputStream excelStream = mock(TestableByteArrayOutputStream.class);
        doNothing().when(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.of(1970, 1, 1);

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Billfold_All_reports");
        reports.setEmployeeName("Billfold_All_reports");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Billfold_All_reports");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Billfold_All_reports");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Billfold_All_reports");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportlist = new ArrayList<>();
        reportlist.add(reports);
        excelGeneratorReportsServiceImpl.generateExcel(excelStream, startDate, endDate, StatusExcel.ALL, reportlist);
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcel(ByteArrayOutputStream, LocalDate, LocalDate, StatusExcel,
     * List)}
     */
    @Test
    void testGenerateExcel6() throws Exception {
        Category category = new Category();
        category.setCategoryDescription("Billfold_All_reports");
        category.setCategoryId(1L);
        category.setCategoryTotal(12L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);

        Employee employee = new Employee();
        employee.setEmployeeEmail("jane.doe@example.org");
        employee.setEmployeeId(1L);
        employee.setExpenseList(new ArrayList<>());
        employee.setFirstName("Jane");
        employee.setHrEmail("jane.doe@example.org");
        employee.setHrName("Billfold_All_reports");
        employee.setImageUrl("https://example.org/example");
        employee.setIsFinanceAdmin(true);
        employee.setIsHidden(true);
        employee.setLastName("Doe");
        employee.setLndEmail("jane.doe@example.org");
        employee.setLndName("Billfold_All_reports");
        employee.setManagerEmail("jane.doe@example.org");
        employee.setManagerName("Billfold_All_reports");
        employee.setMiddleName("Billfold_All_reports");
        employee.setMobileNumber(12L);
        employee.setOfficialEmployeeId("42");
        employee.setRole("Billfold_All_reports");
        employee.setToken("ABC123");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Billfold_All_reports");
        reports.setEmployeeName("Billfold_All_reports");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Billfold_All_reports");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Billfold_All_reports");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Billfold_All_reports");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Expense expense = new Expense();
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("Billfold_All_reports");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("The characteristics of someone or something");
        expense.setEmployee(employee);
        expense.setExpenseId(1L);
        expense.setFile("A\fA\fA\fA\f".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Billfold_All_reports");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);
        TestableByteArrayOutputStream excelStream = mock(TestableByteArrayOutputStream.class);
        doNothing().when(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.of(1970, 1, 1);

        Reports reports2 = new Reports();
        reports2.setDateCreated(LocalDate.of(1970, 1, 1));
        reports2.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports2.setEmployeeId(1L);
        reports2.setEmployeeMail("Billfold_All_reports");
        reports2.setEmployeeName("Billfold_All_reports");
        reports2.setExpensesCount(3L);
        reports2.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports2.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports2.setFinanceComments("Billfold_All_reports");
        reports2.setIsHidden(true);
        reports2.setIsSubmitted(true);
        reports2.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports2.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports2.setManagerComments("Billfold_All_reports");
        reports2.setManagerEmail("jane.doe@example.org");
        reports2.setManagerReviewTime("Billfold_All_reports");
        reports2.setOfficialEmployeeId("42");
        reports2.setReportId(1L);
        reports2.setReportTitle("Dr");
        reports2.setTotalAmount(10.0f);
        reports2.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportlist = new ArrayList<>();
        reportlist.add(reports2);
        excelGeneratorReportsServiceImpl.generateExcel(excelStream, startDate, endDate, StatusExcel.ALL, reportlist);
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcel(ByteArrayOutputStream, LocalDate, LocalDate, StatusExcel,
     * List)}
     */
    @Test
    void testGenerateExcel7() throws Exception {
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        TestableByteArrayOutputStream excelStream = mock(TestableByteArrayOutputStream.class);
        doNothing().when(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.of(1970, 1, 1);

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Billfold_All_reports");
        reports.setEmployeeName("Billfold_All_reports");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Billfold_All_reports");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Billfold_All_reports");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Billfold_All_reports");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportlist = new ArrayList<>();
        reportlist.add(reports);
        excelGeneratorReportsServiceImpl.generateExcel(excelStream, startDate, endDate, StatusExcel.REIMBURSED, reportlist);
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcel(ByteArrayOutputStream, LocalDate, LocalDate, StatusExcel,
     * List)}
     */
    @Test
    void testGenerateExcel8() throws Exception {
        TestableByteArrayOutputStream excelStream = mock(TestableByteArrayOutputStream.class);
        doNothing().when(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.of(1970, 1, 1);

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Billfold_All_reports");
        reports.setEmployeeName("Billfold_All_reports");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Billfold_All_reports");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Billfold_All_reports");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Billfold_All_reports");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportlist = new ArrayList<>();
        reportlist.add(reports);
        excelGeneratorReportsServiceImpl.generateExcel(excelStream, startDate, endDate, StatusExcel.COMPLETED, reportlist);
        verify(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcel(ByteArrayOutputStream, LocalDate, LocalDate, StatusExcel,
     * List)}
     */
    @Test
    void testGenerateExcel9() throws Exception {
        TestableByteArrayOutputStream excelStream = mock(TestableByteArrayOutputStream.class);
        doNothing().when(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.of(1970, 1, 1);

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Billfold_All_reports");
        reports.setEmployeeName("Billfold_All_reports");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Billfold_All_reports");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Billfold_All_reports");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Billfold_All_reports");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportlist = new ArrayList<>();
        reportlist.add(reports);
        excelGeneratorReportsServiceImpl.generateExcel(excelStream, startDate, endDate, StatusExcel.PENDING, reportlist);
        verify(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcel(ByteArrayOutputStream, LocalDate, LocalDate, StatusExcel,
     * List)}
     */
    @Test
    void testGenerateExcel10() throws Exception {
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        TestableByteArrayOutputStream excelStream = mock(TestableByteArrayOutputStream.class);
        doNothing().when(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.of(1970, 1, 1);

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Billfold_All_reports");
        reports.setEmployeeName("Billfold_All_reports");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Billfold_All_reports");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Billfold_All_reports");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Billfold_All_reports");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Reports reports2 = new Reports();
        reports2.setDateCreated(LocalDate.of(1970, 1, 1));
        reports2.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports2.setEmployeeId(2L);
        reports2.setEmployeeMail("Employee Mail");
        reports2.setEmployeeName("Employee Name");
        reports2.setExpensesCount(12L);
        reports2.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports2.setFinanceApprovalStatus(FinanceApprovalStatus.REJECTED);
        reports2.setFinanceComments("Finance Comments");
        reports2.setIsHidden(false);
        reports2.setIsSubmitted(false);
        reports2.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports2.setManagerApprovalStatus(ManagerApprovalStatus.REJECTED);
        reports2.setManagerComments("Manager Comments");
        reports2.setManagerEmail("john.smith@example.org");
        reports2.setManagerReviewTime("Manager Review Time");
        reports2.setOfficialEmployeeId("Billfold_All_reports");
        reports2.setReportId(2L);
        reports2.setReportTitle("Mr");
        reports2.setTotalAmount(0.5f);
        reports2.setTotalApprovedAmount(0.5f);

        ArrayList<Reports> reportlist = new ArrayList<>();
        reportlist.add(reports2);
        reportlist.add(reports);
        excelGeneratorReportsServiceImpl.generateExcel(excelStream, startDate, endDate, StatusExcel.ALL, reportlist);
        verify(iExpenseService, atLeast(1)).getExpenseByReportId(Mockito.<Long>any());
        verify(excelStream).write(Mockito.<byte[]>any(), anyInt(), anyInt());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#generateExcel(ByteArrayOutputStream, LocalDate, LocalDate, StatusExcel,
     * List)}
     */
    @Test
    void testGenerateExcel11() throws Exception {
        Category category = new Category();
        category.setCategoryDescription("");
        category.setCategoryId(3L);
        category.setCategoryTotal(Long.MAX_VALUE);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);

        Employee employee = new Employee();
        employee.setEmployeeEmail("prof.einstein@example.org");
        employee.setEmployeeId(3L);
        employee.setExpenseList(new ArrayList<>());
        employee.setFirstName("Albert");
        employee.setHrEmail("prof.einstein@example.org");
        employee.setHrName("42");
        employee.setImageUrl("EMPLOYEE");
        employee.setIsFinanceAdmin(true);
        employee.setIsHidden(true);
        employee.setLastName("Einstein");
        employee.setLndEmail("prof.einstein@example.org");
        employee.setLndName("42");
        employee.setManagerEmail("prof.einstein@example.org");
        employee.setManagerName("42");
        employee.setMiddleName("42");
        employee.setMobileNumber(Long.MAX_VALUE);
        employee.setOfficialEmployeeId("EMPLOYEE");
        employee.setRole("42");
        employee.setToken("EMPLOYEE");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(3L);
        reports.setEmployeeMail("");
        reports.setEmployeeName("");
        reports.setExpensesCount(-1L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.PENDING);
        reports.setFinanceComments("");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);
        reports.setManagerComments("");
        reports.setManagerEmail("prof.einstein@example.org");
        reports.setManagerReviewTime("");
        reports.setOfficialEmployeeId("");
        reports.setReportId(3L);
        reports.setReportTitle("Prof");
        reports.setTotalAmount(-0.5f);
        reports.setTotalApprovedAmount(-0.5f);

        Expense expense = new Expense();
        expense.setAmount(-0.5d);
        expense.setAmountApproved(-0.5d);
        expense.setCategory(category);
        expense.setCategoryDescription("");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("42");
        expense.setEmployee(employee);
        expense.setExpenseId(3L);
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("42");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.PENDING);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
        expense.setMerchantName("");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Prof");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);
        TestableByteArrayOutputStream excelStream = mock(TestableByteArrayOutputStream.class);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.of(1970, 1, 1);
        Reports reports2 = mock(Reports.class);
        when(reports2.getManagerActionDate()).thenThrow(new IllegalStateException("Billfold_All_reports"));
        when(reports2.getReportTitle()).thenReturn("Dr");
        when(reports2.getDateSubmitted()).thenReturn(LocalDate.of(1970, 1, 1));
        when(reports2.getReportId()).thenReturn(1L);
        when(reports2.getEmployeeMail()).thenReturn("Employee Mail");
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
        reports2.setEmployeeMail("Billfold_All_reports");
        reports2.setEmployeeName("Billfold_All_reports");
        reports2.setExpensesCount(3L);
        reports2.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports2.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports2.setFinanceComments("Billfold_All_reports");
        reports2.setIsHidden(true);
        reports2.setIsSubmitted(true);
        reports2.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports2.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports2.setManagerComments("Billfold_All_reports");
        reports2.setManagerEmail("jane.doe@example.org");
        reports2.setManagerReviewTime("Billfold_All_reports");
        reports2.setOfficialEmployeeId("42");
        reports2.setReportId(1L);
        reports2.setReportTitle("Dr");
        reports2.setTotalAmount(10.0f);
        reports2.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportlist = new ArrayList<>();
        reportlist.add(reports2);
        assertThrows(IllegalStateException.class, () -> excelGeneratorReportsServiceImpl.generateExcel(excelStream, startDate, endDate, StatusExcel.ALL, reportlist));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(reports2, atLeast(1)).getReportId();
        verify(reports2).getEmployeeMail();
        verify(reports2).getReportTitle();
        verify(reports2, atLeast(1)).getDateSubmitted();
        verify(reports2).getManagerActionDate();
        verify(reports2).setDateCreated(Mockito.<LocalDate>any());
        verify(reports2).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports2).setEmployeeId(Mockito.<Long>any());
        verify(reports2).setEmployeeMail(Mockito.<String>any());
        verify(reports2).setEmployeeName(Mockito.<String>any());
        verify(reports2).setExpensesCount(Mockito.<Long>any());
        verify(reports2).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports2).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports2).setFinanceComments(Mockito.<String>any());
        verify(reports2).setIsHidden(Mockito.<Boolean>any());
        verify(reports2).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports2).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports2).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports2).setManagerComments(Mockito.<String>any());
        verify(reports2).setManagerEmail(Mockito.<String>any());
        verify(reports2).setManagerReviewTime(Mockito.<String>any());
        verify(reports2).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports2).setReportId(Mockito.<Long>any());
        verify(reports2).setReportTitle(Mockito.<String>any());
        verify(reports2).setTotalAmount(anyFloat());
        verify(reports2).setTotalApprovedAmount(anyFloat());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#sendEmailWithAttachment(String, String, String, byte[], String)}
     */
    @Test
    void testSendEmailWithAttachment() throws UnsupportedEncodingException, MailException {
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertTrue(excelGeneratorReportsServiceImpl.sendEmailWithAttachment("jane.doe@example.org", "Hello from the Dreaming Spires", "Not all who wander are lost", "AXAXAXAX".getBytes("UTF-8"), "foo.txt"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorReportsServiceImpl#sendEmailWithAttachment(String, String, String, byte[], String)}
     */
    @Test
    void testSendEmailWithAttachment2() throws UnsupportedEncodingException, MailException {
        doThrow(new IllegalStateException("application/vnd.ms-excel")).when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertFalse(excelGeneratorReportsServiceImpl.sendEmailWithAttachment("jane.doe@example.org", "Hello from the Dreaming Spires", "Not all who wander are lost", "AXAXAXAX".getBytes("UTF-8"), "foo.txt"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    /**
     * Method under test: {@link ExcelGeneratorReportsServiceImpl#reimburseAndGenerateExcel(HttpServletResponse)}
     */
    @Test
    void testReimburseAndGenerateExcel() throws Exception {
        when(reportsRepository.findByfinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any())).thenReturn(new ArrayList<>());
        assertEquals("No data available for the selected period. So, Email can't be sent!", excelGeneratorReportsServiceImpl.reimburseAndGenerateExcel(new Response()));
        verify(reportsRepository).findByfinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
    }

    /**
     * Method under test: {@link ExcelGeneratorReportsServiceImpl#reimburseAndGenerateExcel(HttpServletResponse)}
     */
    @Test
    void testReimburseAndGenerateExcel2() throws Exception {
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
        when(employeeRepository.findByRole(Mockito.<String>any())).thenReturn(employee);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("No data available for the selected period. So, Email can't be sent!");
        reports.setEmployeeName("No data available for the selected period. So, Email can't be sent!");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("No data available for the selected period. So, Email can't be sent!");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("No data available for the selected period. So, Email can't be sent!");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("No data available for the selected period. So, Email can't be sent!");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportsList = new ArrayList<>();
        reportsList.add(reports);
        when(reportsRepository.findByfinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any())).thenReturn(reportsList);
        assertEquals("Email sent successfully!", excelGeneratorReportsServiceImpl.reimburseAndGenerateExcel(new Response()));
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
        verify(reportsRepository).findByfinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
    }

    /**
     * Method under test: {@link ExcelGeneratorReportsServiceImpl#reimburseAndGenerateExcel(HttpServletResponse)}
     */
    @Test
    void testReimburseAndGenerateExcel3() throws Exception {
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
        when(employeeRepository.findByRole(Mockito.<String>any())).thenReturn(employee);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        doThrow(new IllegalStateException("Billfold_All_reports")).when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("No data available for the selected period. So, Email can't be sent!");
        reports.setEmployeeName("No data available for the selected period. So, Email can't be sent!");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("No data available for the selected period. So, Email can't be sent!");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("No data available for the selected period. So, Email can't be sent!");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("No data available for the selected period. So, Email can't be sent!");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportsList = new ArrayList<>();
        reportsList.add(reports);
        when(reportsRepository.findByfinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any())).thenReturn(reportsList);
        assertEquals("Email not sent", excelGeneratorReportsServiceImpl.reimburseAndGenerateExcel(new Response()));
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
        verify(reportsRepository).findByfinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
    }

    /**
     * Method under test: {@link ExcelGeneratorReportsServiceImpl#reimburseAndGenerateExcel(HttpServletResponse)}
     */
    @Test
    void testReimburseAndGenerateExcel4() throws Exception {
        Employee employee = mock(Employee.class);
        when(employee.getEmployeeEmail()).thenReturn("");
        doNothing().when(employee).setEmployeeEmail(Mockito.<String>any());
        doNothing().when(employee).setEmployeeId(Mockito.<Long>any());
        doNothing().when(employee).setExpenseList(Mockito.<List<Expense>>any());
        doNothing().when(employee).setFirstName(Mockito.<String>any());
        doNothing().when(employee).setHrEmail(Mockito.<String>any());
        doNothing().when(employee).setHrName(Mockito.<String>any());
        doNothing().when(employee).setImageUrl(Mockito.<String>any());
        doNothing().when(employee).setIsFinanceAdmin(Mockito.<Boolean>any());
        doNothing().when(employee).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(employee).setLastName(Mockito.<String>any());
        doNothing().when(employee).setLndEmail(Mockito.<String>any());
        doNothing().when(employee).setLndName(Mockito.<String>any());
        doNothing().when(employee).setManagerEmail(Mockito.<String>any());
        doNothing().when(employee).setManagerName(Mockito.<String>any());
        doNothing().when(employee).setMiddleName(Mockito.<String>any());
        doNothing().when(employee).setMobileNumber(Mockito.<Long>any());
        doNothing().when(employee).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(employee).setRole(Mockito.<String>any());
        doNothing().when(employee).setToken(Mockito.<String>any());
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
        when(employeeRepository.findByRole(Mockito.<String>any())).thenReturn(employee);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("No data available for the selected period. So, Email can't be sent!");
        reports.setEmployeeName("No data available for the selected period. So, Email can't be sent!");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("No data available for the selected period. So, Email can't be sent!");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("No data available for the selected period. So, Email can't be sent!");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("No data available for the selected period. So, Email can't be sent!");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportsList = new ArrayList<>();
        reportsList.add(reports);
        when(reportsRepository.findByfinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any())).thenReturn(reportsList);
        assertEquals("Email not sent", excelGeneratorReportsServiceImpl.reimburseAndGenerateExcel(new Response()));
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(employee).getEmployeeEmail();
        verify(employee).setEmployeeEmail(Mockito.<String>any());
        verify(employee).setEmployeeId(Mockito.<Long>any());
        verify(employee).setExpenseList(Mockito.<List<Expense>>any());
        verify(employee).setFirstName(Mockito.<String>any());
        verify(employee).setHrEmail(Mockito.<String>any());
        verify(employee).setHrName(Mockito.<String>any());
        verify(employee).setImageUrl(Mockito.<String>any());
        verify(employee).setIsFinanceAdmin(Mockito.<Boolean>any());
        verify(employee).setIsHidden(Mockito.<Boolean>any());
        verify(employee).setLastName(Mockito.<String>any());
        verify(employee).setLndEmail(Mockito.<String>any());
        verify(employee).setLndName(Mockito.<String>any());
        verify(employee).setManagerEmail(Mockito.<String>any());
        verify(employee).setManagerName(Mockito.<String>any());
        verify(employee).setMiddleName(Mockito.<String>any());
        verify(employee).setMobileNumber(Mockito.<Long>any());
        verify(employee).setOfficialEmployeeId(Mockito.<String>any());
        verify(employee).setRole(Mockito.<String>any());
        verify(employee).setToken(Mockito.<String>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(reportsRepository).findByfinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
    }

    /**
     * Method under test: {@link ExcelGeneratorReportsServiceImpl#reimburseAndGenerateExcel(HttpServletResponse)}
     */
    @Test
    void testReimburseAndGenerateExcel5() throws Exception {
        Employee employee = mock(Employee.class);
        when(employee.getEmployeeEmail()).thenReturn("jane.doe@example.org");
        doNothing().when(employee).setEmployeeEmail(Mockito.<String>any());
        doNothing().when(employee).setEmployeeId(Mockito.<Long>any());
        doNothing().when(employee).setExpenseList(Mockito.<List<Expense>>any());
        doNothing().when(employee).setFirstName(Mockito.<String>any());
        doNothing().when(employee).setHrEmail(Mockito.<String>any());
        doNothing().when(employee).setHrName(Mockito.<String>any());
        doNothing().when(employee).setImageUrl(Mockito.<String>any());
        doNothing().when(employee).setIsFinanceAdmin(Mockito.<Boolean>any());
        doNothing().when(employee).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(employee).setLastName(Mockito.<String>any());
        doNothing().when(employee).setLndEmail(Mockito.<String>any());
        doNothing().when(employee).setLndName(Mockito.<String>any());
        doNothing().when(employee).setManagerEmail(Mockito.<String>any());
        doNothing().when(employee).setManagerName(Mockito.<String>any());
        doNothing().when(employee).setMiddleName(Mockito.<String>any());
        doNothing().when(employee).setMobileNumber(Mockito.<Long>any());
        doNothing().when(employee).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(employee).setRole(Mockito.<String>any());
        doNothing().when(employee).setToken(Mockito.<String>any());
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
        when(employeeRepository.findByRole(Mockito.<String>any())).thenReturn(employee);

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

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        Reports reports2 = new Reports();
        reports2.setDateCreated(LocalDate.of(1970, 1, 1));
        reports2.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports2.setEmployeeId(1L);
        reports2.setEmployeeMail("No data available for the selected period. So, Email can't be sent!");
        reports2.setEmployeeName("No data available for the selected period. So, Email can't be sent!");
        reports2.setExpensesCount(3L);
        reports2.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports2.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports2.setFinanceComments("No data available for the selected period. So, Email can't be sent!");
        reports2.setIsHidden(true);
        reports2.setIsSubmitted(true);
        reports2.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports2.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports2.setManagerComments("No data available for the selected period. So, Email can't be sent!");
        reports2.setManagerEmail("jane.doe@example.org");
        reports2.setManagerReviewTime("No data available for the selected period. So, Email can't be sent!");
        reports2.setOfficialEmployeeId("42");
        reports2.setReportId(1L);
        reports2.setReportTitle("Dr");
        reports2.setTotalAmount(10.0f);
        reports2.setTotalApprovedAmount(10.0f);

        ArrayList<Reports> reportsList = new ArrayList<>();
        reportsList.add(reports2);
        when(reportsRepository.findByfinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any())).thenReturn(reportsList);
        assertEquals("Email sent successfully!", excelGeneratorReportsServiceImpl.reimburseAndGenerateExcel(new Response()));
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(employee).getEmployeeEmail();
        verify(employee).setEmployeeEmail(Mockito.<String>any());
        verify(employee).setEmployeeId(Mockito.<Long>any());
        verify(employee).setExpenseList(Mockito.<List<Expense>>any());
        verify(employee).setFirstName(Mockito.<String>any());
        verify(employee).setHrEmail(Mockito.<String>any());
        verify(employee).setHrName(Mockito.<String>any());
        verify(employee).setImageUrl(Mockito.<String>any());
        verify(employee).setIsFinanceAdmin(Mockito.<Boolean>any());
        verify(employee).setIsHidden(Mockito.<Boolean>any());
        verify(employee).setLastName(Mockito.<String>any());
        verify(employee).setLndEmail(Mockito.<String>any());
        verify(employee).setLndName(Mockito.<String>any());
        verify(employee).setManagerEmail(Mockito.<String>any());
        verify(employee).setManagerName(Mockito.<String>any());
        verify(employee).setMiddleName(Mockito.<String>any());
        verify(employee).setMobileNumber(Mockito.<Long>any());
        verify(employee).setOfficialEmployeeId(Mockito.<String>any());
        verify(employee).setRole(Mockito.<String>any());
        verify(employee).setToken(Mockito.<String>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
        verify(reportsRepository).findByfinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
    }
}