package com.nineleaps.expensemanagementproject.service;


import com.nineleaps.expensemanagementproject.repository.CategoryRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcelGeneratorCategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseRepository expenseRepository;


    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private ExcelGeneratorCategoryServiceImpl excelGeneratorCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGenerateExcelAndSendEmail() throws Exception {
//        // Arrange
//        LocalDate startDate = LocalDate.of(2023, 1, 1);
//        LocalDate endDate = LocalDate.of(2023, 1, 31);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        Employee financeAdmin = new Employee();
//        financeAdmin.setEmployeeEmail("finance@example.com");
//        when(employeeRepository.findByRole("FINANCE_ADMIN")).thenReturn(financeAdmin);
//        when(categoryRepository.findAll()).thenReturn(getMockCategories());
//        when(expenseRepository.findByDateBetween(startDate, endDate)).thenReturn(getMockExpenses());
//        when(mailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
//        doNothing().when(mailSender).send(any(MimeMessage.class));
//
//        // Act
//        String result = excelGeneratorCategoryService.generateExcelAndSendEmail(response, startDate, endDate);
//
//        // Assert
//        assertEquals("Email sent successfully!", result);
//        verify(employeeRepository).findByRole("FINANCE_ADMIN");
//        verify(categoryRepository).findAll();
//        verify(expenseRepository).findByDateBetween(startDate, endDate);
//        verify(mailSender).createMimeMessage();
//        verify(mailSender).send(any(MimeMessage.class));
//    }




    @Test
    void testGenerateExcelAndSendEmail_NoDataAvailable() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
        when(expenseRepository.findByDateBetween(startDate, endDate)).thenReturn(new ArrayList<>());

        // Act
        String result = excelGeneratorCategoryService.generateExcelAndSendEmail(response, startDate, endDate);

        // Assert
        assertEquals("No data available for the selected period.So, Email can't be sent!", result);
        verify(categoryRepository).findAll();
        verify(expenseRepository).findByDateBetween(startDate, endDate);
        verifyNoInteractions(mailSender);
    }

//    @Test
//    void testGenerateExcelAndSendEmail_FinanceAdminNotFound() throws Exception {
//        // Arrange
//        LocalDate startDate = LocalDate.of(2023, 1, 1);
//        LocalDate endDate = LocalDate.of(2023, 1, 31);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        when(employeeRepository.findByRole("FINANCE_ADMIN")).thenReturn(null);
//
//        // Act & Assert
//        assertThrows(IllegalStateException.class, () ->
//                excelGeneratorCategoryService.generateExcelAndSendEmail(response, startDate, endDate));
//        verify(employeeRepository).findByRole("FINANCE_ADMIN");
//        verifyNoInteractions(categoryRepository, expenseRepository, mailSender);
//    }

//    @Test
//    void testGenerateExcel() throws Exception {
//        // Arrange
//        LocalDate startDate = LocalDate.of(2023, 1, 1);
//        LocalDate endDate = LocalDate.of(2023, 1, 31);
//        ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
//        List<Category> mockCategories = getMockCategories();
//        List<Expense> mockExpenses = getMockExpenses();
//        when(categoryRepository.findAll()).thenReturn(mockCategories);
//        when(expenseRepository.findByDateBetween(startDate, endDate)).thenReturn(mockExpenses);
//        when(excelGeneratorCategoryService.CategoryTotalAmount(startDate, endDate)).thenCallRealMethod();
//
//        // Act
//        excelGeneratorCategoryService.generateExcel(excelStream, startDate, endDate);
//
//        // Assert
//        byte[] excelBytes = excelStream.toByteArray();
//        assertNotNull(excelBytes);
//        assertTrue(excelBytes.length > 0);
//        verify(categoryRepository).findAll();
//        verify(expenseRepository).findByDateBetween(startDate, endDate);
//    }
//
//    // Helper method to create a list of mock categories
//
//
//    // Helper method to create a mock category amount map
//    private HashMap<String, Float> getMockCategoryAmountMap() {
//        HashMap<String, Float> categoryAmountMap = new HashMap<>();
//        categoryAmountMap.put("Category 1", 50.0f);
//        categoryAmountMap.put("Category 2", 30.0f);
//        return categoryAmountMap;
//    }



//    @Test
//    void testLoadChartImage() throws Exception {
//        // Arrange
//        JFreeChart chart = mock(JFreeChart.class);
//        int width = 640;
//        int height = 480;
//        HSSFWorkbook workbook = mock(HSSFWorkbook.class);
//        ByteArrayOutputStream chartOut = new ByteArrayOutputStream();
//        when(workbook.addPicture(any(byte[].class), eq(Workbook.PICTURE_TYPE_PNG))).thenReturn(1);
//        doAnswer(invocation -> {
//            byte[] chartBytes = invocation.getArgument(1);
//            chartOut.write(chartBytes);
//            return null;
//        }).when(chart).write(any(ByteArrayOutputStream.class), eq(width), eq(height));
//
//        // Act
//        int pictureIndex = excelGeneratorCategoryService.loadChartImage(chart, width, height, workbook);
//
//        // Assert
//        assertEquals(1, pictureIndex);
//        byte[] chartBytes = chartOut.toByteArray();
//        assertNotNull(chartBytes);
//        assertTrue(chartBytes.length > 0);
//        verify(workbook).addPicture(any(byte[].class), eq(Workbook.PICTURE_TYPE_PNG));
//        verify(chart).write(any(ByteArrayOutputStream.class), eq(width), eq(height));
//    }

//    @Test
//    void testCategoryTotalAmount() {
//        // Arrange
//        LocalDate startDate = LocalDate.of(2023, 1, 1);
//        LocalDate endDate = LocalDate.of(2023, 1, 31);
//        List<Expense> expenses = getMockExpenses();
//        when(expenseRepository.findByDateBetween(startDate, endDate)).thenReturn(expenses);
//
//        // Act
//        HashMap<String, Float> result = excelGeneratorCategoryService.CategoryTotalAmount(startDate, endDate);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertEquals(50.0f, result.get("Category 1"));
//        assertEquals(30.0f, result.get("Category 2"));
//        verify(expenseRepository).findByDateBetween(startDate, endDate);
//    }

    @Test
    void testSendEmailWithAttachment_Success() {
        // Arrange
        String toEmail = "finance@example.com";
        String subject = "BillFold:Excel Report";
        String body = "Please find the attached Excel report.";
        byte[] attachmentContent = new byte[]{1, 2, 3};
        String attachmentFilename = "report.xls";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        boolean result = excelGeneratorCategoryService.sendEmailWithAttachment(
                toEmail, subject, body, attachmentContent, attachmentFilename);

        // Assert
        assertTrue(result);
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
    }

}
