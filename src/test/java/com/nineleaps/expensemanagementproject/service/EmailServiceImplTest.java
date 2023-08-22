package com.nineleaps.expensemanagementproject.service;


import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailServiceTest {

    @Mock
    private IReportsService reportsService;

    @Mock
    private IExpenseService expenseService;

    @Mock
    private IPdfManagerGeneratorService pdfGeneratorService;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testManagerNotification_ExpenseListEmpty() throws IOException, MessagingException {
//        // Arrange
//        Long reportId = 1L;
//        List<Long> expenseIds = Arrays.asList(1L, 2L);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        Reports report = createReport(reportId);
//        when(reportsService.getReportById(reportId)).thenReturn(report);
//        when(expenseService.getExpenseByReportId(reportId)).thenReturn(Arrays.asList());
//
//        // Act and Assert
//        assertThrows(IllegalStateException.class, () -> {
//            emailService.managerNotification(reportId, expenseIds, response);
//        });
//
//        // Verify that the methods are called
//        verify(reportsService).getReportById(reportId);
//        verify(expenseService).getExpenseByReportId(reportId);
//        verifyNoMoreInteractions(reportsService, expenseService, javaMailSender);
//    }
//
//    @Test
//    public void testManagerNotification_ExpenseListNotEmpty() throws IOException, MessagingException {
//        // Arrange
//        Long reportId = 1L;
//        List<Long> expenseIds = Arrays.asList(1L, 2L);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        Reports report = createReport(reportId);
//        Expense expense = createExpense(1L, report);
//        List<Expense> expenseList = Arrays.asList(expense);
//
//        when(reportsService.getReportById(reportId)).thenReturn(report);
//        when(expenseService.getExpenseByReportId(reportId)).thenReturn(expenseList);
//        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
//        when(pdfGeneratorService.export(reportId, expenseIds, response)).thenReturn(new byte[0]);
//
//        // Act
//        emailService.managerNotification(reportId, expenseIds, response);
//
//        // Verify that the methods are called
//        verify(reportsService).getReportById(reportId);
//        verify(expenseService).getExpenseByReportId(reportId);
//        verify(javaMailSender).createMimeMessage();
//        verify(pdfGeneratorService).export(reportId, expenseIds, response);
//        verify(javaMailSender).send(any(MimeMessage.class));
//    }
//
//    private Reports createReport(Long reportId) {
//        Reports report = new Reports();
//        report.setReportId(reportId);
//        report.setReportTitle("Expense Report");
//        report.setDateSubmitted(LocalDate.parse("2023-07-19"));
//        report.setTotalAmountINR(1000.0f);
//        return report;
//    }
//
//    private Expense createExpense(Long expenseId, Reports report) {
//        Expense expense = new Expense();
//        expense.setExpenseId(expenseId);
//        expense.setReports(report);
//        return expense;
//    }
}
