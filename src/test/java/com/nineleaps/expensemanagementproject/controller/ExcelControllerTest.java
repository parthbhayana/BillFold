package com.nineleaps.expensemanagementproject.controller;

import java.time.LocalDate;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;

import com.nineleaps.expensemanagementproject.entity.StatusExcel;
import com.nineleaps.expensemanagementproject.service.IExcelGeneratorCategoryService;
import com.nineleaps.expensemanagementproject.service.IExcelGeneratorReportsService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExcelControllerTest {

    @Mock
    private IExcelGeneratorCategoryService excelService;

    @Mock
    private IExcelGeneratorReportsService excelServiceReports;

    @InjectMocks
    private ExcelController excelController;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateExcelReport_CategoryBreakup_Success() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.parse("2022-01-01");
        LocalDate endDate = LocalDate.parse("2022-12-31");

        when(excelService.generateExcelAndSendEmail(any(HttpServletResponse.class), eq(startDate), eq(endDate)))
                .thenReturn("Email sent successfully!");

        // Act
        ResponseEntity<String> responseEntity = excelController.generateExcelReport(response, startDate, endDate);

        // Assert
        assertEquals(ResponseEntity.ok("Email sent successfully!"), responseEntity);
        verify(response).setContentType("application/octet-stream");
        verify(response).setHeader("Content-Disposition", "attachment;filename=Category wise Expense Analytics.xls");
        verify(response).flushBuffer();
    }

    @Test
    void generateExcelReport_CategoryBreakup_NoDataAvailable() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.parse("2022-01-01");
        LocalDate endDate = LocalDate.parse("2022-12-31");

        when(excelService.generateExcelAndSendEmail(any(HttpServletResponse.class), eq(startDate), eq(endDate)))
                .thenReturn("No data available for the selected period.So, Email can't be sent!");

        // Act
        ResponseEntity<String> responseEntity = excelController.generateExcelReport(response, startDate, endDate);

        // Assert
        assertEquals(ResponseEntity.ok("No data available for the selected period.So, Email can't be sent!"), responseEntity);
        verify(response).setContentType("application/octet-stream");
        verify(response).setHeader("Content-Disposition", "attachment;filename=Category wise Expense Analytics.xls");
        assertTrue(responseEntity.getHeaders().isEmpty());
    }

    @Test
    void generateExcelReport_CategoryBreakup_Error() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.parse("2022-01-01");
        LocalDate endDate = LocalDate.parse("2022-12-31");

        when(excelService.generateExcelAndSendEmail(any(HttpServletResponse.class), eq(startDate), eq(endDate)))
                .thenReturn("Some error occurred.");

        // Act
        ResponseEntity<String> responseEntity = excelController.generateExcelReport(response, startDate, endDate);

        // Assert
        assertEquals(ResponseEntity.badRequest().body("Some error occurred."), responseEntity);
        verify(response).setContentType("application/octet-stream");
        verify(response).setHeader("Content-Disposition", "attachment;filename=Category wise Expense Analytics.xls");
        assertTrue(responseEntity.getHeaders().isEmpty());
    }

    @Test
    void generateExcelReport_AllReports_Success() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.parse("2022-01-01");
        LocalDate endDate = LocalDate.parse("2022-12-31");
        StatusExcel status = StatusExcel.COMPLETED;

        when(excelServiceReports.generateExcelAndSendEmail(any(HttpServletResponse.class), eq(startDate), eq(endDate), eq(status)))
                .thenReturn("Email sent successfully!");

        // Act
        ResponseEntity<String> responseEntity = excelController.generateExcelReport(response, startDate, endDate, status);

        // Assert
        assertEquals(ResponseEntity.ok("Email sent successfully!"), responseEntity);
        verify(response).setContentType("application/vnd.ms-excel");
        verify(response).setHeader("Content-Disposition", "attachment; filename=Billfold_All_Submissions_Status.xls");
        verify(response).flushBuffer();
    }

    @Test
    void generateExcelReport_AllReports_NoDataAvailable() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.parse("2022-01-01");
        LocalDate endDate = LocalDate.parse("2022-12-31");
        StatusExcel status = StatusExcel.COMPLETED;

        when(excelServiceReports.generateExcelAndSendEmail(any(HttpServletResponse.class), eq(startDate), eq(endDate), eq(status)))
                .thenReturn("No data available for the selected period.So, Email can't be sent!");

        // Act
        ResponseEntity<String> responseEntity = excelController.generateExcelReport(response, startDate, endDate, status);

        // Assert
        assertEquals(ResponseEntity.ok("No data available for the selected period.So, Email can't be sent!"), responseEntity);
        verify(response).setContentType("application/vnd.ms-excel");
        verify(response).setHeader("Content-Disposition", "attachment; filename=Billfold_All_Submissions_Status.xls");
        assertTrue(responseEntity.getHeaders().isEmpty());
    }

    @Test
    void generateExcelReport_AllReports_Error() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.parse("2022-01-01");
        LocalDate endDate = LocalDate.parse("2022-12-31");
        StatusExcel status = StatusExcel.COMPLETED;

        when(excelServiceReports.generateExcelAndSendEmail(any(HttpServletResponse.class), eq(startDate), eq(endDate), eq(status)))
                .thenReturn("Some error occurred.");

        // Act
        ResponseEntity<String> responseEntity = excelController.generateExcelReport(response, startDate, endDate, status);

        // Assert
        assertEquals(ResponseEntity.badRequest().body("Some error occurred."), responseEntity);
        verify(response).setContentType("application/vnd.ms-excel");
        verify(response).setHeader("Content-Disposition", "attachment; filename=Billfold_All_Submissions_Status.xls");
        assertTrue(responseEntity.getHeaders().isEmpty());
    }
}
