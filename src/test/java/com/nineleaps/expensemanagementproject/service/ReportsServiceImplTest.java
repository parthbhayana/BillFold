package com.nineleaps.expensemanagementproject.service;

import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.ManagerApprovalStatus;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ReportsServiceImplTest {

    @Mock
    private ReportsRepository reportsRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private IExpenseService expenseServices;

    @Mock
    private IEmployeeService employeeServices;

    @Mock
    private IEmailService emailService;


    @InjectMocks
    private ReportsServiceImpl reportsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllReports() {
        // Arrange
        List<Reports> expectedReports = new ArrayList<>();
        when(reportsRepository.findAll()).thenReturn(expectedReports);

        // Act
        List<Reports> actualReports = reportsService.getAllReports();

        // Assert
        assertEquals(expectedReports, actualReports);
    }

    @Test
    void testGetReportById() {
        // Arrange
        Long reportId = 1L;
        Reports expectedReport = new Reports();
        Optional<Reports> optionalReport = Optional.of(expectedReport);
        when(reportsRepository.findById(reportId)).thenReturn(optionalReport);

        // Act
        Reports actualReport = reportsService.getReportById(reportId);

        // Assert
        assertEquals(expectedReport, actualReport);
    }

    @Test
    void testGetReportById_NonexistentReport() {
        // Arrange
        Long reportId = 1L;
        Optional<Reports> optionalReport = Optional.empty();
        when(reportsRepository.findById(reportId)).thenReturn(optionalReport);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> reportsService.getReportById(reportId));
    }

    @Test
    void testAddReport() {
        // Arrange
        Long employeeId = 1L;
        List<Long> expenseIds = Arrays.asList(1L, 2L);

        Reports newReport = new Reports();
        newReport.setReportId(1L);

        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);

        Expense expense1 = new Expense();
        expense1.setExpenseId(1L);
        expense1.setAmountINR(100);

        Expense expense2 = new Expense();
        expense2.setExpenseId(2L);
        expense2.setAmountINR(200);

        when(employeeServices.getEmployeeById(employeeId)).thenReturn(employee);
        when(expenseRepository.getExpenseByexpenseId(1L)).thenReturn(expense1);
        when(expenseRepository.getExpenseByexpenseId(2L)).thenReturn(expense2);
        when(reportsRepository.findById(newReport.getReportId())).thenReturn(Optional.empty()); // Simulate Report not found

        // Act and Assert
//        assertThrows(ReportNotFoundException.class, () -> reportsService.addReport(newReport, employeeId, expenseIds));

        // Verify that the necessary methods were called
        verify(reportsRepository, times(0)).save(any(Reports.class));
        verify(expenseServices, times(0)).updateExpense(anyLong(), anyLong());
    }

