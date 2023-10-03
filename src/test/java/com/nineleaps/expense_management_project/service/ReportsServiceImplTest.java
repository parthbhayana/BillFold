package com.nineleaps.expense_management_project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import com.nineleaps.expense_management_project.dto.ReportsDTO;
import com.nineleaps.expense_management_project.entity.*;
import com.nineleaps.expense_management_project.exception.EmployeeNotFoundException;
import com.nineleaps.expense_management_project.firebase.PushNotificationRequest;
import com.nineleaps.expense_management_project.firebase.PushNotificationService;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.mail.MessagingException;

@ExtendWith(MockitoExtension.class)
public class ReportsServiceImplTest {
@InjectMocks
    private ReportsServiceImpl reportServiceImpl;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ReportsRepository reportsRepository;

    @Mock
    private  ExpenseServiceImpl expenseServices;
    @Mock
    private EmployeeServiceImpl employeeServices;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private PushNotificationService pushNotificationService;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
//        employeeRepository = mock(EmployeeRepository.class);
//        expenseRepository = mock(ExpenseRepository.class);
//
//        reportServiceImpl = new ReportsServiceImpl(reportsRepository, expenseRepository, employeeRepository, expenseServices, employeeServices, emailService, pushNotificationService);
    }

    @Test
    public void testGetAllReportsWhenEmployeeExists() {
        Long employeeId = 1L;

        // Mock the behavior when employee exists
        Employee employee = new Employee();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // Mock expenses and reports
        Expense expense1 = new Expense();
        expense1.setExpenseId(1L);
        Reports reports1 = new Reports();
        reports1.setReportId(101L);
        expense1.setReports(reports1);

        Expense expense2 = new Expense();
        expense2.setExpenseId(2L);

        List<Expense> expenses = List.of(expense1, expense2);

        // Mock the behavior when expenses are found
        when(expenseRepository.findByEmployeeAndIsHidden(employee, false, Sort.by(Sort.Direction.DESC, "dateCreated")))
                .thenReturn(expenses);

        Set<Reports> result = reportServiceImpl.getAllReports(employeeId);

        // Assertions
        assertEquals(1, result.size());
        assertTrue(result.contains(reports1));
    }

    @Test
    public void testGetAllReportsWhenEmployeeDoesNotExist() {
        Long employeeId = 1L;

        // Mock the behavior when employee does not exist
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        Set<Reports> result = reportServiceImpl.getAllReports(employeeId);

        // Assertions
        assertTrue(result.isEmpty());
    }


//    @Test
//    public void testAddReport_InvalidEmployeeId_ThrowsEmployeeNotFoundException() {
//        // Arrange
//        ReportsDTO reportsDTO = new ReportsDTO(/* initialize with valid data */);
//        Long invalidEmployeeId = null;
//        List<Long> expenseIds = Arrays.asList(1L, 2L, 3L);
//
//        // Act and Assert
//        assertThrows(EmployeeNotFoundException.class, () -> {
//            reportServiceImpl.addReport(reportsDTO, invalidEmployeeId, expenseIds);
//        });
//    }




//    @Test
//    public void testValidateEmployeeId_ValidEmployeeId_NoExceptionThrown() {
//        // Arrange
//        Long validEmployeeId = 1L;
//
//        // Act and Assert
//        assertDoesNotThrow(() -> {
//            ReportsServiceImpl.validateEmployeeId(validEmployeeId);
//        });
//    }

//    @Test
//    public void testValidateEmployeeId_NullEmployeeId_ThrowsEmployeeNotFoundException() {
//        // Arrange
//        Long nullEmployeeId = null;
//
//        // Act and Assert
//        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
//            ReportsServiceImpl.validateEmployeeId(nullEmployeeId);
//        });
//
//        assertEquals("EmployeeId not found", exception.getMessage());
//    }

