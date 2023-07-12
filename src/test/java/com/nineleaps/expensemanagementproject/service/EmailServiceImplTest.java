package com.nineleaps.expensemanagementproject.service;

import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;
import com.nineleaps.expensemanagementproject.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {
    @Mock
    private IReportsService reportsService;
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private IExpenseService expenseService;
    @Mock
    private IEmployeeService employeeService;
    @Mock
    private ReportsRepository reportsRepository;
    @Mock
    private IPdfGeneratorService pdfGeneratorService;
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void managerNotification_ShouldSendEmail() throws IOException, MessagingException {
//        // Mock data
//        Long reportId = 1L;
//        List<Long> expenseIds = new ArrayList<>();
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        Reports report = new Reports();
//        report.setReportTitle("Expense Report 1");
//
//        Expense expense = new Expense();
//        Employee employee = new Employee();
//        employee.setFirstName("John");
//        employee.setLastName("Doe");
//        employee.setManagerEmail("manager@example.com");
//        expense.setEmployee(employee);
//
//        List<Expense> expenseList = new ArrayList<>();
//        expenseList.add(expense);
//
//        when(reportsService.getReportById(reportId)).thenReturn(report);
//        when(expenseService.getExpenseByReportId(reportId)).thenReturn(expenseList);
//        when(expenseList.isEmpty()).thenReturn(false);
//        when(expenseList.get(0)).thenReturn(expense);
//        when(javaMailSender.createMimeMessage()).thenReturn(mock(javax.mail.internet.MimeMessage.class));
//
//        // Call the method
//        emailService.managerNotification(reportId, expenseIds, response);
//
//        // Verify the interactions
//        verify(reportsService).getReportById(reportId);
//        verify(expenseService).getExpenseByReportId(reportId);
//        verify(expenseList).isEmpty();
//        verify(expenseList).get(0);
//        verify(javaMailSender).createMimeMessage();
//        verify(javaMailSender).send(any(javax.mail.internet.MimeMessage.class));
//    }

    // Write similar test cases for other methods

    @Test
    public void financeReimbursedNotification_ShouldSendEmail() {
        // TODO: Implement test case
    }

    @Test
    public void financeRejectedNotification_ShouldSendEmail() {
        // TODO: Implement test case
    }

    @Test
    public void financeNotificationToReimburse_ShouldSendEmail() throws IOException, MessagingException {
        // TODO: Implement test case
    }

    @Test
    public void userPartialApprovedExpensesNotification_ShouldSendEmail() {
        // TODO: Implement test case
    }

    @Test
    public void reminderMailToEmployee_ShouldSendEmails() {
        // TODO: Implement test case
    }

    @Test
    public void reminderMailToManager_ShouldSendEmails() {
        // TODO: Implement test case
    }
}
