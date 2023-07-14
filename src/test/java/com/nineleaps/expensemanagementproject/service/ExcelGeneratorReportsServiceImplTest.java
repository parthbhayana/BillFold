package com.nineleaps.expensemanagementproject.service;

import com.nineleaps.expensemanagementproject.entity.*;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;



import javax.mail.internet.MimeMessage;

import java.io.ByteArrayOutputStream;

import java.time.LocalDate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ExcelGeneratorReportsServiceImplTest {

    @Mock
    private ReportsRepository reportRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private IExpenseService expenseService;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ExcelGeneratorReportsServiceImpl excelGeneratorReportsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGenerateExcelAndSendEmail_NoDataAvailable() throws Exception {
//        // Arrange
//        LocalDate startDate = LocalDate.now().minusDays(7);
//        LocalDate endDate = LocalDate.now();
//        StatusExcel status = StatusExcel.ALL;
//        when(reportRepository.findByDateSubmittedBetween(startDate, endDate)).thenReturn(List.of());
//
//        // Act
//        String result = excelGeneratorReportsService.generateExcelAndSendEmail(null, startDate, endDate, status);
//
//        // Assert
//        assertEquals("No data available for the selected period. So, Email can't be sent!", result);
//        verify(mailSender, never()).send(any(MimeMessage.class));
//    }

//    @Test
//    void testGenerateExcelAndSendEmail_EmailNotSent() throws Exception {
//        // Arrange
//        LocalDate startDate = LocalDate.now().minusDays(7);
//        LocalDate endDate = LocalDate.now();
//        StatusExcel status = StatusExcel.ALL;
//        Reports report = new Reports();
//        when(reportRepository.findByDateSubmittedBetween(startDate, endDate)).thenReturn(List.of(report));
//
//        // Create a mock financeAdmin
//        Employee financeAdmin = new Employee();
//        financeAdmin.setEmployeeEmail("finance@example.com");
//        when(employeeRepository.findByRole("FINANCE_ADMIN")).thenReturn(financeAdmin);
//
//        // Update the mockMailSender to throw an exception when send() is called
//        doThrow(new RuntimeException("Failed to send email")).when(mailSender).send(any(MimeMessage.class));
//
//        // Act
//        String result = excelGeneratorReportsService.generateExcelAndSendEmail(null, startDate, endDate, status);
//
//        // Assert
//        assertEquals("Email not sent", result);
//        verify(mailSender).send(any(MimeMessage.class));
//    }



    @Test
    void testGenerateExcelAndSendEmail_EmailSent() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        StatusExcel status = StatusExcel.ALL;
        Reports report = new Reports();
        when(reportRepository.findByDateSubmittedBetween(startDate, endDate)).thenReturn(List.of(report));
        Employee financeAdmin = new Employee();
        financeAdmin.setEmployeeEmail("finance@example.com");
        when(employeeRepository.findByRole("FINANCE_ADMIN")).thenReturn(financeAdmin);
        when(mailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        String result = excelGeneratorReportsService.generateExcelAndSendEmail(null, startDate, endDate, status);

        // Assert
        assertEquals("Email sent successfully!", result);
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testGenerateExcel_AllStatus() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
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
        excelGeneratorReportsService.generateExcel(excelStream, startDate, endDate, status);
        byte[] excelBytes = excelStream.toByteArray();

        // Assert
        assertNotNull(excelBytes);
        // Add more assertions as needed to validate the generated Excel content
    }

    @Test
    void testGenerateExcel_PendingStatus() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        StatusExcel status = StatusExcel.PENDING;
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
        excelGeneratorReportsService.generateExcel(excelStream, startDate, endDate, status);
        byte[] excelBytes = excelStream.toByteArray();

        // Assert
        assertNotNull(excelBytes);
        // Add more assertions as needed to validate the generated Excel content
    }

    @Test
    void testGenerateExcel_ReimbursedStatus() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        StatusExcel status = StatusExcel.REIMBURSED;
        Reports report = new Reports();
        report.setReportId(1L);
        report.setReportTitle("Report 1");
        report.setDateSubmitted(LocalDate.now());
        report.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
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
        excelGeneratorReportsService.generateExcel(excelStream, startDate, endDate, status);
        byte[] excelBytes = excelStream.toByteArray();

        // Assert
        assertNotNull(excelBytes);
        // Add more assertions as needed to validate the generated Excel content
    }

//    @Test
//    void testSendEmailWithAttachment_Successful() throws Exception {
//        // Arrange
//        String toEmail = "finance@example.com";
//        String subject = "BillFold:Excel Report";
//        String body = "Please find the attached Excel report.";
//        byte[] attachmentContent = "attachment data".getBytes();
//        String attachmentFilename = "report.xls";
//        MimeMessage mimeMessage = mock(MimeMessage.class);
//        doNothing().when(mailSender).send(mimeMessage);
//
//        // Act
//        boolean result = excelGeneratorReportsService.sendEmailWithAttachment(toEmail, subject, body, attachmentContent, attachmentFilename);
//
//        // Assert
//        assertTrue(result);
//        verify(mailSender).send(any(MimeMessage.class));
//    }
//
//    @Test
//    void testSendEmailWithAttachment_Failure() throws Exception {
//        // Arrange
//        String toEmail = "finance@example.com";
//        String subject = "BillFold:Excel Report";
//        String body = "Please find the attached Excel report.";
//        byte[] attachmentContent = "attachment data".getBytes();
//        String attachmentFilename = "report.xls";
//        doThrow(new RuntimeException()).when(mailSender).send(any(MimeMessage.class));
//
//        // Act
//        boolean result = excelGeneratorReportsService.sendEmailWithAttachment(toEmail, subject, body, attachmentContent, attachmentFilename);
//
//        // Assert
//        assertFalse(result);
//        verify(mailSender).send(any(MimeMessage.class));
//    }
}