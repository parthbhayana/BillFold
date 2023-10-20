package com.nineleaps.expense_management_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.nineleaps.expense_management_project.entity.Category;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.entity.Expense;
import com.nineleaps.expense_management_project.entity.FinanceApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatusExpense;
import com.nineleaps.expense_management_project.entity.Reports;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PdfGeneratorServiceImpl.class})
@ExtendWith(SpringExtension.class)
class PdfGeneratorServiceImplTest {
    @Autowired
    private PdfGeneratorServiceImpl pdfGeneratorServiceImpl;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private IExpenseService iExpenseService;

    @MockBean
    private ReportsRepository reportsRepository;


    @Mock
    private Document document;
    @Mock
    private PdfWriter writer;

    /**
     * Method under test: {@link PdfGeneratorServiceImpl#generatePdf(Long, List, String)}
     */
    @Test
    void testGeneratePdf() throws IOException {
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertEquals(0, pdfGeneratorServiceImpl.generatePdf(1L, new ArrayList<>(), "Role").length);
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link PdfGeneratorServiceImpl#generatePdf(Long, List, String)}
     */
    @Test
    void testGeneratePdf2() throws IOException {
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
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Category category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);

        Employee employee2 = new Employee();
        employee2.setEmployeeEmail("jane.doe@example.org");
        employee2.setEmployeeId(1L);
        employee2.setExpenseList(new ArrayList<>());
        employee2.setFirstName("Jane");
        employee2.setHrEmail("jane.doe@example.org");
        employee2.setHrName("Hr Name");
        employee2.setImageUrl("https://example.org/example");
        employee2.setIsFinanceAdmin(true);
        employee2.setIsHidden(true);
        employee2.setLastName("Doe");
        employee2.setLndEmail("jane.doe@example.org");
        employee2.setLndName("Lnd Name");
        employee2.setManagerEmail("jane.doe@example.org");
        employee2.setManagerName("Manager Name");
        employee2.setMiddleName("Middle Name");
        employee2.setMobileNumber(1L);
        employee2.setOfficialEmployeeId("42");
        employee2.setRole("Role");
        employee2.setToken("ABC123");

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
        expense.setEmployee(employee2);
        expense.setExpenseId(1L);
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        when(iExpenseService.getExpenseById(Mockito.<Long>any())).thenReturn(expense);

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
        Optional<Reports> ofResult2 = Optional.of(reports2);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        ArrayList<Long> expenseIds = new ArrayList<>();
        expenseIds.add(2L);
        byte[] actualGeneratePdfResult = pdfGeneratorServiceImpl.generatePdf(1L, expenseIds, "Role");
        assertEquals('%', actualGeneratePdfResult[0]);
        assertEquals('P', actualGeneratePdfResult[1]);
        assertEquals('D', actualGeneratePdfResult[2]);
        assertEquals('F', actualGeneratePdfResult[3]);
        assertEquals('-', actualGeneratePdfResult[4]);
        assertEquals('1', actualGeneratePdfResult[5]);
        assertEquals('.', actualGeneratePdfResult[6]);
        assertEquals('5', actualGeneratePdfResult[7]);
        assertEquals('\n', actualGeneratePdfResult[8]);
        assertEquals('%', actualGeneratePdfResult[9]);
        assertEquals((byte) -30, actualGeneratePdfResult[10]);
        assertEquals((byte) -29, actualGeneratePdfResult[11]);
        assertEquals((byte) -49, actualGeneratePdfResult[12]);
        assertEquals((byte) -45, actualGeneratePdfResult[13]);
        assertEquals('\n', actualGeneratePdfResult[14]);
        assertEquals('5', actualGeneratePdfResult[15]);
        assertEquals(' ', actualGeneratePdfResult[Short.SIZE]);
        assertEquals('0', actualGeneratePdfResult[17]);
        assertEquals(' ', actualGeneratePdfResult[18]);
        assertEquals('o', actualGeneratePdfResult[19]);
        assertEquals('b', actualGeneratePdfResult[20]);
        assertEquals('j', actualGeneratePdfResult[21]);
        assertEquals('\n', actualGeneratePdfResult[22]);
        assertEquals('<', actualGeneratePdfResult[23]);
        assertEquals('<', actualGeneratePdfResult[24]);
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(iExpenseService, atLeast(1)).getExpenseById(Mockito.<Long>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link PdfGeneratorServiceImpl#generatePdf(Long, List, String)}
     */
    @Test
    void testGeneratePdf3() throws IOException {
        Employee employee = new Employee("https://example.org/example", true, new ArrayList<>(), "Times", "ABC123");
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
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Category category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1L);
        category.setCategoryTotal(1L);
        category.setExpenseList(new ArrayList<>());
        category.setIsHidden(true);

        Employee employee2 = new Employee();
        employee2.setEmployeeEmail("jane.doe@example.org");
        employee2.setEmployeeId(1L);
        employee2.setExpenseList(new ArrayList<>());
        employee2.setFirstName("Jane");
        employee2.setHrEmail("jane.doe@example.org");
        employee2.setHrName("Hr Name");
        employee2.setImageUrl("https://example.org/example");
        employee2.setIsFinanceAdmin(true);
        employee2.setIsHidden(true);
        employee2.setLastName("Doe");
        employee2.setLndEmail("jane.doe@example.org");
        employee2.setLndName("Lnd Name");
        employee2.setManagerEmail("jane.doe@example.org");
        employee2.setManagerName("Manager Name");
        employee2.setMiddleName("Middle Name");
        employee2.setMobileNumber(1L);
        employee2.setOfficialEmployeeId("42");
        employee2.setRole("Role");
        employee2.setToken("ABC123");

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
        expense.setEmployee(employee2);
        expense.setExpenseId(1L);
        expense.setFile("AXAXAXAX".getBytes("UTF-8"));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        when(iExpenseService.getExpenseById(Mockito.<Long>any())).thenReturn(expense);

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
        Optional<Reports> ofResult2 = Optional.of(reports2);
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        ArrayList<Long> expenseIds = new ArrayList<>();
        expenseIds.add(2L);
        byte[] actualGeneratePdfResult = pdfGeneratorServiceImpl.generatePdf(1L, expenseIds, "Role");
        assertEquals('%', actualGeneratePdfResult[0]);
        assertEquals('P', actualGeneratePdfResult[1]);
        assertEquals('D', actualGeneratePdfResult[2]);
        assertEquals('F', actualGeneratePdfResult[3]);
        assertEquals('-', actualGeneratePdfResult[4]);
        assertEquals('1', actualGeneratePdfResult[5]);
        assertEquals('.', actualGeneratePdfResult[6]);
        assertEquals('5', actualGeneratePdfResult[7]);
        assertEquals('\n', actualGeneratePdfResult[8]);
        assertEquals('%', actualGeneratePdfResult[9]);
        assertEquals((byte) -30, actualGeneratePdfResult[10]);
        assertEquals((byte) -29, actualGeneratePdfResult[11]);
        assertEquals((byte) -49, actualGeneratePdfResult[12]);
        assertEquals((byte) -45, actualGeneratePdfResult[13]);
        assertEquals('\n', actualGeneratePdfResult[14]);
        assertEquals('5', actualGeneratePdfResult[15]);
        assertEquals(' ', actualGeneratePdfResult[Short.SIZE]);
        assertEquals('0', actualGeneratePdfResult[17]);
        assertEquals(' ', actualGeneratePdfResult[18]);
        assertEquals('o', actualGeneratePdfResult[19]);
        assertEquals('b', actualGeneratePdfResult[20]);
        assertEquals('j', actualGeneratePdfResult[21]);
        assertEquals('\n', actualGeneratePdfResult[22]);
        assertEquals('<', actualGeneratePdfResult[23]);
        assertEquals('<', actualGeneratePdfResult[24]);
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(iExpenseService, atLeast(1)).getExpenseById(Mockito.<Long>any());
        verify(reportsRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link PdfGeneratorServiceImpl#getCenterAlignedCell(String, Font)}
     */
    @Test
    void testGetCenterAlignedCell() {
        PdfPCell actualCenterAlignedCell =
                pdfGeneratorServiceImpl.getCenterAlignedCell("Not all who wander are lost", new Font(1));
        assertTrue(actualCenterAlignedCell.isUseVariableBorders());
        assertFalse(actualCenterAlignedCell.isUseBorderPadding());
        assertFalse(actualCenterAlignedCell.isNoWrap());
        assertFalse(actualCenterAlignedCell.hasFixedHeight());
        assertEquals(5, actualCenterAlignedCell.getVerticalAlignment());
        assertEquals(0.0f, actualCenterAlignedCell.getTop());
        assertEquals(1, actualCenterAlignedCell.getRowspan());
        assertEquals(0, actualCenterAlignedCell.getRotation());
        assertEquals(0.0f, actualCenterAlignedCell.getRight());
        assertEquals(1, actualCenterAlignedCell.getPhrase().size());
        assertEquals(5.0f, actualCenterAlignedCell.getPaddingTop());
        assertEquals(5.0f, actualCenterAlignedCell.getPaddingRight());
        assertEquals(5.0f, actualCenterAlignedCell.getPaddingLeft());
        assertEquals(5.0f, actualCenterAlignedCell.getPaddingBottom());
        assertEquals(10.0f, actualCenterAlignedCell.getMaxHeight());
        assertEquals(0.0f, actualCenterAlignedCell.getLeft());
        assertEquals(0.0f, actualCenterAlignedCell.getGrayFill());
        assertNull(actualCenterAlignedCell.getCompositeElements());
        assertEquals(1, actualCenterAlignedCell.getColspan());
        assertNull(actualCenterAlignedCell.getBorderColor());
        assertNull(actualCenterAlignedCell.getBorderColorTop());
        assertEquals(0.01f, actualCenterAlignedCell.getBorderWidth());
        assertNull(actualCenterAlignedCell.getBorderColorBottom());
        assertEquals(0.01f, actualCenterAlignedCell.getBorderWidthBottom());
        assertEquals(0.0f, actualCenterAlignedCell.getBorderWidthLeft());
        assertEquals(3, actualCenterAlignedCell.getBorder());
        assertNull(actualCenterAlignedCell.getBorderColorLeft());
        assertEquals(0.0f, actualCenterAlignedCell.getBorderWidthRight());
        assertEquals(0.01f, actualCenterAlignedCell.getBorderWidthTop());
        assertNull(actualCenterAlignedCell.getBorderColorRight());
        assertEquals(-10.0f, actualCenterAlignedCell.getBottom());
        ColumnText column = actualCenterAlignedCell.getColumn();
        assertEquals(1.0f, column.getMultipliedLeading());
        assertEquals(0.0f, column.getLeading());
        assertEquals(0.0f, column.getIndent());
        assertEquals(0.0f, column.getFollowingIndent());
        assertEquals(0.0f, column.getExtraParagraphSpace());
        assertNull(column.getCanvas());
        assertEquals(0, column.getArabicOptions());
        assertEquals(1, column.getAlignment());
        assertFalse(column.isUseAscender());
        assertEquals(0.0f, column.getSpaceCharRatio());
        assertTrue(column.isAdjustFirstLine());
        assertEquals(0.0f, column.getRightIndent());
        assertEquals(0, column.getRunDirection());
    }

    /**
     * Method under test: {@link PdfGeneratorServiceImpl#getCenterAlignedCell(String, Font)}
     */
    @Test
    void testGetCenterAlignedCell2() {
        PdfPCell actualCenterAlignedCell = pdfGeneratorServiceImpl.getCenterAlignedCell("", new Font(1));
        assertTrue(actualCenterAlignedCell.isUseVariableBorders());
        assertFalse(actualCenterAlignedCell.isUseBorderPadding());
        assertFalse(actualCenterAlignedCell.isNoWrap());
        assertFalse(actualCenterAlignedCell.hasFixedHeight());
        assertEquals(5, actualCenterAlignedCell.getVerticalAlignment());
        assertEquals(0.0f, actualCenterAlignedCell.getTop());
        assertEquals(1, actualCenterAlignedCell.getRowspan());
        assertEquals(0, actualCenterAlignedCell.getRotation());
        assertEquals(0.0f, actualCenterAlignedCell.getRight());
        assertTrue(actualCenterAlignedCell.getPhrase().isEmpty());
        assertEquals(5.0f, actualCenterAlignedCell.getPaddingTop());
        assertEquals(5.0f, actualCenterAlignedCell.getPaddingRight());
        assertEquals(5.0f, actualCenterAlignedCell.getPaddingLeft());
        assertEquals(5.0f, actualCenterAlignedCell.getPaddingBottom());
        assertEquals(10.0f, actualCenterAlignedCell.getMaxHeight());
        assertEquals(0.0f, actualCenterAlignedCell.getLeft());
        assertEquals(0.0f, actualCenterAlignedCell.getGrayFill());
        assertNull(actualCenterAlignedCell.getCompositeElements());
        assertEquals(1, actualCenterAlignedCell.getColspan());
        assertNull(actualCenterAlignedCell.getBorderColor());
        assertNull(actualCenterAlignedCell.getBorderColorTop());
        assertEquals(0.01f, actualCenterAlignedCell.getBorderWidth());
        assertNull(actualCenterAlignedCell.getBorderColorBottom());
        assertEquals(0.01f, actualCenterAlignedCell.getBorderWidthBottom());
        assertEquals(0.0f, actualCenterAlignedCell.getBorderWidthLeft());
        assertEquals(3, actualCenterAlignedCell.getBorder());
        assertNull(actualCenterAlignedCell.getBorderColorLeft());
        assertEquals(0.0f, actualCenterAlignedCell.getBorderWidthRight());
        assertEquals(0.01f, actualCenterAlignedCell.getBorderWidthTop());
        assertNull(actualCenterAlignedCell.getBorderColorRight());
        assertEquals(-10.0f, actualCenterAlignedCell.getBottom());
        ColumnText column = actualCenterAlignedCell.getColumn();
        assertEquals(1.0f, column.getMultipliedLeading());
        assertEquals(0.0f, column.getLeading());
        assertEquals(0.0f, column.getIndent());
        assertEquals(0.0f, column.getFollowingIndent());
        assertEquals(0.0f, column.getExtraParagraphSpace());
        assertNull(column.getCanvas());
        assertEquals(0, column.getArabicOptions());
        assertEquals(1, column.getAlignment());
        assertFalse(column.isUseAscender());
        assertEquals(0.0f, column.getSpaceCharRatio());
        assertTrue(column.isAdjustFirstLine());
        assertEquals(0.0f, column.getRightIndent());
        assertEquals(0, column.getRunDirection());
    }

    /**
     * Method under test: {@link PdfGeneratorServiceImpl#getCenterAlignedCell(String, Font)}
     */
    @Test
    void testGetCenterAlignedCell3() {
        PdfPCell actualCenterAlignedCell =
                pdfGeneratorServiceImpl.getCenterAlignedCell("Not all who wander are lost", mock(Font.class));
        assertTrue(actualCenterAlignedCell.isUseVariableBorders());
        assertFalse(actualCenterAlignedCell.isUseBorderPadding());
        assertFalse(actualCenterAlignedCell.isNoWrap());
        assertFalse(actualCenterAlignedCell.hasFixedHeight());
        assertEquals(5, actualCenterAlignedCell.getVerticalAlignment());
        assertEquals(0.0f, actualCenterAlignedCell.getTop());
        assertEquals(1, actualCenterAlignedCell.getRowspan());
        assertEquals(0, actualCenterAlignedCell.getRotation());
        assertEquals(0.0f, actualCenterAlignedCell.getRight());
        assertEquals(1, actualCenterAlignedCell.getPhrase().size());
        assertEquals(5.0f, actualCenterAlignedCell.getPaddingTop());
        assertEquals(5.0f, actualCenterAlignedCell.getPaddingRight());
        assertEquals(5.0f, actualCenterAlignedCell.getPaddingLeft());
        assertEquals(5.0f, actualCenterAlignedCell.getPaddingBottom());
        assertEquals(0.0f, actualCenterAlignedCell.getLeft());
        assertEquals(0.0f, actualCenterAlignedCell.getGrayFill());
        assertNull(actualCenterAlignedCell.getCompositeElements());
        assertEquals(1, actualCenterAlignedCell.getColspan());
        assertNull(actualCenterAlignedCell.getBorderColor());
        assertNull(actualCenterAlignedCell.getBorderColorTop());
        assertEquals(0.01f, actualCenterAlignedCell.getBorderWidth());
        assertNull(actualCenterAlignedCell.getBorderColorBottom());
        assertEquals(0.01f, actualCenterAlignedCell.getBorderWidthBottom());
        assertEquals(0.0f, actualCenterAlignedCell.getBorderWidthLeft());
        assertEquals(3, actualCenterAlignedCell.getBorder());
        assertNull(actualCenterAlignedCell.getBorderColorLeft());
        assertEquals(0.0f, actualCenterAlignedCell.getBorderWidthRight());
        assertEquals(0.01f, actualCenterAlignedCell.getBorderWidthTop());
        assertNull(actualCenterAlignedCell.getBorderColorRight());
        assertEquals(0.0f, actualCenterAlignedCell.getBottom());
        ColumnText column = actualCenterAlignedCell.getColumn();
        assertEquals(1.0f, column.getMultipliedLeading());
        assertEquals(0.0f, column.getLeading());
        assertEquals(0.0f, column.getIndent());
        assertEquals(0.0f, column.getFollowingIndent());
        assertEquals(0.0f, column.getExtraParagraphSpace());
        assertNull(column.getCanvas());
        assertEquals(0, column.getArabicOptions());
        assertEquals(1, column.getAlignment());
        assertFalse(column.isUseAscender());
        assertEquals(0.0f, column.getSpaceCharRatio());
        assertTrue(column.isAdjustFirstLine());
        assertEquals(0.0f, column.getRightIndent());
        assertEquals(0, column.getRunDirection());
    }

    /**
     * Method under test: {@link PdfGeneratorServiceImpl#getCenterAlignedCells(String, Font)}
     */
    @Test
    void testGetCenterAlignedCells() {
        PdfPCell actualCenterAlignedCells =
                pdfGeneratorServiceImpl.getCenterAlignedCells("Not all who wander are lost", new Font(1));
        assertTrue(actualCenterAlignedCells.isUseVariableBorders());
        assertFalse(actualCenterAlignedCells.isUseBorderPadding());
        assertFalse(actualCenterAlignedCells.isNoWrap());
        assertFalse(actualCenterAlignedCells.hasFixedHeight());
        assertEquals(5, actualCenterAlignedCells.getVerticalAlignment());
        assertEquals(0.0f, actualCenterAlignedCells.getTop());
        assertEquals(1, actualCenterAlignedCells.getRowspan());
        assertEquals(0, actualCenterAlignedCells.getRotation());
        assertEquals(0.0f, actualCenterAlignedCells.getRight());
        assertEquals(1, actualCenterAlignedCells.getPhrase().size());
        assertEquals(5.0f, actualCenterAlignedCells.getPaddingTop());
        assertEquals(5.0f, actualCenterAlignedCells.getPaddingRight());
        assertEquals(5.0f, actualCenterAlignedCells.getPaddingLeft());
        assertEquals(5.0f, actualCenterAlignedCells.getPaddingBottom());
        assertEquals(10.0f, actualCenterAlignedCells.getMaxHeight());
        assertEquals(0.0f, actualCenterAlignedCells.getLeft());
        assertEquals(0.0f, actualCenterAlignedCells.getGrayFill());
        assertNull(actualCenterAlignedCells.getCompositeElements());
        assertEquals(1, actualCenterAlignedCells.getColspan());
        assertNull(actualCenterAlignedCells.getBorderColor());
        assertNull(actualCenterAlignedCells.getBorderColorTop());
        assertEquals(0.01f, actualCenterAlignedCells.getBorderWidth());
        assertNull(actualCenterAlignedCells.getBorderColorBottom());
        assertEquals(0.01f, actualCenterAlignedCells.getBorderWidthBottom());
        assertEquals(0.0f, actualCenterAlignedCells.getBorderWidthLeft());
        assertEquals(3, actualCenterAlignedCells.getBorder());
        assertNull(actualCenterAlignedCells.getBorderColorLeft());
        assertEquals(0.0f, actualCenterAlignedCells.getBorderWidthRight());
        assertEquals(0.01f, actualCenterAlignedCells.getBorderWidthTop());
        assertNull(actualCenterAlignedCells.getBorderColorRight());
        assertEquals(-10.0f, actualCenterAlignedCells.getBottom());
        ColumnText column = actualCenterAlignedCells.getColumn();
        assertNull(column.getCanvas());
        assertEquals(0, column.getArabicOptions());
        assertEquals(1, column.getAlignment());
        assertFalse(column.isUseAscender());
        assertEquals(0.0f, column.getExtraParagraphSpace());
        assertEquals(0.0f, column.getIndent());
        assertEquals(0.0f, column.getLeading());
        assertEquals(0.0f, column.getRightIndent());
        assertEquals(0, column.getRunDirection());
        assertTrue(column.isAdjustFirstLine());
        assertEquals(0.0f, column.getFollowingIndent());
        assertEquals(1.0f, column.getMultipliedLeading());
        assertEquals(0.0f, column.getSpaceCharRatio());
    }

    /**
     * Method under test: {@link PdfGeneratorServiceImpl#getCenterAlignedCells(String, Font)}
     */
    @Test
    void testGetCenterAlignedCells2() {
        PdfPCell actualCenterAlignedCells = pdfGeneratorServiceImpl.getCenterAlignedCells("", new Font(1));
        assertTrue(actualCenterAlignedCells.isUseVariableBorders());
        assertFalse(actualCenterAlignedCells.isUseBorderPadding());
        assertFalse(actualCenterAlignedCells.isNoWrap());
        assertFalse(actualCenterAlignedCells.hasFixedHeight());
        assertEquals(5, actualCenterAlignedCells.getVerticalAlignment());
        assertEquals(0.0f, actualCenterAlignedCells.getTop());
        assertEquals(1, actualCenterAlignedCells.getRowspan());
        assertEquals(0, actualCenterAlignedCells.getRotation());
        assertEquals(0.0f, actualCenterAlignedCells.getRight());
        assertTrue(actualCenterAlignedCells.getPhrase().isEmpty());
        assertEquals(5.0f, actualCenterAlignedCells.getPaddingTop());
        assertEquals(5.0f, actualCenterAlignedCells.getPaddingRight());
        assertEquals(5.0f, actualCenterAlignedCells.getPaddingLeft());
        assertEquals(5.0f, actualCenterAlignedCells.getPaddingBottom());
        assertEquals(10.0f, actualCenterAlignedCells.getMaxHeight());
        assertEquals(0.0f, actualCenterAlignedCells.getLeft());
        assertEquals(0.0f, actualCenterAlignedCells.getGrayFill());
        assertNull(actualCenterAlignedCells.getCompositeElements());
        assertEquals(1, actualCenterAlignedCells.getColspan());
        assertNull(actualCenterAlignedCells.getBorderColor());
        assertNull(actualCenterAlignedCells.getBorderColorTop());
        assertEquals(0.01f, actualCenterAlignedCells.getBorderWidth());
        assertNull(actualCenterAlignedCells.getBorderColorBottom());
        assertEquals(0.01f, actualCenterAlignedCells.getBorderWidthBottom());
        assertEquals(0.0f, actualCenterAlignedCells.getBorderWidthLeft());
        assertEquals(3, actualCenterAlignedCells.getBorder());
        assertNull(actualCenterAlignedCells.getBorderColorLeft());
        assertEquals(0.0f, actualCenterAlignedCells.getBorderWidthRight());
        assertEquals(0.01f, actualCenterAlignedCells.getBorderWidthTop());
        assertNull(actualCenterAlignedCells.getBorderColorRight());
        assertEquals(-10.0f, actualCenterAlignedCells.getBottom());
        ColumnText column = actualCenterAlignedCells.getColumn();
        assertNull(column.getCanvas());
        assertEquals(0, column.getArabicOptions());
        assertEquals(1, column.getAlignment());
        assertFalse(column.isUseAscender());
        assertEquals(0.0f, column.getExtraParagraphSpace());
        assertEquals(0.0f, column.getIndent());
        assertEquals(0.0f, column.getLeading());
        assertEquals(0.0f, column.getRightIndent());
        assertEquals(0, column.getRunDirection());
        assertTrue(column.isAdjustFirstLine());
        assertEquals(0.0f, column.getFollowingIndent());
        assertEquals(1.0f, column.getMultipliedLeading());
        assertEquals(0.0f, column.getSpaceCharRatio());
    }

    /**
     * Method under test: {@link PdfGeneratorServiceImpl#getCenterAlignedCells(String, Font)}
     */
    @Test
    void testGetCenterAlignedCells3() {
        PdfPCell actualCenterAlignedCells =
                pdfGeneratorServiceImpl.getCenterAlignedCells("Not all who wander are lost", mock(Font.class));
        assertTrue(actualCenterAlignedCells.isUseVariableBorders());
        assertFalse(actualCenterAlignedCells.isUseBorderPadding());
        assertFalse(actualCenterAlignedCells.isNoWrap());
        assertFalse(actualCenterAlignedCells.hasFixedHeight());
        assertEquals(5, actualCenterAlignedCells.getVerticalAlignment());
        assertEquals(0.0f, actualCenterAlignedCells.getTop());
        assertEquals(1, actualCenterAlignedCells.getRowspan());
        assertEquals(0, actualCenterAlignedCells.getRotation());
        assertEquals(0.0f, actualCenterAlignedCells.getRight());
        assertEquals(1, actualCenterAlignedCells.getPhrase().size());
        assertEquals(5.0f, actualCenterAlignedCells.getPaddingTop());
        assertEquals(5.0f, actualCenterAlignedCells.getPaddingRight());
        assertEquals(5.0f, actualCenterAlignedCells.getPaddingLeft());
        assertEquals(5.0f, actualCenterAlignedCells.getPaddingBottom());
        assertEquals(0.0f, actualCenterAlignedCells.getLeft());
        assertEquals(0.0f, actualCenterAlignedCells.getGrayFill());
        assertNull(actualCenterAlignedCells.getCompositeElements());
        assertEquals(1, actualCenterAlignedCells.getColspan());
        assertNull(actualCenterAlignedCells.getBorderColor());
        assertNull(actualCenterAlignedCells.getBorderColorTop());
        assertEquals(0.01f, actualCenterAlignedCells.getBorderWidth());
        assertNull(actualCenterAlignedCells.getBorderColorBottom());
        assertEquals(0.01f, actualCenterAlignedCells.getBorderWidthBottom());
        assertEquals(0.0f, actualCenterAlignedCells.getBorderWidthLeft());
        assertEquals(3, actualCenterAlignedCells.getBorder());
        assertNull(actualCenterAlignedCells.getBorderColorLeft());
        assertEquals(0.0f, actualCenterAlignedCells.getBorderWidthRight());
        assertEquals(0.01f, actualCenterAlignedCells.getBorderWidthTop());
        assertNull(actualCenterAlignedCells.getBorderColorRight());
        assertEquals(0.0f, actualCenterAlignedCells.getBottom());
        ColumnText column = actualCenterAlignedCells.getColumn();
        assertNull(column.getCanvas());
        assertEquals(0, column.getArabicOptions());
        assertEquals(1, column.getAlignment());
        assertFalse(column.isUseAscender());
        assertEquals(0.0f, column.getExtraParagraphSpace());
        assertEquals(0.0f, column.getIndent());
        assertEquals(0.0f, column.getLeading());
        assertEquals(0.0f, column.getRightIndent());
        assertEquals(0, column.getRunDirection());
        assertTrue(column.isAdjustFirstLine());
        assertEquals(0.0f, column.getFollowingIndent());
        assertEquals(1.0f, column.getMultipliedLeading());
        assertEquals(0.0f, column.getSpaceCharRatio());
    }

    /**
     * Method under test: {@link PdfGeneratorServiceImpl#export(Long, List, HttpServletResponse, String)}
     */
    @Test
    void testExport() throws IOException {
        Optional<Reports> emptyResult = Optional.empty();
        when(reportsRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        ArrayList<Long> expenseIds = new ArrayList<>();
        Response response = mock(Response.class);
        when(response.getOutputStream()).thenReturn(new DelegatingServletOutputStream(new ByteArrayOutputStream(1)));
        doNothing().when(response).setContentLength(anyInt());
        doNothing().when(response).setContentType(Mockito.<String>any());
        doNothing().when(response).setHeader(Mockito.<String>any(), Mockito.<String>any());
        assertEquals(0, pdfGeneratorServiceImpl.export(1L, expenseIds, response, "Role").length);
        verify(reportsRepository).findById(Mockito.<Long>any());
        verify(response).getOutputStream();
        verify(response).setContentLength(anyInt());
        verify(response).setContentType(Mockito.<String>any());
        verify(response).setHeader(Mockito.<String>any(), Mockito.<String>any());
    }


    @Test
    void testConvertToCustomCase() {
        assertEquals("Input", PdfGeneratorServiceImpl.convertToCustomCase("Input"));
        assertNull(PdfGeneratorServiceImpl.convertToCustomCase(null));
        assertEquals("", PdfGeneratorServiceImpl.convertToCustomCase(""));
    }

    /**
     * Method under test: {@link PdfGeneratorServiceImpl#convertToCustomCase(String)}
     */
    @Test
    void testConvertToCustomCase2() {
        String actualConvertToCustomCaseResult = PdfGeneratorServiceImpl.convertToCustomCase(String.join("", "com.", System.getProperty("user.name"), ".expense_management_project.entity.Reports"));
        assertEquals(String.join("", "Com.", System.getProperty("user.name"), ".expense Management Project.entity.reports"), actualConvertToCustomCaseResult);
    }




}