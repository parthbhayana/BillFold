package com.nineleaps.expense_management_project.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineleaps.expense_management_project.dto.ExpenseDTO;
import com.nineleaps.expense_management_project.dto.ReportsDTO;
import com.nineleaps.expense_management_project.entity.*;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;
import com.nineleaps.expense_management_project.service.EmployeeServiceImpl;
import com.nineleaps.expense_management_project.service.ExpenseServiceImpl;
import com.nineleaps.expense_management_project.service.IReportsService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.IOException;

import java.time.LocalDate;

import java.util.*;


import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;


import org.apache.catalina.connector.Response;
import org.json.simple.parser.ParseException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;





@ContextConfiguration(classes = {ReportsController.class})
@ExtendWith(SpringExtension.class)
class ReportsControllerTest {
    @MockBean
    private IReportsService iReportsService;

    @MockBean
    private ExpenseServiceImpl expenseService;


    private MockMvc mockMvc;

    @Autowired
    private ReportsController reportsController;

    /**
     * Method under test: {@link ReportsController#addExpensesToReport(Long, List)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddExpensesToReport1() throws Exception {
        // TODO: Complete this test.
        //   Diffblue AI was unable to find a test

        // Arrange
        // TODO: Populate arranged inputs
        Object[] uriVariables = new Object[]{1L};
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/addExpenseToReport/{reportId}",
                uriVariables);
        Object[] controllers = new Object[]{reportsController};
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(controllers).build();

        // Act
        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        // Assert
        // TODO: Add assertions on result
    }

    /**
     * Method under test: {@link ReportsController#getAllReports(Long)}
     */
    @Test
    void testGetAllReports() throws Exception {
        // Arrange
        when(iReportsService.getAllReports(Mockito.<Long>any())).thenReturn(new HashSet<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAllReports/{employeeId}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ReportsController#getAllReports(Long)}
     */
    @Test
    void testGetAllReports2() throws Exception {
        // Arrange
        when(iReportsService.getAllReports(Mockito.<Long>any())).thenReturn(new HashSet<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAllReports/{employeeId}", 1L);
        requestBuilder.characterEncoding("Encoding");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ReportsController#getReportByEmpId(Long, String)}
     */
    @Test
    void testGetReportByEmpId() throws Exception {
        // Arrange
        when(iReportsService.getReportByEmpId(Mockito.<Long>any(), Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/getReportByEmployeeId/{employeeId}", 1L)
                .param("request", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ReportsController#getReportsSubmittedToUser(String, String)}
     */
    @Test
    void testGetReportsSubmittedToUser() throws Exception {
        // Arrange
        when(iReportsService.getReportsSubmittedToUser(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/getReportsSubmittedToUser/{managerEmail}", "jane.doe@example.org")
                .param("request", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ReportsController#getAllSubmittedReports()}
     */
    @Test
    void testGetAllSubmittedReports() throws Exception {
        // Arrange
        when(iReportsService.getAllSubmittedReports()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAllSubmittedReports");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ReportsController#getAllSubmittedReports()}
     */
    @Test
    void testGetAllSubmittedReports2() throws Exception {
        // Arrange
        when(iReportsService.getAllSubmittedReports()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAllSubmittedReports");
        requestBuilder.characterEncoding("Encoding");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ReportsController#getAllReportsApprovedByManager(String)}
     */
    @Test
    void testGetAllReportsApprovedByManager() throws Exception {
        // Arrange
        when(iReportsService.getAllReportsApprovedByManager(Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAllReportsApprovedByManager")
                .param("request", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ReportsController#submitReport(Long, String, HttpServletResponse)}
     */
    @Test
    void testSubmitReport() throws Exception {
        // Arrange
        doNothing().when(iReportsService)
                .submitReport(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<HttpServletResponse>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/submitReportToManager/{reportId}", 1L)
                .param("managerEmail", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ReportsController#submitReport(Long, HttpServletResponse)}
     */
    @Test
    void testSubmitReport2() throws Exception {
        // Arrange
        doNothing().when(iReportsService).submitReport(Mockito.<Long>any(), Mockito.<HttpServletResponse>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/submitReport/{reportId}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ReportsController#reimburseReportByFinance(List, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testReimburseReportByFinance() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.nio.charset.IllegalCharsetNameException: https://example.org/example
        //       at java.nio.charset.Charset.checkName(Charset.java:308)
        //       at java.nio.charset.Charset.lookup2(Charset.java:482)
        //       at java.nio.charset.Charset.lookup(Charset.java:462)
        //       at java.nio.charset.Charset.forName(Charset.java:526)
        //       at javax.servlet.http.HttpServlet.service(HttpServlet.java:528)
        //       at javax.servlet.http.HttpServlet.service(HttpServlet.java:596)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        ReportsController reportsController = new ReportsController();

        // Act
        reportsController.reimburseReportByFinance(new ArrayList<>(), "Comments");
    }

    /**
     * Method under test: {@link ReportsController#rejectReportByFinance(Long, String)}
     */
    @Test
    void testRejectReportByFinance() throws Exception {
        // Arrange
        doNothing().when(iReportsService).rejectReportByFinance(Mockito.<Long>any(), Mockito.<String>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/rejectReportByFinance/{reportId}", 1L)
                .param("comments", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ReportsController#hideReport(Long)}
     */
    @Test
    void testHideReport() throws Exception {
        // Arrange
        doNothing().when(iReportsService).hideReport(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/hideReport/{reportId}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ReportsController#hideReport(Long)}
     */
    @Test
    void testHideReport2() throws Exception {
        // Arrange
        doNothing().when(iReportsService).hideReport(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/hideReport/{reportId}", 1L);
        requestBuilder.characterEncoding("Encoding");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ReportsController#totalApprovedAmount(Long)}
     */
    @Test
    void testTotalApprovedAmount() throws Exception {
        // Arrange
        when(iReportsService.totalApprovedAmount(Mockito.<Long>any())).thenReturn(10.0f);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/getTotalApprovedAmount");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("reportId", String.valueOf(1L));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("10.0"));
    }

    /**
     * Method under test: {@link ReportsController#notifyLnD(Long)}
     */
    @Test
    void testNotifyLnD() throws Exception {
        // Arrange
        doNothing().when(iReportsService).notifyLnD(Mockito.<Long>any());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/notifyLnD/{reportId}", 1L);
        MockHttpServletRequestBuilder requestBuilder = postResult.param("reportId", String.valueOf(1L));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ReportsController#updateExpenseStatus(Long, String, String, String, HttpServletResponse)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateExpenseStatus1() throws IOException, MessagingException, ParseException {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   org.json.simple.parser.ParseException
        //       at org.json.simple.parser.Yylex.yylex(Yylex.java:610)
        //       at org.json.simple.parser.JSONParser.nextToken(JSONParser.java:269)
        //       at org.json.simple.parser.JSONParser.parse(JSONParser.java:118)
        //       at org.json.simple.parser.JSONParser.parse(JSONParser.java:81)
        //       at org.json.simple.parser.JSONParser.parse(JSONParser.java:75)
        //       at com.nineleaps.expense_management_project.controller.ReportsController.updateExpenseStatus(ReportsController.java:164)
        //       at javax.servlet.http.HttpServlet.service(HttpServlet.java:528)
        //       at javax.servlet.http.HttpServlet.service(HttpServlet.java:596)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        ReportsController reportsController = new ReportsController();

        // Act
        reportsController.updateExpenseStatus(1L, "Review Time", "Json", "Comments", new Response());
    }

    /**
     * Method under test: {@link ReportsController#addReport(ReportsDTO, Long, List)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddReport1() throws Exception {
        // TODO: Complete this test.
        //   Diffblue AI was unable to find a test

        // Arrange
        // TODO: Populate arranged inputs
        Object[] uriVariables = new Object[]{1L};
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/addReport/{employeeId}", uriVariables);
        ReportsDTO reportsDTO = new ReportsDTO();
        String[] values = new String[]{String.valueOf(reportsDTO)};
        MockHttpServletRequestBuilder requestBuilder = postResult.param("reportsDTO", values);
        Object[] controllers = new Object[]{reportsController};
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(controllers).build();

        // Act
        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        // Assert
        // TODO: Add assertions on result
    }

    /**
     * Method under test: {@link ReportsController#editReport(Long, String, String, List, List)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testEditReport() throws Exception {
        // TODO: Complete this test.
        //   Diffblue AI was unable to find a test

        // Arrange
        // TODO: Populate arranged inputs
        Object[] uriVariables = new Object[]{1L};
        String[] values = new String[]{"foo"};
        String[] values2 = new String[]{"foo"};
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/editReport/{reportId}", uriVariables)
                .param("reportDescription", values)
                .param("reportTitle", values2);
        Object[] controllers = new Object[]{reportsController};
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(controllers).build();

        // Act
        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        // Assert
        // TODO: Add assertions on result
    }

    /**
     * Method under test: {@link ReportsController#getAmountOfReportsInDateRange(LocalDate, LocalDate)}
     */
    @Test
    void testGetAmountOfReportsInDateRange() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/getAmountOfReportsInDateRange");
        MockHttpServletRequestBuilder paramResult = getResult.param("endDate", String.valueOf((Object) null));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("startDate", String.valueOf((Object) null));

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link ReportsController#getReportByReportId(Long)}
     */
    @Test
    void testGetReportByReportId() throws Exception {
        // Arrange
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
        when(iReportsService.getReportById(Mockito.<Long>any())).thenReturn(reports);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getByReportId/{reportId}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"reportId\":1,\"employeeId\":1,\"employeeName\":\"Employee Name\",\"officialEmployeeId\":\"42\",\"reportTitle\":"
                                        + "\"Dr\",\"managerComments\":\"Manager Comments\",\"financeComments\":\"Finance Comments\",\"isSubmitted\":true,"
                                        + "\"employeeMail\":\"Employee Mail\",\"dateSubmitted\":[1970,1,1],\"dateCreated\":[1970,1,1],\"managerActionDate"
                                        + "\":[1970,1,1],\"financeActionDate\":[1970,1,1],\"totalAmount\":10.0,\"totalApprovedAmount\":10.0,\"isHidden\""
                                        + ":true,\"managerEmail\":\"jane.doe@example.org\",\"managerReviewTime\":\"Manager Review Time\",\"financeApprovalStatus"
                                        + "\":\"REIMBURSED\",\"managerApprovalStatus\":\"APPROVED\",\"expensesCount\":3}"));
    }

    /**
     * Method under test: {@link ReportsController#getReportsInDateRange(LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsInDateRange() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/getReportsInDateRange");
        MockHttpServletRequestBuilder paramResult = getResult.param("endDate", String.valueOf((Object) null))
                .param("request", "foo");
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("startDate", String.valueOf((Object) null));

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link ReportsController#getReportsSubmittedToUserInDateRange(String, LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsSubmittedToUserInDateRange() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/getReportsSubmittedToUserInDateRange");
        MockHttpServletRequestBuilder paramResult = getResult.param("endDate", String.valueOf((Object) null))
                .param("managerEmail", "foo")
                .param("request", "foo");
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("startDate", String.valueOf((Object) null));

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link ReportsController#notifyHr(Long)}
     */
    @Test
    void testNotifyHr() throws Exception {
        // Arrange
        doNothing().when(iReportsService).notifyHR(Mockito.<Long>any());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/notifyHr/{reportId}", 1L);
        MockHttpServletRequestBuilder requestBuilder = postResult.param("reportId", String.valueOf(1L));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ReportsController#totalAmountINR(Long)}
     */
    @Test
    void testTotalAmountINR() throws Exception {
        // Arrange
        when(iReportsService.totalAmount(Mockito.<Long>any())).thenReturn(10.0f);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/getTotalAmountInrByReportId");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("reportId", String.valueOf(1L));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("10.0"));
    }

//    @Test
//    void testAddReport1() throws Exception {
//        // Create a sample ReportsDTO for testing
//        ReportsDTO reportsDTO = new ReportsDTO();
//
//        ReportsServiceImpl reportsService = new ReportsServiceImpl();
//        // Set properties of reportsDTO for testing
//
//        // Define a sample employeeId
//        Long employeeId = 1L;
//
//        // Define a list of expenseIds
//        List<Long> expenseIds = new ArrayList<>();
//        expenseIds.add(1L);
//        expenseIds.add(2L);
//
//        // Define a sample Reports entity to be returned by the service
//        Reports mockReport = new Reports();
//        // Set properties of the mockReport
//
//        // Mock the behavior of reportsService.addReport method
//        when(reportsService.addReport(reportsDTO, employeeId, expenseIds)).thenReturn(mockReport);
//
//        // Perform the POST request to add a report
//        mockMvc.perform(MockMvcRequestBuilders.post("/addReport/{employeeId}", employeeId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(reportsDTO))
//                        .param("expenseIds", "1,2"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(asJsonString(mockReport)));
//
//
//        // Verify that the reportsService.addReport method was called with the expected arguments
//        verify(reportsService, times(1)).addReport(reportsDTO, employeeId, expenseIds);
//        verifyNoMoreInteractions(reportsService);
//    }
//
//    // Helper method to convert an object to JSON string
//    private String asJsonString(Object object) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            return objectMapper.writeValueAsString(object);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Test
    void testAddReport() {
        // Create a sample ReportsDTO
        ReportsDTO reportsDTO = new ReportsDTO();
        reportsDTO.setReportTitle("Sample Report");
        reportsDTO.setReportTitle("Sample Report Description");

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
        reports.setReportTitle("Sample Report Description");

        // Mock the service method
        when(iReportsService.addReport(reportsDTO, employeeId, expenseIds)).thenReturn(reports);

        // Call the controller method
        Reports result = reportsController.addReport(reportsDTO, employeeId, expenseIds);

        // Verify the result
        assertEquals(reports, result);
        verify(iReportsService, times(1)).addReport(reportsDTO, employeeId, expenseIds);
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
        reports.setReportTitle("Sample Report Description");

        // Mock the service method
        when(iReportsService.addExpenseToReport(reportId, expenseIds)).thenReturn(reports);

        // Call the controller method
        Reports result = reportsController.addExpensesToReport(reportId, expenseIds);

        // Verify the result
        assertEquals(reports, result);
        verify(iReportsService, times(1)).addExpenseToReport(reportId, expenseIds);
    }

    @Test
    void testEditReport1() {
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
        when(iReportsService.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds)).thenReturn(reportsList);

        // Call the controller method
        List<Reports> result = reportsController.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);

        // Verify the result
        assertEquals(reportsList, result);
        verify(iReportsService, times(1)).editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);
    }

    @Test
    void testReimburseReportByFinance1() {
        // Create test data
        ArrayList<Long> reportIds = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        String comments = "Reimbursement approved";

        // Invoke the method
        reportsController.reimburseReportByFinance(reportIds, comments);

        // Verify that the reportsService.reimburseReportByFinance method was called with the correct arguments
        verify(iReportsService).reimburseReportByFinance(reportIds, comments);
    }

    @Test
    void testGetReportsInDateRange1() {
        // Create test data
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        List<Reports> expectedReports = new ArrayList<>(); // Add your expected reports here

        // Mock the reportsService.getReportsInDateRange method
        when(iReportsService.getReportsInDateRange(startDate, endDate, String.valueOf(endDate))).thenReturn(expectedReports);

        // Invoke the method
        List<Reports> result = reportsController.getReportsInDateRange(startDate, endDate, String.valueOf(endDate));

        // Verify the result
        assertEquals(expectedReports, result);

        // Verify that the reportsService.getReportsInDateRange method was called with the correct arguments
        verify(iReportsService).getReportsInDateRange(startDate, endDate, String.valueOf(endDate));
    }

    @Test
    void testGetReportsSubmittedToUserInDateRange1() {
        // Create test data
        String managerEmail = "example@example.com";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        String request = "exampleRequest";
        List<Reports> expectedReports = new ArrayList<>(); // Add your expected reports here

        // Mock the reportsService.getReportsSubmittedToUserInDateRange method
        when(iReportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request)).thenReturn(expectedReports);

        // Invoke the method
        List<Reports> result = reportsController.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request);

        // Verify the result
        assertEquals(expectedReports, result);

        // Verify that the reportsService.getReportsSubmittedToUserInDateRange method was called with the correct arguments
        verify(iReportsService).getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request);
    }

    @Test
    void testGetAmountOfReportsInDateRange1() {
        // Create test data
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        String expectedAmount = "1000"; // Add your expected amount here

        // Mock the reportsService.getAmountOfReportsInDateRange method
        when(iReportsService.getAmountOfReportsInDateRange(startDate, endDate)).thenReturn(expectedAmount);

        // Invoke the method
        String result = reportsController.getAmountOfReportsInDateRange(startDate, endDate);

        // Verify the result
        assertEquals(expectedAmount, result);

        // Verify that the reportsService.getAmountOfReportsInDateRange method was called with the correct arguments
        verify(iReportsService).getAmountOfReportsInDateRange(startDate, endDate);
    }

    @Test
    void testUpdateExpenseStatus() throws ParseException, MessagingException, IOException {
        // Arrange

        Long reportId = 1L;
        String reviewTime = "2023-10-18";
        String json = "[{\"expenseId\": 1, \"amountApproved\": 100.0, \"status\": \"approved\"}]";
        String comments = "Test comments";
        MockHttpServletResponse response = new MockHttpServletResponse();

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(json);

        List<Long> approvedIds = new ArrayList<>();
        List<Long> rejectedIds = new ArrayList<>();
        Map<Long, Float> partialApprovedMap = new HashMap<>();

        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            long expenseId = (Long) jsonObject.get("expenseId");
            double amountApproved = (double) jsonObject.get("amountApproved");
            String status = (String) jsonObject.get("status");

            if ("approved".equals(status)) {
                approvedIds.add(expenseId);
            } else if ("rejected".equals(status)) {
                rejectedIds.add(expenseId);
            } else if ("partiallyApproved".equals(status)) {
                partialApprovedMap.put(expenseId, (float) amountApproved);
            }
        }

        doNothing().when(iReportsService).updateExpenseStatus(
                eq(reportId), eq(approvedIds), eq(rejectedIds), eq(partialApprovedMap),
                eq(reviewTime), eq(comments), eq(response)
        );

        // Act
        reportsController.updateExpenseStatus(reportId, reviewTime, json, comments, response);

        // Assert
        verify(iReportsService).updateExpenseStatus(
                eq(reportId), eq(approvedIds), eq(rejectedIds), eq(partialApprovedMap),
                eq(reviewTime), eq(comments), eq(response)
        );
    }
}






