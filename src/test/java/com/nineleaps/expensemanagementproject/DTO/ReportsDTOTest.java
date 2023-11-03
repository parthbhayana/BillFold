package com.nineleaps.expensemanagementproject.DTO;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReportsDTOTest {

    @Test
    void testDefaultConstructor() {
        ReportsDTO reportsDTO = new ReportsDTO();

        // Verify that the default constructor initializes fields to their default values
        assertNull(reportsDTO.getReportTitle());
    }

    @Test
    void testParameterizedConstructor() {
        String reportTitle = "Expense Report";

        ReportsDTO reportsDTO = new ReportsDTO(reportTitle);

        // Verify that the parameterized constructor initializes the report title correctly
        assertEquals(reportTitle, reportsDTO.getReportTitle());
    }

    @Test
    void testGetterAndSetterMethods() {
        ReportsDTO reportsDTO = new ReportsDTO();

        // Set the report title using the setter method
        String reportTitle = "Updated Report Title";
        reportsDTO.setReportTitle(reportTitle);

        // Verify that the getter method returns the expected report title
        assertEquals(reportTitle, reportsDTO.getReportTitle());
    }
}