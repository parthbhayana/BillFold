package com.nineleaps.expense_management_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineleaps.expense_management_project.dto.ReportsDTO;
import com.nineleaps.expense_management_project.entity.Reports;
import com.nineleaps.expense_management_project.service.IReportsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReportsControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private IReportsService reportsService;

    @InjectMocks
    private ReportsController reportsController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reportsController).build();
    }



    @Test
    public void testGetReportByEmpId() throws Exception {
        Reports report1 = new Reports();
        report1.setReportId(1L);
        report1.setReportTitle("Report 1");
        report1.setReportTitle("Description 1");

        Reports report2 = new Reports();
        report2.setReportId(2L);
        report2.setReportTitle("Report 2");
        report2.setReportTitle("Description 2");

        List<Reports> reports = new ArrayList<>();
        reports.add(report1);
        reports.add(report2);

        when(reportsService.getReportByEmpId(1L, "request")).thenReturn(reports);

        mockMvc.perform(get("/getReportByEmployeeId/{employeeId}", 1L)
                        .param("request", "request"))
                .andExpect(status().isOk());
    }

    // Utility method to convert an object to JSON string
    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testGetAllReports() {
        // Create a sample employee ID (e.g., 1)
        Long employeeId = 1L;

        // Create a set of sample reports
        Set<Reports> sampleReports = new HashSet<>();
        // Add your sample Reports objects to the set

        // Mock the service method to return the sample reports when called with the employeeId
        when(reportsService.getAllReports(employeeId)).thenReturn(sampleReports);

        // Call the controller method to get all reports for the employee
        Set<Reports> result = reportsController.getAllReports(employeeId);

        // Verify that the result matches the sample reports returned by the service
        assertEquals(sampleReports, result);
    }

    @Test
    public void testGetReportByReportId() {
        // Create a sample report ID (e.g., 1)
        Long reportId = 1L;

        // Create a sample report
        Reports sampleReport = new Reports();
        // Set properties of the sample report

        // Mock the service method to return the sample report when called with the reportId
        when(reportsService.getReportById(reportId)).thenReturn(sampleReport);

        // Call the controller method to get a report by its report ID
        Reports result = reportsController.getReportByReportId(reportId);

        // Verify that the result matches the sample report returned by the service
        assertEquals(sampleReport, result);
    }


    @Test
    public void testGetReportByEmployeeId() {
        // Create a sample employee ID (e.g., 1)
        Long employeeId = 1L;

        // Create a sample request parameter (e.g., "someRequest")
        String request = "someRequest";

        // Create a list of sample reports
        List<Reports> sampleReports = new ArrayList<>();
        // Add sample reports to the list as needed

        // Mock the service method to return the sample reports when called with the employeeId and request parameters
        when(reportsService.getReportByEmpId(employeeId, request)).thenReturn(sampleReports);

        // Call the controller method to get reports by employee ID and request
        List<Reports> result = reportsController.getReportByEmpId(employeeId, request);

        // Verify that the result matches the sample reports returned by the service
        assertEquals(sampleReports, result);
    }

    @Test
    public void testGetReportsSubmittedToUser() {
        // Create a sample manager email (e.g., "manager@example.com")
        String managerEmail = "manager@example.com";

        // Create a sample request parameter (e.g., "someRequest")
        String request = "someRequest";

        // Create a list of sample reports
        List<Reports> sampleReports = new ArrayList<>();
        // Add sample reports to the list as needed

        // Mock the service method to return the sample reports when called with the managerEmail and request parameters
        when(reportsService.getReportsSubmittedToUser(managerEmail, request)).thenReturn(sampleReports);

        // Call the controller method to get reports submitted to the manager with the manager email and request
        List<Reports> result = reportsController.getReportsSubmittedToUser(managerEmail, request);

        // Verify that the result matches the sample reports returned by the service
        assertEquals(sampleReports, result);
    }

    @Test
    public void testGetAllSubmittedReports() {
        // Create a list of sample submitted reports
        List<Reports> sampleSubmittedReports = new ArrayList<>();
        // Add sample submitted reports to the list as needed

        // Mock the service method to return the sample submitted reports when called
        when(reportsService.getAllSubmittedReports()).thenReturn(sampleSubmittedReports);

        // Call the controller method to get all submitted reports
        List<Reports> result = reportsController.getAllSubmittedReports();

        // Verify that the result matches the sample submitted reports returned by the service
        assertEquals(sampleSubmittedReports, result);
    }

    @Test
    public void testGetAllReportsApprovedByManager() {
        // Create a list of sample approved reports
        List<Reports> sampleApprovedReports = new ArrayList<>();
        // Add sample approved reports to the list as needed

        // Mock the service method to return the sample approved reports when called
        when(reportsService.getAllReportsApprovedByManager(any(String.class))).thenReturn(sampleApprovedReports);

        // Call the controller method to get all approved reports
        List<Reports> result = reportsController.getAllReportsApprovedByManager("someRequest");

        // Verify that the result matches the sample approved reports returned by the service
        assertEquals(sampleApprovedReports, result);
    }

    @Test
    public void testAddReport() {
        // Create a sample ReportsDTO
        ReportsDTO sampleReportsDTO = new ReportsDTO();
        // Initialize the sample DTO as needed

        // Create a list of sample expenseIds
        List<Long> sampleExpenseIds = new ArrayList<>();
        // Add sample expenseIds to the list as needed

        // Create a sample Reports object to return from the service
        Reports sampleReports = new Reports();
        // Initialize the sample Reports object as needed

        // Mock the service method to return the sample Reports object when called
        when(reportsService.addReport(any(ReportsDTO.class), any(Long.class), any(List.class)))
                .thenReturn(sampleReports);

        // Call the controller method to add a report
        Reports result = reportsController.addReport(sampleReportsDTO, 123L, sampleExpenseIds);

        // Verify that the result matches the sample Reports object returned by the service
        assertEquals(sampleReports, result);
    }


    @Test
    public void testAddExpensesToReport() {
        // Create a sample reportId
        Long sampleReportId = 123L;

        // Create a list of sample expenseIds
        List<Long> sampleExpenseIds = new ArrayList<>();
        // Add sample expenseIds to the list as needed

        // Create a sample Reports object to return from the service
        Reports sampleReports = new Reports();
        // Initialize the sample Reports object as needed

        // Mock the service method to return the sample Reports object when called
        when(reportsService.addExpenseToReport(any(Long.class), any(List.class)))
                .thenReturn(sampleReports);

        // Call the controller method to add expenses to a report
        Reports result = reportsController.addExpensesToReport(sampleReportId, sampleExpenseIds);

        // Verify that the result matches the sample Reports object returned by the service
        assertEquals(sampleReports, result);
    }

    @Test
    public void testSubmitReport() throws MessagingException, IOException {
        // Create a sample reportId
        Long sampleReportId = 123L;

        // Mock the service method to perform nothing when called
        doNothing().when(reportsService).submitReport(
                eq(sampleReportId),
                any(HttpServletResponse.class)
        );

        // Call the controller method to submit the report
        HttpServletResponse response = new MockHttpServletResponse(); // You can use a real or mock response
        reportsController.submitReport(sampleReportId, response);

        // Verify that the service method was called with the correct arguments
        verify(reportsService).submitReport(
                eq(sampleReportId),
                any(HttpServletResponse.class)
        );
    }



    @Test
    public void testSubmitReportToManager() throws MessagingException, IOException {
        // Create a sample reportId and managerEmail
        Long sampleReportId = 123L;
        String sampleManagerEmail = "manager@example.com";

        // Mock the service method to perform nothing when called
        doNothing().when(reportsService).submitReport(
                ArgumentMatchers.eq(sampleReportId),
                ArgumentMatchers.eq(sampleManagerEmail),
                ArgumentMatchers.any(HttpServletResponse.class)
        );

        // Call the controller method to submit the report
        HttpServletResponse response = new MockHttpServletResponse(); // You can use a real or mock response
        reportsController.submitReport(sampleReportId, sampleManagerEmail, response);

        // Verify that the service method was called with the correct arguments
        verify(reportsService).submitReport(
                ArgumentMatchers.eq(sampleReportId),
                ArgumentMatchers.eq(sampleManagerEmail),
                ArgumentMatchers.any(HttpServletResponse.class)
        );
    }

    @Test
    public void testEditReport() {
        // Create sample input data
        Long sampleReportId = 123L;
        String sampleReportTitle = "Updated Title";
        String sampleReportDescription = "Updated Description";
        List<Long> sampleAddExpenseIds = new ArrayList<>();
        sampleAddExpenseIds.add(456L);
        List<Long> sampleRemoveExpenseIds = new ArrayList<>();
        sampleRemoveExpenseIds.add(789L);

        // Create a sample list of reports as the result
        List<Reports> sampleReportsList = new ArrayList<>();

        // Mock the service method and specify the return value
        when(reportsService.editReport(sampleReportId, sampleReportTitle, sampleReportDescription,
                sampleAddExpenseIds, sampleRemoveExpenseIds))
                .thenReturn(sampleReportsList);

        // Call the controller method
        List<Reports> result = reportsController.editReport(sampleReportId, sampleReportTitle,
                sampleReportDescription, sampleAddExpenseIds,
                sampleRemoveExpenseIds);

        // Verify that the result matches the expected result
        assertEquals(sampleReportsList, result);
    }

    @Test
    public void testReimburseReportByFinance() {
        // Create sample input data
        ArrayList<Long> sampleReportIds = new ArrayList<>();
        sampleReportIds.add(123L);
        sampleReportIds.add(456L);
        String sampleComments = "Sample Comments";

        // Call the controller method
        reportsController.reimburseReportByFinance(sampleReportIds, sampleComments);

        // Verify that the service method is called with the correct arguments
        verify(reportsService).reimburseReportByFinance(sampleReportIds, sampleComments);
    }

    @Test
    public void testRejectReportByFinance() {
        // Create sample input data
        Long sampleReportId = 123L;
        String sampleComments = "Sample Comments";

        // Call the controller method
        reportsController.rejectReportByFinance(sampleReportId, sampleComments);

        // Verify that the service method is called with the correct arguments
        verify(reportsService).rejectReportByFinance(sampleReportId, sampleComments);
    }

    @Test
    public void testHideReport() {
        // Create a sample reportId
        Long sampleReportId = 123L;

        // Call the controller method
        reportsController.hideReport(sampleReportId);

        // Verify that the service method is called with the correct argument
        verify(reportsService).hideReport(sampleReportId);
    }

    @Test
    public void testTotalAmountINR() {
        // Create a sample reportId and expected totalAmountINR
        Long sampleReportId = 123L;
        float expectedTotalAmountINR = 1000.0f; // Replace with the expected value

        // Mock the reportsService.totalAmount method to return the expectedTotalAmountINR
        when(reportsService.totalAmount(sampleReportId)).thenReturn(expectedTotalAmountINR);

        // Call the controller method
        float actualTotalAmountINR = reportsController.totalAmountINR(sampleReportId);

        // Verify that the actual result matches the expected result
        assertEquals(expectedTotalAmountINR, actualTotalAmountINR, 0.01); // Use an appropriate delta for floating-point comparison
    }

    @Test
    public void testGetReportsInDateRange() {
        // Create sample start and end dates, and a sample request parameter
        LocalDate startDate = LocalDate.parse("2023-01-01");
        LocalDate endDate = LocalDate.parse("2023-12-31");
        String sampleRequest = "example";

        // Create a list of sample reports
        List<Reports> sampleReports = new ArrayList<>(); // Replace with your sample reports

        // Mock the reportsService.getReportsInDateRange method to return the sampleReports
        when(reportsService.getReportsInDateRange(startDate, endDate, sampleRequest)).thenReturn(sampleReports);

        // Call the controller method
        List<Reports> actualReports = reportsController.getReportsInDateRange(startDate, endDate, sampleRequest);

        // Verify that the actual result matches the expected result
        assertEquals(sampleReports, actualReports);
    }

    @Test
    public void testGetReportsSubmittedToUserInDateRange() {
        // Create sample managerEmail, start date, end date, and a sample request parameter
        String managerEmail = "example@email.com";
        LocalDate startDate = LocalDate.parse("2023-01-01");
        LocalDate endDate = LocalDate.parse("2023-12-31");
        String sampleRequest = "example";

        // Create a list of sample reports
        List<Reports> sampleReports = new ArrayList<>(); // Replace with your sample reports

        // Mock the reportsService.getReportsSubmittedToUserInDateRange method to return the sampleReports
        when(reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, sampleRequest))
                .thenReturn(sampleReports);

        // Call the controller method
        List<Reports> actualReports = reportsController.getReportsSubmittedToUserInDateRange(
                managerEmail, startDate, endDate, sampleRequest);

        // Verify that the actual result matches the expected result
        assertEquals(sampleReports, actualReports);
    }

    @Test
    public void testGetAmountOfReportsInDateRange() {
        // Create sample start date and end date
        LocalDate startDate = LocalDate.parse("2023-01-01");
        LocalDate endDate = LocalDate.parse("2023-12-31");

        // Create a sample amount as a string
        String sampleAmount = "12345.67"; // Replace with your sample amount

        // Mock the reportsService.getAmountOfReportsInDateRange method to return the sampleAmount
        when(reportsService.getAmountOfReportsInDateRange(startDate, endDate))
                .thenReturn(sampleAmount);

        // Call the controller method
        String actualAmount = reportsController.getAmountOfReportsInDateRange(startDate, endDate);

        // Verify that the actual result matches the expected result
        assertEquals(sampleAmount, actualAmount);
    }

    @Test
    public void testTotalApprovedAmount() {
        // Create a sample report ID
        Long sampleReportId = 1L; // Replace with your sample report ID

        // Create a sample total approved amount
        float sampleTotalApprovedAmount = 1234.56f; // Replace with your sample total approved amount

        // Mock the reportsService.totalApprovedAmount method to return the sampleTotalApprovedAmount
        when(reportsService.totalApprovedAmount(sampleReportId))
                .thenReturn(sampleTotalApprovedAmount);

        // Call the controller method
        float actualTotalApprovedAmount = reportsController.totalApprovedAmount(sampleReportId);

        // Verify that the actual result matches the expected result
        assertEquals(sampleTotalApprovedAmount, actualTotalApprovedAmount, 0.001); // Specify a delta for floating-point comparison
    }

    @Test
    public void testNotifyHr() throws MessagingException {
        // Create a sample report ID
        Long sampleReportId = 1L; // Replace with your sample report ID

        // Mock the reportsService.notifyHR method to do nothing when called with the sample report ID
        doNothing().when(reportsService).notifyHR(sampleReportId);

        // Call the controller method
        reportsController.notifyHr(sampleReportId);

        // Verify that the reportsService.notifyHR method was called with the sample report ID
        verify(reportsService).notifyHR(sampleReportId);
    }

    @Test
    public void testNotifyLnD() throws MessagingException {
        // Create a sample report ID
        Long sampleReportId = 1L; // Replace with your sample report ID

        // Mock the reportsService.notifyLnD method to do nothing when called with the sample report ID
        doNothing().when(reportsService).notifyLnD(sampleReportId);

        // Call the controller method
        reportsController.notifyLnD(sampleReportId);

        // Verify that the reportsService.notifyLnD method was called with the sample report ID
        verify(reportsService).notifyLnD(sampleReportId);
    }

}