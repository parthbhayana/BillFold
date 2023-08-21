package com.nineleaps.expensemanagementproject.service;


import com.nineleaps.expensemanagementproject.entity.*;
import com.nineleaps.expensemanagementproject.repository.CategoryRepository;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.mail.javamail.MimeMessageHelper;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcelGeneratorCategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExcelGeneratorCategoryServiceImpl excelService;

    @Mock
    private HttpServletResponse response;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private EmployeeRepository employeeRepository;



    @Mock
    private ReportsRepository reportRepository;

    @Mock
    private ExpenseServiceImpl expenseService;





    @InjectMocks
    private ExcelGeneratorCategoryServiceImpl excelGeneratorCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }








    @Test
    void testGenerateExcelAndSendEmail_NoDataAvailable() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
        when(expenseRepository.findByDateBetween(startDate, endDate)).thenReturn(new ArrayList<>());

        // Act
        String result = excelGeneratorCategoryService.generateExcelAndSendEmail(response, startDate, endDate);

        // Assert
        assertEquals("No data available for the selected period.So, Email can't be sent!", result);
        verify(categoryRepository).findAll();
        verify(expenseRepository).findByDateBetween(startDate, endDate);
        verifyNoInteractions(mailSender);
    }


    @Test
    void testSendEmailWithAttachment_Success() {
        // Arrange
        String toEmail = "finance@example.com";
        String subject = "BillFold:Excel Report";
        String body = "Please find the attached Excel report.";
        byte[] attachmentContent = new byte[]{1, 2, 3};
        String attachmentFilename = "report.xls";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        boolean result = excelGeneratorCategoryService.sendEmailWithAttachment(
                toEmail, subject, body, attachmentContent, attachmentFilename);

        // Assert
        assertTrue(result);
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
    }





    @Test
    void testGenerateExcel_AllStatus() throws Exception {
        // Test data
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        StatusExcel status = StatusExcel.ALL;
        Reports report = new Reports();
        report.setReportId(1L);
        report.setReportTitle("Report 1");
        report.setDateSubmitted(LocalDate.now());
        report.setFinanceApprovalStatus(FinanceApprovalStatus.PENDING);
        when(reportRepository.findByDateSubmittedBetween(startDate, endDate)).thenReturn(List.of(report));
        Expense expense = new Expense();
        Employee employee = new Employee();
        employee.setOfficialEmployeeId("123456");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        expense.setEmployee(employee);
        when(expenseService.getExpenseByReportId(1L)).thenReturn(List.of(expense));

        // Act
        ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
        excelGeneratorCategoryService.generateExcel(excelStream, startDate, endDate);
        byte[] excelBytes = excelStream.toByteArray();

        // Assert
        assertNotNull(excelBytes);
        // Add more assertions as needed to validate the generated Excel content
    }



    @Test
    void testCategoryTotalAmount_NoExpenses() {
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 7, 31);

        // Mocking expense repository to return an empty list
        when(expenseRepository.findByDateBetween(startDate, endDate)).thenReturn(new ArrayList<>());

        // Test the method
        HashMap<String, Float> result = excelService.CategoryTotalAmount(startDate, endDate);

        // Assert the result
        assertTrue(result.isEmpty());
    }

    @Test
    void testCategoryTotalAmount() {
        // Sample test data
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        List<Expense> mockExpenses = getMockExpenseList();

        // Mock behavior of the repository
        when(expenseRepository.findByDateBetween(startDate, endDate)).thenReturn(mockExpenses);

        // Call the method to be tested
        HashMap<String, Float> result = excelService.CategoryTotalAmount(startDate, endDate);

        // Assert the result
        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(null, result.get("Category1"));
        Assertions.assertEquals(null, result.get("Category2"));
    }

    private List<Expense> getMockExpenseList() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1L, "Expense1", LocalDate.of(2023, 1, 5), 50.0f, new Category(1L, "Category1")));
        expenses.add(new Expense(2L, "Expense2", LocalDate.of(2023, 1, 15), 50.0f, new Category(2L, "Category2")));
        expenses.add(new Expense(3L, "Expense3", LocalDate.of(2023, 1, 20), 25.0f, new Category(1L, "Category1")));
        return expenses;
    }













}