//    @Test
//    void testEditReport() {
//        // Mock data
//        Long reportId = 1L;
//        String reportTitle = "New Report Title";
//        String reportDescription = "New Report Description";
//        List<Long> addExpenseIds = List.of(4L, 5L);
//        List<Long> removeExpenseIds = List.of(2L, 3L);
//
//        Reports report = new Reports();
//        report.setReportId(reportId);
//        report.setIsSubmitted(false);
//        report.setIsHidden(false);
//        report.setManagerApprovalStatus(ManagerApprovalStatus.ACTION_REQUIRED);
//
//        Expense expense1 = new Expense();
//        expense1.setExpenseId(2L);
//        expense1.setIsReported(true);
//
//        Expense expense2 = new Expense();
//        expense2.setExpenseId(3L);
//        expense2.setIsReported(true);
//
//        when(reportsRepository.getReportByReportId(reportId)).thenReturn(report);
//        when(expenseServices.getExpenseByReportId(reportId)).thenReturn(List.of(expense1, expense2));
//        when(expenseServices.getExpenseById(anyLong())).thenReturn(new Expense());
//
//        // Perform the test
//        List<Reports> result = reportsService.editReport(reportId, reportTitle, reportDescription,
//                addExpenseIds, removeExpenseIds);
//
//        // Verify the results
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        Reports editedReport = result.get(0);
//        assertEquals(reportId, editedReport.getReportId());
//        assertEquals(reportTitle, editedReport.getReportTitle());
//        assertEquals(reportDescription, editedReport.getReportDescription());
//        verify(reportsRepository, times(2)).save(any(Reports.class));
//        verify(expenseRepository, times(2)).save(any(Expense.class));
//        verify(expenseServices, times(2)).updateExpense(anyLong(), anyLong());
//    }



    @Test
    void testEditReport_SubmittedReport() {
        // Arrange
        Long reportId = 1L;
        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsSubmitted(true);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> reportsService.editReport(reportId, "Title", "Description", null, null));
        verify(reportsRepository, never()).save(any(Reports.class));
        verify(expenseServices, never()).updateExpense(anyLong(), anyLong());
    }

    @Test
    void testEditReport_HiddenReport() {
        // Arrange
        Long reportId = 1L;
        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsHidden(true);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act and Assert
        assertThrows(NullPointerException.class, () -> reportsService.editReport(reportId, "Title", "Description", null, null));
        verify(reportsRepository, never()).save(any(Reports.class));
        verify(expenseServices, never()).updateExpense(anyLong(), anyLong());
    }

    @Test
    void testEditReport_InvalidRequest() {
        // Arrange
        Long reportId = 1L;
        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsHidden(false);
        report.setIsSubmitted(true);
        report.setManagerApprovalStatus(ManagerApprovalStatus.ACTION_REQUIRED);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> reportsService.editReport(reportId, "Title", "Description", null, null));

        verify(reportsRepository, never()).save(any(Reports.class));
        verify(expenseServices, never()).updateExpense(anyLong(), anyLong());
    }

    @Test
    void testAddExpenseToReport() {
        // Arrange
        Long reportId = 1L;
        List<Long> expenseIds = Arrays.asList(1L, 2L);

        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsHidden(false);

        Expense expense1 = new Expense();
        expense1.setExpenseId(1L);

        Expense expense2 = new Expense();
        expense2.setExpenseId(2L);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(expenseServices.getExpenseById(1L)).thenReturn(expense1);
        when(expenseServices.getExpenseById(2L)).thenReturn(expense2);
        when(expenseRepository.save(any(Expense.class))).thenReturn(null);
        when(reportsRepository.save(any(Reports.class))).thenReturn(report);

        // Act
        Reports updatedReport = reportsService.addExpenseToReport(reportId, expenseIds);

        // Assert
        assertNotNull(updatedReport);
        assertEquals(report, updatedReport);
        verify(expenseServices, times(2)).updateExpense(anyLong(), anyLong());
        verify(reportsRepository).save(any(Reports.class));
    }

    @Test
    void testAddExpenseToReport_HiddenReport() {
        // Arrange
        Long reportId = 1L;
        List<Long> expenseIds = Arrays.asList(1L, 2L);

        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsHidden(true);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act and Assert
        assertThrows(NullPointerException.class, () -> reportsService.addExpenseToReport(reportId, expenseIds));
        verify(expenseServices, never()).updateExpense(anyLong(), anyLong());
        verify(reportsRepository, never()).save(any(Reports.class));
    }

