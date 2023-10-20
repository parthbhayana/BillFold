package com.nineleaps.expense_management_project.controller;

import com.nineleaps.expense_management_project.entity.StatusExcel;

import com.nineleaps.expense_management_project.service.IExcelGeneratorCategoryService;
import com.nineleaps.expense_management_project.service.IExcelGeneratorReportsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcelControllerTest {


    private static final String CONSTANT1 = "hello";
    private static final String  CONSTANT3="No data available for the selected period.So, Email can't be sent!";
    @InjectMocks
    private ExcelController excelController;

    @Mock
    private IExcelGeneratorCategoryService excelService;

    @Mock
    private IExcelGeneratorReportsService excelServiceReports;

    @Mock
    private HttpServletResponse response;

    private static final String SUCCESS_RESPONSE = "Email sent successfully!";
    private static final String NO_DATA_RESPONSE = "No data available for the selected period. So, Email can't be sent!";
    private static final String BAD_REQUEST_RESPONSE = "Bad Request";


    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGenerateExcelReportForCategoryBreakup_Success() throws Exception {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        when(excelService.generateExcelAndSendEmail(response, startDate, endDate)).thenReturn(SUCCESS_RESPONSE);

        ResponseEntity<String> responseEntity = excelController.generateExcelReport(response, startDate, endDate);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(SUCCESS_RESPONSE, responseEntity.getBody());
    }



    @Test
    void testGenerateExcelReportForCategoryBreakup_BadRequest() throws Exception {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        when(excelService.generateExcelAndSendEmail(response, startDate, endDate)).thenReturn(BAD_REQUEST_RESPONSE);

        ResponseEntity<String> responseEntity = excelController.generateExcelReport(response, startDate, endDate);

        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals(BAD_REQUEST_RESPONSE, responseEntity.getBody());
    }

    @Test
    void testGenerateExcelReportForAllReports_Success() throws Exception {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        StatusExcel status = StatusExcel.PENDING;

        when(excelServiceReports.generateExcelAndSendEmail(response, startDate, endDate, status)).thenReturn(SUCCESS_RESPONSE);

        ResponseEntity<String> responseEntity = excelController.generateExcelReport(response, startDate, endDate, status);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(SUCCESS_RESPONSE, responseEntity.getBody());
    }



    @Test
    void testGenerateExcelReportForAllReports_BadRequest() throws Exception {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        StatusExcel status = StatusExcel.PENDING;

        when(excelServiceReports.generateExcelAndSendEmail(response, startDate, endDate, status)).thenReturn(BAD_REQUEST_RESPONSE);

        ResponseEntity<String> responseEntity = excelController.generateExcelReport(response, startDate, endDate, status);

        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals(BAD_REQUEST_RESPONSE, responseEntity.getBody());
    }

    @Test
    void testReimburseAndGenerateExcel_Success() throws Exception {
        when(excelServiceReports.reimburseAndGenerateExcel(response)).thenReturn(SUCCESS_RESPONSE);

        ResponseEntity<String> responseEntity = excelController.reimburseAndGenerateExcel(response);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(SUCCESS_RESPONSE, responseEntity.getBody());
    }

    @Test
    void testReimburseAndGenerateExcelEmailSent() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(excelServiceReports.reimburseAndGenerateExcel(response)).thenReturn(CONSTANT1);

        ResponseEntity<String> result = excelController.reimburseAndGenerateExcel(response);

        verify(excelServiceReports, times(1)).reimburseAndGenerateExcel(response);
        assertEquals(result.getStatusCodeValue(), 400);
        assertEquals(result.getBody(), CONSTANT1);
    }

    @Test
    void testReimburseAndGenerateExcelNoData() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(excelServiceReports.reimburseAndGenerateExcel(response)).thenReturn(CONSTANT3);

        ResponseEntity<String> result = excelController.reimburseAndGenerateExcel(response);

        verify(excelServiceReports, times(1)).reimburseAndGenerateExcel(response);
        assertEquals(result.getStatusCodeValue(), 200);
        assertEquals(result.getBody(), CONSTANT3);
    }

    @Test
    void testReimburseAndGenerateExcelError() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        String errorMessage = "Error occurred";
        when(excelServiceReports.reimburseAndGenerateExcel(response)).thenReturn(errorMessage);

        ResponseEntity<String> result = excelController.reimburseAndGenerateExcel(response);

        verify(excelServiceReports, times(1)).reimburseAndGenerateExcel(response);
        assertEquals(result.getStatusCodeValue(), 400);
        assertEquals(result.getBody(), errorMessage);
    }

    @Test
    void testGenerateExcelReport_WhenResultEqualsConstant3() throws Exception {
        // Arrange
        when(excelService.generateExcelAndSendEmail(response, startDate, endDate)).thenReturn(CONSTANT3);

        // Act
        ResponseEntity<String> resultEntity = excelController.generateExcelReport(response, startDate, endDate);

        // Assert
        assertEquals(CONSTANT3, resultEntity.getBody());
        assertEquals(200, resultEntity.getStatusCodeValue());
    }

    @Test
    void testGenerateExcelReportWithConstant3() throws Exception {
        // Mock the behavior of excelServiceReports.generateExcelAndSendEmail to return CONSTANT3
        when(excelServiceReports.generateExcelAndSendEmail(any(), any(), any(), any())).thenReturn(CONSTANT3);

        // Create a mock HttpServletResponse
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Call the controller method
        ResponseEntity<String> responseEntity = excelController.generateExcelReport(response, LocalDate.now(), LocalDate.now(), StatusExcel.PENDING);

        // Assert that the response status code is 200
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // You can add more assertions if needed, e.g., to check the response content
    }




}