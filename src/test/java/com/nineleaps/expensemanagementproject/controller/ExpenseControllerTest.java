package com.nineleaps.expensemanagementproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineleaps.expensemanagementproject.DTO.ExpenseDTO;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.service.IExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExpenseControllerTest {

    @Mock
    private IExpenseService expenseService;

    @Mock
    private ExpenseRepository expenseRepository;

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





    private byte[] asJsonString(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsBytes(object);
    }
}
