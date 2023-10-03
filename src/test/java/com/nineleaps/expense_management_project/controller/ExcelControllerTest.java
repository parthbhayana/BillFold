package com.nineleaps.expense_management_project.controller;

import com.nineleaps.expense_management_project.entity.StatusExcel;

import com.nineleaps.expense_management_project.service.IExcelGeneratorCategoryService;
import com.nineleaps.expense_management_project.service.IExcelGeneratorReportsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcelControllerTest {

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




}