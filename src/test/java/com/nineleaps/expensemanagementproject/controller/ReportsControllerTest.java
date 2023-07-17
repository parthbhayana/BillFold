package com.nineleaps.expensemanagementproject.controller;

import com.nineleaps.expensemanagementproject.DTO.ReportsDTO;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.service.IReportsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReportsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IReportsService reportsService;

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




}
