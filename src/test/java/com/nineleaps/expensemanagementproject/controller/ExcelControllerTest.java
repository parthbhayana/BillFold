package com.nineleaps.expensemanagementproject.controller;

import com.nineleaps.expensemanagementproject.service.ExcelGeneratorCategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ExcelControllerTest {

    @Mock
    private ExcelGeneratorCategoryServiceImpl excelService;

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private HttpServletResponse response;

    @Mock
    private ExcelController excelController;

//    @Test
//    public void testGenerateExcelReport_Success() throws Exception {
//        // Mocking the service method
//        Mockito.when(excelService.generateExcelAndSendEmail(Mockito.any(), Mockito.any(), Mockito.any()))
//                .thenReturn("Email sent successfully!");
//
//        // Perform the request
//        LocalDate startDate = LocalDate.of(2022, 1, 1);
//        LocalDate endDate = LocalDate.of(2022, 1, 31);
//        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
//                        .get("/excel/categoryBreakup")
//                        .param("startDate", startDate.toString())
//                        .param("endDate", endDate.toString()))
//                .andReturn().getResponse();
//
//        // Verify the response
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
//        assertEquals("Email sent successfully!", response.getContentAsString());
//    }
//
//    @Test
//    public void testGenerateExcelReport_NoDataAvailable() throws Exception {
//        // Mocking the service method
//        Mockito.when(excelService.generateExcelAndSendEmail(Mockito.any(), Mockito.any(), Mockito.any()))
//                .thenReturn("No data available for the selected period.So, Email can't be sent!");
//
//        // Perform the request
//        LocalDate startDate = LocalDate.of(2022, 1, 1);
//        LocalDate endDate = LocalDate.of(2022, 1, 31);
//        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
//                        .get("/excel/categoryBreakup")
//                        .param("startDate", startDate.toString())
//                        .param("endDate", endDate.toString()))
//                .andReturn().getResponse();
//
//        // Verify the response
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
//        assertEquals("No data available for the selected period.So, Email can't be sent!", response.getContentAsString());
//    }
//
//    @Test
//    public void testGenerateExcelReport_BadRequest() throws Exception {
//        // Mocking the service method
//        Mockito.when(excelService.generateExcelAndSendEmail(Mockito.any(), Mockito.any(), Mockito.any()))
//                .thenReturn("Email not sent");
//
//        // Perform the request
//        LocalDate startDate = LocalDate.of(2022, 1, 1);
//        LocalDate endDate = LocalDate.of(2022, 1, 31);
//        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
//                        .get("/excel/categoryBreakup")
//                        .param("startDate", startDate.toString())
//                        .param("endDate", endDate.toString()))
//                .andReturn().getResponse();
//
//        // Verify the response
//        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
//        assertEquals("Email not sent", response.getContentAsString());
//    }
}
