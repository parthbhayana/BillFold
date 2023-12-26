package com.nineleaps.expense_management_project.controller;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineleaps.expense_management_project.dto.ExpenseDTO;
import com.nineleaps.expense_management_project.entity.Category;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.entity.Expense;
import com.nineleaps.expense_management_project.entity.FinanceApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatusExpense;
import com.nineleaps.expense_management_project.entity.Reports;
import com.nineleaps.expense_management_project.service.IExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@ContextConfiguration(classes = {ExpenseController.class})
@ExtendWith(SpringExtension.class)
class ExpenseControllerTest {
    @Autowired
    private ExpenseController expenseController;


    @MockBean
    private IExpenseService iExpenseService;


    @Test
    @Disabled()
    void testAddPotentialDuplicateExpense1() throws Exception {

        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/addPotentialDuplicateExpense/{employeeId}",
                1L);
        MockHttpServletRequestBuilder contentTypeResult = postResult.param("categoryId", String.valueOf(1L))
                .contentType(MediaType.APPLICATION_JSON);

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setAmount(10.0d);
        expenseDTO.setDate(LocalDate.of(1970, 1, 1));
        expenseDTO.setDescription("The characteristics of someone or something");
        expenseDTO.setFile("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
        expenseDTO.setFileName("foo.txt");
        expenseDTO.setMerchantName("Merchant Name");
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString(expenseDTO));
        MockMvcBuilders.standaloneSetup(expenseController).build().perform(requestBuilder);
    }


    @Test
    void testGetAllExpenses() throws Exception {
        when(iExpenseService.getAllExpenses()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/showAllExpenses");
        MockMvcBuilders.standaloneSetup(expenseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testGetExpenseById() throws Exception {
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
        expense.setFile("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
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
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/findExpense/{expenseId}", 1L);
        MockMvcBuilders.standaloneSetup(expenseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"expenseId\":1,\"merchantName\":\"Merchant Name\",\"date\":[1970,1,1],\"dateCreated\":[1970,1,1,0,0],\"amount"
                                        + "\":10.0,\"description\":\"The characteristics of someone or something\",\"categoryDescription\":\"Category"
                                        + " Description\",\"isReported\":true,\"isHidden\":true,\"reportTitle\":\"Dr\",\"amountApproved\":10.0,\"financeApp"
                                        + "rovalStatus\":\"REIMBURSED\",\"managerApprovalStatusExpense\":\"APPROVED\",\"potentialDuplicate\":true,\"file\""
                                        + ":\"QVhBWEFYQVg=\",\"fileName\":\"foo.txt\"}"));
    }


    @Test
    void testUpdateExpenses() throws Exception {
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
        expense.setFile("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        when(iExpenseService.updateExpenses(Mockito.any(), Mockito.<Long>any())).thenReturn(expense);
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setAmount(10.0d);
        expenseDTO.setDate(null);
        expenseDTO.setDescription("The characteristics of someone or something");
        expenseDTO.setFile("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
        expenseDTO.setFileName("foo.txt");
        expenseDTO.setMerchantName("Merchant Name");
        String content = (new ObjectMapper()).writeValueAsString(expenseDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/updateExpenses/{expenseId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(expenseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"expenseId\":1,\"merchantName\":\"Merchant Name\",\"date\":[1970,1,1],\"dateCreated\":[1970,1,1,0,0],\"amount"
                                        + "\":10.0,\"description\":\"The characteristics of someone or something\",\"categoryDescription\":\"Category"
                                        + " Description\",\"isReported\":true,\"isHidden\":true,\"reportTitle\":\"Dr\",\"amountApproved\":10.0,\"financeApp"
                                        + "rovalStatus\":\"REIMBURSED\",\"managerApprovalStatusExpense\":\"APPROVED\",\"potentialDuplicate\":true,\"file\""
                                        + ":\"QVhBWEFYQVg=\",\"fileName\":\"foo.txt\"}"));
    }


    @Test
    void testGetExpenseByReportId() throws Exception {
        when(iExpenseService.getExpenseByReportId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getExpenseByReportId/{reportId}", 1L);
        MockMvcBuilders.standaloneSetup(expenseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testRemoveTaggedExpense() throws Exception {
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
        expense.setFile("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
        expense.setFileName("foo.txt");
        expense.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
        expense.setIsHidden(true);
        expense.setIsReported(true);
        expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
        expense.setMerchantName("Merchant Name");
        expense.setPotentialDuplicate(true);
        expense.setReportTitle("Dr");
        expense.setReports(reports);
        when(iExpenseService.removeTaggedExpense(Mockito.<Long>any())).thenReturn(expense);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/removeTaggedExpense/{expenseId}",
                1L);
        MockMvcBuilders.standaloneSetup(expenseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"expenseId\":1,\"merchantName\":\"Merchant Name\",\"date\":[1970,1,1],\"dateCreated\":[1970,1,1,0,0],\"amount"
                                        + "\":10.0,\"description\":\"The characteristics of someone or something\",\"categoryDescription\":\"Category"
                                        + " Description\",\"isReported\":true,\"isHidden\":true,\"reportTitle\":\"Dr\",\"amountApproved\":10.0,\"financeApp"
                                        + "rovalStatus\":\"REIMBURSED\",\"managerApprovalStatusExpense\":\"APPROVED\",\"potentialDuplicate\":true,\"file\""
                                        + ":\"QVhBWEFYQVg=\",\"fileName\":\"foo.txt\"}"));
    }


    @Test
    void testHideExpense() throws Exception {
        doNothing().when(iExpenseService).hideExpense(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/hideExpense/{expenseId}", 1L);
        MockMvcBuilders.standaloneSetup(expenseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testHideExpense2() throws Exception {
        doNothing().when(iExpenseService).hideExpense(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/hideExpense/{expenseId}", 1L);
        requestBuilder.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(expenseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testGetExpenseByEmpId() throws Exception {
        when(iExpenseService.getExpenseByEmployeeId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getExpenseByEmployeeId/{employeeId}",
                1L);
        MockMvcBuilders.standaloneSetup(expenseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetExpensesById() throws Exception {
        when(iExpenseService.getExpensesByEmployeeId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/getUnreportedExpensesByEmployeeId/{employeeId}", 1L);
        MockMvcBuilders.standaloneSetup(expenseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testSaveExpense() throws Exception {
        when(iExpenseService.addExpense(Mockito.any(), Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn("Add Expense");

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setAmount(10.0d);
        expenseDTO.setDate(null);
        expenseDTO.setDescription("The characteristics of someone or something");
        expenseDTO.setFile("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
        expenseDTO.setFileName("foo.txt");
        expenseDTO.setMerchantName("Merchant Name");
        String content = (new ObjectMapper()).writeValueAsString(expenseDTO);
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/insertExpenses/{employeeId}", 1L);
        MockHttpServletRequestBuilder requestBuilder = postResult.param("categoryId", String.valueOf(1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(expenseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Add Expense"));
    }


}
