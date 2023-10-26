package com.nineleaps.expense_management_project.service;


import com.nineleaps.expense_management_project.dto.ReportsDTO;
import com.nineleaps.expense_management_project.entity.*;
import com.nineleaps.expense_management_project.firebase.PushNotificationRequest;
import com.nineleaps.expense_management_project.firebase.PushNotificationService;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.junit.Assert;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockHttpServletResponse;
import javax.mail.MessagingException;
import static com.nineleaps.expense_management_project.service.ReportsServiceImpl.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

class ReportsServiceImplTest {

    @InjectMocks
    private ReportsServiceImpl reportsService;

    @Mock
    private ReportsRepository reportsRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeServiceImpl employeeService;

    @Mock
    private ExpenseServiceImpl expenseService;

    @Mock
    private PushNotificationService pushNotificationService;

    @Mock
    private EmailServiceImpl emailService;

    private static final Long REPORT_ID = 1L;
    private static final String CONSTANT3 = " constant3";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllReports() {
        // Create a sample employee and a list of expenses with associated reports
        Employee employee = new Employee();
        employee.setEmployeeId(1L);

        Expense expense1 = new Expense();
        expense1.setExpenseId(1L);
        Reports reports1 = new Reports();
        reports1.setReportId(100L);
        expense1.setReports(reports1);

        Expense expense2 = new Expense();
        expense2.setExpenseId(2L);
        Reports reports2 = new Reports();
        reports2.setReportId(200L);
        expense2.setReports(reports2);

        List<Expense> expenses = Arrays.asList(expense1, expense2);

        // Mock repository calls
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(expenseRepository.findByEmployeeAndIsHidden(employee, false, Sort.by(Sort.Direction.DESC, "dateCreated")))
                .thenReturn(expenses);

        Set<Reports> result = reportsService.getAllReports(1L);

        // Verify the result
        assert(result.size() == 2);
        assert(result.contains(reports1));
        assert(result.contains(reports2));
    }

    @Test
    void testGetReportById() {
        // Create a sample report
        Reports report = new Reports();
        report.setReportId(1L);

        // Mock repository call
        when(reportsRepository.findById(1L)).thenReturn(Optional.of(report));

        Reports result = reportsService.getReportById(1L);

        // Verify that the result matches the mocked report
        assertEquals(1L, result.getReportId());
    }

    @Test
    void testGetReportByIdReportNotFound() {
        // Mock repository call that returns an empty Optional
        when(reportsRepository.findById(1L)).thenReturn(Optional.empty());

        // Verify that a RuntimeException is thrown with the expected message
        Exception exception = assertThrows(RuntimeException.class, () -> {
            reportsService.getReportById(1L);
        });
        assertEquals("Report not found", exception.getMessage());
    }

    @Test
    void testAddReport() {
        Employee employee = new Employee();
        employee.setEmployeeEmail("jane.doe@example.org");
        employee.setEmployeeId(1L);
        employee.setExpenseList(new ArrayList<>());
        employee.setFirstName("Jane");
        employee.setHrEmail("jane.doe@example.org");
        employee.setHrName("Hr Name");
        employee.setImageUrl("https://example.org/example");
        employee.setIsFinanceAdmin(true);
        employee.setIsHidden(true);
        employee.setLastName("Doe");
        employee.setLndEmail("jane.doe@example.org");
        employee.setLndName("Lnd Name");
        employee.setManagerEmail("jane.doe@example.org");
        employee.setManagerName("Manager Name");
        employee.setMiddleName("Middle Name");
        employee.setMobileNumber(1L);
        employee.setOfficialEmployeeId("42");
        employee.setRole("Role");
        employee.setToken("ABC123");
        when(employeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(employee);
        when(reportsRepository.save(Mockito.<Reports>any())).thenThrow(new RuntimeException("Report not found"));
        ReportsDTO reportsDTO = new ReportsDTO("Dr");
        assertThrows(RuntimeException.class, () -> reportsService.addReport(reportsDTO, 1L, new ArrayList<>()));
        verify(employeeService).getEmployeeById(Mockito.<Long>any());
        verify(reportsRepository).save(Mockito.<Reports>any());
    }


    @Test
    void testAddReportEmployeeNotFound() {
        // Create a sample ReportsDTO
        ReportsDTO reportsDTO = new ReportsDTO();
        reportsDTO.setReportTitle("Test Report");

        // Mock repository calls
        when(employeeService.getEmployeeById(1L)).thenReturn(null);

        // Verify that an IllegalArgumentException is thrown
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportsService.addReport(reportsDTO, 1L, Collections.emptyList());
        });

