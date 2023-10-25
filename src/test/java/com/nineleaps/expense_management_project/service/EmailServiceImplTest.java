package com.nineleaps.expense_management_project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.nineleaps.expense_management_project.entity.Category;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.entity.Expense;
import com.nineleaps.expense_management_project.entity.FinanceApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatusExpense;
import com.nineleaps.expense_management_project.entity.Reports;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;



@ContextConfiguration(classes = {EmailServiceImpl.class})
@ExtendWith(SpringExtension.class)
class EmailServiceImplTest {
    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private ExpenseRepository expenseRepository;

    @MockBean
    private IEmployeeService iEmployeeService;

    @MockBean
    private IExpenseService iExpenseService;

    @MockBean
    private IPdfGeneratorService iPdfGeneratorService;

    @MockBean
    private IReportsService iReportsService;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private ReportsRepository reportsRepository;



    @Test
    void testNotifyHr() throws MessagingException, MailException {
        // Arrange
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
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports);
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        // Act
        emailServiceImpl.notifyHr(1L, "jane.doe@example.org", "Hr Name");

        // Assert
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    @Test
    void testNotifyHr2() throws MailException {
        // Arrange
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
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports);
        doThrow(new IllegalStateException("billfold.noreply@gmail.com")).when(javaMailSender)
                .send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> emailServiceImpl.notifyHr(1L, "jane.doe@example.org", "Hr Name"));
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    @Test
    void testNotifyLnD() throws MessagingException, MailException {
        // Arrange
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
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports);
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        // Act
        emailServiceImpl.notifyLnD(1L, "jane.doe@example.org", "Lnd Name");

        // Assert
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    @Test
    void testNotifyLnD2() throws MailException {
        // Arrange
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
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports);
        doThrow(new IllegalStateException("billfold.noreply@gmail.com")).when(javaMailSender)
                .send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.notifyLnD(1L, "jane.doe@example.org", "Lnd Name"));
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    @Test
    void testManagerNotification()  {
        // Arrange
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
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports);
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.managerNotification(1L, expenseIds, "jane.doe@example.org", new Response()));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }


    @Test
    void testManagerNotification2()  {
        // Arrange
        when(iReportsService.getReportById(Mockito.<Long>any())).thenThrow(new IllegalStateException("foo"));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.managerNotification(1L, expenseIds, "jane.doe@example.org", new Response()));
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }

    @Test
    void testManagerNotification3() throws IOException, MessagingException, MailException {
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);
        when(iPdfGeneratorService.export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any())).thenReturn("AXAXAXAX".getBytes("UTF-8"));

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
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act
        emailServiceImpl.managerNotification(1L, expenseIds, "jane.doe@example.org", new Response());

        // Assert
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iPdfGeneratorService).export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }


    @Test
    void testManagerNotification4() throws IOException, MailException {
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);
        when(iPdfGeneratorService.export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any())).thenReturn("AXAXAXAX".getBytes("UTF-8"));

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
        doThrow(new IllegalStateException("billfold.noreply@gmail.com")).when(javaMailSender)
                .send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.managerNotification(1L, expenseIds, "jane.doe@example.org", new Response()));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iPdfGeneratorService).export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }


    @Test
    void testManagerNotification5()  {
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
        Employee employee2 = mock(Employee.class);
        when(employee2.getFirstName()).thenThrow(new IllegalStateException("foo"));
        when(employee2.getManagerEmail()).thenReturn("jane.doe@example.org");
        doNothing().when(employee2).setEmployeeEmail(Mockito.<String>any());
        doNothing().when(employee2).setEmployeeId(Mockito.<Long>any());
        doNothing().when(employee2).setExpenseList(Mockito.<List<Expense>>any());
        doNothing().when(employee2).setFirstName(Mockito.<String>any());
        doNothing().when(employee2).setHrEmail(Mockito.<String>any());
        doNothing().when(employee2).setHrName(Mockito.<String>any());
        doNothing().when(employee2).setImageUrl(Mockito.<String>any());
        doNothing().when(employee2).setIsFinanceAdmin(Mockito.<Boolean>any());
        doNothing().when(employee2).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(employee2).setLastName(Mockito.<String>any());
        doNothing().when(employee2).setLndEmail(Mockito.<String>any());
        doNothing().when(employee2).setLndName(Mockito.<String>any());
        doNothing().when(employee2).setManagerEmail(Mockito.<String>any());
        doNothing().when(employee2).setManagerName(Mockito.<String>any());
        doNothing().when(employee2).setMiddleName(Mockito.<String>any());
        doNothing().when(employee2).setMobileNumber(Mockito.<Long>any());
        doNothing().when(employee2).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(employee2).setRole(Mockito.<String>any());
        doNothing().when(employee2).setToken(Mockito.<String>any());
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
        Expense expense = mock(Expense.class);
        when(expense.getEmployee()).thenReturn(employee2);
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);

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
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.managerNotification(1L, expenseIds, "jane.doe@example.org", new Response()));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(expense).getEmployee();
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
        verify(employee2).getFirstName();
        verify(employee2).getManagerEmail();
        verify(employee2).setEmployeeEmail(Mockito.<String>any());
        verify(employee2).setEmployeeId(Mockito.<Long>any());
        verify(employee2).setExpenseList(Mockito.<List<Expense>>any());
        verify(employee2).setFirstName(Mockito.<String>any());
        verify(employee2).setHrEmail(Mockito.<String>any());
        verify(employee2).setHrName(Mockito.<String>any());
        verify(employee2).setImageUrl(Mockito.<String>any());
        verify(employee2).setIsFinanceAdmin(Mockito.<Boolean>any());
        verify(employee2).setIsHidden(Mockito.<Boolean>any());
        verify(employee2).setLastName(Mockito.<String>any());
        verify(employee2).setLndEmail(Mockito.<String>any());
        verify(employee2).setLndName(Mockito.<String>any());
        verify(employee2).setManagerEmail(Mockito.<String>any());
        verify(employee2).setManagerName(Mockito.<String>any());
        verify(employee2).setMiddleName(Mockito.<String>any());
        verify(employee2).setMobileNumber(Mockito.<Long>any());
        verify(employee2).setOfficialEmployeeId(Mockito.<String>any());
        verify(employee2).setRole(Mockito.<String>any());
        verify(employee2).setToken(Mockito.<String>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
    }

    @Test
    void testManagerNotification6() {
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
        Employee employee2 = mock(Employee.class);
        when(employee2.getManagerEmail()).thenReturn(null);
        doNothing().when(employee2).setEmployeeEmail(Mockito.<String>any());
        doNothing().when(employee2).setEmployeeId(Mockito.<Long>any());
        doNothing().when(employee2).setExpenseList(Mockito.<List<Expense>>any());
        doNothing().when(employee2).setFirstName(Mockito.<String>any());
        doNothing().when(employee2).setHrEmail(Mockito.<String>any());
        doNothing().when(employee2).setHrName(Mockito.<String>any());
        doNothing().when(employee2).setImageUrl(Mockito.<String>any());
        doNothing().when(employee2).setIsFinanceAdmin(Mockito.<Boolean>any());
        doNothing().when(employee2).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(employee2).setLastName(Mockito.<String>any());
        doNothing().when(employee2).setLndEmail(Mockito.<String>any());
        doNothing().when(employee2).setLndName(Mockito.<String>any());
        doNothing().when(employee2).setManagerEmail(Mockito.<String>any());
        doNothing().when(employee2).setManagerName(Mockito.<String>any());
        doNothing().when(employee2).setMiddleName(Mockito.<String>any());
        doNothing().when(employee2).setMobileNumber(Mockito.<Long>any());
        doNothing().when(employee2).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(employee2).setRole(Mockito.<String>any());
        doNothing().when(employee2).setToken(Mockito.<String>any());
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
        Expense expense = mock(Expense.class);
        when(expense.getEmployee()).thenReturn(employee2);
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);

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
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.managerNotification(1L, expenseIds, "jane.doe@example.org", new Response()));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(expense).getEmployee();
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
        verify(employee2).getManagerEmail();
        verify(employee2).setEmployeeEmail(Mockito.<String>any());
        verify(employee2).setEmployeeId(Mockito.<Long>any());
        verify(employee2).setExpenseList(Mockito.<List<Expense>>any());
        verify(employee2).setFirstName(Mockito.<String>any());
        verify(employee2).setHrEmail(Mockito.<String>any());
        verify(employee2).setHrName(Mockito.<String>any());
        verify(employee2).setImageUrl(Mockito.<String>any());
        verify(employee2).setIsFinanceAdmin(Mockito.<Boolean>any());
        verify(employee2).setIsHidden(Mockito.<Boolean>any());
        verify(employee2).setLastName(Mockito.<String>any());
        verify(employee2).setLndEmail(Mockito.<String>any());
        verify(employee2).setLndName(Mockito.<String>any());
        verify(employee2).setManagerEmail(Mockito.<String>any());
        verify(employee2).setManagerName(Mockito.<String>any());
        verify(employee2).setMiddleName(Mockito.<String>any());
        verify(employee2).setMobileNumber(Mockito.<Long>any());
        verify(employee2).setOfficialEmployeeId(Mockito.<String>any());
        verify(employee2).setRole(Mockito.<String>any());
        verify(employee2).setToken(Mockito.<String>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }


    @Test
    void testManagerNotification7()  {
        // Arrange
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
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports);
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.managerNotification(1L, expenseIds, new Response()));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }


    @Test
    void testManagerNotification8() {
        // Arrange
        when(iReportsService.getReportById(Mockito.<Long>any())).thenThrow(new IllegalStateException("foo"));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.managerNotification(1L, expenseIds, new Response()));
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }


    @Test
    void testManagerNotification9() throws IOException, MessagingException, MailException {
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);
        when(iPdfGeneratorService.export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any())).thenReturn("AXAXAXAX".getBytes("UTF-8"));

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
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act
        emailServiceImpl.managerNotification(1L, expenseIds, new Response());

        // Assert
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iPdfGeneratorService).export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }


    @Test
    void testManagerNotification10() throws IOException, MailException {
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);
        when(iPdfGeneratorService.export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any())).thenReturn("AXAXAXAX".getBytes("UTF-8"));

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
        doThrow(new IllegalStateException("billfold.noreply@gmail.com")).when(javaMailSender)
                .send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.managerNotification(1L, expenseIds, new Response()));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iPdfGeneratorService).export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }


    @Test
    void testManagerNotification11()  {
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
        Employee employee2 = mock(Employee.class);
        when(employee2.getFirstName()).thenThrow(new IllegalStateException("foo"));
        when(employee2.getManagerEmail()).thenReturn("jane.doe@example.org");
        doNothing().when(employee2).setEmployeeEmail(Mockito.<String>any());
        doNothing().when(employee2).setEmployeeId(Mockito.<Long>any());
        doNothing().when(employee2).setExpenseList(Mockito.<List<Expense>>any());
        doNothing().when(employee2).setFirstName(Mockito.<String>any());
        doNothing().when(employee2).setHrEmail(Mockito.<String>any());
        doNothing().when(employee2).setHrName(Mockito.<String>any());
        doNothing().when(employee2).setImageUrl(Mockito.<String>any());
        doNothing().when(employee2).setIsFinanceAdmin(Mockito.<Boolean>any());
        doNothing().when(employee2).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(employee2).setLastName(Mockito.<String>any());
        doNothing().when(employee2).setLndEmail(Mockito.<String>any());
        doNothing().when(employee2).setLndName(Mockito.<String>any());
        doNothing().when(employee2).setManagerEmail(Mockito.<String>any());
        doNothing().when(employee2).setManagerName(Mockito.<String>any());
        doNothing().when(employee2).setMiddleName(Mockito.<String>any());
        doNothing().when(employee2).setMobileNumber(Mockito.<Long>any());
        doNothing().when(employee2).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(employee2).setRole(Mockito.<String>any());
        doNothing().when(employee2).setToken(Mockito.<String>any());
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
        Expense expense = mock(Expense.class);
        when(expense.getEmployee()).thenReturn(employee2);
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);

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
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.managerNotification(1L, expenseIds, new Response()));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(expense).getEmployee();
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
        verify(employee2).getFirstName();
        verify(employee2, atLeast(1)).getManagerEmail();
        verify(employee2).setEmployeeEmail(Mockito.<String>any());
        verify(employee2).setEmployeeId(Mockito.<Long>any());
        verify(employee2).setExpenseList(Mockito.<List<Expense>>any());
        verify(employee2).setFirstName(Mockito.<String>any());
        verify(employee2).setHrEmail(Mockito.<String>any());
        verify(employee2).setHrName(Mockito.<String>any());
        verify(employee2).setImageUrl(Mockito.<String>any());
        verify(employee2).setIsFinanceAdmin(Mockito.<Boolean>any());
        verify(employee2).setIsHidden(Mockito.<Boolean>any());
        verify(employee2).setLastName(Mockito.<String>any());
        verify(employee2).setLndEmail(Mockito.<String>any());
        verify(employee2).setLndName(Mockito.<String>any());
        verify(employee2).setManagerEmail(Mockito.<String>any());
        verify(employee2).setManagerName(Mockito.<String>any());
        verify(employee2).setMiddleName(Mockito.<String>any());
        verify(employee2).setMobileNumber(Mockito.<Long>any());
        verify(employee2).setOfficialEmployeeId(Mockito.<String>any());
        verify(employee2).setRole(Mockito.<String>any());
        verify(employee2).setToken(Mockito.<String>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
    }


    @Test
    void testManagerNotification12() {
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
        Employee employee2 = mock(Employee.class);
        when(employee2.getManagerEmail()).thenReturn(null);
        doNothing().when(employee2).setEmployeeEmail(Mockito.<String>any());
        doNothing().when(employee2).setEmployeeId(Mockito.<Long>any());
        doNothing().when(employee2).setExpenseList(Mockito.<List<Expense>>any());
        doNothing().when(employee2).setFirstName(Mockito.<String>any());
        doNothing().when(employee2).setHrEmail(Mockito.<String>any());
        doNothing().when(employee2).setHrName(Mockito.<String>any());
        doNothing().when(employee2).setImageUrl(Mockito.<String>any());
        doNothing().when(employee2).setIsFinanceAdmin(Mockito.<Boolean>any());
        doNothing().when(employee2).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(employee2).setLastName(Mockito.<String>any());
        doNothing().when(employee2).setLndEmail(Mockito.<String>any());
        doNothing().when(employee2).setLndName(Mockito.<String>any());
        doNothing().when(employee2).setManagerEmail(Mockito.<String>any());
        doNothing().when(employee2).setManagerName(Mockito.<String>any());
        doNothing().when(employee2).setMiddleName(Mockito.<String>any());
        doNothing().when(employee2).setMobileNumber(Mockito.<Long>any());
        doNothing().when(employee2).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(employee2).setRole(Mockito.<String>any());
        doNothing().when(employee2).setToken(Mockito.<String>any());
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
        Expense expense = mock(Expense.class);
        when(expense.getEmployee()).thenReturn(employee2);
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);

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
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.managerNotification(1L, expenseIds, new Response()));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(expense).getEmployee();
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
        verify(employee2).getManagerEmail();
        verify(employee2).setEmployeeEmail(Mockito.<String>any());
        verify(employee2).setEmployeeId(Mockito.<Long>any());
        verify(employee2).setExpenseList(Mockito.<List<Expense>>any());
        verify(employee2).setFirstName(Mockito.<String>any());
        verify(employee2).setHrEmail(Mockito.<String>any());
        verify(employee2).setHrName(Mockito.<String>any());
        verify(employee2).setImageUrl(Mockito.<String>any());
        verify(employee2).setIsFinanceAdmin(Mockito.<Boolean>any());
        verify(employee2).setIsHidden(Mockito.<Boolean>any());
        verify(employee2).setLastName(Mockito.<String>any());
        verify(employee2).setLndEmail(Mockito.<String>any());
        verify(employee2).setLndName(Mockito.<String>any());
        verify(employee2).setManagerEmail(Mockito.<String>any());
        verify(employee2).setManagerName(Mockito.<String>any());
        verify(employee2).setMiddleName(Mockito.<String>any());
        verify(employee2).setMobileNumber(Mockito.<Long>any());
        verify(employee2).setOfficialEmployeeId(Mockito.<String>any());
        verify(employee2).setRole(Mockito.<String>any());
        verify(employee2).setToken(Mockito.<String>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }


    @Test
    void testUserApprovedNotification()  {
        // Arrange
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
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports);
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.userApprovedNotification(1L, expenseIds, new Response()));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }


    @Test
    void testUserApprovedNotification2() {
        // Arrange
        when(iReportsService.getReportById(Mockito.<Long>any())).thenThrow(new IllegalStateException("foo"));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.userApprovedNotification(1L, expenseIds, new Response()));
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }


    @Test
    void testUserApprovedNotification3() throws IOException, MessagingException, MailException {
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);
        when(iPdfGeneratorService.export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any())).thenReturn("AXAXAXAX".getBytes("UTF-8"));

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
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act
        emailServiceImpl.userApprovedNotification(1L, expenseIds, new Response());

        // Assert
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iPdfGeneratorService).export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }


    @Test
    void testUserApprovedNotification4() throws IOException, MailException {
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);
        when(iPdfGeneratorService.export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any())).thenReturn("AXAXAXAX".getBytes("UTF-8"));

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
        doThrow(new IllegalStateException("billfold.noreply@gmail.com")).when(javaMailSender)
                .send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.userApprovedNotification(1L, expenseIds, new Response()));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iPdfGeneratorService).export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }


    @Test
    void testUserApprovedNotification5() {
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
        Employee employee2 = mock(Employee.class);
        when(employee2.getFirstName()).thenThrow(new IllegalStateException("foo"));
        when(employee2.getEmployeeEmail()).thenReturn("jane.doe@example.org");
        doNothing().when(employee2).setEmployeeEmail(Mockito.<String>any());
        doNothing().when(employee2).setEmployeeId(Mockito.<Long>any());
        doNothing().when(employee2).setExpenseList(Mockito.<List<Expense>>any());
        doNothing().when(employee2).setFirstName(Mockito.<String>any());
        doNothing().when(employee2).setHrEmail(Mockito.<String>any());
        doNothing().when(employee2).setHrName(Mockito.<String>any());
        doNothing().when(employee2).setImageUrl(Mockito.<String>any());
        doNothing().when(employee2).setIsFinanceAdmin(Mockito.<Boolean>any());
        doNothing().when(employee2).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(employee2).setLastName(Mockito.<String>any());
        doNothing().when(employee2).setLndEmail(Mockito.<String>any());
        doNothing().when(employee2).setLndName(Mockito.<String>any());
        doNothing().when(employee2).setManagerEmail(Mockito.<String>any());
        doNothing().when(employee2).setManagerName(Mockito.<String>any());
        doNothing().when(employee2).setMiddleName(Mockito.<String>any());
        doNothing().when(employee2).setMobileNumber(Mockito.<Long>any());
        doNothing().when(employee2).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(employee2).setRole(Mockito.<String>any());
        doNothing().when(employee2).setToken(Mockito.<String>any());
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
        Expense expense = mock(Expense.class);
        when(expense.getEmployee()).thenReturn(employee2);
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);

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
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.userApprovedNotification(1L, expenseIds, new Response()));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(expense).getEmployee();
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
        verify(employee2).getEmployeeEmail();
        verify(employee2).getFirstName();
        verify(employee2).setEmployeeEmail(Mockito.<String>any());
        verify(employee2).setEmployeeId(Mockito.<Long>any());
        verify(employee2).setExpenseList(Mockito.<List<Expense>>any());
        verify(employee2).setFirstName(Mockito.<String>any());
        verify(employee2).setHrEmail(Mockito.<String>any());
        verify(employee2).setHrName(Mockito.<String>any());
        verify(employee2).setImageUrl(Mockito.<String>any());
        verify(employee2).setIsFinanceAdmin(Mockito.<Boolean>any());
        verify(employee2).setIsHidden(Mockito.<Boolean>any());
        verify(employee2).setLastName(Mockito.<String>any());
        verify(employee2).setLndEmail(Mockito.<String>any());
        verify(employee2).setLndName(Mockito.<String>any());
        verify(employee2).setManagerEmail(Mockito.<String>any());
        verify(employee2).setManagerName(Mockito.<String>any());
        verify(employee2).setMiddleName(Mockito.<String>any());
        verify(employee2).setMobileNumber(Mockito.<Long>any());
        verify(employee2).setOfficialEmployeeId(Mockito.<String>any());
        verify(employee2).setRole(Mockito.<String>any());
        verify(employee2).setToken(Mockito.<String>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
    }

    @Test
    void testUserReimbursedNotification() {
        // Arrange
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
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> emailServiceImpl.userReimbursedNotification(1L));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }

    @Test
    void testUserReimbursedNotification2() {
        // Arrange
        when(iReportsService.getReportById(Mockito.<Long>any())).thenThrow(new IllegalStateException("foo"));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> emailServiceImpl.userReimbursedNotification(1L));
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }

    @Test
    void testUserReimbursedNotification3() throws MailException {
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);

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
        doNothing().when(javaMailSender).send(Mockito.<SimpleMailMessage>any());

        // Act
        emailServiceImpl.userReimbursedNotification(1L);

        // Assert
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).send(Mockito.<SimpleMailMessage>any());
    }

    @Test
    void testUserRejectedByFinanceNotification() {
        // Arrange
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
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> emailServiceImpl.userRejectedByFinanceNotification(1L));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }

    @Test
    void testUserRejectedByFinanceNotification2() {
        // Arrange
        when(iReportsService.getReportById(Mockito.<Long>any())).thenThrow(new IllegalStateException("foo"));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> emailServiceImpl.userRejectedByFinanceNotification(1L));
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }


    @Test
    void testUserRejectedByFinanceNotification3() throws MailException {
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);

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
        doNothing().when(javaMailSender).send(Mockito.<SimpleMailMessage>any());

        // Act
        emailServiceImpl.userRejectedByFinanceNotification(1L);

        // Assert
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).send(Mockito.<SimpleMailMessage>any());
    }

    @Test
    void testFinanceNotification() {
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
        when(employeeRepository.findByRole(Mockito.<String>any())).thenReturn(employee);
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
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports);
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.financeNotification(1L, expenseIds, new Response()));
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }

    @Test
    void testFinanceNotification2() {
        // Arrange
        when(iReportsService.getReportById(Mockito.<Long>any())).thenThrow(new IllegalStateException("FINANCE_ADMIN"));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.financeNotification(1L, expenseIds, new Response()));
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }

    @Test
    void testFinanceNotification3() throws IOException, MessagingException, MailException {
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
        when(employeeRepository.findByRole(Mockito.<String>any())).thenReturn(employee);

        Category category = new Category();
        category.setCategoryDescription("FINANCE_ADMIN");
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
        employee2.setHrName("FINANCE_ADMIN");
        employee2.setImageUrl("https://example.org/example");
        employee2.setIsFinanceAdmin(true);
        employee2.setIsHidden(true);
        employee2.setLastName("Doe");
        employee2.setLndEmail("jane.doe@example.org");
        employee2.setLndName("FINANCE_ADMIN");
        employee2.setManagerEmail("jane.doe@example.org");
        employee2.setManagerName("FINANCE_ADMIN");
        employee2.setMiddleName("FINANCE_ADMIN");
        employee2.setMobileNumber(1L);
        employee2.setOfficialEmployeeId("42");
        employee2.setRole("FINANCE_ADMIN");
        employee2.setToken("ABC123");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("FINANCE_ADMIN");
        reports.setEmployeeName("FINANCE_ADMIN");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("FINANCE_ADMIN");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("FINANCE_ADMIN");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("FINANCE_ADMIN");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Expense expense = new Expense();
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("FINANCE_ADMIN");
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
        expense.setMerchantName("FINANCE_ADMIN");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);
        when(iPdfGeneratorService.export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any())).thenReturn("AXAXAXAX".getBytes("UTF-8"));

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
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act
        emailServiceImpl.financeNotification(1L, expenseIds, new Response());

        // Assert
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iPdfGeneratorService).export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }


    @Test
    void testFinanceNotification4() throws IOException, MailException {
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
        when(employeeRepository.findByRole(Mockito.<String>any())).thenReturn(employee);

        Category category = new Category();
        category.setCategoryDescription("FINANCE_ADMIN");
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
        employee2.setHrName("FINANCE_ADMIN");
        employee2.setImageUrl("https://example.org/example");
        employee2.setIsFinanceAdmin(true);
        employee2.setIsHidden(true);
        employee2.setLastName("Doe");
        employee2.setLndEmail("jane.doe@example.org");
        employee2.setLndName("FINANCE_ADMIN");
        employee2.setManagerEmail("jane.doe@example.org");
        employee2.setManagerName("FINANCE_ADMIN");
        employee2.setMiddleName("FINANCE_ADMIN");
        employee2.setMobileNumber(1L);
        employee2.setOfficialEmployeeId("42");
        employee2.setRole("FINANCE_ADMIN");
        employee2.setToken("ABC123");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("FINANCE_ADMIN");
        reports.setEmployeeName("FINANCE_ADMIN");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("FINANCE_ADMIN");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("FINANCE_ADMIN");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("FINANCE_ADMIN");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Expense expense = new Expense();
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("FINANCE_ADMIN");
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
        expense.setMerchantName("FINANCE_ADMIN");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);
        when(iPdfGeneratorService.export(Mockito.<Long>any(), Mockito.<List<Long>>any(),
                Mockito.<HttpServletResponse>any(), Mockito.<String>any())).thenReturn("AXAXAXAX".getBytes("UTF-8"));

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
        doThrow(new IllegalStateException("FINANCE_ADMIN")).when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.financeNotification(1L, expenseIds, new Response()));
        verify(employeeRepository).findByRole(Mockito.any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iPdfGeneratorService).export(Mockito.<Long>any(), Mockito.any(),
                Mockito.any(), Mockito.any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }


    @Test
    void testFinanceNotification5() {
        // Arrange
        Employee employee = mock(Employee.class);
        when(employee.getFirstName()).thenThrow(new IllegalStateException("foo"));
        when(employee.getEmployeeEmail()).thenReturn("jane.doe@example.org");
        doNothing().when(employee).setEmployeeEmail(Mockito.any());
        doNothing().when(employee).setEmployeeId(Mockito.<Long>any());
        doNothing().when(employee).setExpenseList(Mockito.any());
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
        category.setCategoryDescription("FINANCE_ADMIN");
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
        employee2.setHrName("FINANCE_ADMIN");
        employee2.setImageUrl("https://example.org/example");
        employee2.setIsFinanceAdmin(true);
        employee2.setIsHidden(true);
        employee2.setLastName("Doe");
        employee2.setLndEmail("jane.doe@example.org");
        employee2.setLndName("FINANCE_ADMIN");
        employee2.setManagerEmail("jane.doe@example.org");
        employee2.setManagerName("FINANCE_ADMIN");
        employee2.setMiddleName("FINANCE_ADMIN");
        employee2.setMobileNumber(1L);
        employee2.setOfficialEmployeeId("42");
        employee2.setRole("FINANCE_ADMIN");
        employee2.setToken("ABC123");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("FINANCE_ADMIN");
        reports.setEmployeeName("FINANCE_ADMIN");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("FINANCE_ADMIN");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("FINANCE_ADMIN");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("FINANCE_ADMIN");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Expense expense = new Expense();
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("FINANCE_ADMIN");
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
        expense.setMerchantName("FINANCE_ADMIN");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);

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
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        ArrayList<Long> expenseIds = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalStateException.class,
                () -> emailServiceImpl.financeNotification(1L, expenseIds, new Response()));
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(employee).getEmployeeEmail();
        verify(employee).getFirstName();
        verify(employee).setEmployeeEmail(Mockito.<String>any());
        verify(employee).setEmployeeId(Mockito.<Long>any());
        verify(employee).setExpenseList(Mockito.<List<Expense>>any());
        verify(employee).setFirstName(Mockito.<String>any());
        verify(employee).setHrEmail(Mockito.<String>any());
        verify(employee).setHrName(Mockito.<String>any());
        verify(employee).setImageUrl(Mockito.<String>any());
        verify(employee).setIsFinanceAdmin(Mockito.<Boolean>any());
        verify(employee).setIsHidden(Mockito.<Boolean>any());
        verify(employee).setLastName(Mockito.any());
        verify(employee).setLndEmail(Mockito.<String>any());
        verify(employee).setLndName(Mockito.any());
        verify(employee).setManagerEmail(Mockito.<String>any());
        verify(employee).setManagerName(Mockito.<String>any());
        verify(employee).setMiddleName(Mockito.<String>any());
        verify(employee).setMobileNumber(Mockito.<Long>any());
        verify(employee).setOfficialEmployeeId(Mockito.any());
        verify(employee).setRole(Mockito.<String>any());
        verify(employee).setToken(Mockito.<String>any());
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).createMimeMessage();
    }


    @Test
    void testUserPartialApprovedExpensesNotification() {
        // Arrange
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
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> emailServiceImpl.userPartialApprovedExpensesNotification(1L));
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }


    @Test
    void testUserPartialApprovedExpensesNotification2() {
        // Arrange
        when(iReportsService.getReportById(Mockito.<Long>any())).thenThrow(new IllegalStateException("foo"));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> emailServiceImpl.userPartialApprovedExpensesNotification(1L));
        verify(iReportsService).getReportById(Mockito.<Long>any());
    }


    @Test
    void testUserPartialApprovedExpensesNotification3() throws MailException {
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
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(expenseList);

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
        doNothing().when(javaMailSender).send(Mockito.<SimpleMailMessage>any());

        // Act
        emailServiceImpl.userPartialApprovedExpensesNotification(1L);

        // Assert
        verify(iExpenseService).getExpenseByReportId(Mockito.<Long>any());
        verify(iReportsService).getReportById(Mockito.<Long>any());
        verify(javaMailSender).send(Mockito.<SimpleMailMessage>any());
    }


    @Test
    void testReminderMailToEmployee() throws UnsupportedEncodingException, MailException {
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
        doNothing().when(javaMailSender).send(Mockito.<SimpleMailMessage>any());

        ArrayList<Long> expenseIds = new ArrayList<>();
        expenseIds.add(1L);

        // Act
        emailServiceImpl.reminderMailToEmployee(expenseIds);

        // Assert
        verify(expenseRepository).findById(Mockito.<Long>any());
        verify(javaMailSender).send(Mockito.<SimpleMailMessage>any());
    }


    @Test
    void testReminderMailToEmployee2() throws UnsupportedEncodingException, MailException {
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
        doThrow(new IllegalStateException("billfold.noreply@gmail.com")).when(javaMailSender)
                .send(Mockito.<SimpleMailMessage>any());

        ArrayList<Long> expenseIds = new ArrayList<>();
        expenseIds.add(1L);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> emailServiceImpl.reminderMailToEmployee(expenseIds));
        verify(expenseRepository).findById(Mockito.<Long>any());
        verify(javaMailSender).send(Mockito.<SimpleMailMessage>any());
    }


    @Test
    void testReminderMailToManager() throws MailException {
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
        doNothing().when(javaMailSender).send(Mockito.<SimpleMailMessage>any());

        ArrayList<Long> reportIds = new ArrayList<>();
        reportIds.add(1L);

        // Act
        emailServiceImpl.reminderMailToManager(reportIds);

        // Assert
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(javaMailSender).send(Mockito.<SimpleMailMessage>any());
    }


    @Test
    void testReminderMailToManager2() throws MailException {
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
        doThrow(new IllegalStateException("billfold.noreply@gmail.com")).when(javaMailSender)
                .send(Mockito.<SimpleMailMessage>any());

        ArrayList<Long> reportIds = new ArrayList<>();
        reportIds.add(1L);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> emailServiceImpl.reminderMailToManager(reportIds));
        verify(iEmployeeService).getEmployeeById(Mockito.<Long>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(javaMailSender).send(Mockito.<SimpleMailMessage>any());
    }

}