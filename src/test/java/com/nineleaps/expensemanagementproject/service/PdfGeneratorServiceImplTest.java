package com.nineleaps.expensemanagementproject.service;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PdfGeneratorServiceImplTest {

    private PdfManagerGeneratorServiceImpl pdfGeneratorService;


    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ReportsRepository reportsRepository;

    @Mock
    private EmployeeRepository employeeRepository;




    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ExpenseRepository expenseRepository = mock(ExpenseRepository.class);
        ReportsRepository reportsRepository = mock(ReportsRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        IExpenseService expenseService = mock(IExpenseService.class);
        pdfGeneratorService = new PdfManagerGeneratorServiceImpl();
        pdfGeneratorService.expenseRepository = expenseRepository;
        pdfGeneratorService.reportsRepository = reportsRepository;
        pdfGeneratorService.employeeRepository = employeeRepository;
        pdfGeneratorService.expenseService = expenseService;
        pdfGeneratorService.mailSender = mock(JavaMailSender.class);
    }



     @Test
    void testGetCenterAlignedCell() {
        // Mock data
        String content = "Test Content";
        Font font = new Font();

        // Perform the test
        PdfPCell cell = pdfGeneratorService.getCenterAlignedCell(content, font);

        // Verify the results
        assertNotNull(cell);
        assertEquals(content, cell.getPhrase().getContent());
        assertEquals(Element.ALIGN_CENTER, cell.getHorizontalAlignment());
        assertEquals(Element.ALIGN_MIDDLE, cell.getVerticalAlignment());
        assertEquals(new Color(240, 240, 240), cell.getBackgroundColor());
        // Add more assertions as needed
    }

    @Test
    void testGetCenterAlignedCells() {
        // Mock data
        String content = "Test Content";
        Font font = new Font();

        // Perform the test
        PdfPCell cell = pdfGeneratorService.getCenterAlignedCells(content, font);

        // Verify the results
        assertNotNull(cell);
        assertEquals(content, cell.getPhrase().getContent());
        assertEquals(Element.ALIGN_CENTER, cell.getHorizontalAlignment());
        assertEquals(Element.ALIGN_MIDDLE, cell.getVerticalAlignment());
        // Add more assertions as needed
    }

//    @Test
//    void testExport() throws IOException {
//        // Test case inputs
//        Long reportId = 1L;
//        List<Long> expenseIds = Collections.singletonList(1L);
//
//        // Mock the behavior of the expenseRepository
//        Expense expense = new Expense();
//        expense.setDate(LocalDate.of(2023, 1, 10));
//        expense.setMerchantName("Test Merchant");
//        expense.setDescription("Test Description");
//        expense.setAmount((long) 100.0f);
//        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
//        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
//
//        // Mock the behavior of the reportsRepository
//        Reports report = new Reports();
//        report.setCurrency("USD");
//        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
//
//        // Mock the behavior of the employeeRepository
//        Employee employee = new Employee();
//        employee.setFirstName("John");
//        employee.setLastName("Doe");
//        employee.setEmployeeEmail("john.doe@example.com");
//        employee.setOfficialEmployeeId("EMP001");
//        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
//
//        // Create a mock HttpServletResponse
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        // Call the export method
//        pdfGeneratorService.export(reportId, expenseIds, response);
//
//        // Perform assertions on the HttpServletResponse
//        Assertions.assertEquals("application/pdf", response.getContentType());
//        Assertions.assertNotNull(response.getHeader("Content-Disposition"));
//        // ...
//
//        // Verify that the expenseRepository.findById method was called for each expenseId
//        verify(expenseRepository, times(expenseIds.size())).findById(anyLong());
//
//        // Verify that the reportsRepository.findById method was called once
//        verify(reportsRepository, times(1)).findById(reportId);
//
//        // Verify that the employeeRepository.findById method was called once
//        verify(employeeRepository, times(1)).findById(anyLong());
//    }
}