//
//    @Test
//    public void testGetEmployeeById_NullEmployeeId_ThrowsNullPointerException() {
//        // Arrange
//        Long nullEmployeeId = null;
//
//        // Act and Assert
//        assertThrows(NullPointerException.class, () -> {
//            ReportsServiceImpl.getEmployeeById(nullEmployeeId);
//        });
//    }

//    @Test
//    public void testUpdateExpenseReportTitles() {
//        // Arrange
//        String reportTitle = "New Report Title";
//        Reports newReport = new Reports();
//        newReport.setReportId(1L);
//        newReport.setReportTitle(reportTitle); // Set the report title
//
//        // Create a list of mock expenses
//        List<Expense> mockExpenses = new ArrayList<>();
//        mockExpenses.add(new Expense());
//        mockExpenses.add(new Expense());
//        mockExpenses.add(new Expense());
//
//        // Stub the behavior of expenseServices to return the list of mock expenses
//        when(expenseServices.getExpenseByReportId(newReport.getReportId())).thenReturn(mockExpenses);
//
//        // Act
//        reportServiceImpl.updateExpenseReportTitles(newReport);
//
//        // Assert
//        // Verify that expenseRepository.save(expense) is called for each expense in the list
//        for (Expense expense : mockExpenses) {
//            verify(expenseRepository).save(expense);
//            assertEquals(reportTitle, expense.getReportTitle()); // Check that the report title is updated
//        }
//    }
//
//    @Test
//    public void testSaveReport() {
//        // Arrange
//        Reports reportToSave = new Reports();
//        // Set other properties of reportToSave as needed
//
//        Reports savedReport = new Reports();
//        // Set properties of the savedReport that you expect after saving
//
//        // Stub the behavior of reportsRepository.save(report) to return savedReport
//        when(reportsRepository.save(reportToSave)).thenReturn(savedReport);
//
//        // Act
//        Reports result = reportServiceImpl.saveReport(reportToSave);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(savedReport, result);
//
//        // Verify that the reportsRepository.save method was called with the reportToSave object
//        verify(reportsRepository).save(reportToSave);
//    }
//
//    @Test
//    public void testCreateReport() {
//        // Arrange
//        ReportsDTO reportsDTO = new ReportsDTO();
//        reportsDTO.setReportTitle("Test Report");
//
//        Employee employee = new Employee();
//        employee.setEmployeeEmail("employee@example.com");
//        employee.setManagerEmail("manager@example.com");
//        employee.setFirstName("John");
//        employee.setLastName("Doe");
//        employee.setOfficialEmployeeId("EMP123");
//        employee.setEmployeeId(1L);
//
//        // Use a fixed date for testing
//        LocalDate fixedDate = LocalDate.of(2023, 9, 28);
//
//        // Act
//        Reports newReport = reportServiceImpl.createReport(reportsDTO, employee);
//
//        // Assert
//        assertNotNull(newReport);
//        assertEquals("Test Report", newReport.getReportTitle());
//        assertEquals("employee@example.com", newReport.getEmployeeMail());
//        assertEquals("manager@example.com", newReport.getManagerEmail());
//        assertEquals("John Doe", newReport.getEmployeeName());
//        assertEquals("EMP123", newReport.getOfficialEmployeeId());
//        assertEquals(fixedDate, newReport.getDateCreated());
//        assertEquals(1L, newReport.getEmployeeId());
//    }
//
//    @Test
//    public void testCalculateAndSetAmounts() {
//        // Arrange
//        Reports newReport = new Reports();
//        List<Long> expenseIds = Arrays.asList(1L, 2L, 3L);
//
//        // Create a list of mock Expense objects
//        List<Expense> mockExpenses = Arrays.asList(
//                new Expense(), // Create an empty Expense
//                new Expense(),
//                new Expense()
//        );
//
//        // Set the id and amount using setters
//        mockExpenses.get(0).setExpenseId(1L);
//        mockExpenses.get(0).setAmount(100.0);
//
//        mockExpenses.get(1).setExpenseId(2L);
//        mockExpenses.get(1).setAmount(150.0);
//
//        mockExpenses.get(2).setExpenseId(3L);
//        mockExpenses.get(2).setAmount(75.0);
//
//        // Stub the behavior of expenseRepository.findAllById to return mockExpenses
//        when(expenseRepository.findAllById(expenseIds)).thenReturn(mockExpenses);
//
//        // Act
//        reportServiceImpl.calculateAndSetAmounts(newReport, expenseIds);
//
//        // Assert
//        assertEquals(3L, newReport.getExpensesCount());
//        assertEquals(325.0f, newReport.getTotalAmount(), 0.01f); // Use delta for float comparison
//    }

    @Test
    public void testGetReportById_ReportFound_ReturnsReport() {
        // Arrange
        Long reportId = 1L;
        Reports expectedReport = new Reports(/* Initialize with valid data */);

        // Mock the behavior of reportsRepository.findById to return an Optional containing expectedReport
        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(expectedReport));

        // Act
        Reports actualReport = reportServiceImpl.getReportById(reportId);

        // Assert
        assertNotNull(actualReport);
        assertEquals(expectedReport, actualReport);
    }

    @Test
    public void testGetReportById_ReportNotFound_ThrowsException() {
        // Arrange
        Long reportId = 1L;

        // Mock the behavior of reportsRepository.findById to return an empty Optional
        when(reportsRepository.findById(reportId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            reportServiceImpl.getReportById(reportId);
        });
    }

    @Test
    public void testEditReport_Successful() {
        // Arrange
        Long reportId = 1L;
        String reportTitle = "Updated Report";
        String reportDescription = "Updated Description";
        List<Long> addExpenseIds = Arrays.asList(4L, 5L);
        List<Long> removeExpenseIds = Arrays.asList(2L, 3L);

        Reports report = new Reports(/* initialize with valid data */);
        Employee employee = new Employee(/* initialize with valid data */);

        // Mock the behavior of various methods and repositories
        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));



        when(reportsRepository.save(any(Reports.class))).thenReturn(report);

        // Mock the behavior of expenseServices.getExpenseById to return mock Expense objects
        when(expenseServices.getExpenseById(4L)).thenReturn(new Expense());
        when(expenseServices.getExpenseById(5L)).thenReturn(new Expense());

        // Mock the behavior of expenseServices.getExpenseById for removeExpenseIds
        when(expenseServices.getExpenseById(2L)).thenReturn(new Expense());
        when(expenseServices.getExpenseById(3L)).thenReturn(new Expense());

        // Mock other necessary method behaviors...

        // Act
        List<Reports> editedReports = reportServiceImpl.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);

        // Assert
        assertNotNull(editedReports);
        // Add more assertions based on the expected behavior of the method
    }

