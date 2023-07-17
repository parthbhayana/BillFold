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
