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





}
