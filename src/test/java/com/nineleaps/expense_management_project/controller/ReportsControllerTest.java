package com.nineleaps.expense_management_project.controller;

import com.nineleaps.expense_management_project.dto.ReportsDTO;
import com.nineleaps.expense_management_project.entity.FinanceApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatus;
import com.nineleaps.expense_management_project.entity.Reports;
import com.nineleaps.expense_management_project.service.IReportsService;

import java.io.IOException;

import java.time.LocalDate;

import java.util.ArrayList;

import java.util.HashSet;

import java.util.List;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import com.nineleaps.expense_management_project.service.ReportsServiceImpl;
import org.apache.catalina.connector.Response;
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ContextConfiguration(classes = {ReportsController.class})
@ExtendWith(SpringExtension.class)
class ReportsControllerTest {
    @MockBean
    private IReportsService iReportsService;

    @Autowired
    private ReportsController reportsController;

    /**
     * Method under test: {@link ReportsController#addExpensesToReport(Long, List)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddExpensesToReport() throws Exception {
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
        when(iReportsService.getAllReports(Mockito.<Long>any())).thenReturn(new HashSet<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAllReports/{employeeId}", 1L);
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
        when(iReportsService.getAllReports(Mockito.<Long>any())).thenReturn(new HashSet<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAllReports/{employeeId}", 1L);
        requestBuilder.characterEncoding("Encoding");
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
        when(iReportsService.getReportByEmpId(Mockito.<Long>any(), Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/getReportByEmployeeId/{employeeId}", 1L)
                .param("request", "foo");
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
        when(iReportsService.getReportsSubmittedToUser(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/getReportsSubmittedToUser/{managerEmail}", "jane.doe@example.org")
                .param("request", "foo");
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
        when(iReportsService.getAllSubmittedReports()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAllSubmittedReports");
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
        when(iReportsService.getAllSubmittedReports()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAllSubmittedReports");
        requestBuilder.characterEncoding("Encoding");
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
        when(iReportsService.getAllReportsApprovedByManager(Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAllReportsApprovedByManager")
                .param("request", "foo");
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
        doNothing().when(iReportsService)
                .submitReport(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<HttpServletResponse>any());
        MockHttpServletRequestBuilder requestBuilder = post("/submitReportToManager/{reportId}", 1L)
                .param("managerEmail", "foo");
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
        doNothing().when(iReportsService).submitReport(Mockito.<Long>any(), Mockito.<HttpServletResponse>any());
        MockHttpServletRequestBuilder requestBuilder = post("/submitReport/{reportId}", 1L);
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

        ReportsController reportsController = new ReportsController();
        reportsController.reimburseReportByFinance(new ArrayList<>(), "Comments");
    }

    /**
     * Method under test: {@link ReportsController#rejectReportByFinance(Long, String)}
     */
    @Test
    void testRejectReportByFinance() throws Exception {
        doNothing().when(iReportsService).rejectReportByFinance(Mockito.<Long>any(), Mockito.<String>any());
        MockHttpServletRequestBuilder requestBuilder = post("/rejectReportByFinance/{reportId}", 1L)
                .param("comments", "foo");
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
        doNothing().when(iReportsService).hideReport(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = post("/hideReport/{reportId}", 1L);
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
        doNothing().when(iReportsService).hideReport(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = post("/hideReport/{reportId}", 1L);
        requestBuilder.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ReportsController#getReportsInDateRange(LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsInDateRange() throws Exception {
        when(iReportsService.getReportsInDateRange(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(),
                Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/getReportsInDateRange");
        MockHttpServletRequestBuilder paramResult = getResult.param("endDate", String.valueOf(LocalDate.of(1970, 1, 1)))
                .param("request", "foo");
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("startDate",
                String.valueOf(LocalDate.of(1970, 1, 1)));
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ReportsController#getReportsSubmittedToUserInDateRange(String, LocalDate, LocalDate, String)}
     */
    @Test
    void testGetReportsSubmittedToUserInDateRange() throws Exception {
        when(iReportsService.getReportsSubmittedToUserInDateRange(Mockito.<String>any(), Mockito.<LocalDate>any(),
                Mockito.<LocalDate>any(), Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/getReportsSubmittedToUserInDateRange");
        MockHttpServletRequestBuilder paramResult = getResult.param("endDate", String.valueOf(LocalDate.of(1970, 1, 1)))
                .param("managerEmail", "foo")
                .param("request", "foo");
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("startDate",
                String.valueOf(LocalDate.of(1970, 1, 1)));
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ReportsController#getAmountOfReportsInDateRange(LocalDate, LocalDate)}
     */
    @Test
    void testGetAmountOfReportsInDateRange() throws Exception {
        when(iReportsService.getAmountOfReportsInDateRange(Mockito.<LocalDate>any(), Mockito.<LocalDate>any()))
                .thenReturn("2020-03-01");
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/getAmountOfReportsInDateRange");
        MockHttpServletRequestBuilder paramResult = getResult.param("endDate", String.valueOf(LocalDate.of(1970, 1, 1)));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("startDate",
                String.valueOf(LocalDate.of(1970, 1, 1)));
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("2020-03-01"));
    }

    /**
     * Method under test: {@link ReportsController#totalApprovedAmount(Long)}
     */
    @Test
    void testTotalApprovedAmount() throws Exception {
        when(iReportsService.totalApprovedAmount(Mockito.<Long>any())).thenReturn(10.0f);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/getTotalApprovedAmount");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("reportId", String.valueOf(1L));
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
        doNothing().when(iReportsService).notifyLnD(Mockito.<Long>any());
        MockHttpServletRequestBuilder postResult = post("/notifyLnD/{reportId}", 1L);
        MockHttpServletRequestBuilder requestBuilder = postResult.param("reportId", String.valueOf(1L));
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
    void testUpdateExpenseStatus() throws IOException, MessagingException, ParseException {
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

        ReportsController reportsController = new ReportsController();
        reportsController.updateExpenseStatus(1L, "Review Time", "Json", "Comments", new Response());
    }

    /**
     * Method under test: {@link ReportsController#addReport(ReportsDTO, Long, List)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddReport() throws Exception {
        // TODO: Complete this test.
        //   Diffblue AI was unable to find a test

        // Arrange
        // TODO: Populate arranged inputs
        Object[] uriVariables = new Object[]{1L};
        MockHttpServletRequestBuilder postResult = post("/addReport/{employeeId}", uriVariables);
        ReportsDTO reportsDTO = new ReportsDTO("Dr");
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
     * Method under test: {@link ReportsController#getReportByReportId(Long)}
     */
    @Test
    void testGetReportByReportId() throws Exception {
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
     * Method under test: {@link ReportsController#notifyHr(Long)}
     */
    @Test
    void testNotifyHr() throws Exception {
        doNothing().when(iReportsService).notifyHR(Mockito.<Long>any());
        MockHttpServletRequestBuilder postResult = post("/notifyHr/{reportId}", 1L);
        MockHttpServletRequestBuilder requestBuilder = postResult.param("reportId", String.valueOf(1L));
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
        when(iReportsService.totalAmount(Mockito.<Long>any())).thenReturn(10.0f);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/getTotalAmountInrByReportId");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("reportId", String.valueOf(1L));
        MockMvcBuilders.standaloneSetup(reportsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("10.0"));
    }



}

