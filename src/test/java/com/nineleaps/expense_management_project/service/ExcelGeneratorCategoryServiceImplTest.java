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
import com.nineleaps.expense_management_project.repository.CategoryRepository;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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

@ContextConfiguration(classes = {ExcelGeneratorCategoryServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ExcelGeneratorCategoryServiceImplTest {
    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private ExcelGeneratorCategoryServiceImpl excelGeneratorCategoryServiceImpl;

    @MockBean
    private ExpenseRepository expenseRepository;

    @MockBean
    private JavaMailSender javaMailSender;


    @Test
    void testGenerateExcelAndSendEmail() throws Exception {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(new ArrayList<>());
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("No data available for the selected period.So, Email can't be sent!", excelGeneratorCategoryServiceImpl.generateExcelAndSendEmail(response, startDate, LocalDate.of(1970, 1, 1)));
        verify(categoryRepository).findAll();
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    @Test
    void testGenerateExcelAndSendEmail2() throws Exception {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenThrow(new IllegalStateException("No data available for the selected period.So, Email can't be sent!"));
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalStateException.class, () -> excelGeneratorCategoryServiceImpl.generateExcelAndSendEmail(response, startDate, LocalDate.of(1970, 1, 1)));
        verify(categoryRepository).findAll();
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }


    @Test
    void testGenerateExcelAndSendEmail3() throws Exception {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

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
        category.setCategoryDescription("No data available for the selected period.So, Email can't be sent!");
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
        employee2.setHrName("No data available for the selected period.So, Email can't be sent!");
        employee2.setImageUrl("https://example.org/example");
        employee2.setIsFinanceAdmin(true);
        employee2.setIsHidden(true);
        employee2.setLastName("Doe");
        employee2.setLndEmail("jane.doe@example.org");
        employee2.setLndName("No data available for the selected period.So, Email can't be sent!");
        employee2.setManagerEmail("jane.doe@example.org");
        employee2.setManagerName("No data available for the selected period.So, Email can't be sent!");
        employee2.setMiddleName("No data available for the selected period.So, Email can't be sent!");
        employee2.setMobileNumber(1L);
        employee2.setOfficialEmployeeId("42");
        employee2.setRole("No data available for the selected period.So, Email can't be sent!");
        employee2.setToken("ABC123");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("No data available for the selected period.So, Email can't be sent!");
        reports.setEmployeeName("No data available for the selected period.So, Email can't be sent!");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("No data available for the selected period.So, Email can't be sent!");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("No data available for the selected period.So, Email can't be sent!");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("No data available for the selected period.So, Email can't be sent!");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Expense expense = new Expense();
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("No data available for the selected period.So, Email can't be sent!");
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
        expense.setMerchantName("No data available for the selected period.So, Email can't be sent!");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(expenseList);
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("Email sent successfully!", excelGeneratorCategoryServiceImpl.generateExcelAndSendEmail(response, startDate, LocalDate.of(1970, 1, 1)));
        verify(categoryRepository, atLeast(1)).findAll();
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(expenseRepository, atLeast(1)).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }


    @Test
    void testGenerateExcelAndSendEmail4() throws Exception {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

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
        category.setCategoryDescription("No data available for the selected period.So, Email can't be sent!");
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
        employee2.setHrName("No data available for the selected period.So, Email can't be sent!");
        employee2.setImageUrl("https://example.org/example");
        employee2.setIsFinanceAdmin(true);
        employee2.setIsHidden(true);
        employee2.setLastName("Doe");
        employee2.setLndEmail("jane.doe@example.org");
        employee2.setLndName("No data available for the selected period.So, Email can't be sent!");
        employee2.setManagerEmail("jane.doe@example.org");
        employee2.setManagerName("No data available for the selected period.So, Email can't be sent!");
        employee2.setMiddleName("No data available for the selected period.So, Email can't be sent!");
        employee2.setMobileNumber(1L);
        employee2.setOfficialEmployeeId("42");
        employee2.setRole("No data available for the selected period.So, Email can't be sent!");
        employee2.setToken("ABC123");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("No data available for the selected period.So, Email can't be sent!");
        reports.setEmployeeName("No data available for the selected period.So, Email can't be sent!");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("No data available for the selected period.So, Email can't be sent!");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("No data available for the selected period.So, Email can't be sent!");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("No data available for the selected period.So, Email can't be sent!");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Expense expense = new Expense();
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("No data available for the selected period.So, Email can't be sent!");
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
        expense.setMerchantName("No data available for the selected period.So, Email can't be sent!");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(expenseList);
        doThrow(new IllegalStateException("Category Wise Expense Analytics")).when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("Email not sent", excelGeneratorCategoryServiceImpl.generateExcelAndSendEmail(response, startDate, LocalDate.of(1970, 1, 1)));
        verify(categoryRepository, atLeast(1)).findAll();
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(expenseRepository, atLeast(1)).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }


    @Test
    void testGenerateExcelAndSendEmail5() throws Exception {
        Category category = new Category();
        category.setCategoryDescription("Sl.no.");
        category.setCategoryId(2L);
        category.setCategoryTotal(2L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(false);

        ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.add(category);
        when(categoryRepository.findAll()).thenReturn(categoryList);

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

        Category category2 = new Category();
        category2.setCategoryDescription("No data available for the selected period.So, Email can't be sent!");
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
        employee2.setHrName("No data available for the selected period.So, Email can't be sent!");
        employee2.setImageUrl("https://example.org/example");
        employee2.setIsFinanceAdmin(true);
        employee2.setIsHidden(true);
        employee2.setLastName("Doe");
        employee2.setLndEmail("jane.doe@example.org");
        employee2.setLndName("No data available for the selected period.So, Email can't be sent!");
        employee2.setManagerEmail("jane.doe@example.org");
        employee2.setManagerName("No data available for the selected period.So, Email can't be sent!");
        employee2.setMiddleName("No data available for the selected period.So, Email can't be sent!");
        employee2.setMobileNumber(1L);
        employee2.setOfficialEmployeeId("42");
        employee2.setRole("No data available for the selected period.So, Email can't be sent!");
        employee2.setToken("ABC123");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("No data available for the selected period.So, Email can't be sent!");
        reports.setEmployeeName("No data available for the selected period.So, Email can't be sent!");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("No data available for the selected period.So, Email can't be sent!");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("No data available for the selected period.So, Email can't be sent!");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("No data available for the selected period.So, Email can't be sent!");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Expense expense = new Expense();
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category2);
        expense.setCategoryDescription("No data available for the selected period.So, Email can't be sent!");
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
        expense.setMerchantName("No data available for the selected period.So, Email can't be sent!");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(expenseList);
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("Email sent successfully!", excelGeneratorCategoryServiceImpl.generateExcelAndSendEmail(response, startDate, LocalDate.of(1970, 1, 1)));
        verify(categoryRepository, atLeast(1)).findAll();
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(expenseRepository, atLeast(1)).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    /**
     * Method under test:
     * {@link ExcelGeneratorCategoryServiceImpl#generateExcelAndSendEmail(HttpServletResponse, LocalDate, LocalDate)}
     */
    @Test
    void testGenerateExcelAndSendEmail6() throws Exception {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
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

        Category category = new Category();
        category.setCategoryDescription("No data available for the selected period.So, Email can't be sent!");
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
        employee2.setHrName("No data available for the selected period.So, Email can't be sent!");
        employee2.setImageUrl("https://example.org/example");
        employee2.setIsFinanceAdmin(true);
        employee2.setIsHidden(true);
        employee2.setLastName("Doe");
        employee2.setLndEmail("jane.doe@example.org");
        employee2.setLndName("No data available for the selected period.So, Email can't be sent!");
        employee2.setManagerEmail("jane.doe@example.org");
        employee2.setManagerName("No data available for the selected period.So, Email can't be sent!");
        employee2.setMiddleName("No data available for the selected period.So, Email can't be sent!");
        employee2.setMobileNumber(1L);
        employee2.setOfficialEmployeeId("42");
        employee2.setRole("No data available for the selected period.So, Email can't be sent!");
        employee2.setToken("ABC123");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("No data available for the selected period.So, Email can't be sent!");
        reports.setEmployeeName("No data available for the selected period.So, Email can't be sent!");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("No data available for the selected period.So, Email can't be sent!");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("No data available for the selected period.So, Email can't be sent!");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("No data available for the selected period.So, Email can't be sent!");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Expense expense = new Expense();
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("No data available for the selected period.So, Email can't be sent!");
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
        expense.setMerchantName("No data available for the selected period.So, Email can't be sent!");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(expenseList);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("Email not sent", excelGeneratorCategoryServiceImpl.generateExcelAndSendEmail(response, startDate, LocalDate.of(1970, 1, 1)));
        verify(categoryRepository, atLeast(1)).findAll();
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
        verify(expenseRepository, atLeast(1)).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
        verify(javaMailSender).createMimeMessage();
    }

    @Test
    void testGenerateExcelAndSendEmail7()  {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
        Employee employee = mock(Employee.class);
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

        Category category = new Category();
        category.setCategoryDescription("No data available for the selected period.So, Email can't be sent!");
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
        employee2.setHrName("No data available for the selected period.So, Email can't be sent!");
        employee2.setImageUrl("https://example.org/example");
        employee2.setIsFinanceAdmin(true);
        employee2.setIsHidden(true);
        employee2.setLastName("Doe");
        employee2.setLndEmail("jane.doe@example.org");
        employee2.setLndName("No data available for the selected period.So, Email can't be sent!");
        employee2.setManagerEmail("jane.doe@example.org");
        employee2.setManagerName("No data available for the selected period.So, Email can't be sent!");
        employee2.setMiddleName("No data available for the selected period.So, Email can't be sent!");
        employee2.setMobileNumber(1L);
        employee2.setOfficialEmployeeId("42");
        employee2.setRole("No data available for the selected period.So, Email can't be sent!");
        employee2.setToken("ABC123");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("No data available for the selected period.So, Email can't be sent!");
        reports.setEmployeeName("No data available for the selected period.So, Email can't be sent!");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("No data available for the selected period.So, Email can't be sent!");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("No data available for the selected period.So, Email can't be sent!");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("No data available for the selected period.So, Email can't be sent!");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Category category2 = new Category();
        category2.setCategoryDescription("Category Description");
        category2.setCategoryId(1L);
        category2.setCategoryTotal(1L);
        category2.setExpenseList(new ArrayList<>());
        category2.setIsHidden(true);
        Expense expense = mock(Expense.class);
        when(expense.getAmountApproved()).thenThrow(new IllegalStateException("foo"));
        when(expense.getCategory()).thenReturn(category2);
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
        expense.setCategoryDescription("No data available for the selected period.So, Email can't be sent!");
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
        expense.setMerchantName("No data available for the selected period.So, Email can't be sent!");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(expenseList);
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalStateException.class, () -> excelGeneratorCategoryServiceImpl.generateExcelAndSendEmail(response, startDate, LocalDate.of(1970, 1, 1)));
        verify(categoryRepository).findAll();
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
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
        verify(expense).getCategory();
        verify(expense).getAmountApproved();
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
    void testGenerateExcel() throws Exception {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(new ArrayList<>());
        ByteArrayOutputStream excelStream = new ByteArrayOutputStream(1);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        excelGeneratorCategoryServiceImpl.generateExcel(excelStream, startDate, LocalDate.of(1970, 1, 1));
        verify(categoryRepository).findAll();
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }


    @Test
    void testGenerateExcel2() throws Exception {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenThrow(new IllegalStateException("Category Wise Expense Analytics"));
        ByteArrayOutputStream excelStream = new ByteArrayOutputStream(1);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalStateException.class, () -> excelGeneratorCategoryServiceImpl.generateExcel(excelStream, startDate, LocalDate.of(1970, 1, 1)));
        verify(categoryRepository).findAll();
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    @Test
    void testGenerateExcel3() throws Exception {
        Category category = new Category();
        category.setCategoryDescription("Category Wise Expense Analytics");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);

        ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.add(category);
        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(new ArrayList<>());
        ByteArrayOutputStream excelStream = new ByteArrayOutputStream(1);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        excelGeneratorCategoryServiceImpl.generateExcel(excelStream, startDate, LocalDate.of(1970, 1, 1));
        verify(categoryRepository).findAll();
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }


    @Test
    void testGenerateExcel4() throws Exception {
        Category category = new Category();
        category.setCategoryDescription("Category Wise Expense Analytics");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);

        Category category2 = new Category();
        category2.setCategoryDescription("Sl.no.");
        category2.setCategoryId(2L);
        category2.setCategoryTotal(2L);
        category2.setExpenseList(new ArrayList<>());
        category2.setIsHidden(false);

        ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.add(category2);
        categoryList.add(category);
        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(new ArrayList<>());
        ByteArrayOutputStream excelStream = new ByteArrayOutputStream(1);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        excelGeneratorCategoryServiceImpl.generateExcel(excelStream, startDate, LocalDate.of(1970, 1, 1));
        verify(categoryRepository).findAll();
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }


    @Test
    void testGenerateExcel5() throws Exception {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

        Category category = new Category();
        category.setCategoryDescription("Category Wise Expense Analytics");
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
        employee.setHrName("Category Wise Expense Analytics");
        employee.setImageUrl("https://example.org/example");
        employee.setIsFinanceAdmin(true);
        employee.setIsHidden(true);
        employee.setLastName("Doe");
        employee.setLndEmail("jane.doe@example.org");
        employee.setLndName("Category Wise Expense Analytics");
        employee.setManagerEmail("jane.doe@example.org");
        employee.setManagerName("Category Wise Expense Analytics");
        employee.setMiddleName("Category Wise Expense Analytics");
        employee.setMobileNumber(1L);
        employee.setOfficialEmployeeId("42");
        employee.setRole("Category Wise Expense Analytics");
        employee.setToken("ABC123");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Category Wise Expense Analytics");
        reports.setEmployeeName("Category Wise Expense Analytics");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Category Wise Expense Analytics");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Category Wise Expense Analytics");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Category Wise Expense Analytics");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Expense expense = new Expense();
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("Category Wise Expense Analytics");
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
        expense.setMerchantName("Category Wise Expense Analytics");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(expenseList);
        ByteArrayOutputStream excelStream = new ByteArrayOutputStream(1);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        excelGeneratorCategoryServiceImpl.generateExcel(excelStream, startDate, LocalDate.of(1970, 1, 1));
        verify(categoryRepository).findAll();
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    @Test
    void testGenerateExcel6() throws Exception {
        Category category = new Category();
        category.setCategoryDescription("Category Wise Expense Analytics");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);

        Category category2 = new Category();
        category2.setCategoryDescription("Sl.no.");
        category2.setCategoryId(2L);
        category2.setCategoryTotal(2L);
        category2.setExpenseList(new ArrayList<>());
        category2.setIsHidden(false);

        Category category3 = new Category();
        category3.setCategoryDescription("Category Wise Expense Analytics");
        category3.setCategoryId(1L);
        category3.setCategoryTotal(1L);
        category3.setExpenseList(new ArrayList<>());
        category3.setIsHidden(false);

        ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.add(category3);
        categoryList.add(category2);
        categoryList.add(category);
        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(new ArrayList<>());
        ByteArrayOutputStream excelStream = new ByteArrayOutputStream(1);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        excelGeneratorCategoryServiceImpl.generateExcel(excelStream, startDate, LocalDate.of(1970, 1, 1));
        verify(categoryRepository).findAll();
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }


    @Test
    void testCategoryTotalAmount() {
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(new ArrayList<>());
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertTrue(excelGeneratorCategoryServiceImpl.categoryTotalAmount(startDate, LocalDate.of(1970, 1, 1)).isEmpty());
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }


    @Test
    void testCategoryTotalAmount2() throws UnsupportedEncodingException {
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
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(expenseList);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        HashMap<String, Double> actualCategoryTotalAmountResult = excelGeneratorCategoryServiceImpl.categoryTotalAmount(startDate, LocalDate.of(1970, 1, 1));
        assertEquals(1, actualCategoryTotalAmountResult.size());
        Double expectedGetResult = new Double(10.0d);
        assertEquals(expectedGetResult, actualCategoryTotalAmountResult.get("Category Description"));
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    @Test
    void testCategoryTotalAmount3() {
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenThrow(new IllegalStateException("foo"));
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalStateException.class, () -> excelGeneratorCategoryServiceImpl.categoryTotalAmount(startDate, LocalDate.of(1970, 1, 1)));
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }


    @Test
    void testCategoryTotalAmount4() throws UnsupportedEncodingException {
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

        Category category2 = new Category();
        category2.setCategoryDescription("Category Description");
        category2.setCategoryId(1L);
        category2.setCategoryTotal(1L);
        category2.setExpenseList(new ArrayList<>());
        category2.setIsHidden(true);
        Expense expense = mock(Expense.class);
        when(expense.getAmountApproved()).thenThrow(new IllegalStateException("foo"));
        when(expense.getCategory()).thenReturn(category2);
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
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(expenseList);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(IllegalStateException.class, () -> excelGeneratorCategoryServiceImpl.categoryTotalAmount(startDate, LocalDate.of(1970, 1, 1)));
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
        verify(expense).getCategory();
        verify(expense).getAmountApproved();
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
    void testSendEmailWithAttachment() throws UnsupportedEncodingException, MailException {
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertTrue(excelGeneratorCategoryServiceImpl.sendEmailWithAttachment("jane.doe@example.org", "Hello from the Dreaming Spires", "Not all who wander are lost", "AXAXAXAX".getBytes("UTF-8"), "foo.txt"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }


    @Test
    void testSendEmailWithAttachment2() throws UnsupportedEncodingException, MailException {
        doThrow(new IllegalStateException("application/vnd.ms-excel")).when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertFalse(excelGeneratorCategoryServiceImpl.sendEmailWithAttachment("jane.doe@example.org", "Hello from the Dreaming Spires", "Not all who wander are lost", "AXAXAXAX".getBytes("UTF-8"), "foo.txt"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }







}