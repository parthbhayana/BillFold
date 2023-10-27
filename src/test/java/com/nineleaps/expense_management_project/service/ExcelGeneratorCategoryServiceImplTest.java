package com.nineleaps.expense_management_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
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
import java.io.IOException;

import java.io.UnsupportedEncodingException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.JFreeChart;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    private IExcelGeneratorReportsService iExcelGeneratorReportsService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void testGenerateExcelAndSendEmail() throws Exception {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(new ArrayList<>());
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("No data available for the selected period.So, Email can't be sent!",
                excelGeneratorCategoryServiceImpl.generateExcelAndSendEmail(response, startDate, LocalDate.of(1970, 1
                        , 1)));
        verify(categoryRepository).findAll();
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    @Test
    void testGenerateExcelAndSendEmail2() throws Exception {
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
        when(iExcelGeneratorReportsService.sendEmailWithAttachment(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<String>any(), Mockito.<byte[]>any(), Mockito.<String>any())).thenReturn(true);
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("Email sent successfully!", excelGeneratorCategoryServiceImpl.generateExcelAndSendEmail(response
                , startDate, LocalDate.of(1970, 1, 1)));
        verify(categoryRepository, atLeast(1)).findAll();
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(expenseRepository, atLeast(1)).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
        verify(iExcelGeneratorReportsService).sendEmailWithAttachment(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<String>any(), Mockito.<byte[]>any(), Mockito.<String>any());
    }


    @Test
    void testGenerateExcelAndSendEmail3() throws Exception {
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
        when(iExcelGeneratorReportsService.sendEmailWithAttachment(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<String>any(), Mockito.<byte[]>any(), Mockito.<String>any())).thenReturn(true);
        Response response = new Response();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertEquals("Email sent successfully!", excelGeneratorCategoryServiceImpl.generateExcelAndSendEmail(response
                , startDate, LocalDate.of(1970, 1, 1)));
        verify(categoryRepository, atLeast(1)).findAll();
        verify(employeeRepository).findByRole(Mockito.<String>any());
        verify(expenseRepository, atLeast(1)).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
        verify(iExcelGeneratorReportsService).sendEmailWithAttachment(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<String>any(), Mockito.<byte[]>any(), Mockito.<String>any());
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
    void testGenerateExcel3() throws Exception {
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
    void testGenerateExcel4() throws Exception {
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
    void testGenerateExcel5() throws Exception {
        Category category = mock(Category.class);
        when(category.getCategoryDescription()).thenReturn("Category Description");
        doNothing().when(category).setCategoryDescription(Mockito.<String>any());
        doNothing().when(category).setCategoryId(Mockito.<Long>any());
        doNothing().when(category).setCategoryTotal(anyLong());
        doNothing().when(category).setExpenseList(Mockito.<List<Expense>>any());
        doNothing().when(category).setIsHidden(Mockito.<Boolean>any());
        category.setCategoryDescription("Category Wise Expense Analytics");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);
        Category category2 = mock(Category.class);
        when(category2.getCategoryDescription()).thenReturn("Category Description");
        doNothing().when(category2).setCategoryDescription(Mockito.<String>any());
        doNothing().when(category2).setCategoryId(Mockito.<Long>any());
        doNothing().when(category2).setCategoryTotal(anyLong());
        doNothing().when(category2).setExpenseList(Mockito.<List<Expense>>any());
        doNothing().when(category2).setIsHidden(Mockito.<Boolean>any());
        category2.setCategoryDescription("Sl.no.");
        category2.setCategoryId(2L);
        category2.setCategoryTotal(2L);
        category2.setExpenseList(new ArrayList<>());
        category2.setIsHidden(true);

        ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.add(category2);
        categoryList.add(category);
        when(categoryRepository.findAll()).thenReturn(categoryList);

        Category category3 = new Category();
        category3.setCategoryDescription("Category Description");
        category3.setCategoryId(1L);
        category3.setCategoryTotal(1L);
        category3.setExpenseList(new ArrayList<>());
        category3.setIsHidden(false);

        Employee employee = new Employee();
        employee.setEmployeeEmail("jane.doe@example.org");
        employee.setEmployeeId(1L);
        employee.setExpenseList(new ArrayList<>());
        employee.setFirstName("Jane");
        employee.setHrEmail("jane.doe@example.org");
        employee.setHrName("Hr Name");
        employee.setImageUrl("https://example.org/example");
        employee.setIsFinanceAdmin(false);
        employee.setIsHidden(false);
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
        reports.setIsHidden(false);
        reports.setIsSubmitted(false);
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
        expense.setCategory(category3);
        expense.setCategoryDescription("Category Description");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("The characteristics of someone or something");
        expense.setEmployee(employee);
        expense.setExpenseId(1L);
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(false);
        expense.setIsReported(false);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(false);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(expenseRepository.findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any())).thenReturn(expenseList);
        ByteArrayOutputStream excelStream = new ByteArrayOutputStream(1);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        excelGeneratorCategoryServiceImpl.generateExcel(excelStream, startDate, LocalDate.of(1970, 1, 1));
        verify(categoryRepository).findAll();
        verify(category2, atLeast(1)).getCategoryDescription();
        verify(category2).setCategoryDescription(Mockito.<String>any());
        verify(category2).setCategoryId(Mockito.<Long>any());
        verify(category2).setCategoryTotal(anyLong());
        verify(category2).setExpenseList(Mockito.<List<Expense>>any());
        verify(category2).setIsHidden(Mockito.<Boolean>any());
        verify(category, atLeast(1)).getCategoryDescription();
        verify(category).setCategoryDescription(Mockito.<String>any());
        verify(category).setCategoryId(Mockito.<Long>any());
        verify(category).setCategoryTotal(anyLong());
        verify(category).setExpenseList(Mockito.<List<Expense>>any());
        verify(category).setIsHidden(Mockito.<Boolean>any());
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }

    @Test
    @Disabled
    void testLoadChartImage() throws IOException {

        JFreeChart chart = null;
        int width = 0;
        int height = 0;
        HSSFWorkbook workbook = null;

        int actualLoadChartImageResult =
                this.excelGeneratorCategoryServiceImpl.loadChartImage(chart, width, height, workbook);
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
        HashMap<String, Double> actualCategoryTotalAmountResult =
                excelGeneratorCategoryServiceImpl.categoryTotalAmount(startDate, LocalDate.of(1970, 1, 1));
        assertEquals(1, actualCategoryTotalAmountResult.size());
        Double expectedGetResult = new Double(10.0d);
        assertEquals(expectedGetResult, actualCategoryTotalAmountResult.get("Category Description"));
        verify(expenseRepository).findByDateBetween(Mockito.<LocalDate>any(), Mockito.<LocalDate>any());
    }
}

