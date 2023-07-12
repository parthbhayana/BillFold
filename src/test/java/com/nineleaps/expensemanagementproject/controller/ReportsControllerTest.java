package com.nineleaps.expensemanagementproject.controller;


import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.service.IExpenseService;
import com.nineleaps.expensemanagementproject.service.IReportsService;
import com.nineleaps.expensemanagementproject.service.PdfGeneratorServiceImpl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import javax.mail.MessagingException;
import javax.mail.internet.ParseException;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class ReportsControllerTest {

    @Mock
    private IReportsService reportsService;

    @Mock
    private PdfGeneratorServiceImpl pdfGeneratorService;

    @Mock
    private IExpenseService expenseService;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ReportsController reportsController;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllReports_Success() {
        // Arrange
        List<Reports> reports = new ArrayList<>();
        reports.add(new Reports());
        reports.add(new Reports());

        when(reportsService.getAllReports()).thenReturn(reports);

        // Act
        List<Reports> result = reportsController.getAllReports();

        // Assert
        assertEquals(reports, result);
        verify(reportsService).getAllReports();
    }

    @Test
    void getReportByReportId_ExistingReportId_Success() {
        // Arrange
        Long reportId = 1L;
        Reports report = new Reports();

        when(reportsService.getReportById(reportId)).thenReturn(report);

        // Act
        Reports result = reportsController.getReportByReportId(reportId);

        // Assert
        assertEquals(report, result);
        verify(reportsService).getReportById(reportId);
    }

    @Test
    void getReportByReportId_NonExistingReportId_NotFound() {
        // Arrange
        Long reportId = 1L;

        when(reportsService.getReportById(reportId)).thenReturn(null);

        // Act
        Reports response = reportsController.getReportByReportId(reportId);

        // Assert
        assertNull(response);
        // Additional assertion if needed
        verify(reportsService).getReportById(reportId);
    }

    @Test
    void getReportByEmpId_Success() {
        // Arrange
        Long employeeId = 1L;
        String request = "request";
        List<Reports> reports = new ArrayList<>();
        reports.add(new Reports());
        reports.add(new Reports());

        when(reportsService.getReportByEmpId(employeeId, request)).thenReturn(reports);

        // Act
        List<Reports> result = reportsController.getReportByEmpId(employeeId, request);

        // Assert
        assertEquals(reports, result);
        verify(reportsService).getReportByEmpId(employeeId, request);
    }

    @Test
    void getReportsSubmittedToUser_Success() {
        // Arrange
        String managerEmail = "manager@example.com";
        String request = "request";
        List<Reports> reports = new ArrayList<>();
        reports.add(new Reports());
        reports.add(new Reports());

        when(reportsService.getReportsSubmittedToUser(managerEmail, request)).thenReturn(reports);

        // Act
        List<Reports> result = reportsController.getReportsSubmittedToUser(managerEmail, request);

        // Assert
        assertEquals(reports, result);
        verify(reportsService).getReportsSubmittedToUser(managerEmail, request);
    }

    @Test
    void getAllSubmittedReports_Success() {
        // Arrange
        List<Reports> reports = new ArrayList<>();
        reports.add(new Reports());
        reports.add(new Reports());

        when(reportsService.getAllSubmittedReports()).thenReturn(reports);

        // Act
        List<Reports> result = reportsController.getAllSubmittedReports();

        // Assert
        assertEquals(reports, result);
        verify(reportsService).getAllSubmittedReports();
    }

    @Test
    void getAllReportsApprovedByManager_Success() {
        // Arrange
        String request = "request";
        List<Reports> reports = new ArrayList<>();
        reports.add(new Reports());
        reports.add(new Reports());

        when(reportsService.getAllReportsApprovedByManager(request)).thenReturn(reports);

        // Act
        List<Reports> result = reportsController.getAllReportsApprovedByManager(request);

        // Assert
        assertEquals(reports, result);
        verify(reportsService).getAllReportsApprovedByManager(request);
    }

    @Test
    void addReport_Success() {
        // Arrange
        Reports newReport = new Reports();
        Long employeeId = 1L;
        ArrayList<Long> expenseIds = new ArrayList<>();
        Reports addedReport = new Reports();

        when(reportsService.addReport(newReport, employeeId, expenseIds)).thenReturn(addedReport);

        // Act
        Reports result = reportsController.addReport(newReport, employeeId, expenseIds);

        // Assert
        assertEquals(addedReport, result);
        verify(reportsService).addReport(newReport, employeeId, expenseIds);
    }

    @Test
    void addExpensesToReport_Success() {
        // Arrange
        Long reportId = 1L;
        ArrayList<Long> expenseIds = new ArrayList<>();
        Reports updatedReport = new Reports();

        when(reportsService.addExpenseToReport(reportId, expenseIds)).thenReturn(updatedReport);

        // Act
        Reports result = reportsController.addExpensesToReport(reportId, expenseIds);

        // Assert
        assertEquals(updatedReport, result);
        verify(reportsService).addExpenseToReport(reportId, expenseIds);
    }

    @Test
    void submitReport_Success() throws MessagingException, FileNotFoundException, IOException {
        // Arrange
        Long reportId = 1L;

        // Act
        reportsController.submitReport(reportId, response);

        // Assert
        verify(reportsService).submitReport(reportId, response);
    }

    @Test
    void editReport_Success() {
        // Arrange
        Long reportId = 1L;
        String reportTitle = "Updated Title";
        String reportDescription = "Updated Description";
        ArrayList<Long> addExpenseIds = new ArrayList<>();
        ArrayList<Long> removeExpenseIds = new ArrayList<>();
        List<Reports> updatedReports = new ArrayList<>();

        when(reportsService.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds))
                .thenReturn(updatedReports);

        // Act
        List<Reports> result = reportsController.editReport(reportId, reportTitle, reportDescription, addExpenseIds,
                removeExpenseIds);

        // Assert
        assertEquals(updatedReports, result);
        verify(reportsService).editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);
    }

    @Test
    void approveReportByManager_Success() throws MessagingException, IOException {
        // Arrange
        Long reportId = 1L;
        String comments = "Approved";

        // Act
        reportsController.approveReportByManager(reportId, comments, response);

        // Assert
        verify(reportsService).approveReportByManager(reportId, comments, response);
    }

    @Test
    void rejectReportByManager_Success() throws MessagingException, IOException {
        // Arrange
        Long reportId = 1L;
        String comments = "Rejected";

        // Act
        reportsController.rejectReportByManager(reportId, comments, response);

        // Assert
        verify(reportsService).rejectReportByManager(reportId, comments, response);
    }

    @Test
    void reimburseReportByFinance_Success() {
        // Arrange
        ArrayList<Long> reportIds = new ArrayList<>();
        String comments = "Reimbursed";

        // Act
        reportsController.reimburseReportByFinance(reportIds, comments);

        // Assert
        verify(reportsService).reimburseReportByFinance(reportIds, comments);
    }

    @Test
    void rejectReportByFinance_Success() {
        // Arrange
        Long reportId = 1L;
        String comments = "Rejected";

        // Act
        reportsController.rejectReportByFinance(reportId, comments);

        // Assert
        verify(reportsService).rejectReportByFinance(reportId, comments);
    }

    @Test
    void hideReport_Success() {
        // Arrange
        Long reportId = 1L;

        // Act
        reportsController.hideReport(reportId);

        // Assert
        verify(reportsService).hideReport(reportId);
    }

    @Test
    void totalAmountINR_Success() {
        // Arrange
        Long reportId = 1L;
        float totalAmount = 100.0f;

        when(reportsService.totalAmountINR(reportId)).thenReturn(totalAmount);

        // Act
        float result = reportsController.totalAmountINR(reportId);

        // Assert
        assertEquals(totalAmount, result);
        verify(reportsService).totalAmountINR(reportId);
    }

    @Test
    void totalAmountCurrency_Success() {
        // Arrange
        Long reportId = 1L;
        float totalAmount = 100.0f;

        when(reportsService.totalAmountCurrency(reportId)).thenReturn(totalAmount);

        // Act
        float result = reportsController.totalAmountCurrency(reportId);

        // Assert
        assertEquals(totalAmount, result);
        verify(reportsService).totalAmountCurrency(reportId);
    }

    @Test
    void getReportsInDateRange_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        List<Reports> reports = new ArrayList<>();
        reports.add(new Reports());
        reports.add(new Reports());

        when(reportsService.getReportsInDateRange(startDate, endDate)).thenReturn(reports);

        // Act
        List<Reports> result = reportsController.getReportsInDateRange(startDate, endDate);

        // Assert
        assertEquals(reports, result);
        verify(reportsService).getReportsInDateRange(startDate, endDate);
    }

    @Test
    void getReportsSubmittedToUserInDateRange_Success() {
        // Arrange
        String managerEmail = "manager@example.com";
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        String request = "request";
        List<Reports> reports = new ArrayList<>();
        reports.add(new Reports());
        reports.add(new Reports());

        when(reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request))
                .thenReturn(reports);

        // Act
        List<Reports> result = reportsController.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate,
                request);

        // Assert
        assertEquals(reports, result);
        verify(reportsService).getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request);
    }

    @Test
    void getAmountOfReportsInDateRange_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        String amount = "1000 USD";

        when(reportsService.getAmountOfReportsInDateRange(startDate, endDate)).thenReturn(amount);

        // Act
        String result = reportsController.getAmountOfReportsInDateRange(startDate, endDate);

        // Assert
        assertEquals(amount, result);
        verify(reportsService).getAmountOfReportsInDateRange(startDate, endDate);
    }

    @Test
    void totalApprovedAmount_Success() {
        // Arrange
        Long reportId = 1L;
        float totalAmount = 100.0f;

        when(reportsService.totalApprovedAmountCurrency(reportId)).thenReturn(totalAmount);

        // Act
        float result = reportsController.totalApprovedAmount(reportId);

        // Assert
        assertEquals(totalAmount, result);
        verify(reportsService).totalApprovedAmountCurrency(reportId);
    }

    @Test
    void updateExpenseStatus_Success() throws IOException, ParseException, org.json.simple.parser.ParseException {
        // Arrange
        Long reportId = 1L;
        String reviewTime = "2022-01-01";
        String json = "[{\"expenseId\": 1, \"amountApproved\": 100, \"status\": \"approved\"}]";

        // Act
        reportsController.updateExpenseStatus(reportId, reviewTime, json);

        // Assert
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(json); // Parse the 'json' variable
        Map<Long, Float> partialApprovedMap = new HashMap<>();
        List<Long> approvedIds = new ArrayList<>();
        List<Long> rejectedIds = new ArrayList<>();
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            long expenseId = ((Long) jsonObject.get("expenseId")); // Modify the casting
            float amountApproved = ((Long) jsonObject.get("amountApproved")); // Modify the casting
            String status = (String) jsonObject.get("status");

            if (Objects.equals(status, "approved")) {
                approvedIds.add(expenseId);
            }
            if (Objects.equals(status, "rejected")) {
                rejectedIds.add(expenseId);
            }
            if (Objects.equals(status, "partiallyApproved")) {
                partialApprovedMap.put(expenseId, amountApproved);
            }
        }

        verify(reportsService).updateExpenseStatus(reportId, approvedIds, rejectedIds, partialApprovedMap, reviewTime);

    }

    }