//    @Test
//    public void testValidateReportForEdit_ReportNotSubmitted_NoExceptionsThrown() {
//        // Arrange
//        Reports report = new Reports();
//        report.setIsSubmitted(false);
//        report.setManagerApprovalStatus(ManagerApprovalStatus.PARTIALLY_APPROVED);
//        report.setIsHidden(false);
//
//        // Act & Assert (no exceptions should be thrown)
//        reportServiceImpl.validateReportForEdit(report);
//    }
//
//    @Test
//    public void testValidateReportForEdit_ReportHidden_ThrowsNullPointerException() {
//        // Arrange
//        Reports report = new Reports();
//        report.setIsSubmitted(false);
//        report.setManagerApprovalStatus(ManagerApprovalStatus.PARTIALLY_APPROVED);
//        report.setIsHidden(true);
//
//        // Act & Assert (expecting a NullPointerException)
//        assertThrows(NullPointerException.class, () -> {
//            reportServiceImpl.validateReportForEdit(report);
//        });
//    }
//
//    @Test
//    public void testValidateReportForEdit_ReportSubmittedPendingApproval_ThrowsIllegalStateException() {
//        // Arrange
//        Reports report = new Reports();
//        report.setIsSubmitted(true);
//        report.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);
//        report.setIsHidden(false);
//
//        // Act & Assert (expecting an IllegalStateException)
//        assertThrows(IllegalStateException.class, () -> {
//            reportServiceImpl.validateReportForEdit(report);
//        });
//    }
//
//    @Test
//    public void testValidateReportForEdit_ReportSubmittedRejected_ThrowsIllegalStateException() {
//        // Arrange
//        Reports report = new Reports();
//        report.setIsSubmitted(true);
//        report.setManagerApprovalStatus(ManagerApprovalStatus.REJECTED);
//        report.setIsHidden(false);
//
//        // Act & Assert (expecting an IllegalStateException)
//        assertThrows(IllegalStateException.class, () -> {
//            reportServiceImpl.validateReportForEdit(report);
//        });
//    }

    @Test
    public void testAddExpenseToReport_HiddenReport_ThrowsNullPointerException() {
        // Arrange
        Long reportId = 1L;
        List<Long> expenseIds = Arrays.asList(1L, 2L);

        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsHidden(true);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act and Assert
        assertThrows(NullPointerException.class, () -> {
            reportServiceImpl.addExpenseToReport(reportId, expenseIds);
        });
    }

    @Test
    public void testAddExpenseToReport_ExpenseAlreadyReported_ThrowsIllegalStateException() {
        // Arrange
        Long reportId = 1L;
        Long expenseId = 1L;
        List<Long> expenseIds = Arrays.asList(expenseId);

        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsHidden(false);

        Expense expense = new Expense();
        expense.setIsReported(true); // Simulate an expense already reported

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(expenseServices.getExpenseById(expenseId)).thenReturn(expense);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> {
            reportServiceImpl.addExpenseToReport(reportId, expenseIds);
        });
    }

    @Test
    public void testGetAllSubmittedReports() {
        // Arrange
        List<Reports> allReports = new ArrayList<>();

        Reports submittedReport1 = new Reports();
        submittedReport1.setIsSubmitted(true);
        // Set other properties for submittedReport1

        Reports submittedReport2 = new Reports();
        submittedReport2.setIsSubmitted(true);
        // Set other properties for submittedReport2

        Reports draftReport = new Reports();
        draftReport.setIsSubmitted(false);
        // Set other properties for draftReport

        allReports.add(submittedReport1);
        allReports.add(submittedReport2);
        allReports.add(draftReport);

        when(reportsRepository.findAll()).thenReturn(allReports);

        // Act
        List<Reports> submittedReports = reportServiceImpl.getAllSubmittedReports();

        // Assert
        assertEquals(2, submittedReports.size()); // Expecting 2 submitted reports
        // Add more assertions based on the expected behavior of the method
    }

    @Test
    public void testTotalApprovedAmount() {
        // Arrange
        Long reportId = 1L;

        Reports report = new Reports();
        report.setReportId(reportId);
        // Set other properties for the report

        Expense approvedExpense1 = new Expense();
        approvedExpense1.setIsReported(true);
        approvedExpense1.setIsHidden(false);
        approvedExpense1.setAmountApproved(100.0);

        Expense approvedExpense2 = new Expense();
        approvedExpense2.setIsReported(true);
        approvedExpense2.setIsHidden(false);
        approvedExpense2.setAmountApproved(150.0);

        Expense unapprovedExpense = new Expense();
        unapprovedExpense.setIsReported(true);
        unapprovedExpense.setIsHidden(false);
        // This expense has no amount approved

        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(approvedExpense1);
        expenseList.add(approvedExpense2);
        expenseList.add(unapprovedExpense);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(expenseRepository.findExpenseByReportsAndIsReportedAndIsHidden(report, true, false))
                .thenReturn(expenseList);

        // Act
        float totalApprovedAmount = reportServiceImpl.totalApprovedAmount(reportId);

        // Assert
        assertEquals(250.0f, totalApprovedAmount, 0.01f); // Use delta for float comparison
    }

    @Test
    public void testHideReport() {
        // Arrange
        Long reportId = 1L;

        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsHidden(false);

        List<Expense> expenseList = new ArrayList<>();

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(expenseServices.getExpenseByReportId(reportId)).thenReturn(expenseList);

        // Act
        reportServiceImpl.hideReport(reportId);

        // Assert
        assertTrue(report.getIsHidden());

        for (Expense exp : expenseList) {
            assertFalse(exp.getIsReported());
            assertNull(exp.getReports());
            assertNull(exp.getReportTitle());
            assertNull(exp.getManagerApprovalStatusExpense());
            assertNull(exp.getAmountApproved());

            // Verify that save was called on expenseRepository for each expense
            verify(expenseRepository).save(exp);
        }

        // Verify that save was called on reportsRepository for the report
        verify(reportsRepository).save(report);
    }