        assertEquals("Employee with ID 1 not found", exception.getMessage());
    }


    @Test
    void testEditReport_WhenReportAlreadySubmitted() {
        // Mock data for a report that is already submitted
        Reports report = new Reports();
        report.setIsSubmitted(true);
        report.setIsHidden(false);
        report.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);

        // Mock behavior for the methods
        when(reportsRepository.findById(1L)).thenReturn(java.util.Optional.of(report));

        // Execute the editReport method and expect an exception
        assertThrows(NullPointerException.class, () -> {
            reportsService.editReport(1L, "New Title", "Description", new ArrayList<>(), new ArrayList<>());
        });
    }

    @Test
    void testEditReport_WhenReportIsHidden() {
        // Mock data for a report that is hidden
        Reports report = new Reports();
        report.setIsSubmitted(false);
        report.setIsHidden(true);
        report.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);

        // Mock behavior for the methods
        when(reportsRepository.findById(1L)).thenReturn(java.util.Optional.of(report));

        // Execute the editReport method and expect an exception
        assertThrows(NullPointerException.class, () -> {
            reportsService.editReport(1L, "New Title", "Description", new ArrayList<>(), new ArrayList<>());
        });
    }

    @Test
    void testEditReport_WhenReportRejected() {
        // Mock data for a report that is rejected
        Reports report = new Reports();
        report.setIsSubmitted(true);
        report.setIsHidden(false);
        report.setManagerApprovalStatus(ManagerApprovalStatus.REJECTED);

        // Mock behavior for the methods
        when(reportsRepository.findById(1L)).thenReturn(java.util.Optional.of(report));

        // Execute the editReport method and expect an exception
        assertThrows(NullPointerException.class, () -> {
            reportsService.editReport(1L, "New Title", "Description", new ArrayList<>(), new ArrayList<>());
        });
    }

    @Test
    void testUpdateReportAndExpenseTitles() {
        // Mock data for a report and expenses
        Reports report = new Reports();
        report.setReportId(1L);
        report.setReportTitle("Old Title");

        List<Expense> expenses = new ArrayList<>();
        Expense expense1 = new Expense();
        expense1.setExpenseId(1L);
        expenses.add(expense1);

        // Mock behavior for the methods
        when(reportsRepository.findById(1L)).thenReturn(java.util.Optional.of(report));
        when(expenseService.getExpenseByReportId(1L)).thenReturn(expenses);

        // Execute the updateReportAndExpenseTitles method
        reportsService.updateReportAndExpenseTitles(1L, "New Title");

        // Verify that the report and expense titles were updated

//        verify(expenseRepository).save(expense1);
    }

    @Test
    void testAddExpensesToReport() {
        // Mock data for an expense and report
        Expense expense = new Expense();
        expense.setExpenseId(1L);
        expense.setIsReported(false);

        Reports report = new Reports();
        report.setReportId(1L);

        // Mock behavior for the methods
        when(expenseService.getExpenseById(1L)).thenReturn(expense);

        // Execute the addExpensesToReport method
        reportsService.addExpensesToReport(1L, List.of(1L));

        // Verify that the expense was added to the report
        verify(expenseService).updateExpense(1L, 1L);
    }

    @Test
    void testRemoveExpensesFromReport() {
        // Mock data for an expense and report
        Expense expense = new Expense();
        expense.setExpenseId(1L);
        expense.setIsReported(true);
        expense.setReports(new Reports());

        // Mock behavior for the methods
        when(expenseService.getExpenseById(1L)).thenReturn(expense);

        // Execute the removeExpensesFromReport method
        reportsService.removeExpensesFromReport(List.of(1L));

        // Verify that the expense is no longer reported in the report
        verify(expenseRepository).save(expense);
    }
    @Test
    void testGetReportByEmpId() {
        assertThrows(IllegalArgumentException.class, () -> reportsService.getReportByEmpId(1L, "Request"));
    }


    @Test
    void testGetReportByEmpId2() {
        when(reportsRepository.getReportsByEmployeeIdAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<Long>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(new ArrayList<>());
        assertTrue(reportsService.getReportByEmpId(1L, "approved").isEmpty());
        verify(reportsRepository, atLeast(1)).getReportsByEmployeeIdAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Mockito.<Long>any(), Mockito.<ManagerApprovalStatus>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    @Test
    void testGetReportByEmpId3() {
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.getReportsByEmployeeIdAndIsSubmittedAndIsHidden(Mockito.<Long>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(reportsList);
        List<Reports> actualReportByEmpId = reportsService.getReportByEmpId(1L, "drafts");
        assertSame(reportsList, actualReportByEmpId);
        assertTrue(actualReportByEmpId.isEmpty());
        verify(reportsRepository).getReportsByEmployeeIdAndIsSubmittedAndIsHidden(Mockito.<Long>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    @Test
    void testGetReportByEmpId4() {
        when(reportsRepository.getReportsByEmployeeIdAndIsSubmittedAndIsHidden(Mockito.<Long>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenThrow(new IllegalStateException("drafts"));
        assertThrows(IllegalStateException.class, () -> reportsService.getReportByEmpId(1L, "drafts"));
        verify(reportsRepository).getReportsByEmployeeIdAndIsSubmittedAndIsHidden(Mockito.<Long>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
    }

    @Test
    void testGetReportByEmpId5() {
        ArrayList<Reports> reportsList = new ArrayList<>();
        when(reportsRepository.getReportsByEmployeeIdAndManagerApprovalStatusAndIsHidden(Mockito.<Long>any(), Mockito.<ManagerApprovalStatus>any(), anyBoolean())).thenReturn(reportsList);
        List<Reports> actualReportByEmpId = reportsService.getReportByEmpId(1L, "rejected");
        assertSame(reportsList, actualReportByEmpId);
        assertTrue(actualReportByEmpId.isEmpty());
        verify(reportsRepository).getReportsByEmployeeIdAndManagerApprovalStatusAndIsHidden(Mockito.<Long>any(), Mockito.<ManagerApprovalStatus>any(), anyBoolean());
    }

    @Test
    void testGetReportsSubmittedToUserApproved() {
        String managerEmail = "manager@example.com";
        String request = "approved";

        when(reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(
                managerEmail, ManagerApprovalStatus.APPROVED, true, false))
                .thenReturn(List.of(new Reports()));

        when(reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(
                managerEmail, ManagerApprovalStatus.PARTIALLY_APPROVED, true, false))
                .thenReturn(List.of(new Reports()));

        // Act
        List<Reports> result = reportsService.getReportsSubmittedToUser(managerEmail, request);

        // Assert
        assertEquals(2, result.size()); // Ensure that the result contains both approved and partially approved reports
    }

    @Test
    void testGetReportsSubmittedToUserRejected() {
        String managerEmail = "manager@example.com";
        String request = CONSTANT4;

        when(reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsHidden(
                managerEmail, ManagerApprovalStatus.REJECTED, false))
                .thenReturn(List.of(new Reports()));

        // Act
        List<Reports> result = reportsService.getReportsSubmittedToUser(managerEmail, request);

        // Assert
        assertEquals(1, result.size()); // Ensure that the result contains the rejected report
    }

    @Test
    void testGetReportsSubmittedToUserPending() {
        String managerEmail = "manager@example.com";
        String request = CONSTANT7;

        when(reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(
                managerEmail, ManagerApprovalStatus.PENDING, true, false))
                .thenReturn(List.of(new Reports()));

        // Act
        List<Reports> result = reportsService.getReportsSubmittedToUser(managerEmail, request);

        // Assert
        assertEquals(1, result.size()); // Ensure that the result contains the pending report
    }

    @Test
    void testGetAllSubmittedReportsWithSubmittedReports() {
        // Arrange
        Reports report1 = new Reports();
        report1.setIsSubmitted(true);

        Reports report2 = new Reports();
        report2.setIsSubmitted(true);

        when(reportsRepository.findAll()).thenReturn(Arrays.asList(report1, report2));

        // Act
        List<Reports> result = reportsService.getAllSubmittedReports();

        // Assert
        assertEquals(2, result.size()); // Ensure that all submitted reports are in the result
    }

    @Test
    void testGetAllSubmittedReportsWithNoSubmittedReports() {
        // Arrange
        Reports report1 = new Reports();
        report1.setIsSubmitted(false);

        Reports report2 = new Reports();
        report2.setIsSubmitted(false);

        when(reportsRepository.findAll()).thenReturn(Arrays.asList(report1, report2));

        // Act
        List<Reports> result = reportsService.getAllSubmittedReports();

        // Assert
        assertEquals(0, result.size()); // Ensure that there are no submitted reports in the result
    }

    @Test
    void testGetAllSubmittedReportsWithEmptyReportsList() {
        // Arrange
        when(reportsRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Reports> result = reportsService.getAllSubmittedReports();

        // Assert
        assertEquals(0, result.size()); // Ensure that there are no reports in the result
    }


    @Test
    void testGetAllReportsApprovedByManagerApproved() {
        // Arrange
        String request = CONSTANT5;

        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.APPROVED, true, false))
                .thenReturn(List.of(new Reports()));

        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.APPROVED, true, false))
                .thenReturn(List.of(new Reports()));

        // Act
        List<Reports> result = reportsService.getAllReportsApprovedByManager(request);

        // Assert
        assertEquals(2, result.size()); // Ensure that the result contains both approved and partially approved reports
    }

    @Test
    void testGetAllReportsApprovedByManagerRejected() {
        // Arrange
        String request = CONSTANT4;

        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.REJECTED, true, false))
                .thenReturn(List.of(new Reports()));

        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.REJECTED, true, false))
                .thenReturn(List.of(new Reports()));

        // Act
        List<Reports> result = reportsService.getAllReportsApprovedByManager(request);

        // Assert
        assertEquals(2, result.size()); // Ensure that the result contains both approved and partially approved reports with rejected finance status
    }

    @Test
    void testGetAllReportsApprovedByManagerPending() {
        // Arrange
        String request = CONSTANT7;

        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.PENDING, true, false))
                .thenReturn(List.of(new Reports()));

        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.PENDING, true, false))
                .thenReturn(List.of(new Reports()));

        // Act
        List<Reports> result = reportsService.getAllReportsApprovedByManager(request);

        // Assert
        assertEquals(2, result.size()); // Ensure that the result contains both approved and partially approved reports with pending finance status
    }

    @Test
    void testGetAllReportsApprovedByManagerReimbursed() {
        // Arrange
        String request = "reimbursed";

        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.REIMBURSED, true, false))
                .thenReturn(List.of(new Reports()));

        when(reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(
                ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.REIMBURSED, true, false))
                .thenReturn(List.of(new Reports()));

        // Act
        List<Reports> result = reportsService.getAllReportsApprovedByManager(request);

        // Assert
        assertEquals(2, result.size()); // Ensure that the result contains both approved and partially approved reports with reimbursed finance status
    }

    @Test
    void testGetAllReportsApprovedByManagerDefault() {
        // Arrange
        String request = "asdfghjk";

        // Act and Assert (This should throw an IllegalArgumentException)
        assertThrows(IllegalArgumentException.class, () -> reportsService.getAllReportsApprovedByManager(request));
    }

    @Test
    void testSubmitReport() throws IOException, MessagingException {
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(RuntimeException.class, () -> reportsService.submitReport(1L, "jane.doe@example.org", new Response()));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testSubmitReport2() throws IOException, MessagingException {
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(RuntimeException.class, () -> reportsService.submitReport(1L, new Response()));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testRejectReportByFinance() {
        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);
        Optional<Reports> ofResult = Optional.of(reports);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ObjectNotFoundException.class, () -> reportsService.rejectReportByFinance(1L, "Comments"));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testRejectReportByFinance3() {
        doNothing().when(emailService).userRejectedByFinanceNotification(Mockito.<Long>any());

        Employee employee = new Employee();
        employee.setEmployeeEmail("jane.doe@example.org");
        employee.setEmployeeId(1L);
        employee.setExpenseList(new ArrayList<>());
        employee.setFirstName("Jane");
        employee.setHrEmail("jane.doe@example.org");
        employee.setHrName("Hr Name");
        employee.setImageUrl("https://example.org/example");
        employee.setIsFinanceAdmin(true);
        employee.setIsHidden(true);
        employee.setLastName("Doe");
        employee.setLndEmail("jane.doe@example.org");
        employee.setLndName("Lnd Name");
        employee.setManagerEmail("jane.doe@example.org");
        employee.setManagerName("Manager Name");
        employee.setMiddleName("Middle Name");
        employee.setMobileNumber(1L);
        employee.setOfficialEmployeeId("42");
        employee.setRole("Role");
        employee.setToken("ABC123");
        when(employeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(employee);
        when(expenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        //doNothing().when(pushNotificationService).sendPushNotificationToToken(Mockito.<PushNotificationRequest>any());
        Reports reports = mock(Reports.class);
        when(reports.getManagerApprovalStatus()).thenReturn(ManagerApprovalStatus.APPROVED);
        when(reports.getIsHidden()).thenReturn(false);
        when(reports.getReportTitle()).thenReturn("Dr");
        when(reports.getIsSubmitted()).thenReturn(true);
        when(reports.getEmployeeId()).thenReturn(1L);
        doNothing().when(reports).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports).setManagerComments(Mockito.<String>any());
        doNothing().when(reports).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports).setReportId(Mockito.<Long>any());
        doNothing().when(reports).setReportTitle(Mockito.<String>any());
        doNothing().when(reports).setTotalAmount(anyFloat());
        doNothing().when(reports).setTotalApprovedAmount(anyFloat());
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);
        Optional<Reports> ofResult = Optional.of(reports);

        Reports reports2 = new Reports();
        reports2.setDateCreated(LocalDate.of(1970, 1, 1));
        reports2.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports2.setEmployeeId(1L);
        reports2.setEmployeeMail("Employee Mail");
        reports2.setEmployeeName("Employee Name");
        reports2.setExpensesCount(3L);
        reports2.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports2.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports2.setFinanceComments("Finance Comments");
        reports2.setIsHidden(true);
        reports2.setIsSubmitted(true);
        reports2.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports2.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports2.setManagerComments("Manager Comments");
        reports2.setManagerEmail("jane.doe@example.org");
        reports2.setManagerReviewTime("Manager Review Time");
        reports2.setOfficialEmployeeId("42");
        reports2.setReportId(1L);
        reports2.setReportTitle("Dr");
        reports2.setTotalAmount(10.0f);
        reports2.setTotalApprovedAmount(10.0f);
        when(reportsRepository.save(Mockito.<Reports>any())).thenReturn(reports2);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        reportsService.rejectReportByFinance(1L, "Comments");
        verify(emailService).userRejectedByFinanceNotification(Mockito.<Long>any());
        //verify(employeeService).getEmployeeById(Mockito.<Long>any());
        //verify(expenseService).getExpenseByReportId(Mockito.<Long>any());
        //verify(pushNotificationService).sendPushNotificationToToken(Mockito.<PushNotificationRequest>any());
        verify(reportsRepository).save(Mockito.<Reports>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(reports).getManagerApprovalStatus();
        verify(reports, atLeast(1)).getIsHidden();
        verify(reports, atLeast(1)).getIsSubmitted();
        verify(reports).getEmployeeId();
        //verify(reports).getReportTitle();
        verify(reports).setDateCreated(Mockito.<LocalDate>any());
        verify(reports).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports).setEmployeeId(Mockito.<Long>any());
        verify(reports).setEmployeeMail(Mockito.<String>any());
        verify(reports).setEmployeeName(Mockito.<String>any());
        verify(reports).setExpensesCount(Mockito.<Long>any());
        verify(reports, atLeast(1)).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports, atLeast(1)).setFinanceComments(Mockito.<String>any());
        verify(reports).setIsHidden(Mockito.<Boolean>any());
        verify(reports).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports).setManagerComments(Mockito.<String>any());
        verify(reports).setManagerEmail(Mockito.<String>any());
        verify(reports).setManagerReviewTime(Mockito.<String>any());
        verify(reports).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports).setReportId(Mockito.<Long>any());
        verify(reports).setReportTitle(Mockito.<String>any());
        verify(reports).setTotalAmount(anyFloat());
        verify(reports).setTotalApprovedAmount(anyFloat());
    }

   
    @Test
    void testRejectReportByFinance4() {
        Reports reports = mock(Reports.class);
        when(reports.getManagerApprovalStatus()).thenReturn(ManagerApprovalStatus.APPROVED);
        when(reports.getIsHidden()).thenReturn(false);
        when(reports.getIsSubmitted()).thenReturn(true);
        when(reports.getEmployeeId()).thenReturn(1L);
        doNothing().when(reports).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports).setManagerComments(Mockito.<String>any());
        doNothing().when(reports).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports).setReportId(Mockito.<Long>any());
        doNothing().when(reports).setReportTitle(Mockito.<String>any());
        doNothing().when(reports).setTotalAmount(anyFloat());
        doNothing().when(reports).setTotalApprovedAmount(anyFloat());
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);
        Optional<Reports> ofResult = Optional.of(reports);
        when(reportsRepository.save(Mockito.<Reports>any())).thenThrow(new IllegalStateException("Accounts admin rejected your expense report."));
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(IllegalStateException.class, () -> reportsService.rejectReportByFinance(1L, "Comments"));
        verify(reportsRepository).save(Mockito.<Reports>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(reports).getManagerApprovalStatus();
        verify(reports, atLeast(1)).getIsHidden();
        verify(reports, atLeast(1)).getIsSubmitted();
        verify(reports).getEmployeeId();
        verify(reports).setDateCreated(Mockito.<LocalDate>any());
        verify(reports).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports).setEmployeeId(Mockito.<Long>any());
        verify(reports).setEmployeeMail(Mockito.<String>any());
        verify(reports).setEmployeeName(Mockito.<String>any());
        verify(reports).setExpensesCount(Mockito.<Long>any());
        verify(reports, atLeast(1)).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports, atLeast(1)).setFinanceComments(Mockito.<String>any());
        verify(reports).setIsHidden(Mockito.<Boolean>any());
        verify(reports).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports).setManagerComments(Mockito.<String>any());
        verify(reports).setManagerEmail(Mockito.<String>any());
        verify(reports).setManagerReviewTime(Mockito.<String>any());
        verify(reports).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports).setReportId(Mockito.<Long>any());
        verify(reports).setReportTitle(Mockito.<String>any());
        verify(reports).setTotalAmount(anyFloat());
        verify(reports).setTotalApprovedAmount(anyFloat());
    }

    @Test
    void testTotalAmountReportFound() {
        // Arrange
        Long reportId = 1L;

        Reports report = new Reports();
        report.setReportId(reportId);

        Expense expense1 = new Expense();
        expense1.setAmount(100.0);
        Expense expense2 = new Expense();
        expense2.setAmount(200.0);
        List<Expense> expenses = Arrays.asList(expense1, expense2);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(expenseRepository.findByReports(Optional.of(report))).thenReturn(expenses);

        // Act
        float totalAmount = reportsService.totalAmount(reportId);

        // Assert
        assertEquals(300.0f, totalAmount, 0.001f);
    }

    @Test
    void testTotalAmountReportNotFound() {
        // Arrange
        Long reportId = 1L;

        when(reportsRepository.findById(reportId)).thenReturn(Optional.empty());

        // Act
        float totalAmount = reportsService.totalAmount(reportId);

        // Assert
        assertEquals(0.0f, totalAmount, 0.001f);
    }

    @Test
    void testTotalApprovedAmount() {
        // Arrange
        Long reportId = 1L;

        Reports report = new Reports();
        report.setReportId(reportId);

        Expense expense1 = new Expense();
        expense1.setAmountApproved(100.0);

        Expense expense2 = new Expense();
        expense2.setAmountApproved(200.0);

        Expense expense3 = new Expense(); // This expense is not approved
        expense3.setAmountApproved(null);

        List<Expense> expenses = Arrays.asList(expense1, expense2, expense3);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(expenseRepository.findExpenseByReportsAndIsReportedAndIsHidden(report, true, false)).thenReturn(expenses);

        // Act
        float totalApprovedAmount = reportsService.totalApprovedAmount(reportId);

        // Assert
        assertEquals(0.0f, totalApprovedAmount, 0.001f);
    }

    @Test
    void testHideReport() {
        // Arrange
        Long reportId = 1L;

        Reports report = new Reports();
        report.setReportId(reportId);

        Expense expense1 = new Expense();
        expense1.setIsReported(true);
        expense1.setReports(report);
        expense1.setReportTitle("Expense 1");
        expense1.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
        expense1.setAmountApproved(100.0);

        Expense expense2 = new Expense();
        expense2.setIsReported(true);
        expense2.setReports(report);
        expense2.setReportTitle("Expense 2");
        expense2.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense2.setAmountApproved(200.0);

        List<Expense> expenses = Arrays.asList(expense1, expense2);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(expenseService.getExpenseByReportId(reportId)).thenReturn(expenses);

        // Act
        reportsService.hideReport(reportId);

        // Assert
        assertTrue(report.getIsHidden());

        for (Expense expense : expenses) {
            assertFalse(expense.getIsReported());
            assertNull(expense.getReports());
            assertNull(expense.getReportTitle());
            assertNull(expense.getManagerApprovalStatusExpense());
            assertNull(expense.getAmountApproved());
        }
    }

    @Test
    void testGetReportsInDateRangeApproved() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(
                startDate, endDate, FinanceApprovalStatus.APPROVED
        )).thenReturn(new ArrayList<>());

        // Act
        List<Reports> reports = reportsService.getReportsInDateRange(startDate, endDate, "approved");

        // Assert
        assertNotNull(reports);
    }

    @Test
    void testGetReportsInDateRangePending() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(
                startDate, endDate, FinanceApprovalStatus.PENDING
        )).thenReturn(new ArrayList<>());

        // Act
        List<Reports> reports = reportsService.getReportsInDateRange(startDate, endDate, "pending");

        // Assert
        assertNotNull(reports);
    }

    @Test
    void testGetReportsInDateRangeReimbursed() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(
                startDate, endDate, FinanceApprovalStatus.REIMBURSED
        )).thenReturn(new ArrayList<>());

        // Act
        List<Reports> reports = reportsService.getReportsInDateRange(startDate, endDate, "reimbursed");

        // Assert
        assertNotNull(reports);
    }

    @Test
    void testGetReportsInDateRangeRejected() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        when(reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(
                startDate, endDate, FinanceApprovalStatus.REJECTED
        )).thenReturn(new ArrayList<>());

        // Act
        List<Reports> reports = reportsService.getReportsInDateRange(startDate, endDate, "rejected");

        // Assert
        assertNotNull(reports);
    }

    @Test
    void testGetReportsInDateRangeInvalidRequest() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        // Act and Assert (Use assertThrows)
        assertThrows(IllegalArgumentException.class, () -> {
            reportsService.getReportsInDateRange(startDate, endDate, "invalid_request");
        });
    }

    @Test
    void testGetReportsSubmittedToUserInDateRangeApproved() {
        // Arrange
        String managerEmail = "manager@example.com";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        when(reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(
                managerEmail, startDate, endDate, ManagerApprovalStatus.APPROVED, true, false
        )).thenReturn(new ArrayList<>());

        // Act
        List<Reports> reports = reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, "approved");

        // Assert
        assertNotNull(reports);
    }

    @Test
    void testGetReportsSubmittedToUserInDateRangeRejected() {
        // Arrange
        String managerEmail = "manager@example.com";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        when(reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(
                managerEmail, startDate, endDate, ManagerApprovalStatus.REJECTED, true, false
        )).thenReturn(new ArrayList<>());

        // Act
        List<Reports> reports = reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, "rejected");

        // Assert
        assertNotNull(reports);
    }

    @Test
    void testGetReportsSubmittedToUserInDateRangePending() {
        // Arrange
        String managerEmail = "manager@example.com";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        when(reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(
                managerEmail, startDate, endDate, ManagerApprovalStatus.PENDING, true, false
        )).thenReturn(new ArrayList<>());

        // Act
        List<Reports> reports = reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, "pending");

        // Assert
        assertNotNull(reports);
    }

    @Test
    void testGetReportsSubmittedToUserInDateRangeInvalidRequest() {
        // Arrange
        String managerEmail = "mtftm";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        // Act and Assert (Use assertThrows)
        assertThrows(IllegalArgumentException.class, () -> {
            reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, "invalid_request");
        });
    }

    @Test
    void testGetAmountOfReportsInDateRange() {
        // AyourServicerrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        // Create a sample list of reports
        List<Reports> reports = new ArrayList<>();
        Reports report1 = new Reports();
        report1.setTotalAmount(100.0f);
        Reports report2 = new Reports();
        report2.setTotalAmount(200.0f);
        reports.add(report1);
        reports.add(report2);

        when(reportsRepository.findByDateSubmittedBetween(startDate, endDate)).thenReturn(reports);

        // Act
        String result = reportsService.getAmountOfReportsInDateRange(startDate, endDate);

        // Assert
        assertEquals("300.0 INR", result);
    }

    @Test
    void testGetAmountOfReportsInDateRangeNoReports() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        // Create an empty list of reports
        List<Reports> reports = new ArrayList<>();

        when(reportsRepository.findByDateSubmittedBetween(startDate, endDate)).thenReturn(reports);

        // Act
        String result = reportsService.getAmountOfReportsInDateRange(startDate, endDate);

        // Assert
        assertEquals("0.0 INR", result);
    }

    @Test
    void testUpdateExpenseStatus() throws IOException, MessagingException {
        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);
        Optional<Reports> ofResult = Optional.of(reports);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ArrayList<Long> approveExpenseIds = new ArrayList<>();
        ArrayList<Long> rejectExpenseIds = new ArrayList<>();
        HashMap<Long, Float> partiallyApprovedMap = new HashMap<>();
        assertThrows(IllegalStateException.class, () -> reportsService.updateExpenseStatus(1L, approveExpenseIds, rejectExpenseIds, partiallyApprovedMap, "Review Time", "Comments", new Response()));
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    
    @Test
    void testUpdateExpenseStatus2() throws IOException, MessagingException {
        when(expenseRepository.findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(new ArrayList<>());
        Reports reports = mock(Reports.class);
        when(reports.getIsHidden()).thenReturn(false);
        when(reports.getIsSubmitted()).thenReturn(true);
        doNothing().when(reports).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports).setManagerComments(Mockito.<String>any());
        doNothing().when(reports).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports).setReportId(Mockito.<Long>any());
        doNothing().when(reports).setReportTitle(Mockito.<String>any());
        doNothing().when(reports).setTotalAmount(anyFloat());
        doNothing().when(reports).setTotalApprovedAmount(anyFloat());
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);
        Optional<Reports> ofResult = Optional.of(reports);

        Reports reports2 = new Reports();
        reports2.setDateCreated(LocalDate.of(1970, 1, 1));
        reports2.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports2.setEmployeeId(1L);
        reports2.setEmployeeMail("Employee Mail");
        reports2.setEmployeeName("Employee Name");
        reports2.setExpensesCount(3L);
        reports2.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports2.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports2.setFinanceComments("Finance Comments");
        reports2.setIsHidden(true);
        reports2.setIsSubmitted(true);
        reports2.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports2.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports2.setManagerComments("Manager Comments");
        reports2.setManagerEmail("jane.doe@example.org");
        reports2.setManagerReviewTime("Manager Review Time");
        reports2.setOfficialEmployeeId("42");
        reports2.setReportId(1L);
        reports2.setReportTitle("Dr");
        reports2.setTotalAmount(10.0f);
        reports2.setTotalApprovedAmount(10.0f);
        when(reportsRepository.save(Mockito.<Reports>any())).thenReturn(reports2);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ArrayList<Long> approveExpenseIds = new ArrayList<>();
        ArrayList<Long> rejectExpenseIds = new ArrayList<>();
        HashMap<Long, Float> partiallyApprovedMap = new HashMap<>();
        reportsService.updateExpenseStatus(1L, approveExpenseIds, rejectExpenseIds, partiallyApprovedMap, "Review Time", "Comments", new Response());
        verify(expenseRepository).findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
        verify(reportsRepository, atLeast(1)).save(Mockito.<Reports>any());
        verify(reportsRepository, atLeast(1)).findById(Mockito.<Long>any());
        verify(reports).getIsHidden();
        verify(reports).getIsSubmitted();
        verify(reports).setDateCreated(Mockito.<LocalDate>any());
        verify(reports).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports).setEmployeeId(Mockito.<Long>any());
        verify(reports).setEmployeeMail(Mockito.<String>any());
        verify(reports).setEmployeeName(Mockito.<String>any());
        verify(reports).setExpensesCount(Mockito.<Long>any());
        verify(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports).setFinanceComments(Mockito.<String>any());
        verify(reports).setIsHidden(Mockito.<Boolean>any());
        verify(reports).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports, atLeast(1)).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports, atLeast(1)).setManagerComments(Mockito.<String>any());
        verify(reports).setManagerEmail(Mockito.<String>any());
        verify(reports, atLeast(1)).setManagerReviewTime(Mockito.<String>any());
        verify(reports).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports).setReportId(Mockito.<Long>any());
        verify(reports).setReportTitle(Mockito.<String>any());
        verify(reports).setTotalAmount(anyFloat());
        verify(reports, atLeast(1)).setTotalApprovedAmount(anyFloat());
    }

    @Test
    void testUpdateExpenseStatus3() throws IOException, MessagingException {
        Reports reports = mock(Reports.class);
        when(reports.getIsHidden()).thenReturn(false);
        when(reports.getIsSubmitted()).thenReturn(true);
        doNothing().when(reports).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports).setManagerComments(Mockito.<String>any());
        doNothing().when(reports).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports).setReportId(Mockito.<Long>any());
        doNothing().when(reports).setReportTitle(Mockito.<String>any());
        doNothing().when(reports).setTotalAmount(anyFloat());
        doNothing().when(reports).setTotalApprovedAmount(anyFloat());
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);
        Optional<Reports> ofResult = Optional.of(reports);
        when(reportsRepository.save(Mockito.<Reports>any())).thenThrow(new IllegalStateException("foo"));
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ArrayList<Long> approveExpenseIds = new ArrayList<>();
        ArrayList<Long> rejectExpenseIds = new ArrayList<>();
        HashMap<Long, Float> partiallyApprovedMap = new HashMap<>();
        assertThrows(IllegalStateException.class, () -> reportsService.updateExpenseStatus(1L, approveExpenseIds, rejectExpenseIds, partiallyApprovedMap, "Review Time", "Comments", new Response()));
        verify(reportsRepository).save(Mockito.<Reports>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(reports).getIsHidden();
        verify(reports).getIsSubmitted();
        verify(reports).setDateCreated(Mockito.<LocalDate>any());
        verify(reports).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports).setEmployeeId(Mockito.<Long>any());
        verify(reports).setEmployeeMail(Mockito.<String>any());
        verify(reports).setEmployeeName(Mockito.<String>any());
        verify(reports).setExpensesCount(Mockito.<Long>any());
        verify(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports).setFinanceComments(Mockito.<String>any());
        verify(reports).setIsHidden(Mockito.<Boolean>any());
        verify(reports).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports, atLeast(1)).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports).setManagerComments(Mockito.<String>any());
        verify(reports).setManagerEmail(Mockito.<String>any());
        verify(reports).setManagerReviewTime(Mockito.<String>any());
        verify(reports).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports).setReportId(Mockito.<Long>any());
        verify(reports).setReportTitle(Mockito.<String>any());
        verify(reports).setTotalAmount(anyFloat());
        verify(reports).setTotalApprovedAmount(anyFloat());
    }

    @Test
    void testUpdateExpenseStatus4() throws IOException, MessagingException {
        Category category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);

        Employee employee = new Employee();
        employee.setEmployeeEmail("jane.doe@example.org");
        employee.setEmployeeId(1L);
        employee.setExpenseList(new ArrayList<>());
        employee.setFirstName("Jane");
        employee.setHrEmail("jane.doe@example.org");
        employee.setHrName("Hr Name");
        employee.setImageUrl("https://example.org/example");
        employee.setIsFinanceAdmin(true);
        employee.setIsHidden(true);
        employee.setLastName("Doe");
        employee.setLndEmail("jane.doe@example.org");
        employee.setLndName("Lnd Name");
        employee.setManagerEmail("jane.doe@example.org");
        employee.setManagerName("Manager Name");
        employee.setMiddleName("Middle Name");
        employee.setMobileNumber(1L);
        employee.setOfficialEmployeeId("42");
        employee.setRole("Role");
        employee.setToken("ABC123");

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);

        Expense expense = new Expense();
        expense.setAmount(10.0d);
        expense.setAmountApproved(10.0d);
        expense.setCategory(category);
        expense.setCategoryDescription("Category Description");
        expense.setDate(LocalDate.of(1970, 1, 1));
        expense.setDateCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        expense.setDescription("The characteristics of someone or something");
        expense.setEmployee(employee);
        expense.setExpenseId(1L);
        expense.setFile(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);

        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        when(expenseRepository.findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any())).thenReturn(expenseList);
        Reports reports2 = mock(Reports.class);
        when(reports2.getIsHidden()).thenReturn(false);
        when(reports2.getIsSubmitted()).thenReturn(true);
        doNothing().when(reports2).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports2).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports2).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports2).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports2).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports2).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports2).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports2).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports2).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports2).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports2).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports2).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports2).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports2).setManagerComments(Mockito.<String>any());
        doNothing().when(reports2).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports2).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports2).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports2).setReportId(Mockito.<Long>any());
        doNothing().when(reports2).setReportTitle(Mockito.<String>any());
        doNothing().when(reports2).setTotalAmount(anyFloat());
        doNothing().when(reports2).setTotalApprovedAmount(anyFloat());
        reports2.setDateCreated(LocalDate.of(1970, 1, 1));
        reports2.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports2.setEmployeeId(1L);
        reports2.setEmployeeMail("Employee Mail");
        reports2.setEmployeeName("Employee Name");
        reports2.setExpensesCount(3L);
        reports2.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports2.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports2.setFinanceComments("Finance Comments");
        reports2.setIsHidden(true);
        reports2.setIsSubmitted(true);
        reports2.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports2.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports2.setManagerComments("Manager Comments");
        reports2.setManagerEmail("jane.doe@example.org");
        reports2.setManagerReviewTime("Manager Review Time");
        reports2.setOfficialEmployeeId("42");
        reports2.setReportId(1L);
        reports2.setReportTitle("Dr");
        reports2.setTotalAmount(10.0f);
        reports2.setTotalApprovedAmount(10.0f);
        Optional<Reports> ofResult = Optional.of(reports2);

        Reports reports3 = new Reports();
        reports3.setDateCreated(LocalDate.of(1970, 1, 1));
        reports3.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports3.setEmployeeId(1L);
        reports3.setEmployeeMail("Employee Mail");
        reports3.setEmployeeName("Employee Name");
        reports3.setExpensesCount(3L);
        reports3.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports3.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports3.setFinanceComments("Finance Comments");
        reports3.setIsHidden(true);
        reports3.setIsSubmitted(true);
        reports3.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports3.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports3.setManagerComments("Manager Comments");
        reports3.setManagerEmail("jane.doe@example.org");
        reports3.setManagerReviewTime("Manager Review Time");
        reports3.setOfficialEmployeeId("42");
        reports3.setReportId(1L);
        reports3.setReportTitle("Dr");
        reports3.setTotalAmount(10.0f);
        reports3.setTotalApprovedAmount(10.0f);
        when(reportsRepository.save(Mockito.<Reports>any())).thenReturn(reports3);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ArrayList<Long> approveExpenseIds = new ArrayList<>();
        ArrayList<Long> rejectExpenseIds = new ArrayList<>();
        HashMap<Long, Float> partiallyApprovedMap = new HashMap<>();
        reportsService.updateExpenseStatus(1L, approveExpenseIds, rejectExpenseIds, partiallyApprovedMap, "Review Time", "Comments", new Response());
        verify(expenseRepository).findExpenseByReportsAndIsReportedAndIsHidden(Mockito.<Reports>any(), Mockito.<Boolean>any(), Mockito.<Boolean>any());
        verify(reportsRepository, atLeast(1)).save(Mockito.<Reports>any());
        verify(reportsRepository, atLeast(1)).findById(Mockito.<Long>any());
        verify(reports2).getIsHidden();
        verify(reports2).getIsSubmitted();
        verify(reports2).setDateCreated(Mockito.<LocalDate>any());
        verify(reports2).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports2).setEmployeeId(Mockito.<Long>any());
        verify(reports2).setEmployeeMail(Mockito.<String>any());
        verify(reports2).setEmployeeName(Mockito.<String>any());
        verify(reports2).setExpensesCount(Mockito.<Long>any());
        verify(reports2).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports2, atLeast(1)).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports2).setFinanceComments(Mockito.<String>any());
        verify(reports2).setIsHidden(Mockito.<Boolean>any());
        verify(reports2).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports2, atLeast(1)).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports2, atLeast(1)).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports2, atLeast(1)).setManagerComments(Mockito.<String>any());
        verify(reports2).setManagerEmail(Mockito.<String>any());
        verify(reports2, atLeast(1)).setManagerReviewTime(Mockito.<String>any());
        verify(reports2).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports2).setReportId(Mockito.<Long>any());
        verify(reports2).setReportTitle(Mockito.<String>any());
        verify(reports2).setTotalAmount(anyFloat());
        verify(reports2, atLeast(1)).setTotalApprovedAmount(anyFloat());
    }

    @Test
    void testUpdateExpenseStatus5() throws IOException, MessagingException {
        Reports reports = mock(Reports.class);
        when(reports.getIsHidden()).thenReturn(false);
        when(reports.getIsSubmitted()).thenReturn(false);
        doNothing().when(reports).setDateCreated(Mockito.<LocalDate>any());
        doNothing().when(reports).setDateSubmitted(Mockito.<LocalDate>any());
        doNothing().when(reports).setEmployeeId(Mockito.<Long>any());
        doNothing().when(reports).setEmployeeMail(Mockito.<String>any());
        doNothing().when(reports).setEmployeeName(Mockito.<String>any());
        doNothing().when(reports).setExpensesCount(Mockito.<Long>any());
        doNothing().when(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        doNothing().when(reports).setFinanceComments(Mockito.<String>any());
        doNothing().when(reports).setIsHidden(Mockito.<Boolean>any());
        doNothing().when(reports).setIsSubmitted(Mockito.<Boolean>any());
        doNothing().when(reports).setManagerActionDate(Mockito.<LocalDate>any());
        doNothing().when(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        doNothing().when(reports).setManagerComments(Mockito.<String>any());
        doNothing().when(reports).setManagerEmail(Mockito.<String>any());
        doNothing().when(reports).setManagerReviewTime(Mockito.<String>any());
        doNothing().when(reports).setOfficialEmployeeId(Mockito.<String>any());
        doNothing().when(reports).setReportId(Mockito.<Long>any());
        doNothing().when(reports).setReportTitle(Mockito.<String>any());
        doNothing().when(reports).setTotalAmount(anyFloat());
        doNothing().when(reports).setTotalApprovedAmount(anyFloat());
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);
        Optional<Reports> ofResult = Optional.of(reports);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ArrayList<Long> approveExpenseIds = new ArrayList<>();
        ArrayList<Long> rejectExpenseIds = new ArrayList<>();
        HashMap<Long, Float> partiallyApprovedMap = new HashMap<>();
        assertThrows(IllegalStateException.class, () -> reportsService.updateExpenseStatus(1L, approveExpenseIds, rejectExpenseIds, partiallyApprovedMap, "Review Time", "Comments", new Response()));
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(reports).getIsHidden();
        verify(reports).getIsSubmitted();
        verify(reports).setDateCreated(Mockito.<LocalDate>any());
        verify(reports).setDateSubmitted(Mockito.<LocalDate>any());
        verify(reports).setEmployeeId(Mockito.<Long>any());
        verify(reports).setEmployeeMail(Mockito.<String>any());
        verify(reports).setEmployeeName(Mockito.<String>any());
        verify(reports).setExpensesCount(Mockito.<Long>any());
        verify(reports).setFinanceActionDate(Mockito.<LocalDate>any());
        verify(reports).setFinanceApprovalStatus(Mockito.<FinanceApprovalStatus>any());
        verify(reports).setFinanceComments(Mockito.<String>any());
        verify(reports).setIsHidden(Mockito.<Boolean>any());
        verify(reports).setIsSubmitted(Mockito.<Boolean>any());
        verify(reports).setManagerActionDate(Mockito.<LocalDate>any());
        verify(reports).setManagerApprovalStatus(Mockito.<ManagerApprovalStatus>any());
        verify(reports).setManagerComments(Mockito.<String>any());
        verify(reports).setManagerEmail(Mockito.<String>any());
        verify(reports).setManagerReviewTime(Mockito.<String>any());
        verify(reports).setOfficialEmployeeId(Mockito.<String>any());
        verify(reports).setReportId(Mockito.<Long>any());
        verify(reports).setReportTitle(Mockito.<String>any());
        verify(reports).setTotalAmount(anyFloat());
        verify(reports).setTotalApprovedAmount(anyFloat());
    }

    @Test
    void testNotifyHR() throws MessagingException {
        doNothing().when(emailService).notifyHr(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());

        Employee employee = new Employee();
        employee.setEmployeeEmail("jane.doe@example.org");
        employee.setEmployeeId(1L);
        ArrayList<Expense> expenseList = new ArrayList<>();
        employee.setExpenseList(expenseList);
        employee.setFirstName("Jane");
        employee.setHrEmail("jane.doe@example.org");
        employee.setHrName("Hr Name");
        employee.setImageUrl("https://example.org/example");
        employee.setIsFinanceAdmin(true);
        employee.setIsHidden(true);
        employee.setLastName("Doe");
        employee.setLndEmail("jane.doe@example.org");
        employee.setLndName("Lnd Name");
        employee.setManagerEmail("jane.doe@example.org");
        employee.setManagerName("Manager Name");
        employee.setMiddleName("Middle Name");
        employee.setMobileNumber(1L);
        employee.setOfficialEmployeeId("42");
        employee.setRole("Role");
        employee.setToken("ABC123");
        when(employeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(employee);

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);
        Optional<Reports> ofResult = Optional.of(reports);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        reportsService.notifyHR(1L);
        verify(emailService).notifyHr(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());
        verify(employeeService).getEmployeeById(Mockito.<Long>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        assertEquals(expenseList, reportsService.getAllSubmittedReports());
    }

    @Test
    void testNotifyHR2() throws MessagingException {
        doThrow(new IllegalStateException("foo")).when(emailService).notifyHr(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());

        Employee employee = new Employee();
        employee.setEmployeeEmail("jane.doe@example.org");
        employee.setEmployeeId(1L);
        employee.setExpenseList(new ArrayList<>());
        employee.setFirstName("Jane");
        employee.setHrEmail("jane.doe@example.org");
        employee.setHrName("Hr Name");
        employee.setImageUrl("https://example.org/example");
        employee.setIsFinanceAdmin(true);
        employee.setIsHidden(true);
        employee.setLastName("Doe");
        employee.setLndEmail("jane.doe@example.org");
        employee.setLndName("Lnd Name");
        employee.setManagerEmail("jane.doe@example.org");
        employee.setManagerName("Manager Name");
        employee.setMiddleName("Middle Name");
        employee.setMobileNumber(1L);
        employee.setOfficialEmployeeId("42");
        employee.setRole("Role");
        employee.setToken("ABC123");
        when(employeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(employee);

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);
        Optional<Reports> ofResult = Optional.of(reports);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(IllegalStateException.class, () -> reportsService.notifyHR(1L));
        verify(emailService).notifyHr(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());
        verify(employeeService).getEmployeeById(Mockito.<Long>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testNotifyLnD() throws MessagingException {
        doNothing().when(emailService).notifyLnD(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());

        Employee employee = new Employee();
        employee.setEmployeeEmail("jane.doe@example.org");
        employee.setEmployeeId(1L);
        ArrayList<Expense> expenseList = new ArrayList<>();
        employee.setExpenseList(expenseList);
        employee.setFirstName("Jane");
        employee.setHrEmail("jane.doe@example.org");
        employee.setHrName("Hr Name");
        employee.setImageUrl("https://example.org/example");
        employee.setIsFinanceAdmin(true);
        employee.setIsHidden(true);
        employee.setLastName("Doe");
        employee.setLndEmail("jane.doe@example.org");
        employee.setLndName("Lnd Name");
        employee.setManagerEmail("jane.doe@example.org");
        employee.setManagerName("Manager Name");
        employee.setMiddleName("Middle Name");
        employee.setMobileNumber(1L);
        employee.setOfficialEmployeeId("42");
        employee.setRole("Role");
        employee.setToken("ABC123");
        when(employeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(employee);

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);
        Optional<Reports> ofResult = Optional.of(reports);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        reportsService.notifyLnD(1L);
        verify(emailService).notifyLnD(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());
        verify(employeeService).getEmployeeById(Mockito.<Long>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
        assertEquals(expenseList, reportsService.getAllSubmittedReports());
    }


    @Test
    void testNotifyLnD2() throws MessagingException {
        doThrow(new IllegalStateException("foo")).when(emailService).notifyLnD(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());

        Employee employee = new Employee();
        employee.setEmployeeEmail("jane.doe@example.org");
        employee.setEmployeeId(1L);
        employee.setExpenseList(new ArrayList<>());
        employee.setFirstName("Jane");
        employee.setHrEmail("jane.doe@example.org");
        employee.setHrName("Hr Name");
        employee.setImageUrl("https://example.org/example");
        employee.setIsFinanceAdmin(true);
        employee.setIsHidden(true);
        employee.setLastName("Doe");
        employee.setLndEmail("jane.doe@example.org");
        employee.setLndName("Lnd Name");
        employee.setManagerEmail("jane.doe@example.org");
        employee.setManagerName("Manager Name");
        employee.setMiddleName("Middle Name");
        employee.setMobileNumber(1L);
        employee.setOfficialEmployeeId("42");
        employee.setRole("Role");
        employee.setToken("ABC123");
        when(employeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(employee);

        Reports reports = new Reports();
        reports.setDateCreated(LocalDate.of(1970, 1, 1));
        reports.setDateSubmitted(LocalDate.of(1970, 1, 1));
        reports.setEmployeeId(1L);
        reports.setEmployeeMail("Employee Mail");
        reports.setEmployeeName("Employee Name");
        reports.setExpensesCount(3L);
        reports.setFinanceActionDate(LocalDate.of(1970, 1, 1));
        reports.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        reports.setFinanceComments("Finance Comments");
        reports.setIsHidden(true);
        reports.setIsSubmitted(true);
        reports.setManagerActionDate(LocalDate.of(1970, 1, 1));
        reports.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
        reports.setManagerComments("Manager Comments");
        reports.setManagerEmail("jane.doe@example.org");
        reports.setManagerReviewTime("Manager Review Time");
        reports.setOfficialEmployeeId("42");
        reports.setReportId(1L);
        reports.setReportTitle("Dr");
        reports.setTotalAmount(10.0f);
        reports.setTotalApprovedAmount(10.0f);
        Optional<Reports> ofResult = Optional.of(reports);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(IllegalStateException.class, () -> reportsService.notifyLnD(1L));
        verify(emailService).notifyLnD(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any());
        verify(employeeService).getEmployeeById(Mockito.<Long>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testProcessPartiallyApprovedExpenses() {
        // Arrange
        Long expenseId = 1L;
        Map<Long, Float> partiallyApprovedMap = Collections.singletonMap(expenseId, 50.0f); // Sample values
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
        expense.setAmount(100.0); // Sample amount

        when(expenseService.getExpenseById(expenseId)).thenReturn(expense);

        // Act
        reportsService.processPartiallyApprovedExpenses(partiallyApprovedMap);

        // Assert
        verify(expenseRepository).save(expense);

        // Verify that the expense has been updated correctly
        Assert.assertEquals(ManagerApprovalStatusExpense.PARTIALLY_APPROVED, expense.getManagerApprovalStatusExpense());
        Assert.assertEquals(50.0, expense.getAmountApproved(), 0.001); // Add a delta for floating-point comparison
    }

    @Test
    void testValidateExpenseNotHidden() {
        // Arrange
        Long expenseId = 1L;
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);
        expense.setIsHidden(false);

        // Act and Assert
        reportsService.validateExpense(expense, expenseId); // This should not throw an exception.
    }

    @Test
    void testValidateExpenseHidden() {
        // Arrange
        Long expenseId = 1L;
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);
        expense.setIsHidden(true);

        // Act and Assert (This should throw an exception)
        try {
            reportsService.validateExpense(expense, expenseId);
            fail("Expected an IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Expense with ID 1 does not exist!", e.getMessage()); // Add the expected exception message
        }
    }




    @Test
    void testAddExpenseToHiddenReport() {
        // Test when a hidden report is provided
        Reports report = new Reports();
        report.setReportId(1L);
        report.setIsHidden(true);
        when(reportsRepository.findById(1L)).thenReturn(Optional.of(report));

        List<Long> expenseIds = new ArrayList<>();
        expenseIds.add(2L);

        assertThrows(NullPointerException.class, () -> {
            reportsService.addExpenseToReport(1L, expenseIds);
        });
    }

    @Test
    void testAddMissingExpenseToReport() {
        // Test when an expense is missing
        when(reportsRepository.findById(1L)).thenReturn(Optional.of(new Reports()));
        when(expenseService.getExpenseById(Mockito.anyLong())).thenReturn(null);

        List<Long> expenseIds = new ArrayList<>();
        expenseIds.add(2L);

        assertThrows(NullPointerException.class, () -> {
            reportsService.addExpenseToReport(1L, expenseIds);
        });
    }


    @Test
    void testAddReportEmployeeNotFound1() {
        // Test throwing an exception when an employee is not found

        ReportsDTO reportsDTO = new ReportsDTO();
        reportsDTO.setReportTitle("Sample Report");

        when(employeeService.getEmployeeById(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            reportsService.addReport(reportsDTO, 1L, new ArrayList<>());
        });
    }

    @Test
    void testAddExpenseToReportSuccess() {
        // Test adding expenses to a report with valid data

        Reports report = new Reports();
        report.setReportId(REPORT_ID);
        // Set other required fields

        List<Long> expenseIds = new ArrayList<>();
        expenseIds.add(2L);
        expenseIds.add(3L);

        Expense expense1 = new Expense();
        expense1.setExpenseId(2L);
        expense1.setIsReported(false);

        Expense expense2 = new Expense();
        expense2.setExpenseId(3L);
        expense2.setIsReported(false);

        when(reportsRepository.getReportByReportId(REPORT_ID)).thenReturn(report);
        when(expenseService.getExpenseById(2L)).thenReturn(expense1);
        when(expenseService.getExpenseById(3L)).thenReturn(expense2);

        reportsService.addExpenseToReport(REPORT_ID, expenseIds);

        // Add assertions based on your requirements
        verify(expenseService, times(2)).updateExpense(eq(REPORT_ID), anyLong());
        assertFalse(expense1.getIsReported());
        assertFalse(expense2.getIsReported());
    }

    @Test
    void testAddExpenseToReportReportNotFound() {
        // Test throwing an exception when the report is not found

        List<Long> expenseIds = new ArrayList<>();
        expenseIds.add(2L);
        expenseIds.add(3L);

        when(reportsRepository.getReportByReportId(REPORT_ID)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            reportsService.addExpenseToReport(REPORT_ID, expenseIds);
        });
    }

    @Test
    void testAddExpenseToReportExpenseNotFound() {
        // Test throwing an exception when an expense is not found

        Reports report = new Reports();
        report.setReportId(REPORT_ID);

        List<Long> expenseIds = new ArrayList<>();
        expenseIds.add(2L);
        expenseIds.add(3L);

        Expense expense1 = new Expense();
        expense1.setExpenseId(2L);
        expense1.setIsReported(false);

        when(reportsRepository.getReportByReportId(REPORT_ID)).thenReturn(report);
        when(expenseService.getExpenseById(2L)).thenReturn(expense1);
        when(expenseService.getExpenseById(3L)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            reportsService.addExpenseToReport(REPORT_ID, expenseIds);
        });
    }

    @Test
    void testUpdateReportTotalAmounts() {
        // Test updating total amounts for a report

        Reports report = new Reports();
        report.setReportId(REPORT_ID);

        // Mock the behavior of getReportById, totalAmount, and totalApprovedAmount
        when(reportsRepository.findById(REPORT_ID)).thenReturn(Optional.of(report));
        when(reportsRepository.save(report)).thenReturn(report); // Mock saving the report

        reportsService.updateReportTotalAmounts(REPORT_ID);

    }


    @Test
    void testSubmitReportReportHidden() {
        // Test throwing an exception when the report is hidden

        Reports report = new Reports();
        report.setReportId(REPORT_ID);
        report.setIsHidden(true);

        when(reportsRepository.findById(REPORT_ID)).thenReturn(Optional.of(report));

        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        assertThrows(NullPointerException.class, () -> {
            reportsService.submitReport(REPORT_ID, mockResponse);
        });
    }

    @Test
    void testSubmitReportHiddenReport() {
        // Test when the report is hidden
        long reportId = 1L;

        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsHidden(true);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        try {
            reportsService.submitReport(reportId, "manager@example.com", new MockHttpServletResponse());
        } catch (NullPointerException e) {
            assertEquals("Report with ID 1 does not exist!", CONSTANT2 + reportId + CONSTANT1, e.getMessage());
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void testSubmitReportAlreadySubmitted2() {
        // Test when the report is already submitted and not rejected
        long reportId = 1L;

        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsHidden(false);
        report.setIsSubmitted(true);
        report.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));

        try {
            reportsService.submitReport(reportId, "manager@example.com", new MockHttpServletResponse());
        } catch (IllegalStateException e) {
            assertEquals("Report with ID 1 is already submitted!", CONSTANT2 + reportId + " is already submitted!", e.getMessage());
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void testSubmitReportManagerEmailDBNull() {
        // Test when manager's email from the database is not found

        long reportId = 1L;

        Reports report = new Reports();
        report.setReportId(reportId);
        report.setIsHidden(false);
        report.setIsSubmitted(false);
        report.setManagerApprovalStatus(ManagerApprovalStatus.REJECTED);

        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(expenseService.getRejectedExpensesByReportId(reportId)).thenReturn(Collections.emptyList());
        when(expenseService.getExpenseByReportId(reportId)).thenReturn(Collections.emptyList());

        // Set employee with null managerEmail
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        try {
            reportsService.submitReport(reportId, "manager@example.com", new MockHttpServletResponse());
        } catch (NullPointerException e) {
            assertEquals( "does not exist!", CONSTANT1, e.getMessage());
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }

     
    }


    @Test
    void testEditReportWithCoverage() {
        // Prepare test data
        Long reportId = 1L;
        String reportTitle = "Updated Title";
        String reportDescription = "Updated Description";
        List<Long> addExpenseIds = Arrays.asList(2L, 3L);
        List<Long> removeExpenseIds = Arrays.asList(4L, 5L);

        // Mock Reports and Expenses
        Reports report = new Reports();
        report.setReportId(1L);
        report.setReportTitle("yuiopghjkl");
        when(reportsRepository.getReportByReportId(reportId)).thenReturn(report);
        Expense expense2 = new Expense();
        expense2.setExpenseId(2L);
        Expense expense3 = new Expense();
        expense3.setExpenseId(3L);
        when(expenseService.getExpenseById(2L)).thenReturn(expense2);
        when(expenseService.getExpenseById(3L)).thenReturn(expense3);
        when(expenseService.getExpenseById(4L)).thenReturn(null);
        when(expenseService.getExpenseById(5L)).thenReturn(null);

        // Execute the method
        List<Reports> updatedReports = reportsService.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);




        verify(expenseService).updateExpense(reportId,expense2.getExpenseId());
        verify(expenseService).updateExpense(reportId,expense3.getExpenseId());

        verify(expenseService, times(0)).deleteExpenseById(any());


        // Verify the result
        assertEquals(0, updatedReports.size());

    }

    @Test
    void testReportNotSubmitted() {
        // Mock data
        long reportId = 1L;
        Reports report = new Reports();
        report.setIsSubmitted(false);
        report.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);

        // Mock the necessary method call
        Mockito.when(reportsRepository.getReportByReportId(reportId)).thenReturn(report);


    }


    @Test
    void testReportNotApproved() {
        // Mock data
        long reportId = 1L;
        Reports report = new Reports();
        report.setIsSubmitted(true);
        report.setManagerApprovalStatus(ManagerApprovalStatus.REJECTED);

        // Mock the necessary method call
        Mockito.when(reportsRepository.getReportByReportId(reportId)).thenReturn(report);

        // Verify that an exception is thrown for not being approved
        assertThrows(IllegalStateException.class, () -> {
            reportsService.validateReportForEdit(report);
        });
    }

    @Test
    void testReportPartiallyApproved() {
        // Mock data
        long reportId = 1L;
        Reports report = new Reports();
        report.setIsSubmitted(true);
        report.setManagerApprovalStatus(ManagerApprovalStatus.PARTIALLY_APPROVED);

        // Mock the necessary method call
        Mockito.when(reportsRepository.getReportByReportId(reportId)).thenReturn(report);

        // Verify that no exception is thrown for being partially approved
        reportsService.validateReportForEdit(report);
    }

    @Test
    void testReportApproved() {
        // Mock data
        long reportId = 1L;
        Reports report = new Reports();
        report.setIsSubmitted(true);
        report.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);

        // Mock the necessary method call
        Mockito.when(reportsRepository.getReportByReportId(reportId)).thenReturn(report);

        // Verify that no exception is thrown for being approved
        reportsService.validateReportForEdit(report);
    }

    @Test
    void testSendPushNotificationsToMultipleReports() {
        // Prepare test data
        List<Long> reportIds = Arrays.asList(1L, 2L, 3L);

        // Mock report data and manager email
        Reports report1 = new Reports();
        Reports report2 = new Reports();
        Reports report3 = new Reports();
        report1.setManagerEmail("manager1@example.com");
        report2.setManagerEmail("manager2@example.com");
        report3.setManagerEmail("manager3@example.com");
        when(reportsRepository.findById(1L)).thenReturn(Optional.of(report1));
        when(reportsRepository.findById(2L)).thenReturn(Optional.of(report2));
        when(reportsRepository.findById(3L)).thenReturn(Optional.of(report3));

        // Mock manager data and tokens
        Employee manager1 = new Employee();
        Employee manager2 = new Employee();
        Employee manager3 = new Employee();
        manager1.setToken("token1");
        manager2.setToken("token2");
        manager3.setToken("token3");
        when(employeeService.getEmployeeByEmail("manager1@example.com")).thenReturn(manager1);
        when(employeeService.getEmployeeByEmail("manager2@example.com")).thenReturn(manager2);
        when(employeeService.getEmployeeByEmail("manager3@example.com")).thenReturn(manager3);

        // Execute the method
        //reportsService.sendReportNotApprovedByManagerReminder();


    }

    @Test
    void testGetReportById1() {
        Long reportId = 1L;
        Reports mockReport = new Reports(); // Create a mock report

        // Mock the behavior of reportsRepository.findById to return the mockReport
        when(reportsRepository.findById(reportId)).thenReturn(Optional.of(mockReport));

        // Call the service method
        Reports result = reportsService.getReportById(reportId);

        // Verify that reportsRepository.findById was called with the correct argument
        verify(reportsRepository, times(1)).findById(reportId);

        // Assert that the result is the mockReport
        assertEquals(mockReport, result);
    }

    @Test
    void testAddReport1() {
        // Create a sample ReportsDTO
        ReportsDTO reportsDTO = new ReportsDTO("Sample Report");
        Long employeeId = 1L;
        List<Long> expenseIds = new ArrayList<>();

        // Create a mock Employee
        Employee mockEmployee = new Employee();
        mockEmployee.setEmployeeEmail("employee@example.com");
        mockEmployee.setManagerEmail("manager@example.com");
        mockEmployee.setFirstName("John");
        mockEmployee.setLastName("Doe");
        mockEmployee.setOfficialEmployeeId("EMP123");

        // Mock the behavior of employeeService.getEmployeeById to return the mockEmployee
        when(employeeService.getEmployeeById(employeeId)).thenReturn(mockEmployee);

        // Create a mock Reports object
        Reports mockReport = new Reports();
        mockReport.setReportTitle("Sample Report");
        mockReport.setEmployeeMail("employee@example.com");
        mockReport.setManagerEmail("manager@example.com");
        mockReport.setEmployeeName("John Doe");
        mockReport.setOfficialEmployeeId("EMP123");
        mockReport.setDateCreated(LocalDate.now());
        mockReport.setEmployeeId(1L);

        when(reportsRepository.save(Mockito.any(Reports.class))).thenReturn(mockReport);

        // Create mock Expense objects
        List<Expense> mockExpenses = new ArrayList<>();
        for (Long id : expenseIds) {
            Expense mockExpense = new Expense();
            mockExpense.setAmount(100.0); // Set sample data as needed
            mockExpenses.add(mockExpense);
        }

        // Mock the behavior of expenseRepository.findAllById to return the mockExpenses
        when(expenseRepository.findAllById(expenseIds)).thenReturn(mockExpenses);

        // Call the service method
        Reports result = reportsService.addReport(reportsDTO, employeeId, expenseIds);

        // Verify that reportsRepository.save was called
        //Mockito.verify(reportsRepository, Mockito.times(1)).save(Mockito.any(Reports.class));

        // Verify that the expenseRepository.save was called for each expense
        for (Expense mockExpense : mockExpenses) {
            Mockito.verify(expenseRepository, Mockito.times(1)).save(mockExpense);
        }

        // Assert that the result is the mockReport
        assertEquals(mockReport, result);
    }


    @Test
    void testValidateReportForEdit_ReportNotSubmitted() {
        // Create a report with isSubmitted = false and PENDING status
        Reports report = new Reports();
        report.setIsSubmitted(false);
        report.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);

        try {
            // Call the validateReportForEdit method
            reportsService.validateReportForEdit(report);
        } catch (IllegalStateException e) {
            fail("An IllegalStateException should not have been thrown.");
        }
    }

    @Test
    void testValidateReportForEdit_ReportSubmitted() {
        // Create a report with isSubmitted = true and PENDING status
        Reports report = new Reports();
        report.setIsSubmitted(true);
        report.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);

        try {
            // Call the validateReportForEdit method
            reportsService.validateReportForEdit(report);
            fail("An IllegalStateException should have been thrown.");
        } catch (IllegalStateException e) {
            // The method should throw an IllegalStateException, which is expected.
        }
    }





}