//    @Test
//    void testSubmitReport() throws MessagingException, IOException {
//        // Arrange
//        Long reportId = 1L;
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        Reports report = new Reports();
//        report.setReportId(reportId);
//        report.setIsHidden(false);
//        report.setIsSubmitted(false);
//        report.setManagerApprovalStatus(ManagerApprovalStatus.REJECTED);
//
//        Employee employee = new Employee();
//        employee.setManagerEmail("manager@example.com");
//
//        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
//        when(employeeServices.getEmployeeById(anyLong())).thenReturn(employee);
//        when(reportsRepository.save(any(Reports.class))).thenReturn(report); // Use thenReturn() for non-void method
//        doNothing().when(emailService).managerNotification(anyLong(), anyList(), any(HttpServletResponse.class));
//
//        // Act
//        reportsService.submitReport(reportId, response);
//
//        // Assert
//        assertTrue(report.getIsSubmitted());
//        assertEquals(ManagerApprovalStatus.PENDING, report.getManagerapprovalstatus()); // Corrected method name
//        assertNotNull(report.getDateSubmitted());
//        verify(reportsRepository).save(any(Reports.class));
//        verify(emailService).managerNotification(anyLong(), anyList(), any(HttpServletResponse.class));
//    }

    @Test
    void testSubmitReport_AlreadySubmitted() throws MessagingException, IOException {
        // Arrange
        Long reportId = 1L;
        HttpServletResponse response = mock(HttpServletResponse.class);

        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsHidden(false);
        report.setIsSubmitted(true);
        report.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> reportsService.submitReport(reportId, response));
        verify(reportsRepository, never()).save(any(Reports.class));
        verify(emailService, never()).managerNotification(anyLong(), anyList(), any(HttpServletResponse.class));
    }

    @Test
    void testSubmitReport_HiddenReport() throws MessagingException, IOException {
        // Arrange
        Long reportId = 1L;
        HttpServletResponse response = mock(HttpServletResponse.class);

        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsHidden(true);
        report.setIsSubmitted(false);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act and Assert
        assertThrows(NullPointerException.class, () -> reportsService.submitReport(reportId, response));
        verify(reportsRepository, never()).save(any(Reports.class));
        verify(emailService, never()).managerNotification(anyLong(), anyList(), any(HttpServletResponse.class));
    }

//    @Test
//    void testApproveReport() throws MessagingException, IOException {
//        // Arrange
//        Long reportId = 1L;
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        Reports report = Mockito.spy(new Reports());
//        report.setReportId(reportId);
//        report.setIsHidden(false);
//        report.setIsSubmitted(true);
//        report.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);
//
//        Employee employee = new Employee();
//        employee.setEmployeeEmail("employee@example.com");
//
//        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
//        when(employeeServices.getEmployeeByEmail(anyString())).thenReturn(employee);
//        doNothing().when(reportsRepository).save(report);
//        doNothing().when(emailService).managerNotification(anyLong(), ArgumentMatchers.<Long>anyList(), any(HttpServletResponse.class));
//
//        // Act
//        String comments = "hello";
//        reportsService.approveReportByManager(reportId, comments, response);
//
//        // Assert
//        assertEquals(ManagerApprovalStatus.APPROVED, report.getManagerapprovalstatus());
//        verify(reportsRepository).save(report);
//        verify(emailService).managerNotification(anyLong(), ArgumentMatchers.<Long>anyList(), any(HttpServletResponse.class));
//    }



//    @Test
//    void testApproveReport_AlreadyApproved() throws MessagingException, IOException {
//        // Arrange
//        Long reportId = 1L;
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        Reports report = new Reports();
//        report.setReportId(reportId);
//        report.setIsHidden(false);
//        report.setIsSubmitted(true);
//        report.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
//
//        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
//
//        // Act
//        String comments = "hello";
//        reportsService.approveReportByManager(reportId, comments, response);
//
//        // Assert
//        assertEquals(ManagerApprovalStatus.APPROVED, report.getManagerapprovalstatus());
//        verify(reportsRepository, never()).save(any(Reports.class));
//        verify(emailService).managerNotification(anyLong(), ArgumentMatchers.<Long>anyList(), any(HttpServletResponse.class));
//    }


