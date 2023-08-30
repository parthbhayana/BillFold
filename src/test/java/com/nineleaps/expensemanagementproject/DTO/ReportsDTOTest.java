package com.nineleaps.expensemanagementproject.DTO;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReportsDTOTest {

    @Test
    void testReportsDTOConstructor() {
        String reportTitle = "Test Report";
        String reportDescription = "This is a test report";

        ReportsDTO reportsDTO = new ReportsDTO(reportTitle);

        assertEquals(reportTitle, reportsDTO.getReportTitle());
    }

    @Test
    void testGetSetReportTitle() {
        ReportsDTO reportsDTO = new ReportsDTO();

        assertNull(reportsDTO.getReportTitle());

        String reportTitle = "Test Report";
        reportsDTO.setReportTitle(reportTitle);

        assertEquals(reportTitle, reportsDTO.getReportTitle());
    }
}
