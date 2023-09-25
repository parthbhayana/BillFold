package com.nineleaps.expense_management_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineleaps.expense_management_project.entity.Expense;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.nineleaps.expense_management_project.dto.ExpenseDTO;
import com.nineleaps.expense_management_project.service.IExpenseService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class ExpenseControllerTest {

    @InjectMocks
    private ExpenseController expenseController;

    private MockMvc mockMvc;

    @Mock
    private IExpenseService expenseService;



    @Test
    public void testAddPotentialDuplicateExpense() {
        // Create a sample ExpenseDTO
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setAmount(100.0);
        expenseDTO.setDescription("Test Expense");
        expenseDTO.setMerchantName("Test Merchant");
        // Set other fields as needed

        // Mock the service method to return a sample Expense object
        Expense expectedExpense = new Expense();
        expectedExpense.setExpenseId(1L); // Set any desired fields

        when(expenseService.addPotentialDuplicateExpense(expenseDTO, 1L, 2L)).thenReturn(expectedExpense);

        // Call the controller method
        Expense resultExpense = expenseController.addPotentialDuplicateExpense(expenseDTO, 1L, 2L);

        // Assert that the returned Expense matches the expected Expense
        assertEquals(expectedExpense.getExpenseId(), resultExpense.getExpenseId());
        // Add additional assertions for other fields if needed
    }

    @Test
    public void testSaveExpense() throws IllegalAccessException {
        // Create a sample ExpenseDTO
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setAmount(100.0);
        expenseDTO.setDescription("Test Expense");
        expenseDTO.setMerchantName("Test Merchant");
        // Set other fields as needed

        // Mock the service method to return a sample result string
        String expectedResult = "Expense saved successfully";

        when(expenseService.addExpense(expenseDTO, 1L, 2L)).thenReturn(expectedResult);

        // Call the controller method
        String result = expenseController.saveExpense(expenseDTO, 1L, 2L);

        // Assert that the returned result matches the expected result
        assertEquals(expectedResult, result);
    }


    @Test
    public void testGetExpenseById() {
        // Create a sample expense
        Expense expectedExpense = new Expense();
        expectedExpense.setExpenseId(1L);
        // Set other fields as needed for the expense

        // Mock the service method to return the sample expense when called with ID 1
        when(expenseService.getExpenseById(1L)).thenReturn(expectedExpense);

        // Call the controller method to get the expense with ID 1
        Expense actualExpense = expenseController.getExpenseById(1L);

        // Assert that the returned expense matches the expected expense
        assertEquals(expectedExpense, actualExpense);

        // Test case for a non-existent expense (ID 2)
        when(expenseService.getExpenseById(2L)).thenReturn(null);

        // Call the controller method to get an expense with a non-existent ID (ID 2)
        Expense nonExistentExpense = expenseController.getExpenseById(2L);

        // Assert that the returned expense is null
        assertNull(nonExistentExpense);
    }

    @Test
    public void testUpdateExpenses() {
        // Create a sample expense DTO with updated values
        ExpenseDTO updatedExpenseDTO = new ExpenseDTO();
        updatedExpenseDTO.setAmount(100.0);
        // Set other fields as needed for the updated DTO

        // Create a sample expense with the updated values
        Expense updatedExpense = new Expense();
        updatedExpense.setExpenseId(1L);
        // Set other fields as needed for the updated expense

        // Mock the service method to return the updated expense when called with the updated DTO and ID 1
        when(expenseService.updateExpenses(updatedExpenseDTO, 1L)).thenReturn(updatedExpense);

        // Call the controller method to update the expense with ID 1
        Expense actualUpdatedExpense = expenseController.updateExpenses(updatedExpenseDTO, 1L);

        // Assert that the returned updated expense matches the expected updated expense
        assertEquals(updatedExpense, actualUpdatedExpense);

        // Test case for updating a non-existent expense (ID 2)
        when(expenseService.updateExpenses(updatedExpenseDTO, 2L)).thenReturn(null);

        // Call the controller method to update a non-existent expense with ID 2
        Expense nonExistentUpdatedExpense = expenseController.updateExpenses(updatedExpenseDTO, 2L);

        // Assert that the returned updated expense is null
        assertNull(nonExistentUpdatedExpense);
    }

    @Test
    public void testGetExpenseByEmpId() {
        // Create a sample employee ID (e.g., 1)
        Long employeeId = 1L;

        // Create a list of sample expenses for the employee with ID 1
        List<Expense> expensesForEmployee1 = new ArrayList<>();
        // Add sample expenses to the list
        // You can create and add Expense objects as needed for testing

        // Mock the service method to return the list of expenses for the employee with ID 1
        when(expenseService.getExpenseByEmployeeId(employeeId)).thenReturn(expensesForEmployee1);

        // Call the controller method to get expenses for the employee with ID 1
        List<Expense> actualExpenses = expenseController.getExpenseByEmpId(employeeId);

        // Assert that the returned list of expenses matches the expected list
        assertEquals(expensesForEmployee1, actualExpenses);
    }

    @Test
    public void testGetExpenseByReportId() {
        // Create a sample report ID (e.g., 1)
        Long reportId = 1L;

        // Create a list of sample expenses for the report with ID 1
        List<Expense> expensesForReport1 = new ArrayList<>();
        // Add sample expenses to the list
        // You can create and add Expense objects as needed for testing

        // Mock the service method to return the list of expenses for the report with ID 1
        when(expenseService.getExpenseByReportId(reportId)).thenReturn(expensesForReport1);

        // Call the controller method to get expenses for the report with ID 1
        List<Expense> actualExpenses = expenseController.getExpenseByReportId(reportId);

        // Assert that the returned list of expenses matches the expected list
        assertEquals(expensesForReport1, actualExpenses);
    }

    @Test
    public void testRemoveTaggedExpense() {
        // Create a sample expense ID (e.g., 1)
        Long expenseId = 1L;

        // Create a sample Expense object that represents the removed expense
        // You can customize this Expense object based on your test case
        Expense removedExpense = new Expense();
        removedExpense.setExpenseId(expenseId);

        // Mock the service method to return the removed expense
        when(expenseService.removeTaggedExpense(expenseId)).thenReturn(removedExpense);

        // Call the controller method to remove the expense with the given ID
        Expense actualRemovedExpense = expenseController.removeTaggedExpense(expenseId);

        // Assert that the returned Expense object matches the expected removedExpense
        assertEquals(removedExpense, actualRemovedExpense);
    }

    @Test
    public void testGetExpensesById() {
        // Create a sample employee ID (e.g., 1)
        Long employeeId = 1L;

        // Create a list of sample Expense objects that represent expenses for the employee
        // You can customize this list of expenses based on your test case
        List<Expense> expensesForEmployee = new ArrayList<>();

        // Mock the service method to return the list of expenses for the employee
        when(expenseService.getExpensesByEmployeeId(employeeId)).thenReturn(expensesForEmployee);

        // Call the controller method to get expenses by employee ID
        List<Expense> actualExpenses = expenseController.getExpensesById(employeeId);

        // Assert that the returned list of Expense objects matches the expected expensesForEmployee
        assertEquals(expensesForEmployee, actualExpenses);
    }

    @Test
    public void testHideExpense() {
        // Create a sample expense ID (e.g., 1)
        Long expenseId = 1L;

        // Call the controller method to hide the expense
        expenseController.hideExpense(expenseId);

        // Verify that the expenseService.hideExpense method was called with the correct expense ID
        verify(expenseService).hideExpense(expenseId);
    }


}

















