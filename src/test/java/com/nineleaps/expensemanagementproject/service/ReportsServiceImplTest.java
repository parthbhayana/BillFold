package com.nineleaps.expensemanagementproject.service;

import com.nineleaps.expensemanagementproject.entity.*;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

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

    public void setup() {
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

    // EditReport tests...

    @Test
    void testEditReport_SubmittedReport() {
        // Arrange
        Long reportId = 1L;
        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsSubmitted(true);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act and Assert
        assertThrows(IllegalStateException.class, () ->
                reportsService.editReport(reportId, "Title", "Description", null, null));
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
        assertThrows(NullPointerException.class, () ->
                reportsService.editReport(reportId, "Title", "Description", null, null));
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
        assertThrows(IllegalStateException.class, () ->
                reportsService.editReport(reportId, "Title", "Description", null, null));

        verify(reportsRepository, never()).save(any(Reports.class));
        verify(expenseServices, never()).updateExpense(anyLong(), anyLong());
    }

    // AddExpenseToReport tests...

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
        assertSame(report, updatedReport);
        verify(expenseServices, times(2)).updateExpense(anyLong(), anyLong());
        verify(reportsRepository).save(any(Reports.class));
    }

    @Test
    void testGetReportByEmpId_Drafts() {
        // Arrange
        Long employeeId = 1L;
        String request = "drafts";
        List<Reports> expectedReports = new ArrayList<>();
        when(reportsRepository.getReportsByEmployeeIdAndIsSubmittedAndIsHidden(
                employeeId, false, false)).thenReturn(expectedReports);

        // Act
        List<Reports> actualReports = reportsService.getReportByEmpId(employeeId, request);

        // Assert
        Assertions.assertEquals(expectedReports, actualReports);
        verify(reportsRepository).getReportsByEmployeeIdAndIsSubmittedAndIsHidden(
                employeeId, false, false);
        verifyNoMoreInteractions(reportsRepository);
    }

    @Test
    void testGetReportByEmpId_Submitted() {
        // Arrange
        Long employeeId = 1L;
        String request = "submitted";
        List<Reports> expectedReports = new ArrayList<>();
        when(reportsRepository.getReportsByEmployeeIdAndManagerapprovalstatusAndIsSubmittedAndIsHidden(
                employeeId, ManagerApprovalStatus.PENDING, true, false)).thenReturn(expectedReports);

        // Act
        List<Reports> actualReports = reportsService.getReportByEmpId(employeeId, request);

        // Assert
        Assertions.assertEquals(expectedReports, actualReports);
        verify(reportsRepository).getReportsByEmployeeIdAndManagerapprovalstatusAndIsSubmittedAndIsHidden(
                employeeId, ManagerApprovalStatus.PENDING, true, false);
        verifyNoMoreInteractions(reportsRepository);
    }




    @Test
    void testGetReportByEmpId_InvalidRequest() {
        // Arrange
        Long employeeId = 1L;
        String request = "invalidRequest";

        // Act and Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> reportsService.getReportByEmpId(employeeId, request));

        verifyNoInteractions(reportsRepository);
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
        assertThrows(NullPointerException.class, () ->
                reportsService.addExpenseToReport(reportId, expenseIds));
        verify(expenseServices, never()).updateExpense(anyLong(), anyLong());
        verify(reportsRepository, never()).save(any(Reports.class));
    }

    // SubmitReport tests...

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
        assertThrows(IllegalStateException.class, () ->
                reportsService.submitReport(reportId, response));
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
        assertThrows(NullPointerException.class, () ->
                reportsService.submitReport(reportId, response));
        verify(reportsRepository, never()).save(any(Reports.class));
        verify(emailService, never()).managerNotification(anyLong(), anyList(), any(HttpServletResponse.class));
    }

    // DeleteReport tests...

    @Test
    void testDeleteReport_NonexistentReport() {
        // Arrange
        Long reportId = 1L;
        when(reportsRepository.findById(reportId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () ->
                reportsService.hideReport(reportId));
        verify(reportsRepository, never()).delete(any(Reports.class));
    }

    // GetReportsSubmittedToUser tests...

    @Test
    void testGetReportsSubmittedToUser_ApprovedAndPartiallyApproved() {
        // Arrange
        String managerEmail = "manager@example.com";
        String request = "approved";

        Reports report1 = new Reports();
        Reports report2 = new Reports();
        List<Reports> approvedList = new ArrayList<>();
        approvedList.add(report1);
        List<Reports> partiallyApprovedList = new ArrayList<>();
        partiallyApprovedList.add(report2);
        List<Reports> mergedList = new ArrayList<>();
        mergedList.addAll(approvedList);
        mergedList.addAll(partiallyApprovedList);

        when(reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(
                managerEmail, ManagerApprovalStatus.APPROVED, true, false)).thenReturn(approvedList);
        when(reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(
                managerEmail, ManagerApprovalStatus.PARTIALLY_APPROVED, true, false)).thenReturn(partiallyApprovedList);

        // Act
        List<Reports> result = reportsService.getReportsSubmittedToUser(managerEmail, request);

        // Assert
        Assertions.assertEquals(mergedList, result);

        verify(reportsRepository, times(1)).findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(
                managerEmail, ManagerApprovalStatus.APPROVED, true, false);
        verify(reportsRepository, times(1)).findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(
                managerEmail, ManagerApprovalStatus.PARTIALLY_APPROVED, true, false);
        verifyNoMoreInteractions(reportsRepository);
    }

    @Test
    void testGetReportsSubmittedToUser_Pending() {
        // Arrange
        String managerEmail = "manager@example.com";
        String request = "pending";

        Reports report1 = new Reports();
        List<Reports> pendingList = new ArrayList<>();
        pendingList.add(report1);

        when(reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(
                managerEmail, ManagerApprovalStatus.PENDING, true, false)).thenReturn(pendingList);

        // Act
        List<Reports> result = reportsService.getReportsSubmittedToUser(managerEmail, request);

        // Assert
        Assertions.assertEquals(pendingList, result);

        verify(reportsRepository, times(1)).findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(
                managerEmail, ManagerApprovalStatus.PENDING, true, false);
        verifyNoMoreInteractions(reportsRepository);
    }

    @Test
    void testGetReportsSubmittedToUser_InvalidRequest() {
        // Arrange
        String managerEmail = "manager@example.com";
        String request = "invalidRequest";

        // Act and Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> reportsService.getReportsSubmittedToUser(managerEmail, request));

        verifyNoInteractions(reportsRepository);
    }

    // Other test methods...




    @Test
    void testGetAllSubmittedReports() {
        // Arrange
        List<Reports> allReports = new ArrayList<>();
        Reports report1 = new Reports();
        report1.setIsSubmitted(true);
        Reports report2 = new Reports();
        report2.setIsSubmitted(false);
        Reports report3 = new Reports();
        report3.setIsSubmitted(true);
        allReports.add(report1);
        allReports.add(report2);
        allReports.add(report3);

        when(reportsRepository.findAll()).thenReturn(allReports);

        // Act
        List<Reports> submittedReports = reportsService.getAllSubmittedReports();

        // Assert
        Assertions.assertEquals(2, submittedReports.size());
        Assertions.assertTrue(submittedReports.contains(report1));
        Assertions.assertTrue(submittedReports.contains(report3));

        verify(reportsRepository, times(1)).findAll();
        verifyNoMoreInteractions(reportsRepository);
    }

    @Test
    void testGetAllSubmittedReports_NoSubmittedReports() {
        // Arrange
        List<Reports> allReports = new ArrayList<>();
        Reports report1 = new Reports();
        report1.setIsSubmitted(false);
        Reports report2 = new Reports();
        report2.setIsSubmitted(false);
        allReports.add(report1);
        allReports.add(report2);

        when(reportsRepository.findAll()).thenReturn(allReports);

        // Act
        List<Reports> submittedReports = reportsService.getAllSubmittedReports();

        // Assert
        Assertions.assertEquals(0, submittedReports.size());

        verify(reportsRepository, times(1)).findAll();
        verifyNoMoreInteractions(reportsRepository);
    }

    @Test
    void testGetAllReportsApprovedByManager_ApprovedAndReimbursed() {
        // Arrange
        String request = "approved";

        Reports report1 = new Reports();
        Reports report2 = new Reports();
        List<Reports> approvedReimbursedList = new ArrayList<>();
        approvedReimbursedList.add(report1);
        approvedReimbursedList.add(report2);

        when(reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.REIMBURSED, true, false))
                .thenReturn(approvedReimbursedList);

        // Act
        List<Reports> result = reportsService.getAllReportsApprovedByManager(request);

        // Assert
        Assertions.assertEquals(approvedReimbursedList, result);

        verify(reportsRepository, times(1)).findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.REIMBURSED, true, false);
        verifyNoMoreInteractions(reportsRepository);
    }

    @Test
    void testGetAllReportsApprovedByManager_NoReports() {
        // Arrange
        String request = "pending";

        List<Reports> approvedList = new ArrayList<>();
        List<Reports> partiallyApprovedList = new ArrayList<>();
        List<Reports> mergedList = new ArrayList<>();
        mergedList.addAll(approvedList);
        mergedList.addAll(partiallyApprovedList);

        when(reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.PENDING, true, false))
                .thenReturn(approvedList);
        when(reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.PENDING, true, false))
                .thenReturn(partiallyApprovedList);

        // Act
        List<Reports> result = reportsService.getAllReportsApprovedByManager(request);

        // Assert
        Assertions.assertEquals(0, result.size());

        verify(reportsRepository, times(1)).findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.PENDING, true, false);
        verify(reportsRepository, times(1)).findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.PENDING, true, false);
        verifyNoMoreInteractions(reportsRepository);
    }



    @Test
    void testSubmitReport_ReportAlreadySubmitted() throws MessagingException, IOException {
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
        verify(emailService, never()).managerNotification(anyLong(), anyList(), (HttpServletResponse) any());
    }

    @Test
    void totalAmountINR_ReturnsCorrectTotalAmount() {
        // Prepare test data
        long reportId = 1L;
        Reports report = new Reports();
        report.setReportId(reportId);
        Expense expense1 = new Expense();
        expense1.setAmountINR(100.0f);
        Expense expense2 = new Expense();
        expense2.setAmountINR(200.0f);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense1);
        expenses.add(expense2);

        // Mock dependencies
        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(expenseRepository.findByReports(report)).thenReturn(expenses);

        // Call the method
        float totalAmount = reportsService.totalAmountINR(reportId);

        // Verify the expected behavior
        verify(reportsRepository).findById(reportId);
        verify(expenseRepository).findByReports(report);

        // Assert the result
        float expectedTotalAmount = 300.0f;
        assertEquals(expectedTotalAmount, totalAmount, 0.01f);
    }

    @Test
    void getReportsInDateRange_ReturnsReportsInRange() {
        // Prepare test data
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        List<Reports> expectedReports = new ArrayList<>(); // Add the expected reports in the date range

        // Mock the repository method
        when(reportsRepository.findByDateSubmittedBetween(startDate, endDate)).thenReturn(expectedReports);

        // Call the method
        List<Reports> actualReports = reportsService.getReportsInDateRange(startDate, endDate);

        // Verify the expected behavior
        verify(reportsRepository).findByDateSubmittedBetween(startDate, endDate);

        // Assert the result
        assertEquals(expectedReports, actualReports);
    }








}