//    @Test
//    public void testRejectReportByFinance() {
//        // Arrange
//        Long reportId = 1L;
//        String comments = "Finance rejected the report";
//
//        Reports report = new Reports();
//        report.setReportId(reportId);
//        report.setIsSubmitted(true);
//        report.setIsHidden(false);
//        report.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
//
//        List<Expense> expenseList = new ArrayList<>();
//        // Mock expense data...
//
//        Employee employee = new Employee();
//        employee.setEmployeeId(123L);
//        employee.setToken("employee_token");
//
//        // Mock the behavior of various methods and services
//        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
//        when(expenseServices.getExpenseByReportId(reportId)).thenReturn(expenseList);
//        when(employeeServices.getEmployeeById(anyLong())).thenReturn(employee);
//
//        // Act
//        reportServiceImpl.rejectReportByFinance(reportId, comments);
//
//        // Assert
//        assertEquals(FinanceApprovalStatus.REJECTED, report.getFinanceApprovalStatus());
//        assertEquals(comments, report.getFinanceComments());
//        assertNotNull(report.getFinanceActionDate());
//
//        verify(reportsRepository).save(report);
//
//        for (Expense exp : expenseList) {
//            assertEquals(FinanceApprovalStatus.REJECTED, exp.getFinanceApprovalStatus());
//            verify(expenseRepository).save(exp);
//        }
//
//        // Verify that emailService and pushNotificationService were called
//        verify(emailService).userRejectedByFinanceNotification(reportId);
//
//        PushNotificationRequest expectedNotification = new PushNotificationRequest();
//        expectedNotification.setTitle("[REJECTED]: " + report.getReportTitle());
//        expectedNotification.setMessage("Accounts admin rejected your expense report.");
//        expectedNotification.setToken(employee.getToken());
//        verify(pushNotificationService).sendPushNotificationToToken(expectedNotification);
//    }

    @Test
    public void testGetReportByEmpId_Drafts() {
        // Arrange
        Long employeeId = 1L;
        String request = "drafts";

        List<Reports> expectedReports = new ArrayList<>();
        // Mock expected reports...

        when(reportsRepository.getReportsByEmployeeIdAndIsSubmittedAndIsHidden(employeeId, false, false))
                .thenReturn(expectedReports);

        // Act
        List<Reports> resultReports = reportServiceImpl.getReportByEmpId(employeeId, request);

        // Assert
        assertEquals(expectedReports, resultReports);
    }

    @Test
    public void testGetReportByEmpId_Submitted() {
        // Arrange
        Long employeeId = 1L;
        String request = "submitted";

        List<Reports> expectedReports = new ArrayList<>();
        // Mock expected reports...

        when(reportsRepository.getReportsByEmployeeIdAndManagerApprovalStatusAndIsSubmittedAndIsHidden(
                employeeId, ManagerApprovalStatus.PENDING, true, false))
                .thenReturn(expectedReports);

        // Act
        List<Reports> resultReports = reportServiceImpl.getReportByEmpId(employeeId, request);

        // Assert
        assertEquals(expectedReports, resultReports);
    }

    @Test
    public void testGetReportByEmpId_Rejected() {
        // Arrange
        Long employeeId = 1L;
        String request = "rejected";

        List<Reports> expectedReports = new ArrayList<>();
        // Mock expected reports...

        when(reportsRepository.getReportsByEmployeeIdAndManagerApprovalStatusAndIsHidden(
                employeeId, ManagerApprovalStatus.REJECTED, false))
                .thenReturn(expectedReports);

        // Act
        List<Reports> resultReports = reportServiceImpl.getReportByEmpId(employeeId, request);

        // Assert
        assertEquals(expectedReports, resultReports);
    }


    @Test
    public void testGetReportsSubmittedToUser_Rejected() {
        // Arrange
        String managerEmail = "manager@example.com";
        String request = "rejected";

        List<Reports> expectedReports = new ArrayList<>();
        // Mock expected reports...

        when(reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsHidden(
                managerEmail, ManagerApprovalStatus.REJECTED, false))
                .thenReturn(expectedReports);

        // Act
        List<Reports> resultReports = reportServiceImpl.getReportsSubmittedToUser(managerEmail, request);

        // Assert
        assertEquals(expectedReports, resultReports);
    }

    @Test
    public void testGetReportsSubmittedToUser_Pending() {
        // Arrange
        String managerEmail = "manager@example.com";
        String request = "pending";

        List<Reports> expectedReports = new ArrayList<>();
        // Mock expected reports...

        when(reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(
                managerEmail, ManagerApprovalStatus.PENDING, true, false))
                .thenReturn(expectedReports);

        // Act
        List<Reports> resultReports = reportServiceImpl.getReportsSubmittedToUser(managerEmail, request);

        // Assert
        assertEquals(expectedReports, resultReports);
    }


    @Test
    public void testSubmitReport_AlreadySubmitted() throws MessagingException, IOException {
        // Arrange
        Long reportId = 1L;
        Reports report = new Reports(/* initialize with valid data */);
        report.setIsHidden(false);
        report.setIsSubmitted(true); // Report is already submitted
        report.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        report.setDateSubmitted(LocalDate.now());

        // Mock the required dependencies...

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> reportServiceImpl.submitReport(reportId, "manager@example.com", new MockHttpServletResponse()));
    }

    @Test
    public void testSubmitReport_HiddenReport() throws MessagingException, IOException {
        // Arrange
        Long reportId = 1L;
        Reports report = new Reports(/* initialize with valid data */);
        report.setIsHidden(true); // Hidden report

        // Mock the required dependencies...

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> reportServiceImpl.submitReport(reportId, "manager@example.com", new MockHttpServletResponse()));
    }


    @Test
    public void testReimburseReportByFinance_NotSubmittedReport() {
        // Arrange
        Long reportId = 1L;
        ArrayList<Long> reportIds = new ArrayList<>();
        reportIds.add(reportId);

        Reports report = new Reports(/* initialize with valid data */);
        report.setIsHidden(false);
        report.setIsSubmitted(false); // Report is not submitted

        // Mock the required dependencies...

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> reportServiceImpl.reimburseReportByFinance(reportIds, "Reimbursement comments"));
    }

    @Test
    public void testReimburseReportByFinance_NotApprovedByManager() {
        // Arrange
        Long reportId = 1L;
        ArrayList<Long> reportIds = new ArrayList<>();
        reportIds.add(reportId);

        Reports report = new Reports(/* initialize with valid data */);
        report.setIsHidden(false);
        report.setIsSubmitted(true);
        report.setManagerApprovalStatus(ManagerApprovalStatus.REJECTED); // Not approved by manager

        // Mock the required dependencies...

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> reportServiceImpl.reimburseReportByFinance(reportIds, "Reimbursement comments"));
    }



    @Test
    public void testRejectReportByFinance_HiddenReport() {
        // Arrange
        Long reportId = 1L;

        Reports report = new Reports(/* initialize with valid data */);
        report.setIsHidden(true); // Hidden report

        // Mock the required dependencies...

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> reportServiceImpl.rejectReportByFinance(reportId, "Rejection comments"));
    }

    @Test
    public void testRejectReportByFinance_NotSubmittedReport() {
        // Arrange
        Long reportId = 1L;

        Reports report = new Reports(/* initialize with valid data */);
        report.setIsHidden(false);
        report.setIsSubmitted(false); // Report is not submitted

        // Mock the required dependencies...

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> reportServiceImpl.rejectReportByFinance(reportId, "Rejection comments"));
    }

    @Test
    public void testGetReportsInDateRange_Approved() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String request = "approved";

        // Mock the repository to return a list of Reports when findByDateSubmittedBetweenAndFinanceApprovalStatus is called
        List<Reports> expectedReports = Arrays.asList(/* create some Reports objects */);
        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(startDate, endDate, FinanceApprovalStatus.APPROVED))
                .thenReturn(expectedReports);

        // Act
        List<Reports> result = reportServiceImpl.getReportsInDateRange(startDate, endDate, request);

        // Assert
        assertEquals(expectedReports, result);
    }

    @Test
    public void testGetReportsInDateRange_Pending() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String request = "pending";

        // Mock the repository to return a list of Reports when findByDateSubmittedBetweenAndFinanceApprovalStatus is called
        List<Reports> expectedReports = Arrays.asList(/* create some Reports objects */);
        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(startDate, endDate, FinanceApprovalStatus.PENDING))
                .thenReturn(expectedReports);

        // Act
        List<Reports> result = reportServiceImpl.getReportsInDateRange(startDate, endDate, request);

        // Assert
        assertEquals(expectedReports, result);
    }

