package com.nineleaps.expensemanagementproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineleaps.expensemanagementproject.DTO.ExpenseDTO;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.service.IExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExpenseControllerTest {

    @Mock
    private IExpenseService expenseService;


    @InjectMocks
    private ExpenseController expenseController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
    }





    @Test
    void getExpenseById_ShouldReturnExpense() throws Exception {
        // Arrange
        Long expenseId = 1L;
        Expense expense = new Expense();
        when(expenseService.getExpenseById(expenseId)).thenReturn(expense);

        // Act
        mockMvc.perform(get("/findExpense/{expenseId}", expenseId))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        verify(expenseService, times(1)).getExpenseById(expenseId);
    }

    @Test
    void updateExpenses_ShouldReturnUpdatedExpense() throws Exception {
        // Arrange
        Long expenseId = 1L;
        ExpenseDTO expenseDTO = new ExpenseDTO();
        Expense updatedExpense = new Expense();
        when(expenseService.updateExpenses(any(ExpenseDTO.class), eq(expenseId))).thenReturn(updatedExpense);

        // Act
        mockMvc.perform(put("/updateExpenses/{expenseId}", expenseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expenseDTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        verify(expenseService, times(1)).updateExpenses(any(ExpenseDTO.class), eq(expenseId));
    }





    @Test
    void removeTaggedExpense_ShouldReturnExpense() throws Exception {
        // Arrange
        Long expenseId = 1L;
        Expense expense = new Expense();
        when(expenseService.removeTaggedExpense(expenseId)).thenReturn(expense);

        // Act
        mockMvc.perform(post("/removeTaggedExpense/{expenseId}", expenseId))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        verify(expenseService, times(1)).removeTaggedExpense(expenseId);
    }

    @Test
    void testSaveExpense_Success() {
        // Mocking the request data
        ExpenseDTO expenseDTO = new ExpenseDTO();
        // Set expense DTO properties as needed

        Long employeeId = 1L;
        Long categoryId = 2L;

        // Mocking the expense service response
        Expense expense = new Expense();
        // Set expense properties as needed
//        when(expenseService.addExpense(expenseDTO, employeeId, categoryId)).thenReturn(expense);

        // Performing the controller method
//        Expense savedExpense = expenseController.saveExpense(expenseDTO, employeeId, categoryId);

        // Verifying the response
//        assertNotNull(savedExpense);
        // Add more assertions to verify the properties of the saved expense
    }

    @Test
    void testGetAllExpenses() {
        // Mocking the expense service response
        List<Expense> mockExpenses = new ArrayList<>();
        // Add mock Expense objects to the list

        when(expenseService.getAllExpenses()).thenReturn(mockExpenses);

        // Performing the controller method
        List<Expense> result = expenseController.getAllExpenses();

        // Verifying the response
        assertNotNull(result);
        assertEquals(mockExpenses.size(), result.size());
        // Add more assertions to verify the content of the returned list
    }
    @Test
    void testGetExpenseByEmpId() {
        // Mocking the employeeId
        Long employeeId = 1L;

        // Mocking the expense service response
        List<Expense> mockExpenses = new ArrayList<>();
        // Add mock Expense objects to the list

        when(expenseService.getExpenseByEmployeeId(employeeId)).thenReturn(mockExpenses);

        // Performing the controller method
        List<Expense> result = expenseController.getExpenseByEmpId(employeeId);

        // Verifying the response
        assertNotNull(result);
        assertEquals(mockExpenses.size(), result.size());
        // Add more assertions to verify the content of the returned list
    }
    @Test
    void testGetExpenseByReportId() {
        // Mocking the reportId
        Long reportId = 1L;

        // Mocking the expense service response
        List<Expense> mockExpenses = new ArrayList<>();
        // Add mock Expense objects to the list

        when(expenseService.getExpenseByReportId(reportId)).thenReturn(mockExpenses);

        // Performing the controller method
        List<Expense> result = expenseController.getExpenseByReportId(reportId);

        // Verifying the response
        assertNotNull(result);
        assertEquals(mockExpenses.size(), result.size());
        // Add more assertions to verify the content of the returned list
    }
    @Test
    void testGetExpensesById() {
        // Mocking the employeeId
        Long employeeId = 1L;

        // Mocking the expense service response
        List<Expense> mockExpenses = new ArrayList<>();
        // Add mock Expense objects to the list

        when(expenseService.getExpensesByEmployeeId(employeeId)).thenReturn(mockExpenses);

        // Performing the controller method
        List<Expense> result = expenseController.getExpensesById(employeeId);

        // Verifying the response
        assertNotNull(result);
        assertEquals(mockExpenses.size(), result.size());
        // Add more assertions to verify the content of the returned list
    }
    @Test
    void testHideExpense() {
        // Mocking the expenseId
        Long expenseId = 1L;

        // Performing the controller method
        expenseController.hideExpense(expenseId);

        // Verifying the service method invocation
        verify(expenseService).hideExpense(expenseId);
    }

    private byte[] asJsonString(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsBytes(object);
    }
}
