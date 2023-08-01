package com.nineleaps.expensemanagementproject.controller;

import com.nineleaps.expensemanagementproject.DTO.ExpenseDTO;
import com.nineleaps.expensemanagementproject.DTO.ReportsDTO;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.service.IExpenseService;
import com.nineleaps.expensemanagementproject.service.IReportsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReportsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IReportsService reportsService;


    @Mock
    private ReportsDTO reportsDTO;

    @Mock
    private ExpenseController expenseController;

    @Mock
    private IExpenseService expenseService;


    @InjectMocks
    private ReportsController reportsController;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reportsController).build();
    }

    @Test
    void testGetAllReports() throws Exception {
        List<Reports> reportsList = new ArrayList<>();

        when(reportsService.getAllReports()).thenReturn(reportsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/getAllReports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(reportsService, times(1)).getAllReports();
        verifyNoMoreInteractions(reportsService);
    }

    @Test
    void testGetReportByReportId() throws Exception {
        Long reportId = 1L;
        Reports report = new Reports();

        when(reportsService.getReportById(reportId)).thenReturn(report);

        mockMvc.perform(MockMvcRequestBuilders.get("/getByReportId/{reportId}", reportId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(reportsService, times(1)).getReportById(reportId);
        verifyNoMoreInteractions(reportsService);
    }

    @Test
    void testGetReportByEmployeeId() throws Exception {
        Long employeeId = 1L;
        String request = "test";
        List<Reports> reportsList = new ArrayList<>();

        when(reportsService.getReportByEmpId(employeeId, request)).thenReturn(reportsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/getReportByEmployeeId/{employeeId}", employeeId)
                        .param("request", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(reportsService, times(1)).getReportByEmpId(employeeId, request);
        verifyNoMoreInteractions(reportsService);
    }


    @Test
    void testGetReportsSubmittedToUser() throws Exception {
        String managerEmail = "test@test.com";
        String request = "test";
        List<Reports> reportsList = new ArrayList<>();

        when(reportsService.getReportsSubmittedToUser(managerEmail, request)).thenReturn(reportsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/getReportsSubmittedToUser/{managerEmail}", managerEmail)
                        .param("request", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(reportsService, times(1)).getReportsSubmittedToUser(managerEmail, request);
        verifyNoMoreInteractions(reportsService);
    }

    @Test
    void testGetAllSubmittedReports() throws Exception {
        List<Reports> reportsList = new ArrayList<>();

        when(reportsService.getAllSubmittedReports()).thenReturn(reportsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/getAllSubmittedReports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(reportsService, times(1)).getAllSubmittedReports();
        verifyNoMoreInteractions(reportsService);
    }

    @Test
    void testGetAllReportsApprovedByManager() throws Exception {
        String request = "test";
        List<Reports> reportsList = new ArrayList<>();

        when(reportsService.getAllReportsApprovedByManager(request)).thenReturn(reportsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/getAllReportsApprovedByManager")
                        .param("request", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(reportsService, times(1)).getAllReportsApprovedByManager(request);
        verifyNoMoreInteractions(reportsService);
    }

    @Test
    void testAddReport() {
        // Create a sample ReportsDTO
        ReportsDTO reportsDTO = new ReportsDTO();
        reportsDTO.setReportTitle("Sample Report");
        reportsDTO.setReportDescription("Sample Report Description");

        // Create a sample employeeId
        Long employeeId = 1L;

        // Create a sample expenseIds list
        List<Long> expenseIds = new ArrayList<>();
        expenseIds.add(1L);
        expenseIds.add(2L);

        // Create a sample Reports object
        Reports reports = new Reports();
        reports.setReportId(1L);
        reports.setReportTitle("Sample Report");
        reports.setReportDescription("Sample Report Description");

        // Mock the service method
        when(reportsService.addReport(reportsDTO, employeeId, expenseIds)).thenReturn(reports);

        // Call the controller method
        Reports result = reportsController.addReport(reportsDTO, employeeId, expenseIds);

        // Verify the result
        assertEquals(reports, result);
        verify(reportsService, times(1)).addReport(reportsDTO, employeeId, expenseIds);
    }

    @Test
    void testAddExpensesToReport() {
        // Create a sample reportId
        Long reportId = 1L;

        // Create a sample expenseIds list
        List<Long> expenseIds = new ArrayList<>();
        expenseIds.add(1L);
        expenseIds.add(2L);

        // Create a sample Reports object
        Reports reports = new Reports();
        reports.setReportId(1L);
        reports.setReportTitle("Sample Report");
        reports.setReportDescription("Sample Report Description");

        // Mock the service method
        when(reportsService.addExpenseToReport(reportId, expenseIds)).thenReturn(reports);

        // Call the controller method
        Reports result = reportsController.addExpensesToReport(reportId, expenseIds);

        // Verify the result
        assertEquals(reports, result);
        verify(reportsService, times(1)).addExpenseToReport(reportId, expenseIds);
    }

    @Test
    void testSubmitReport() throws MessagingException, IOException {
        // Create a sample reportId
        Long reportId = 1L;

        // Create a mock HttpServletResponse
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Call the controller method
        reportsController.submitReport(reportId, response);

        // Verify the service method is called
        verify(reportsService, times(1)).submitReport(reportId, response);
    }

    @Test
    void testEditReport() {
        // Create a sample reportId
        Long reportId = 1L;

        // Create sample request parameters
        String reportTitle = "Updated Report Title";
        String reportDescription = "Updated Report Description";
        List<Long> addExpenseIds = new ArrayList<>();
        addExpenseIds.add(3L);
        List<Long> removeExpenseIds = new ArrayList<>();
        removeExpenseIds.add(2L);

        // Create a sample list of Reports
        List<Reports> reportsList = new ArrayList<>();
        Reports report1 = new Reports();
        report1.setReportId(1L);
        report1.setReportTitle("Report 1");
        Reports report2 = new Reports();
        report2.setReportId(2L);
        report2.setReportTitle("Report 2");
        reportsList.add(report1);
        reportsList.add(report2);

        // Mock the service method
        when(reportsService.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds)).thenReturn(reportsList);

        // Call the controller method
        List<Reports> result = reportsController.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);

        // Verify the result
        assertEquals(reportsList, result);
        verify(reportsService, times(1)).editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);
    }
    @Test
    void testAddReport_Success() {
        Reports expectedReport = new Reports();

        // Mocking the reportsService.addReport method to return the expected report
        when(reportsService.addReport(any(ReportsDTO.class), any(Long.class), any(List.class)))
                .thenReturn(expectedReport);

        // Create actual Long objects instead of mocking Long
        Long employeeId = 1L;
        List<Long> expenseIds = Arrays.asList(1L, 2L, 3L);

        // Invoke the addReport method
        Reports result = reportsController.addReport(reportsDTO, employeeId, expenseIds);

        // Verify that the reportsService.addReport method was called with the correct arguments
        verify(reportsService).addReport(reportsDTO, employeeId, expenseIds);

        // Verify the result
        assertEquals(expectedReport, result);
    }

    @Test
    void testApproveReportByManager() throws MessagingException, IOException {
        // Create a mock HttpServletResponse
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Create test data
        Long reportId = 1L;
        String comments = "Approved";

        // Invoke the method
        reportsController.approveReportByManager(reportId, comments, response);

        // Verify that the reportsService.approveReportByManager method was called with the correct arguments
        verify(reportsService).approveReportByManager(reportId, comments, response);
    }

    @Test
    void testRejectReportByManager() throws MessagingException, IOException {
        // Create a mock HttpServletResponse
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Create test data
        Long reportId = 1L;
        String comments = "Rejected";

        // Invoke the method
        reportsController.rejectReportByManager(reportId, comments, response);

        // Verify that the reportsService.rejectReportByManager method was called with the correct arguments
        verify(reportsService).rejectReportByManager(reportId, comments, response);
    }

    @Test
    void testReimburseReportByFinance() {
        // Create test data
        ArrayList<Long> reportIds = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        String comments = "Reimbursement approved";

        // Invoke the method
        reportsController.reimburseReportByFinance(reportIds, comments);

        // Verify that the reportsService.reimburseReportByFinance method was called with the correct arguments
        verify(reportsService).reimburseReportByFinance(reportIds, comments);
    }

    @Test
    void testRejectReportByFinance() {
        // Create test data
        Long reportId = 1L;
        String comments = "Reimbursement rejected";

        // Invoke the method
        reportsController.rejectReportByFinance(reportId, comments);

        // Verify that the reportsService.rejectReportByFinance method was called with the correct arguments
        verify(reportsService).rejectReportByFinance(reportId, comments);
    }

    @Test
    void testHideReport() {
        // Create test data
        Long reportId = 1L;

        // Invoke the method
        reportsController.hideReport(reportId);

        // Verify that the reportsService.hideReport method was called with the correct argument
        verify(reportsService).hideReport(reportId);
    }

    @Test
    void testTotalAmountINR() {
        // Create test data
        Long reportId = 1L;
        float expectedAmount = 1000.0f;

        // Mock the reportsService.totalAmountINR method
        when(reportsService.totalAmountINR(reportId)).thenReturn(expectedAmount);

        // Invoke the method
        float result = reportsController.totalAmountINR(reportId);

        // Verify the result
        assertEquals(expectedAmount, result, 0.001);

        // Verify that the reportsService.totalAmountINR method was called with the correct argument
        verify(reportsService).totalAmountINR(reportId);
    }
    @Test
    void testTotalAmountCurrency() {
        // Create test data
        Long reportId = 1L;
        float expectedAmount = 1000.0f;

        // Mock the reportsService.totalAmountCurrency method
        when(reportsService.totalAmountCurrency(reportId)).thenReturn(expectedAmount);

        // Invoke the method
        float result = reportsController.totalAmountCurrency(reportId);

        // Verify the result
        assertEquals(expectedAmount, result, 0.001);

        // Verify that the reportsService.totalAmountCurrency method was called with the correct argument
        verify(reportsService).totalAmountCurrency(reportId);
    }

    @Test
    void testGetReportsInDateRange() {
        // Create test data
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        List<Reports> expectedReports = new ArrayList<>(); // Add your expected reports here

        // Mock the reportsService.getReportsInDateRange method
        when(reportsService.getReportsInDateRange(startDate, endDate)).thenReturn(expectedReports);

        // Invoke the method
        List<Reports> result = reportsController.getReportsInDateRange(startDate, endDate);

        // Verify the result
        assertEquals(expectedReports, result);

        // Verify that the reportsService.getReportsInDateRange method was called with the correct arguments
        verify(reportsService).getReportsInDateRange(startDate, endDate);
    }

    @Test
    void testGetReportsSubmittedToUserInDateRange() {
        // Create test data
        String managerEmail = "example@example.com";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        String request = "exampleRequest";
        List<Reports> expectedReports = new ArrayList<>(); // Add your expected reports here

        // Mock the reportsService.getReportsSubmittedToUserInDateRange method
        when(reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request)).thenReturn(expectedReports);

        // Invoke the method
        List<Reports> result = reportsController.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request);

        // Verify the result
        assertEquals(expectedReports, result);

        // Verify that the reportsService.getReportsSubmittedToUserInDateRange method was called with the correct arguments
        verify(reportsService).getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request);
    }

    @Test
    void testGetAmountOfReportsInDateRange() {
        // Create test data
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        String expectedAmount = "1000"; // Add your expected amount here

        // Mock the reportsService.getAmountOfReportsInDateRange method
        when(reportsService.getAmountOfReportsInDateRange(startDate, endDate)).thenReturn(expectedAmount);

        // Invoke the method
        String result = reportsController.getAmountOfReportsInDateRange(startDate, endDate);

        // Verify the result
        assertEquals(expectedAmount, result);

        // Verify that the reportsService.getAmountOfReportsInDateRange method was called with the correct arguments
        verify(reportsService).getAmountOfReportsInDateRange(startDate, endDate);
    }

    @Test
    void testTotalApprovedAmount() {
        // Create test data
        Long reportId = 123L; // Add your report ID here
        float expectedAmount = 1000.0f; // Add your expected amount here

        // Mock the reportsService.totalApprovedAmountCurrency method
        when(reportsService.totalApprovedAmountCurrency(reportId)).thenReturn(expectedAmount);

        // Invoke the method
        float result = reportsController.totalApprovedAmount(reportId);

        // Verify the result
        assertEquals(expectedAmount, result, 0.001); // Use a delta value for float comparison

        // Verify that the reportsService.totalApprovedAmountCurrency method was called with the correct argument
        verify(reportsService).totalApprovedAmountCurrency(reportId);
    }
    @Test
    void testUpdateExpenses() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock input parameters
        Long expenseId = 1L;
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setDescription("New description");

        // Mock the expected result
        Expense expected = new Expense();
        expected.setExpenseId(expenseId);
        expected.setDescription("New description");

        // Mock the behavior of the expenseService interface
        when(expenseService.updateExpenses(expenseDTO, expenseId)).thenReturn(expected);

        // Call the controller method
        Expense result = expenseService.updateExpenses(expenseDTO, expenseId);

        // Verify the interactions and expected behavior
        verify(expenseService).updateExpenses(expenseDTO, expenseId);
        assertEquals(expected, result);
    }


}