// Repeat the same pattern for "reimbursed" and "rejected" cases

    @Test
    public void testGetReportsInDateRange_Default() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String request = "invalidRequest";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reportServiceImpl.getReportsInDateRange(startDate, endDate, request));
    }

    @Test
    public void testGetReportsSubmittedToUserInDateRange_Approved() {
        // Arrange
        String managerEmail = "manager@example.com";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String request = "approved";

        // Mock the repository to return a list of Reports when findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden is called
        List<Reports> expectedReports = Arrays.asList(/* create some Reports objects */);
        when(reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(managerEmail, startDate, endDate, ManagerApprovalStatus.APPROVED, true, false))
                .thenReturn(expectedReports);

        // Act
        List<Reports> result = reportServiceImpl.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request);

        // Assert
        assertEquals(expectedReports, result);
    }

    @Test
    public void testGetReportsSubmittedToUserInDateRange_Rejected() {
        // Arrange
        String managerEmail = "manager@example.com";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String request = "rejected";

        // Mock the repository to return a list of Reports when findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden is called
        List<Reports> expectedReports = Arrays.asList(/* create some Reports objects */);
        when(reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(managerEmail, startDate, endDate, ManagerApprovalStatus.REJECTED, true, false))
                .thenReturn(expectedReports);

        // Act
        List<Reports> result = reportServiceImpl.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request);

        // Assert
        assertEquals(expectedReports, result);
    }

