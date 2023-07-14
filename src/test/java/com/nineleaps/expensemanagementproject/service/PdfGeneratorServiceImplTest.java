package com.nineleaps.expensemanagementproject.service;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;


 class PdfGeneratorServiceImplTest {

    private PdfGeneratorServiceImpl pdfGeneratorService;




     @BeforeEach
    void setUp() {
        ExpenseRepository expenseRepository = Mockito.mock(ExpenseRepository.class);
        ReportsRepository reportsRepository = Mockito.mock(ReportsRepository.class);
        EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
        IExpenseService expenseService = Mockito.mock(IExpenseService.class);
        pdfGeneratorService = new PdfGeneratorServiceImpl();
        pdfGeneratorService.expenseRepository = expenseRepository;
        pdfGeneratorService.reportsRepository = reportsRepository;
        pdfGeneratorService.employeeRepository = employeeRepository;
        pdfGeneratorService.expenseService = expenseService;
        pdfGeneratorService.mailSender = Mockito.mock(JavaMailSender.class);
    }


//    @Test
//    void testGeneratePdf() throws IOException {
//        // Mock data
//        Long reportId = 1L;
//        List<Long> expenseIds = Arrays.asList(1L, 2L, 3L);
//
//        // Mock repositories
//        Reports report = new Reports();
//        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
//
//        Expense expense1 = new Expense();
//        expense1.setExpenseId(1L);
//        expense1.setDate(LocalDate.now());
//        expense1.setMerchantName("Merchant 1");
//        expense1.setDescription("Expense 1");
//        expense1.setAmount((long) 100.0);
//        expense1.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
//
//        Expense expense2 = new Expense();
//        expense2.setExpenseId(2L);
//        expense2.setDate(LocalDate.now());
//        expense2.setMerchantName("Merchant 2");
//        expense2.setDescription("Expense 2");
//        expense2.setAmount((long) 200.0);
//        expense2.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
//
//        Expense expense3 = new Expense();
//        expense3.setExpenseId(3L);
//        expense3.setDate(LocalDate.now());
//        expense3.setMerchantName("Merchant 3");
//        expense3.setDescription("Expense 3");
//        expense3.setAmount((long) 300.0);
//        expense3.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
//
//        List<Expense> expenses = Arrays.asList(expense1, expense2, expense3);
//        when(expenseRepository.findByReports(report)).thenReturn(expenses);
//
//        Employee employee = new Employee();
//        employee.setFirstName("John");
//        employee.setLastName("Doe");
//        employee.setEmployeeEmail("john.doe@example.com");
//        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
//
//        // Perform the test
//        byte[] pdfBytes = pdfGeneratorService.generatePdf(reportId, expenseIds);
//
//        // Verify the results
//        assertNotNull(pdfBytes);
//        // Add more assertions as needed
//    }

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
//        // Mock data
//        Long reportId = 1L;
//        List<Long> expenseIds = Arrays.asList(1L, 2L, 3L);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        ServletOutputStream outputStream = mock(ServletOutputStream.class);
//        when(response.getOutputStream()).thenReturn(outputStream);
//
//        // Mock generatePdf method
//        byte[] pdfBytes = new byte[10];
//        when(pdfGeneratorService.generatePdf(reportId, expenseIds)).thenReturn(pdfBytes);
//
//        // Perform the test
//        byte[] result = pdfGeneratorService.export(reportId, expenseIds, response);
//
//        // Verify the results
//        assertNotNull(result);
//        assertEquals(pdfBytes.length, result.length);
//        verify(response).setContentType("application/pdf");
//
//        // Verify the filename header
//        ArgumentCaptor<String> headerKeyCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> headerValueCaptor = ArgumentCaptor.forClass(String.class);
//        verify(response).setHeader(headerKeyCaptor.capture(), headerValueCaptor.capture());
//        assertEquals("Content-Disposition", headerKeyCaptor.getValue());
//        assertTrue(headerValueCaptor.getValue().startsWith("attachment; filename=pdf_"));
//        assertTrue(headerValueCaptor.getValue().endsWith(".pdf"));
//
//        verify(response).setContentLength(pdfBytes.length);
//        verify(outputStream).write(pdfBytes);
//        verify(outputStream).flush();
//        verify(outputStream).close();
//        verify(pdfGeneratorService).generatePdf(reportId, expenseIds);
//        verify(response).setContentType("application/pdf");
//        verify(response).setContentLength(pdfBytes.length);
//        verify(outputStream).write(pdfBytes);
//        verify(outputStream).flush();
//        verify(outputStream).close();
//
//    }


}