//
//    @Test
//    void testApproveReport_HiddenReport() throws MessagingException, IOException {
//        // Arrange
//        Long reportId = 1L;
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        Reports report = new Reports();
//        report.setReportId(reportId);
//        report.setIsHidden(true);
//        report.setIsSubmitted(true);
//        report.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);
//
//        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
//
//        // Act and Assert
//        String comments = "hello";
//        assertThrows(NullPointerException.class, () -> reportsService.approveReportByManager(reportId,comments, response));
//        verify(reportsRepository, never()).save(any(Reports.class));
//        doNothing().when(emailService).managerNotification(anyLong(), ArgumentMatchers.<Long>anyList(), (HttpServletResponse) any());
//    }
//
//    @Test
//    void testRejectReport() throws MessagingException, IOException {
//        // Arrange
//        Long reportId = 1L;
//        String rejectionReason = "Rejected";
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        Reports report = new Reports();
//        report.setReportId(reportId);
//        report.setIsHidden(false);
//        report.setIsSubmitted(true);
//        report.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);
//
//        Employee employee = new Employee();
//        employee.setEmployeeEmail("employee@example.com");
//
//        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
//        when(employeeServices.getEmployeeByEmail(anyString())).thenReturn(employee);
//        doNothing().when(reportsRepository).save(any(Reports.class));
//        doNothing().when(emailService).managerNotification(anyLong(), ArgumentMatchers.<Long>anyList(), (HttpServletResponse) any());
//
//        // Act
//        reportsService.rejectReportByManager(reportId, rejectionReason, response);
//
//        // Assert
//        assertEquals(ManagerApprovalStatus.REJECTED, report.getManagerapprovalstatus());
//        assertEquals(rejectionReason, report.getManagerComments());
//        verify(reportsRepository).save(any(Reports.class));
//        doNothing().when(emailService).managerNotification(anyLong(), ArgumentMatchers.<Long>anyList(), (HttpServletResponse) any());
//    }
//
//    @Test
//    void testRejectReport_AlreadyRejected() throws MessagingException, IOException {
//        // Arrange
//        Long reportId = 1L;
//        String rejectionReason = "Rejected";
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        Reports report = new Reports();
//        report.setReportId(reportId);
//        report.setIsHidden(false);
//        report.setIsSubmitted(true);
//        report.setManagerApprovalStatus(ManagerApprovalStatus.REJECTED);
//
//        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
//
//        // Act and Assert
//        assertThrows(IllegalStateException.class, () -> reportsService.rejectReportByManager(reportId, rejectionReason, response));
//        verify(reportsRepository, never()).save(any(Reports.class));
//        doNothing().when(emailService).managerNotification(anyLong(), ArgumentMatchers.<Long>anyList(), (HttpServletResponse) any());
//    }
//
//    @Test
//    void testRejectReport_HiddenReport() throws MessagingException, IOException {
//        // Arrange
//        Long reportId = 1L;
//        String rejectionReason = "Rejected";
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        Reports report = new Reports();
//        report.setReportId(reportId);
//        report.setIsHidden(true);
//        report.setIsSubmitted(true);
//        report.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);
//
//        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
//
//        // Act and Assert
//        assertThrows(NullPointerException.class, () -> reportsService.rejectReportByManager(reportId, rejectionReason, response));
//        verify(reportsRepository, never()).save(any(Reports.class));
//        doNothing().when(emailService).managerNotification(anyLong(), ArgumentMatchers.<Long>anyList(), (HttpServletResponse) any());
//    }
//
//    @Test
//    void testDeleteReport() {
//        // Arrange
//        Long reportId = 1L;
//
//        Reports report = new Reports();
//        report.setReportId(reportId);
//
//        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
//        doNothing().when(reportsRepository).delete(any(Reports.class));
//
//        // Act
//        reportsService.hideReport(reportId);
//
//        // Assert
//        verify(reportsRepository).delete(any(Reports.class));
//    }

    @Test
    void testDeleteReport_NonexistentReport() {
        // Arrange
        Long reportId = 1L;
        when(reportsRepository.findById(reportId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> reportsService.hideReport(reportId));
        verify(reportsRepository, never()).delete(any(Reports.class));
    }
}