// Repeat the same pattern for "pending" case

    @Test
    public void testGetReportsSubmittedToUserInDateRange_Default() {
        // Arrange
        String managerEmail = "manager@example.com";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String request = "invalidRequest";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reportServiceImpl.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request));
    }

    @Test
    public void testGetAmountOfReportsInDateRange_WithReports() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        List<Reports> reports = new ArrayList<>();
        Reports report1 = new Reports();
        report1.setTotalAmount(100.0f);
        Reports report2 = new Reports();
        report2.setTotalAmount(150.0f);
        reports.add(report1);
        reports.add(report2);

        when(reportsRepository.findByDateSubmittedBetween(startDate, endDate)).thenReturn(reports);

        // Act
        String result = reportServiceImpl.getAmountOfReportsInDateRange(startDate, endDate);

        // Assert
        assertEquals("250.0 INR", result);
    }

    @Test
    public void testGetAmountOfReportsInDateRange_NoReports() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        // Mock the repository to return an empty list when findByDateSubmittedBetween is called
        when(reportsRepository.findByDateSubmittedBetween(startDate, endDate)).thenReturn(Collections.emptyList());

        // Act
        String result = reportServiceImpl.getAmountOfReportsInDateRange(startDate, endDate);

        // Assert
        assertEquals("0.0 INR", result);
    }

    @Test
    public void testUpdateExpenseStatus_PartiallyApproved() throws MessagingException, IOException {
        // Arrange
        Long reportId = 1L;
        List<Long> approveExpenseIds = Collections.emptyList();
        List<Long> rejectExpenseIds = Collections.emptyList();
        Map<Long, Float> partiallyApprovedMap = new HashMap<>();
        partiallyApprovedMap.put(1L, 50.0f);
        partiallyApprovedMap.put(2L, 30.0f);

        Reports report = new Reports(/* initialize with valid data */);
        report.setIsHidden(false);
        report.setIsSubmitted(true);
        report.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);

        Expense expense1 = new Expense(/* initialize with valid data */);
        expense1.setIsReported(true);
        expense1.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
        expense1.setAmount(100.0);

        Expense expense2 = new Expense(/* initialize with valid data */);
        expense2.setIsReported(true);
        expense2.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
        expense2.setAmount(200.0);

        // Mock the required dependencies...

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(expenseServices.getExpenseById(1L)).thenReturn(expense1);
        when(expenseServices.getExpenseById(2L)).thenReturn(expense2);

        // Act
        reportServiceImpl.updateExpenseStatus(reportId, approveExpenseIds, rejectExpenseIds, partiallyApprovedMap, "Review", "Comments", null);

        // Assert
        assertEquals(ManagerApprovalStatus.PARTIALLY_APPROVED, report.getManagerApprovalStatus());
        // Add more assertions based on the expected behavior of the method
    }

    @Test
    public void testUpdateExpenseStatus_MixedActions() throws MessagingException, IOException {
        // Arrange
        Long reportId = 1L;
        List<Long> approveExpenseIds = Arrays.asList(1L);
        List<Long> rejectExpenseIds = Arrays.asList(2L);
        Map<Long, Float> partiallyApprovedMap = new HashMap<>();
        partiallyApprovedMap.put(3L, 70.0f);

        Reports report = new Reports(/* initialize with valid data */);
        report.setIsHidden(false);
        report.setIsSubmitted(true);
        report.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);

        Expense expense1 = new Expense(/* initialize with valid data */);
        expense1.setIsReported(true);
        expense1.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
        expense1.setAmount(100.0);

        Expense expense2 = new Expense(/* initialize with valid data */);
        expense2.setIsReported(true);
        expense2.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
        expense2.setAmount(200.0);

        Expense expense3 = new Expense(/* initialize with valid data */);
        expense3.setIsReported(true);
        expense3.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
        expense3.setAmount(300.0);

        // Mock the required dependencies...

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(expenseServices.getExpenseById(1L)).thenReturn(expense1);
        when(expenseServices.getExpenseById(2L)).thenReturn(expense2);
        when(expenseServices.getExpenseById(3L)).thenReturn(expense3);

        // Act
        reportServiceImpl.updateExpenseStatus(reportId, approveExpenseIds, rejectExpenseIds, partiallyApprovedMap, "Review", "Comments", null);

        // Assert
        assertEquals(ManagerApprovalStatus.REJECTED, report.getManagerApprovalStatus());
        // Add more assertions based on the expected behavior of the method
    }

}




